package com.love.loveme.actvites;

import android.app.Activity;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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
import com.love.loveme.adapters.ChatAdapter;
import com.love.loveme.databinding.ActivityChatBinding;
import com.love.loveme.databinding.BottomsheetPaymentmethodBinding;
import com.love.loveme.databinding.ItemChatGirlBinding;
import com.love.loveme.models.CoinPackageRoot;
import com.love.loveme.models.GiftCategoryRoot;
import com.love.loveme.models.MessageRoot;
import com.love.loveme.models.MessageUserRoot;
import com.love.loveme.models.OfferCoinPackageRoot;
import com.love.loveme.models.User;
import com.love.loveme.popups.NoBalancePopup;
import com.love.loveme.retrofit.ApiCallWork;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;

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

public class ChatActivity extends BaseActivity implements  PaymentResultListener {
    private static final String TAG = "chatact";
    ActivityChatBinding binding;
    ChatAdapter chatAdapter = new ChatAdapter();
    private MessageUserRoot.DataItem girl;
    Handler handler = new Handler();
    Checkout checkout = new Checkout();
    private List<GiftCategoryRoot.DataItem> giftCategories = new ArrayList<>();
    private SessionManager sessionManager;
    private BottomSheetDialog bottomSheetDialog;
    private String purchasedCoin = "";
    private int oldPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        sessionManager = new SessionManager(this);
        List<MessageRoot.DataItem> messages = MainActivity.messages;
        binding.rvChat.setAdapter(chatAdapter);


        String girlStr = getIntent().getStringExtra("girl");
        if (girlStr != null && !girlStr.equals("")) {
            girl = new Gson().fromJson(girlStr, MessageUserRoot.DataItem.class);
            if (messages.isEmpty()) {
                //
            } else {
                setData();
                initListnear();
            }
            binding.lytTyping.setVisibility(View.GONE);
            
            initRazorPay();
            getGiftCategory();
        }

