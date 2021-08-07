package com.love.loveme.utils.ads;

import android.content.Context;

import androidx.annotation.Keep;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.love.loveme.SessionManager;

import static com.google.android.gms.ads.formats.NativeAdOptions.ADCHOICES_TOP_RIGHT;

@Keep
public class MultipleCustomNativeAds {
    private final Context context;
    private final SessionManager sessionManager;
    private boolean loadMoreAds = true;
    int offset;
    int index;
    private OnLoadAds onLoadAds;

    public MultipleCustomNativeAds(Context context, OnLoadAds onLoadAds, int offset) {
        this.context = context;
        sessionManager = new SessionManager(context);
        this.onLoadAds = onLoadAds;
        this.offset = offset;
        index = offset - 1;
//        initAds();
    }

    private void initAds() {
        loadNativeAds();
    }


    private void loadNativeAds() {
        if (loadMoreAds) {
            AdLoader.Builder builder = null;
            builder = new AdLoader.Builder(context, sessionManager.getAdmobNative().isEmpty() ? sessionManager.getAdmobNative() : "ca-app-pub-3940256099942544/2247696110");
            builder.forNativeAd(
                    unifiedNativeAd -> {
                        loadMoreAds = onLoadAds.onLoad(unifiedNativeAd, index);
                        index = index + offset;

                        loadNativeAds();
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            loadFbNativeAds();
                        }

                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            .setRequestCustomMuteThisAd(true)
                            .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                            .build()).build();
        }
    }


    private void loadFbNativeAds() {
        if (context != null && loadMoreAds) {
            NativeAdsManager mNativeAdsManager = new NativeAdsManager(context, sessionManager.getFBNative(), 1);
            mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    loadMoreAds = onLoadAds.onLoad(mNativeAdsManager.nextNativeAd(), index);
                    index = index + offset;
                    loadFbNativeAds();
                }

                @Override
                public void onAdError(AdError adError) {
                    //
                }
            });

            mNativeAdsManager.loadAds(NativeAdBase.MediaCacheFlag.ALL);
        }
    }


    public interface OnLoadAds {
        boolean onLoad(Object adsData, int position);
    }
}
