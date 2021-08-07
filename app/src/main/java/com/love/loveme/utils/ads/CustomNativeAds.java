package com.love.loveme.utils.ads;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Keep;

import com.bumptech.glide.Glide;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.love.loveme.R;
import com.love.loveme.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.ads.formats.NativeAdOptions.ADCHOICES_TOP_RIGHT;

@Keep
public class CustomNativeAds {
    private boolean isEnabledAds = true;
    private Context context;
    private SessionManager sessionManager;
    private LinearLayout adsContainer;
    private int admobNative;
    private int facebookNative;

    private NativeAdsManager mNativeAdsManager;

    public CustomNativeAds(Context context, LinearLayout adsContainer, int admobNative, int facebookNative) {
        this.context = context;
        sessionManager = new SessionManager(context);
        this.adsContainer = adsContainer;
        this.admobNative = admobNative;
        this.facebookNative = facebookNative;
        initAds();
    }

    private void initAds() {
        loadNativeAds();
    }


    private void loadNativeAds() {


        AdLoader.Builder builder = null;
        builder = new AdLoader.Builder(context, sessionManager.getAdmobNative());
        // A native ad loaded successfully, check if the ad loader has finished loading
        // and if so, insert the ads into the list.
        // A native ad failed to load, check if the ad loader has finished loading
        // and if so, insert the ads into the list.
        AdLoader adLoader = builder.forNativeAd(
                unifiedNativeAd -> {
                    Log.d(TAG, "onUnifiedNativeAdLoaded: ");
                    if (isEnabledAds) {
                        showAdmobAds(unifiedNativeAd);
                    }
                    // A native ad loaded successfully, check if the ad loader has finished loading
                    // and if so, insert the ads into the list.
                }).withAdListener(
                new AdListener() {

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "The previous native ad failed to load. Attempting to"
                                + " load another." + loadAdError.getCode());

                        if (isEnabledAds) {
                            loadFbNativeAds();
                        }
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .setRequestCustomMuteThisAd(true)
                        .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                        .build()).build();


    }

    private void showAdmobAds(com.google.android.gms.ads.nativead.NativeAd unifiedNativeAd) {
        if (context != null) {
            View view = LayoutInflater.from(context).inflate(admobNative, null, false);
            com.google.android.gms.ads.nativead.NativeAdView adView = view.findViewById(R.id.ad_view);

            // The MediaView will display a video asset if one is present in the ad, and the
            // first image asset otherwise.
            adView.setMediaView(adView.findViewById(R.id.ad_media));

            // Register the view used for each individual asset.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
            adsContainer.removeAllViews();
            adsContainer.addView(view, 0);
            populateNativeAdView(unifiedNativeAd, adView);
        }
    }


    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd,
                                      NativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        com.google.android.gms.ads.nativead.NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else if (context != null && isEnabledAds) {
            Glide.with(adView.getIconView())
                    .load(icon.getDrawable())
                    .circleCrop()
                    .into((ImageView) adView.getIconView());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);

    }

    private void loadFbNativeAds() {
        if (context != null && isEnabledAds) {
            mNativeAdsManager = new NativeAdsManager(context, sessionManager.getFBNative(), 1);
            mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    Log.d(TAG, "onAdsLoaded: fb ");
                    if (isEnabledAds) {
                        showFaceBookAds();
                    }
                }

                @Override
                public void onAdError(AdError adError) {
                    Log.e(TAG, "AdError: " + adError.getErrorMessage());
                }
            });

            mNativeAdsManager.loadAds(NativeAdBase.MediaCacheFlag.ALL);
        }
    }

    private void showFaceBookAds() {
        NativeAd ad = mNativeAdsManager.nextNativeAd();
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        adsContainer.removeAllViews();
        if (ad != null) {
            View view = LayoutInflater.from(context).inflate(facebookNative, null, false);
            nativeAdLayout.addView(view);
            NativeAdLayout linearLayout = view.findViewById(R.id.ad_choices_container);
            linearLayout.removeAllViews();
            com.facebook.ads.MediaView mvAdMedia;
            MediaView ivAdIcon;
            TextView tvAdTitle;
            TextView tvAdBody;
            TextView tvAdSocialContext;
            TextView tvAdSponsoredLabel;
            Button btnAdCallToAction;
            mvAdMedia = (com.facebook.ads.MediaView) view.findViewById(R.id.native_ad_media);
            tvAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
            tvAdBody = (TextView) view.findViewById(R.id.native_ad_body);
            tvAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
            tvAdSponsoredLabel = (TextView) view.findViewById(R.id.native_ad_sponsored_label);
            btnAdCallToAction = (Button) view.findViewById(R.id.native_ad_call_to_action);
            ivAdIcon = view.findViewById(R.id.native_ad_icon);
            tvAdTitle.setText(ad.getAdvertiserName());
            tvAdBody.setText(ad.getAdBodyText());
            tvAdSocialContext.setText(ad.getAdSocialContext());
            tvAdSponsoredLabel.setText(ad.getSponsoredTranslation());
            btnAdCallToAction.setText("  " + ad.getAdCallToAction());
            btnAdCallToAction.setVisibility(
                    ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            AdOptionsView adChoicesView = new AdOptionsView(context,
                    ad, nativeAdLayout);


            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(ivAdIcon);
            clickableViews.add(mvAdMedia);
            clickableViews.add(btnAdCallToAction);
            ad.registerViewForInteraction(
                    view,
                    mvAdMedia,
                    ivAdIcon,
                    clickableViews);

            linearLayout.addView(adChoicesView, 0);
            adsContainer.addView(nativeAdLayout, 0);
        }
    }

}
