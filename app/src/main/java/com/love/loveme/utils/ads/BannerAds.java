package com.love.loveme.utils.ads;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Keep;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.love.loveme.SessionManager;

@Keep
public class BannerAds {
    private static final String TAG = "bannerads";
    SessionManager sessionManager;
    private Context context;
    private FrameLayout adsContainer;
    private com.facebook.ads.AdView adViewfb;

    public BannerAds(Context context, FrameLayout adsContainer) {
        this.context = context;
        this.adsContainer = adsContainer;
        if (context != null) {
            sessionManager = new SessionManager(context);
            initAds();
        }
    }

    private void initAds() {

        AdView adView = new AdView(context);
        adView.setAdUnitId(sessionManager.getAdmobBanner());

        adView.setAdSize(new AdSize(320, 50));
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                initFacebook();
                Log.d("TAG", "onAdFailedToLoad: " + loadAdError.toString());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (adsContainer != null) {
                    adsContainer.removeAllViews();
                    adsContainer.addView(adView);
                }
                Log.d("TAG", "onAdLoaded: ");
            }
        });


    }

    private void initFacebook() {
        adViewfb = new com.facebook.ads.AdView(context, sessionManager.getFbBanner(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);


        adViewfb.loadAd(adViewfb.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "onError: fb " + adError.getErrorMessage());


            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "onAdLoaded: facebbok");
                if (adsContainer != null) {
                    adsContainer.removeAllViews();
                    adsContainer.addView(adViewfb);
                }
//mm
            }

            @Override
            public void onAdClicked(Ad ad) {
//mm
            }

            @Override
            public void onLoggingImpression(Ad ad) {
//mm
            }
        }).build());


    }
}
