package com.love.loveme.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.love.loveme.R;

import java.util.ArrayList;
import java.util.List;

public class AdHolder extends RecyclerView.ViewHolder {
    MediaView mvAdMedia;
    MediaView ivAdIcon;
    TextView tvAdTitle;
    TextView tvAdBody;
    TextView tvAdSocialContext;
    TextView tvAdSponsoredLabel;
    Button btnAdCallToAction;
    NativeAdLayout adChoicesContainer;

    AdHolder(View view) {
        super(view);

        mvAdMedia = (MediaView) view.findViewById(R.id.native_ad_media);
        tvAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
        tvAdBody = (TextView) view.findViewById(R.id.native_ad_body);
        tvAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
        tvAdSponsoredLabel = (TextView) view.findViewById(R.id.native_ad_sponsored_label);
        btnAdCallToAction = (Button) view.findViewById(R.id.native_ad_call_to_action);
        ivAdIcon = view.findViewById(R.id.native_ad_icon);
        adChoicesContainer = view.findViewById(R.id.ad_choices_container);

    }

    public void showAds(NativeAd ad) {
        if (ad != null) {

            tvAdTitle.setText(ad.getAdvertiserName());
            tvAdBody.setText(ad.getAdBodyText());
            tvAdSocialContext.setText(ad.getAdSocialContext());
            tvAdSponsoredLabel.setText(ad.getSponsoredTranslation());
            btnAdCallToAction.setText("  " + ad.getAdCallToAction());
            btnAdCallToAction.setVisibility(
                    ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            AdOptionsView adChoicesView = new AdOptionsView(itemView.getContext(),
                    ad, adChoicesContainer);
            adChoicesContainer.addView(adChoicesView, 0);

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(ivAdIcon);
            clickableViews.add(mvAdMedia);
            clickableViews.add(btnAdCallToAction);
            ad.registerViewForInteraction(
                    itemView,
                    mvAdMedia,
                    ivAdIcon,
                    clickableViews);
        }
    }
}