package com.love.loveme.actvites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.love.loveme.JanuWork;
import com.love.loveme.R;
import com.love.loveme.SessionManager;
import com.love.loveme.adapters.BottomViewPagerAdapter;
import com.love.loveme.adapters.CommntAdapter;
import com.love.loveme.adapters.EmojiAdapterHorizontol;
import com.love.loveme.databinding.ActivityLiveBinding;
import com.love.loveme.databinding.BottomsheetPaymentmethodBinding;
import com.love.loveme.models.CoinPackageRoot;
import com.love.loveme.models.CommentRoot;
import com.love.loveme.models.CountryVideoListRoot;
import com.love.loveme.models.GiftCategoryRoot;
import com.love.loveme.models.GiftRoot;
import com.love.loveme.models.OfferCoinPackageRoot;
import com.love.loveme.models.User;
import com.love.loveme.popups.CallRejectPopup;
import com.love.loveme.popups.CommenPopup;
import com.love.loveme.popups.NoBalancePopup;
import com.love.loveme.retrofit.ApiCallWork;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.MyInterstitialAds;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;
import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

public class LiveActivity extends BaseActivity implements  PaymentResultListener {
    private static final String TAG = "@@Liveactrivity";
    ActivityLiveBinding binding;
    CountryVideoListRoot.DataItem dataItem;
    SessionManager sessionManager;
    EmojiAdapterHorizontol emojiAdapterHorizontol = new EmojiAdapterHorizontol();
    CommntAdapter commntAdapter = new CommntAdapter();
    private int start = 0;
    private boolean giftCategoryLoded = false;
    private List<GiftCategoryRoot.DataItem> giftCategories;
    private int giftStart = 0;
    private SimpleExoPlayer player;
    private List<CommentRoot.DataItem> comments = new ArrayList<>();
    private Random rand = new Random();

    Checkout checkout = new Checkout();
    Runnable commentRunnable;


    Handler commentHandler = new Handler();
    Handler timerHandler = new Handler();
    private String purchasedCoin = "";
    Runnable timerRunnable;
    private BottomSheetDialog bottomSheetDialog;
    private long oneMinRate = 60000;
    private MyInterstitialAds myInterstitialAds;

    private boolean reduceCoinWork() {

        String rate = dataItem.getRate();
        if (Integer.parseInt(sessionManager.getUser().getMyWallet()) >= Integer.parseInt(rate)) {
            callApiForLessCoin(rate);
            JanuWork januWork = new JanuWork();
            januWork.lessCoinLocal(LiveActivity.this, Integer.parseInt(rate));
            timerHandler.postDelayed(timerRunnable, oneMinRate);
            setMyWallet();
            return true;
        } else {
            timerHandler.removeCallbacks(timerRunnable);
            if (player != null) {
                player.setPlayWhenReady(false);
            }
            NoBalancePopup noBalancePopup = new NoBalancePopup(LiveActivity.this, sessionManager.getUser().getMyWallet());
            noBalancePopup.setPopupClickListnear(new NoBalancePopup.PopupClickListnear() {
                @Override
                public void onPlanItemClick(CoinPackageRoot.DataItem value) {
                    openPaymentSheet(value);
                }

                @Override
                public void onDismiss() {
                    onBackPressed();
                }
            });
        }
        setMyWallet();

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live);
        timerRunnable = () -> {

            Log.d(TAG, "run: =====================================one min");
            reduceCoinWork();
            timerHandler.postDelayed(timerRunnable, oneMinRate);
        };
        commentRunnable = () -> {

            addSingleComment();
            commentHandler.postDelayed(commentRunnable, 3000);
        };
        sessionManager = new SessionManager(this);

