package com.love.loveme.actvites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.love.loveme.R;
import com.love.loveme.databinding.ActivityWebBinding;
import com.love.loveme.utils.ads.MyInterstitialAds;


public class WebActivity extends BaseActivity {
    ActivityWebBinding binding;
    private MyInterstitialAds myInterstitialAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);


        Intent intent = getIntent();
        if(intent != null) {
            String url = intent.getStringExtra("URL");
            String title = intent.getStringExtra("TITLE");
            if(title != null) {
                binding.tvtitle.setText(title);
            }
            if (url != null) {
                binding.webView.loadUrl(url);
                binding.webView.setWebViewClient(new WebViewClient());

            }
        }
        myInterstitialAds = new MyInterstitialAds(this);
    }

    public void onClickBack(View view) {
        Log.d("TAG", "onclickAggrement: " + view);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        myInterstitialAds.showAds();
        super.onBackPressed();
    }
}