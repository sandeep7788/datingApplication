package com.love.loveme.actvites;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.love.loveme.JanuWork;
import com.love.loveme.R;
import com.love.loveme.SessionManager;
import com.love.loveme.databinding.ActivityStartBinding;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.SettingRoot;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.MyInterstitialAds;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private boolean isSettingLoded;
    private boolean isCountryLoded = false;
    ActivityStartBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        sessionManager = new SessionManager(this);


        getCountry();
        JanuWork.setNames();



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                sessionManager.saveStringValue(Const.NOTIFICATION_TOKEN,token);
            }
        });

        try {

            TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
            String countryCodeValue = tm.getNetworkCountryIso();

            if (countryCodeValue.equalsIgnoreCase("in")){
                sessionManager.saveBooleanValue("india",true);
            }

        }catch (Exception e){

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getCountry() {
        Call<CountryRoot> call = RetrofitBuilder.create().getCountryList(Const.DEV_KEY);
        call.enqueue(new Callback<CountryRoot>() {
            @Override
            public void onResponse(Call<CountryRoot> call, Response<CountryRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && !response.body().getData().isEmpty()) {
                    List<CountryRoot.DataItem> list = response.body().getData();
                    sessionManager.saveCountry(list);
                    isCountryLoded = true;
                    getSettings();


                }
            }

            @Override
            public void onFailure(Call<CountryRoot> call, Throwable t) {
                getSettings();
            }
        });
    }

    private void getSettings() {
        Call<SettingRoot> call = RetrofitBuilder.create().getSettings(Const.DEV_KEY);
        call.enqueue(new Callback<SettingRoot>() {


            @Override
            public void onResponse(Call<SettingRoot> call, Response<SettingRoot> response) {
                if (response.code() == 200 && response.body() != null && response.body().isStatus() && response.body().getData() != null) {
                    sessionManager.saveSettings(response.body().getData());
                    isSettingLoded = true;
                    onClickLetsFind();

                }
            }

            @Override
            public void onFailure(Call<SettingRoot> call, Throwable t) {
//
            }
        });
    }


    public void onClickLetsFind() {
        if (isSettingLoded && isCountryLoded) {
            binding.pb.setVisibility(View.GONE);
            binding.tvLetsFind.setVisibility(View.VISIBLE);
            binding.lnrLetsFind.setOnClickListener(v -> {
                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
            });

        } else {
            getCountry();
            getSettings();
        }

    }
}