        getGiftCategory();
        setMyWallet();
        Intent intent = getIntent();
        if (intent != null) {
            binding.imgprofile.setTransitionName(getIntent().getStringExtra("position"));
            String objStr = intent.getStringExtra("girl");
            if (objStr != null && !objStr.equals("")) {
                dataItem = new Gson().fromJson(objStr, CountryVideoListRoot.DataItem.class);
                if (dataItem != null) {
                    Log.d("TAG", "onCreate: " + dataItem.getName());
                    initView();
                    initGirlData();
                    initUser();
                    getComments();

                    initListnear();

                    initRazorPay();

                }
            }
        }
        myInterstitialAds = new MyInterstitialAds(this);
    }

    private void initRazorPay() {

        Checkout.preload(this);

        checkout.setKeyID(getResources().getString(R.string.rzp_key));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentHandler.removeCallbacks(commentRunnable);
        timerHandler.removeCallbacks(timerRunnable);
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void getGiftCategory() {
        Call<GiftCategoryRoot> call = RetrofitBuilder.create().getGiftCategory(Const.DEV_KEY);
        call.enqueue(new Callback<GiftCategoryRoot>() {


            @Override
            public void onResponse(Call<GiftCategoryRoot> call, Response<GiftCategoryRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    giftCategories = response.body().getData();
                    settabLayout(response.body().getData());
                    giftCategoryLoded = true;
                    getGiftList(response.body().getData().get(0));
                }

            }

            @Override
            public void onFailure(Call<GiftCategoryRoot> call, Throwable t) {
//
            }
        });
    }

    private void getGiftList(GiftCategoryRoot.DataItem dataItem) {
        Call<GiftRoot> call = RetrofitBuilder.create().getGifts(Const.DEV_KEY, dataItem.getGiftCatId(), giftStart, Const.COUNT);
        call.enqueue(new Callback<GiftRoot>() {
            @Override
            public void onResponse(Call<GiftRoot> call, Response<GiftRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    emojiAdapterHorizontol.addData(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GiftRoot> call, Throwable t) {
//
            }
        });

        emojiAdapterHorizontol.setOnEmojiClickListnear((bitmap, coin) -> {
            User user = sessionManager.getUser();
            if (Integer.parseInt(user.getMyWallet()) >= Integer.parseInt(coin)) {
                callApiForLessCoin(coin);
                JanuWork januWork = new JanuWork();
                januWork.lessCoinLocal(LiveActivity.this, Integer.parseInt(coin));


            } else {
                timerHandler.removeCallbacks(timerRunnable);
                if (player != null) {
                    player.setPlayWhenReady(false);
                }
                NoBalancePopup noBalancePopup = new NoBalancePopup(LiveActivity.this, sessionManager.getUser().getMyWallet());
                noBalancePopup.setPopupClickListnear(new NoBalancePopup.PopupClickListnear() {
                    @Override
                    public void onPlanItemClick(CoinPackageRoot.DataItem value) {

                        openPaymentSheet(value);
                    }

                    @Override
                    public void onDismiss() {
                        onBackPressed();
                    }
                });
            }
            setMyWallet();

        });
    }


    private void initGirlData() {
        Glide.with(this).load(Const.IMAGE_URL + dataItem.getThumbImg()).circleCrop().into(binding.imgprofile);
        Glide.with(this).load(Const.IMAGE_URL + dataItem.getCountry().getCountryMedia()).circleCrop().into(binding.imgcountry);
        binding.tvGirlName.setText(dataItem.getName());
        binding.tvbio.setText(dataItem.getBio());
        binding.tvcountryName.setText(dataItem.getCountry().getCountry());
        binding.tvrate.setText(dataItem.getRate().concat("/min"));


        boolean isRedused = reduceCoinWork();
        Log.d(TAG, "initGirlData: redused ==" + isRedused);
        if (isRedused) {
            setVideo();
        }
    }

    private void getComments() {
        Call<CommentRoot> call = RetrofitBuilder.create().getComments(Const.DEV_KEY, dataItem.getCountryId(), start, Const.COUNT);
        call.enqueue(new Callback<CommentRoot>() {
            @Override
            public void onResponse(Call<CommentRoot> call, Response<CommentRoot> response) {
                if (response.code() == 200 && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    comments.addAll(response.body().getData());

                }
            }

            @Override
            public void onFailure(Call<CommentRoot> call, Throwable t) {
//
            }
        });
    }

    private void initUser() {
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            User user = sessionManager.getUser();
            binding.tvuserBalance.setText(String.valueOf(user.getMyWallet()));
        }
    }

    private void setVideo() {

        initTimer();

        player = new SimpleExoPlayer.Builder(this).build();
        binding.playerview.setPlayer(player);
        binding.playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
        Uri uri = Uri.parse(Const.IMAGE_URL + dataItem.getVideo());
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-retrytech");
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);

        Log.d(TAG, "initializePlayer: " + uri);
        player.setPlayWhenReady(true);
        player.prepare(mediaSource, false, false);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
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

                        Log.d(TAG, "ready: " + uri);

                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void
    initTimer() {
        Log.d(TAG, "initTimer: ---------");
        commentHandler.postDelayed(commentRunnable, 3000);
        timerHandler.postDelayed(timerRunnable, oneMinRate);


    }

    @Override
    protected void onStop() {
        super.onStop();
        commentHandler.removeCallbacks(commentRunnable);
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void initListnear() {
        binding.video.setOnClickListener(v -> binding.lytSheet.lytSheet.setVisibility(View.GONE));
        binding.lytbalance.setOnClickListener(v -> {
            CommenPopup commenPopup = new CommenPopup(this, "Hello, Jhon",
                    "To continue with Stacy\nwatch this video ad or\npurchase coins", "Purchase coins", "Watch Ad");
            commenPopup.showCloseButton(true);
            commenPopup.setPopupClickListnear(new CommenPopup.PopupClickListnear() {
                @Override
                public void onPositiveClick() {
                    Toast.makeText(LiveActivity.this, "Purchase click", Toast.LENGTH_SHORT).show();
                    commenPopup.closePopup();
                }

                @Override
                public void onNegativeClick() {
                    Toast.makeText(LiveActivity.this, "Ad click", Toast.LENGTH_SHORT).show();
                    commenPopup.closePopup();
                }

                @Override
                public void onCloseClick() {
                    commenPopup.closePopup();
                    onBackPressed();
                }
            });
        });
    }

    private void initView() {
        binding.rvEmojis.setAdapter(emojiAdapterHorizontol);
        binding.rvcomment.setAdapter(commntAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setVolume(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.setVolume(1);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.lytSheet.lytSheet.getVisibility() == View.VISIBLE) {
            binding.lytSheet.lytSheet.setVisibility(View.GONE);
        } else {
            myInterstitialAds.showAds();
            super.onBackPressed();
        }
    }

    public void onGiftClick(View view) {
        Log.d("TAG", view.toString());
        if (giftCategoryLoded) {
            binding.lytSheet.btnClose.setOnClickListener(v -> onBackPressed());

            binding.lytSheet.lytSheet.setVisibility(View.VISIBLE);
            BottomViewPagerAdapter bottomViewPagerAdapter = new BottomViewPagerAdapter(giftCategories);
            binding.lytSheet.viewpager.setAdapter(bottomViewPagerAdapter);

            binding.lytSheet.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.lytSheet.tablayout));
            binding.lytSheet.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    binding.lytSheet.viewpager.setCurrentItem(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
//
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    //ll
                }
            });


            bottomViewPagerAdapter.setEmojiListnerViewPager((bitmap, coin) -> {


                User user = sessionManager.getUser();
                if (Integer.parseInt(user.getMyWallet()) >= Integer.parseInt(coin)) {
                    callApiForLessCoin(coin);
                    JanuWork januWork = new JanuWork();
                    januWork.lessCoinLocal(LiveActivity.this, Integer.parseInt(coin));


                } else {
                    timerHandler.removeCallbacks(timerRunnable);
                    if (player != null) {
                        player.setPlayWhenReady(false);
                    }
                    NoBalancePopup noBalancePopup = new NoBalancePopup(LiveActivity.this, sessionManager.getUser().getMyWallet());
                    noBalancePopup.setPopupClickListnear(new NoBalancePopup.PopupClickListnear() {
                        @Override
                        public void onPlanItemClick(CoinPackageRoot.DataItem value) {
                            openPaymentSheet(value);
                        }

                        @Override
                        public void onDismiss() {
                            onBackPressed();
                        }
                    });
                }
                setMyWallet();


            });
        } else {
            getGiftCategory();
        }
    }

    private void setMyWallet() {
        String wallet = sessionManager.getUser().getMyWallet();
        binding.tvuserBalance.setText(wallet);
    }

    private void openPaymentSheet(CoinPackageRoot.DataItem dataItem) {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        BottomsheetPaymentmethodBinding paymentmethodBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottomsheet_paymentmethod, null, false);
        bottomSheetDialog.setContentView(paymentmethodBinding.getRoot());
        paymentmethodBinding.tvamount.setText(dataItem.getPrice());
        paymentmethodBinding.imgrazorpay.setOnClickListener(v -> {
            configRazorpay(dataItem);
            bottomSheetDialog.dismiss();

        });
