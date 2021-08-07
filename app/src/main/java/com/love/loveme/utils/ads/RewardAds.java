package com.love.loveme.utils.ads;

import android.app.Activity;
import android.content.Context;

import com.facebook.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.love.loveme.SessionManager;

public class RewardAds {

    RewardAdListnear rewardAdListnear;
    SessionManager sessionManager;
    private RewardedAd rewardedAd;
    private Context context;
    private InterstitialAd interstitialAdfb;

    public RewardAds(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);

        initGoogle();
    }

    public RewardAdListnear getRewardAdListnear() {
        return rewardAdListnear;
    }

    public void setRewardAdListnear(RewardAdListnear rewardAdListnear) {
        this.rewardAdListnear = rewardAdListnear;
    }

    private void initGoogle() {

        initFacebook();
    }

    private void initFacebook() {
        interstitialAdfb = new com.facebook.ads.InterstitialAd(context, sessionManager.getFBInt());
        interstitialAdfb.loadAd(
                interstitialAdfb.buildLoadAdConfig()

                        .build());

    }


    public void showAd() {
        if (rewardedAd != null) {
            Activity activityContext = (Activity) context;
            rewardedAd.show(activityContext, rewardItem -> rewardAdListnear.onEarned());
        } else if (interstitialAdfb != null && interstitialAdfb.isAdLoaded()) {
            interstitialAdfb.show();
            rewardAdListnear.onEarned();
        }

    }


    public interface RewardAdListnear {
        void onAdClosed();

        void onEarned();
    }
}
