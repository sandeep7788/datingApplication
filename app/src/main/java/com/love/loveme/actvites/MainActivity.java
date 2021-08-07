package com.love.loveme.actvites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.love.loveme.JanuWork;
import com.love.loveme.R;
import com.love.loveme.databinding.ActivityMainBinding;
import com.love.loveme.fragments.ChatFragment;
import com.love.loveme.fragments.MachineFragment;
import com.love.loveme.fragments.MapFragment;
import com.love.loveme.fragments.ProfileFragment;
import com.love.loveme.fragments.StrimmingFragment;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.MessageRoot;
import com.love.loveme.models.RandomVideoRoot;
import com.love.loveme.popups.PermisionPopup;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.MyInterstitialAds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements MachineFragment.MachineFragentClickListaer, MapFragment.MapFragmentListnear {
    public static final AtomicBoolean IS_CALLING_NOW = new AtomicBoolean(false);
    ActivityMainBinding binding;
    private boolean mapOpen = false;
    Handler handler2 = new Handler();
    private Runnable runnable2;


    protected final static List<MessageRoot.DataItem> messages = new ArrayList<>();

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MobileAds.initialize(this, initializationStatus -> {
        });

        initView();
        getMessages();



    }

    private void getMessages() {
        Call<MessageRoot> call = RetrofitBuilder.create().getMessageList(Const.DEV_KEY);
        call.enqueue(new Callback<MessageRoot>() {
            @Override
            public void onResponse(Call<MessageRoot> call, Response<MessageRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && !response.body().getData().isEmpty()) {
                    messages.clear();
                    messages.addAll(response.body().getData());

                }
            }

            @Override
            public void onFailure(Call<MessageRoot> call, Throwable t) {
//
            }
        });
    }

    private void initPermissions() {


        PermisionPopup permisionPopup = new PermisionPopup(this);
        permisionPopup.setPopupClickListnear(() -> {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    initCall();
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    initCall();
                }


            };
            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();


        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (runnable2 != null) {
            handler2.removeCallbacks(runnable2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            initPermissions();
        } else {
            initCall();
        }

    }

    private void initCall() {
        if (runnable2 != null) {
            handler2.removeCallbacks(runnable2);
        }
        runnable2 = () -> {
            try {
                if (!MainActivity.IS_CALLING_NOW.get()) {
                    Call<RandomVideoRoot> call = RetrofitBuilder.create().getRandomVideo(Const.DEV_KEY);
                    call.enqueue(new Callback<RandomVideoRoot>() {
                        @Override
                        public void onResponse(Call<RandomVideoRoot> call, Response<RandomVideoRoot> response) {
                            if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null) {
                                RandomVideoRoot.Data girl = response.body().getData();
                                CountryRoot.DataItem country = JanuWork.getCountry(girl.getCountryId(), MainActivity.this);
                                girl.setCountry(country);
                                startActivity(new Intent(MainActivity.this, CallAcceptActivity.class).putExtra("girl", new Gson().toJson(girl)));
                            }
                        }

                        @Override
                        public void onFailure(Call<RandomVideoRoot> call, Throwable t) {
//
                        }
                    });
                    Log.d("TAG", "run: call ing handler==" + MainActivity.IS_CALLING_NOW);

                }
            } catch (IllegalStateException ed) {
                ed.printStackTrace();
            }
        };

        handler2.postDelayed(runnable2, 30000);


    }

    private void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.frame, new MachineFragment(this)).commit();
        initListnear();

    }

    private void initListnear() {
        binding.imgbinoculars.setOnClickListener(v -> {
            binding.imgbinoculars.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
            binding.imgstrimming.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.imgprofile.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.lytchat.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            if (mapOpen) {
                switchFragment(new MapFragment(this));
            } else
                switchFragment(new MachineFragment(this));
        });

        binding.imgstrimming.setOnClickListener(v -> {
            binding.imgstrimming.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
            binding.imgbinoculars.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.imgprofile.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.lytchat.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            switchFragment(new StrimmingFragment());
        });
        binding.lytchat.setOnClickListener(v -> {
            binding.lytchat.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
            binding.imgbinoculars.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.imgprofile.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.imgstrimming.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            switchFragment(new ChatFragment());
        });
        binding.imgprofile.setOnClickListener(v -> {
            binding.imgprofile.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
            binding.imgstrimming.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.imgbinoculars.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            binding.lytchat.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
            switchFragment(new ProfileFragment());
        });
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }

    @Override
    public void onStartMatchingClick() {
        mapOpen = true;
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MapFragment(this)).commit();
    }

    @Override
    public void onMatchDone() {
        startActivity(new Intent(this, MatchDoneActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    public void resetPostion() {
        binding.imgbinoculars.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
        binding.imgstrimming.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
        binding.imgprofile.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
        binding.lytchat.setImageTintList(ContextCompat.getColorStateList(this, R.color.dim_gray));
        if (mapOpen) {
            switchFragment(new MapFragment(this));
        } else
            switchFragment(new MachineFragment(this));
    }
}