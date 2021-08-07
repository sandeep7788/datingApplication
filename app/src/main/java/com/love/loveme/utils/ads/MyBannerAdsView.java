package com.love.loveme.utils.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyBannerAdsView extends FrameLayout {
    public MyBannerAdsView(@NonNull Context context) {
        super(context);
        initBanner();
    }

    public MyBannerAdsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBanner();
    }

    public MyBannerAdsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBanner();
    }

    public MyBannerAdsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBanner();
    }

    private void initBanner() {
        new BannerAds(getContext(), this);
    }

}