/*        paymentmethodBinding.imggooglepay.setOnClickListener(v -> {
            configGooglePurchase(dataItem);
            bottomSheetDialog.dismiss();
        });*/
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);
        paymentmethodBinding.tvcancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
    }

    private void callApiForLessCoin(String rate) {
        ApiCallWork apiCallWork = new ApiCallWork(this);
        apiCallWork.lessCoin(rate);
        apiCallWork.setOnResponseListnear(new ApiCallWork.OnResponseListnear() {
            @Override
            public void onSuccess() {
//
            }

            @Override
            public void onFailure() {
                //
            }
        });
    }

    private void settabLayout(List<GiftCategoryRoot.DataItem> data) {
        binding.lytSheet.tablayout.removeAllTabs();
        binding.lytSheet.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (int i = 0; i < data.size(); i++) {

            binding.lytSheet.tablayout.addTab(binding.lytSheet.tablayout.newTab().setCustomView(createCustomView(data.get(i))));

        }
    }

    private View createCustomView(GiftCategoryRoot.DataItem dataItem) {

        View v = LayoutInflater.from(this).inflate(R.layout.custom_tabgift, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(dataItem.getGiftCatName());
        ImageView img = (ImageView) v.findViewById(R.id.imagetab);

        Glide.with(getApplicationContext())
                .load(Const.IMAGE_URL + dataItem.getGiftCatMedia()).circleCrop()
                .into(img);
        return v;

    }

    private void addSingleComment() {
        try {
            Log.d(TAG, "addSingleComment: size " + comments.size());
            if (comments.size() > 1) {
                List<String> names = new ArrayList<>();
                int number = rand.nextInt(names.size() - 1);
                String name = names.get(number);

                CommentRoot.DataItem comment = comments.get(0);
                comment.setName(name);
                commntAdapter.addSingleComment(comment);
                comments.remove(0);
                binding.rvcomment.scrollToPosition(commntAdapter.getItemCount() - 1);

            } else {
                getComments();
            }
        }catch (Exception e){

        }
    }

    public void onClickSend(View view) {
        Log.d(TAG, "onClickSend: " + view);
        if (!binding.etComment.getText().toString().equals("")) {
            CommentRoot.DataItem comment = new CommentRoot.DataItem();
            comment.setComment(binding.etComment.getText().toString());
            comment.setName(sessionManager.getUser().getFullName());
            commntAdapter.addSingleComment(comment);
            binding.rvcomment.scrollToPosition(commntAdapter.getItemCount() - 1);
            binding.etComment.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }

    }

    private void configRazorpay(Object dataitem) {


        if (checkout != null) {
            checkout.setImage(R.drawable.icon_round);
            try {
                Activity activity = this;
                JSONObject options = new JSONObject();

                options.put("name", sessionManager.getUser().getFullName());
                options.put("description", "user id: " + sessionManager.getUser().getUserId());
                options.put("image", "https://preview.pixlr.com/images/800wm/1117/1/1117101074.jpg");
                options.put("currency", "INR");



                if (dataitem instanceof CoinPackageRoot.DataItem) {
                    purchasedCoin = ((CoinPackageRoot.DataItem) dataitem).getCoinAmount();
                    Log.d(TAG, "razorpay : rate1= " + ((CoinPackageRoot.DataItem) dataitem).getPrice());
                    options.put("amount", 100 * Integer.parseInt(((CoinPackageRoot.DataItem) dataitem).getPrice()));
                } else {
                    purchasedCoin = ((OfferCoinPackageRoot.DataItem) dataitem).getCoinAmount();
                    Log.d(TAG, "razorpay : rate2= " + ((OfferCoinPackageRoot.DataItem) dataitem).getPrice());
                    options.put("amount", 100 * Integer.parseInt(((OfferCoinPackageRoot.DataItem) dataitem).getPrice()));

                }

                options.put("prefill.email", sessionManager.getUser().getUserEmail());
                options.put("prefill.contact", "0000000000");
                checkout.open(activity, options);


            } catch (Exception e) {
                Log.e(TAG, "Error in submitting payment details", e);
            }
        }
    }




    private void updateCoin(String purchasedCoin) {
        Log.d(TAG, "updateCoin: " + purchasedCoin);

        ApiCallWork apiCallWork = new ApiCallWork(this);
        apiCallWork.addCoin(sessionManager.getUser().getToken(), Integer.parseInt(purchasedCoin));
        apiCallWork.setOnResponseListnear(new ApiCallWork.OnResponseListnear() {
            @Override
            public void onSuccess() {
                JanuWork januWork = new JanuWork();
                boolean added = januWork.addCoinLocal(LiveActivity.this, Integer.parseInt(purchasedCoin));
                setMyWallet();
                Log.d(TAG, "onSuccess: coin added " + added);
            }

            @Override
            public void onFailure() {
//
            }
        });
    }







    @Override
    public void onPaymentSuccess(String s) {
        Log.d(TAG, "onPaymentSuccess: ");
        updateCoin(purchasedCoin);
        purchasedCoin = "";
        if (bottomSheetDialog != null) {
            bottomSheetDialog.cancel();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        purchasedCoin = "";
        Log.d(TAG, "onPaymentError: err razorpay " + s);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        onDestroy();
    }

    public void onClickReview(View view) {
        Log.d(TAG, "onClickReview: " + view);
        CallRejectPopup callRejectPopup = new CallRejectPopup(this, "Do you really want to\nReview this girl..?");
        callRejectPopup.setPopupClickListnear(new CallRejectPopup.PopupClickListnear() {
            @Override
            public void onContinueClick() {
                callRejectPopup.closePopup();
            }

            @Override
            public void onPositiveClik() {
                Toast.makeText(LiveActivity.this, "Thank You For Complain", Toast.LENGTH_SHORT).show();
                callRejectPopup.closePopup();
                onBackPressed();
            }
        });
    }
}