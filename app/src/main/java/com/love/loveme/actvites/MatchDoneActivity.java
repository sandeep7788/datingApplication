package com.love.loveme.actvites;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.love.loveme.JanuWork;
import com.love.loveme.R;
import com.love.loveme.databinding.ActivityMatchDoneBinding;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.RandomVideoRoot;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.MyInterstitialAds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;


public class MatchDoneActivity extends BaseActivity {

    private static final String TAG = "machdone";
    ActivityMatchDoneBinding binding;
    private RandomVideoRoot.Data girl;
    private SimpleExoPlayer player;
    private CountDownTimer countDownTimer;
    CountDownTimer countDownTimer2;
    private MyInterstitialAds myInterstitialAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_match_done);
        initCamera();
        getRandomGirl();
        scaleView(binding.imageHeart);
        myInterstitialAds = new MyInterstitialAds(this);
    }

    public void scaleView(View v) {

        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f);
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v, scalex, scaley);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setDuration(1500);
        anim.start();

    }

    private void initCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            binding.surfaceCamera.bindToLifecycle(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        binding.surfaceCamera.bindToLifecycle(this);
    }


    private void setTImer() {
        countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {

                Log.d(TAG, "onTick: " + millisUntilFinished / 1000);
                int second = (int) (millisUntilFinished / 1000);
                binding.tvSeconds.setText(String.valueOf(second));
                String text = "Your Dream Girl Will be gone with in " + second + " seconds";
                binding.tvnotice.setText(text);
            }

            public void onFinish() {
                Log.d(TAG, "onFinish: finished");
                getRandomGirl();
            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (player != null) {
            player.release();
        }
    }

    private void getRandomGirl() {
        binding.playerloder.setVisibility(View.VISIBLE);
        binding.lytloder.lytloder.setVisibility(View.VISIBLE);
        Call<RandomVideoRoot> call = RetrofitBuilder.create().getRandomVideo(Const.DEV_KEY);
        call.enqueue(new Callback<RandomVideoRoot>() {
            @Override
            public void onResponse(Call<RandomVideoRoot> call, Response<RandomVideoRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null) {
                    if (girl != null) {
                        if (girl.getVideoId().equalsIgnoreCase(response.body().getData().getVideoId())) {
                            getRandomGirl();
                        } else {

                            girl = response.body().getData();
                            setGirl();
                            binding.lytloder.lytloder.setVisibility(View.GONE);
                        }
                    } else {
                        girl = response.body().getData();
                        setGirl();
                        binding.lytloder.lytloder.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<RandomVideoRoot> call, Throwable t) {
//
            }
        });
    }

    @Override
    public void onBackPressed() {
        myInterstitialAds.showAds();
        super.onBackPressed();
    }

    private void setGirl() {
        if (player != null) {
            player.release();
        }
        player = new SimpleExoPlayer.Builder(this).build();
        binding.playerview.setPlayer(player);
        binding.playerview.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING);
        Uri uri = Uri.parse(Const.IMAGE_URL + girl.getVideo());
        com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-retrytech");
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        player.setVolume(0);
        Log.d(TAG, "initializePlayer: " + uri);
        player.setPlayWhenReady(true);
        player.prepare(mediaSource, false, false);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == STATE_BUFFERING) {
                    binding.playerloder.setVisibility(View.VISIBLE);
                } else {
                    binding.playerloder.setVisibility(View.GONE);
                }
                switch (playbackState) {
                    case STATE_BUFFERING:
                        Log.d(TAG, "buffer: " + uri);
                        break;
                    case STATE_ENDED:
                        player.setRepeatMode(Player.REPEAT_MODE_ALL);
                        Log.d(TAG, "end: " + uri);
                        break;
                    case STATE_IDLE:
                        Log.d(TAG, "idle: " + uri);
                        break;

                    case STATE_READY:
                        setTImer();
                        Log.d(TAG, "ready: " + uri);

                        break;
                    default:
                        break;
                }
            }
        });


        Glide.with(this).load(Const.IMAGE_URL + girl.getThumbImg()).circleCrop().into(binding.imgprofile);
        Glide.with(this).load(Const.IMAGE_URL + girl.getThumbImg()).circleCrop().into(binding.imgprofile2);
        Glide.with(this).load(Const.IMAGE_URL.concat(!girl.getImageGallery().isEmpty() && !girl.getImageGallery().get(0).isEmpty() ? girl.getImageGallery().get(0) : girl.getThumbImg())).centerCrop().into(binding.imgmain);

        CountryRoot.DataItem country = JanuWork.getCountry(girl.getCountryId(), MatchDoneActivity.this);
        if (country != null) {
            girl.setCountry(country);
            binding.tvcountryName.setText(country.getCountry());
            Glide.with(this).load(Const.IMAGE_URL + country.getCountryMedia()).circleCrop().into(binding.imgflag);

        }

        binding.tvbio.setText(girl.getBio());
        binding.tvName.setText(girl.getName());
        binding.tvName2.setText(girl.getName());
        binding.tvcoin.setText(girl.getRate().concat("/min"));


        binding.imageHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                binding.lytafterLike.setVisibility(View.VISIBLE);

                countDownTimer2 = new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
//
                    }

                    public void onFinish() {
                        Log.d(TAG, "onFinish: finished timer 2");

                        if (player != null) {
                            player.release();
                        }
                        countDownTimer2.cancel();


                        startActivity(new Intent(MatchDoneActivity.this, CallAcceptActivity.class).putExtra("girl", new Gson().toJson(girl)));

                        finish();

                    }
                }.start();

            }

            @Override
            public void unLiked(LikeButton likeButton) {
//
            }
        });

    }


}