package com.love.loveme.utils.ads;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.love.loveme.SessionManager;

public class MyInterstitialAds {

    private Context context;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd interstitialAdfb;
    SessionManager sessionManager;

    public MyInterstitialAds(Context context) {
        this.context = context;
        initAds();
    }


    private void initAds() {
        sessionManager = new SessionManager(context);


    }

    public void showAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
        } else if (interstitialAdfb != null && interstitialAdfb.isAdLoaded()) {
            interstitialAdfb.show();
        }
    }
}
