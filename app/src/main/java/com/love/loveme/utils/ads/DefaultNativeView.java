package com.love.loveme.utils.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.love.loveme.R;

public class DefaultNativeView extends LinearLayout {
    public DefaultNativeView(@NonNull Context context) {
        super(context);
        initNative();
    }

    public DefaultNativeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initNative();
    }

    public DefaultNativeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNative();
    }

    public DefaultNativeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initNative();
    }

    private void initNative() {
        new CustomNativeAds(getContext(), this, R.layout.admob_native_new, R.layout.item_native_facebook);
    }
}
