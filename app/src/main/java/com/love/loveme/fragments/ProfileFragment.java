package com.love.loveme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.love.loveme.BuildConfig;
import com.love.loveme.JanuWork;
import com.love.loveme.R;
import com.love.loveme.SessionManager;
import com.love.loveme.actvites.PlanListActivity;
import com.love.loveme.actvites.StartActivity;
import com.love.loveme.actvites.WebActivity;
import com.love.loveme.databinding.BottomSheetconfirmationBinding;
import com.love.loveme.databinding.FragmentProfileBinding;
import com.love.loveme.models.RestResponseRoot;
import com.love.loveme.models.User;
import com.love.loveme.models.UserRoot;
import com.love.loveme.retrofit.ApiCallWork;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.RewardAds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String TITLESSTR = "TITLE";
    FragmentProfileBinding binding;

    SessionManager sessionManager;
    private String userTkn;
    private User data;
    RewardAds rewardAds;
    public ProfileFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        sessionManager = new SessionManager(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            userTkn = sessionManager.getUser().getToken();
            Log.d("TAG", "onActivityCreated: tkn== " + userTkn);
            getData();
            rewardAds = new RewardAds(getActivity());
        }


    }

    private void getData() {
        binding.main.setVisibility(View.INVISIBLE);
        binding.shimmer.startShimmer();
        binding.shimmer.setVisibility(View.VISIBLE);
        Call<UserRoot> call = RetrofitBuilder.create().getUserDetail(userTkn, Const.DEV_KEY);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null) {
                    data = response.body().getData();
                    data.setToken(userTkn);
                    sessionManager.saveUser(data);
                    setData();
                    initListnear();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.main.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
//
            }
        });
    }

    private void initListnear() {
        binding.tvLogout.setOnClickListener(v -> openLogOutSheet());

        binding.lytMorecoins.setOnClickListener(v -> startActivity(new Intent(getActivity(), PlanListActivity.class)));
        binding.lytfreecoins.setOnClickListener(v -> showReward());

        binding.lytprivacy.setOnClickListener(v -> startActivity(new Intent(getActivity(), WebActivity.class).putExtra("URL", sessionManager.getSettings().getPrivacyPolicy()).putExtra(TITLESSTR, "Privacy Policy")));
        binding.lytterms.setOnClickListener(v -> startActivity(new Intent(getActivity(), WebActivity.class).putExtra("URL", sessionManager.getSettings().getPrivacyPolicy()).putExtra(TITLESSTR, "Tearms of Use")));
        binding.lytshare.setOnClickListener(v -> {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                e.getLocalizedMessage();
            }
        });

    }

    private void showReward() {
        rewardAds.showAd();
        rewardAds.setRewardAdListnear(new RewardAds.RewardAdListnear() {
            @Override
            public void onAdClosed() {
                Toast.makeText(getActivity(), "Reward Fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEarned() {
                Toast.makeText(getActivity(), "reward", Toast.LENGTH_SHORT).show();
                addCoin(50);
            }
        });

    }

    private void addCoin(int i) {
        ApiCallWork apiCallWork = new ApiCallWork(getActivity());
        apiCallWork.addCoin(sessionManager.getUser().getToken(), i);
        apiCallWork.setOnResponseListnear(new ApiCallWork.OnResponseListnear() {
            @Override
            public void onSuccess() {
                JanuWork januWork = new JanuWork();
                boolean added = januWork.addCoinLocal(getActivity(), i);
                Log.d("TAG", "onSuccess: coin added " + added);
                getData();

            }

            @Override
            public void onFailure() {
//
            }
        });
    }

    private void openLogOutSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        BottomSheetconfirmationBinding sheetconfirmationBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottom_sheetconfirmation, null, false);
        bottomSheetDialog.setContentView(sheetconfirmationBinding.getRoot());
        bottomSheetDialog.show();
        sheetconfirmationBinding.tvCencel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        sheetconfirmationBinding.tvYes.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            logoutUser();
        });


    }



    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            userTkn = sessionManager.getUser().getToken();
            Log.d("TAG", "onActivityResumed: tkn== " + userTkn);
            getData();
        }
    }

    private void logoutUser() {
        Call<RestResponseRoot> call = RetrofitBuilder.create().logoutUser(userTkn, Const.DEV_KEY);
        call.enqueue(new Callback<RestResponseRoot>() {
            @Override
            public void onResponse(Call<RestResponseRoot> call, Response<RestResponseRoot> response) {
                if (response.code() == 200 && response.body().isStatus()) {
                    Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
                    sessionManager.saveUser(new User());
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    sessionManager.saveBooleanValue(Const.IS_LOGIN, false);
                    startActivity(new Intent(getActivity(), StartActivity.class));
                    getActivity().finishAffinity();
                }

            }

            @Override
            public void onFailure(Call<RestResponseRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: logot " + t.getMessage());
            }
        });
    }

    private void setData() {
        if (getActivity() != null) {
            if (data.getUserProfile() != null && !data.getUserProfile().equals("")) {
                Glide.with(getActivity()).load(data.getUserProfile()).circleCrop().placeholder(R.drawable.ic_userfilled).into(binding.imgprofile);
            } else {
                Glide.with(getActivity()).load(sessionManager.getStringValue(Const.PROFILE_IMAGE)).circleCrop().placeholder(R.drawable.ic_userfilled).into(binding.imgprofile);

            }
        }
        binding.tvName.setText(data.getFullName());
        binding.tvemail.setText(data.getUserEmail());
        binding.tvcoin.setText(String.valueOf(data.getMyWallet()));

    }
}