        binding.ivBack.setOnClickListener(v -> onBackPressed());
    }

    private void initRazorPay() {

        Checkout.preload(this);

        checkout.setKeyID(getResources().getString(R.string.rzp_key));
    }




    ExoPlayer player;
    ItemChatGirlBinding lastDataBinding;

    private void initListnear() {
        binding.btnSend.setOnClickListener(v -> {
            String userMsg = binding.etMessage.getText().toString();
            if (!userMsg.equals("")) {
                MyChat myChat = new MyChat();
                myChat.setRobot(false);
                myChat.setType("3");
                myChat.setContent("text");
                myChat.setMessageFileText(userMsg);
                chatAdapter.addData(myChat);
                binding.etMessage.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                binding.lytTyping.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    fetchNewMessage();
                    binding.lytTyping.setVisibility(View.GONE);
                }, (new Random().nextInt(4 - 1) + 1) * 1000L);

            }
        });
        chatAdapter.setOnChatAdapterListnear((myChat, position, chatBinding) -> {

            if (lastDataBinding != null) {
                lastDataBinding.imgPlay.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this, R.drawable.exo_icon_play));
                lastDataBinding.anim.pauseAnimation();
            }

            Player.EventListener listener = new Player.EventListener() {
                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.d(TAG, "onPlayerError: " + error.getLocalizedMessage());

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case STATE_BUFFERING:
                            Log.d(TAG, "buffer: ");
                            break;
                        case STATE_ENDED:
                            player.setRepeatMode(Player.REPEAT_MODE_ALL);
                            Log.d(TAG, "end: ");
                            break;
                        case STATE_IDLE:
                            Log.d(TAG, "idle: ");
                            break;

                        case STATE_READY:
                            chatBinding.imgPlay.setVisibility(View.VISIBLE);
                            chatBinding.imgPlay.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this, R.drawable.exo_icon_pause));
                            chatBinding.anim.playAnimation();
                            Log.d(TAG, "ready: ");

                            break;
                        default:
                            break;
                    }
                }
            };
            if (myChat.isPlaying()) {
                chatAdapter.getList().get(position).setPlaying(false);

                if (player != null) {
                    player.setPlayWhenReady(false);
                    player.removeListener(listener);
                    player.release();
                }
                if (lastDataBinding != null) {
                    lastDataBinding.imgPlay.setImageResource(R.drawable.exo_icon_play);
                    lastDataBinding.anim.pauseAnimation();
                }
            } else {
                if (player != null) {
                    player.setPlayWhenReady(false);
                    player.removeListener(listener);
                    player.release();
                    chatAdapter.getList().get(oldPos).setPlaying(false);
                }
                if (lastDataBinding != null) {
                    lastDataBinding.imgPlay.setImageResource(R.drawable.exo_icon_play);
                    lastDataBinding.anim.pauseAnimation();
                }
                chatAdapter.getList().get(position).setPlaying(true);
                player = new SimpleExoPlayer.Builder(ChatActivity.this).build();
                Uri uri = Uri.parse(Const.IMAGE_URL + myChat.getMessageFileText());
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(ChatActivity.this,
                        Util.getUserAgent(ChatActivity.this, ChatActivity.this.getResources().getString(R.string.app_name)));
                MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);

                Log.d(TAG, "initializePlayer: " + uri);
                player.prepare(videoSource);
                player.addListener(listener);
                player.setPlayWhenReady(true);


            }


            oldPos = position;
            lastDataBinding = chatBinding;

        });
    }

    @Override
    protected void onPause() {
        if (player != null) {
            player.release();
            player = null;
        }
        super.onPause();
    }

    private void fetchNewMessage() {
        MyChat tempMessage = getSingleMessage();
        chatAdapter.addData(tempMessage);
        binding.rvChat.smoothScrollToPosition(chatAdapter.getList().size() - 1);
    }

    public void onClickBack(View view) {
        Log.d(TAG, "onClickReview: " + view);
        onBackPressed();
    }

    public void onClickinfo(View view) {
        Log.d(TAG, "onClickReview: " + view);
    }

    @Override
    public void onBackPressed() {
        if (binding.lytSheet.lytSheet.getVisibility() == View.VISIBLE) {
            binding.lytSheet.lytSheet.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void setData() {
        Glide.with(this).load(Const.IMAGE_URL + girl.getImage()).circleCrop().into(binding.imgprofile);
        Glide.with(this).load(Const.IMAGE_URL + girl.getImage()).circleCrop().into(binding.imgGirl2);
        binding.tvName.setText(girl.getName());
        chatAdapter.setGirl(girl);

        fetchNewMessage();

    }

    private MyChat getSingleMessage() {
        if (!MainActivity.messages.isEmpty()) {
            int randomNumber = new Random().nextInt(MainActivity.messages.size());
            MessageRoot.DataItem tempchat = MainActivity.messages.get(randomNumber);
            MyChat myChat = new MyChat();
            myChat.setContent(tempchat.getContent());
            myChat.setMessageFileText(tempchat.getMessageFileText());
            Log.d("TAG", "getSingleMessage: type " + tempchat.getType());
            myChat.setType(tempchat.getType());
            myChat.setRobot(true);

            return myChat;
        } else {
            return null;
        }


    }

    private void getGiftCategory() {
        giftCategories.clear();
        Call<GiftCategoryRoot> call = RetrofitBuilder.create().getGiftCategory(Const.DEV_KEY);
        call.enqueue(new Callback<GiftCategoryRoot>() {


            @Override
            public void onResponse(Call<GiftCategoryRoot> call, Response<GiftCategoryRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    giftCategories = response.body().getData();
                    settabLayout(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GiftCategoryRoot> call, Throwable t) {
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

    public void onGiftClick(View view) {
        Log.d(TAG, "onGiftClick: " + view);
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
        bottomViewPagerAdapter.setEmojiListnerViewPager((giftEmoji, coin) -> {

            User user = sessionManager.getUser();
            if (Integer.parseInt(user.getMyWallet()) >= Integer.parseInt(coin)) {
                callApiForLessCoin(coin);
                JanuWork januWork = new JanuWork();
                januWork.lessCoinLocal(this, Integer.parseInt(coin));
                onBackPressed();
                sendGiftToAdapter(giftEmoji, coin);
            } else {

                NoBalancePopup noBalancePopup = new NoBalancePopup(this, sessionManager.getUser().getMyWallet());
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


        });

        binding.lytSheet.btnClose.setOnClickListener(v -> onBackPressed());

    }

    private void sendGiftToAdapter(String giftEmoji, String coin) {
        MyChat myChat = new MyChat();
        myChat.setRobot(false);
        myChat.setType("4");
        myChat.setBitmap(giftEmoji);
        myChat.setContent(coin + " Coins");
        myChat.setMessageFileText(sessionManager.getUser().getFullName() + " Send a gift ");
        chatAdapter.addData(myChat);
        binding.etMessage.setText("");


        String[] ans = {"Thanks 😍", "Aww thankyou dear 😘", "Thanks for gift 💕"};
        int random = new Random().nextInt(3);
        Log.d(TAG, "sendGiftToAdapter: random==" + random);

        binding.lytTyping.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            MyChat myChat1 = new MyChat();
            myChat1.setType("3");
            myChat1.setContent("text");
            myChat1.setMessageFileText(ans[random]);
            myChat1.setRobot(true);
            chatAdapter.addData(myChat1);
            binding.lytTyping.setVisibility(View.GONE);
        }, (random + 1) * 1000L);
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
        bottomSheetDialog.setContentView(paymentmethodBinding.getRoot());
        paymentmethodBinding.tvamount.setText(dataItem.getPrice());
        paymentmethodBinding.imgrazorpay.setOnClickListener(v -> {
            configRazorpay(dataItem);
            bottomSheetDialog.dismiss();

        });
/*
        paymentmethodBinding.imggooglepay.setOnClickListener(v -> {
            configGooglePurchase(dataItem);
            bottomSheetDialog.dismiss();
        });
*/
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);
        paymentmethodBinding.tvcancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
    }

    private void configRazorpay(Object dataitem) {


        if (checkout != null) {
            checkout.setImage(R.drawable.icon_round);
            try {
                Activity activity = this;
                JSONObject options = new JSONObject();

                options.put("name", sessionManager.getUser().getFullName());
                options.put("description", "user id: " + sessionManager.getUser().getUserId());
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
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



    private void updateCoin(String purchasedCoin) {
        Log.d(TAG, "updateCoin: " + purchasedCoin);

        ApiCallWork apiCallWork = new ApiCallWork(this);
        apiCallWork.addCoin(sessionManager.getUser().getToken(), Integer.parseInt(purchasedCoin));
        apiCallWork.setOnResponseListnear(new ApiCallWork.OnResponseListnear() {
            @Override
            public void onSuccess() {
                JanuWork januWork = new JanuWork();
                boolean added = januWork.addCoinLocal(ChatActivity.this, Integer.parseInt(purchasedCoin));
                Log.d(TAG, "onSuccess: coin added " + added);
            }

            @Override
            public void onFailure() {
//
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d(TAG, "onPaymentSuccess: ");
        updateCoin(purchasedCoin);
        purchasedCoin = "";
    }

    @Override
    public void onPaymentError(int i, String s) {
        purchasedCoin = "";
        Log.d(TAG, "onPaymentError: err razorpay " + s);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public class MyChat {
        boolean isRobot = false;
        private String type;
        private String messageFileText;
        private String content;
        boolean isPlaying = false;
        String giftImg;

        public String getBitmap() {
            return giftImg;
        }

        public void setBitmap(String bitmap) {
            this.giftImg = bitmap;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public boolean isRobot() {
            return isRobot;
        }

        public void setRobot(boolean robot) {
            isRobot = robot;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessageFileText() {
            return messageFileText;
        }

        public void setMessageFileText(String messageFileText) {
            this.messageFileText = messageFileText;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}