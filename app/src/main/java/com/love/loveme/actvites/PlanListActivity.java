package com.love.loveme.actvites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.love.loveme.JanuWork;
import com.love.loveme.R;
import com.love.loveme.SessionManager;
import com.love.loveme.adapters.CoinPlanAdapter;
import com.love.loveme.adapters.DotAdapter;
import com.love.loveme.adapters.OfferCoinPlanAdapter;
import com.love.loveme.adapters.ProductImageAdapter;
import com.love.loveme.databinding.ActivityPlanListBinding;
import com.love.loveme.databinding.BottomsheetPaymentmethodBinding;
import com.love.loveme.models.BrandingImageRoot;
import com.love.loveme.models.CoinPackageRoot;
import com.love.loveme.models.OfferCoinPackageRoot;
import com.love.loveme.retrofit.ApiCallWork;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.MyInterstitialAds;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanListActivity extends BaseActivity implements PaymentResultListener {
    private static final String TAG = "planlistact";
    ActivityPlanListBinding binding;
    private List<BrandingImageRoot.DataItem> images;
    Checkout checkout = new Checkout();
    private BottomSheetDialog bottomSheetDialog;
    SessionManager sessionManager;
    private String purchasedCoin = "0";
    private MyInterstitialAds myInterstitialAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_list);

        sessionManager = new SessionManager(this);

        initRazorPay();
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();

        getData();

        myInterstitialAds = new MyInterstitialAds(this);
    }

    private void initRazorPay() {

        Checkout.preload(this);

        checkout.setKeyID(getResources().getString(R.string.rzp_key));
    }



    private void getData() {

        Call<BrandingImageRoot> call = RetrofitBuilder.create().getBrandingImages(Const.DEV_KEY);
        call.enqueue(new Callback<BrandingImageRoot>() {
            @Override
            public void onResponse(Call<BrandingImageRoot> call, Response<BrandingImageRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    binding.lytMain.setVisibility(View.VISIBLE);
                    images = response.body().getData();

                    setPlanImages();
                }

            }

            @Override
            public void onFailure(Call<BrandingImageRoot> call, Throwable t) {
//
            }
        });

        Call<CoinPackageRoot> call1 = RetrofitBuilder.create().getCoinPackages(Const.DEV_KEY);
        call1.enqueue(new Callback<CoinPackageRoot>() {
            @Override
            public void onResponse(Call<CoinPackageRoot> call, Response<CoinPackageRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && !response.body().getData().isEmpty()) {
                    CoinPlanAdapter coinPlanAdapter = new CoinPlanAdapter(response.body().getData(), PlanListActivity.this::openBottomSheet);
                    binding.rvplans.setAdapter(coinPlanAdapter);

                    new Handler().postDelayed(() -> binding.shimmer.setVisibility(View.GONE), 1500);

                }
            }

            @Override
            public void onFailure(Call<CoinPackageRoot> call, Throwable t) {
//
            }
        });


        Call<OfferCoinPackageRoot> call2 = RetrofitBuilder.create().getOfferCoinsPakages(Const.DEV_KEY);
        call2.enqueue(new Callback<OfferCoinPackageRoot>() {
            @Override
            public void onResponse(Call<OfferCoinPackageRoot> call, Response<OfferCoinPackageRoot> response) {
                OfferCoinPlanAdapter coinPlanAdapter = new OfferCoinPlanAdapter(response.body().getData(), PlanListActivity.this::openBottomSheet);
                binding.rvofferplan.setAdapter(coinPlanAdapter);
            }

            @Override
            public void onFailure(Call<OfferCoinPackageRoot> call, Throwable t) {
//
            }
        });
    }

    private void openBottomSheet(Object dataitem) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        BottomsheetPaymentmethodBinding paymentmethodBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottomsheet_paymentmethod, null, false);
        bottomSheetDialog.setContentView(paymentmethodBinding.getRoot());

        if(dataitem instanceof CoinPackageRoot.DataItem) {

            paymentmethodBinding.tvamount.setText(((CoinPackageRoot.DataItem) dataitem).getPrice());
        } else {
            paymentmethodBinding.tvamount.setText(((OfferCoinPackageRoot.DataItem) dataitem).getPrice());
        }

        paymentmethodBinding.imgrazorpay.setOnClickListener(v -> {

            configRazorpay(dataitem);
            bottomSheetDialog.dismiss();

        });
/*        paymentmethodBinding.imggooglepay.setOnClickListener(v -> {

            configGooglePurchase(dataitem);
            bottomSheetDialog.dismiss();
        });*/
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);
        paymentmethodBinding.tvcancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
    }

    private void configRazorpay(Object dataitem) {


        if(checkout != null) {
            checkout.setImage(R.drawable.icon_round);
            try {
                Activity activity = this;
                JSONObject options = new JSONObject();

                options.put("name", sessionManager.getUser().getFullName());
                options.put("description", "user id: " + sessionManager.getUser().getUserId());
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("currency", "INR");

                if(dataitem instanceof CoinPackageRoot.DataItem) {
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


    private void setPlanImages() {
        DotAdapter dotAdapter = new DotAdapter(images);
        binding.rvdots.setAdapter(dotAdapter);

        ProductImageAdapter productImageAdapter = new ProductImageAdapter(images);
        binding.rvphotos.setAdapter(productImageAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvphotos);

        binding.rvphotos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager myLayoutManager = (LinearLayoutManager) binding.rvphotos.getLayoutManager();
                int scrollPosition = myLayoutManager.findFirstVisibleItemPosition();
                dotAdapter.changeDot(scrollPosition);
            }
        });


    }

    public void onClickClose(View view) {
        Log.d(TAG, "onClickClose: " + view);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myInterstitialAds.showAds();


    }


    private void updateCoin(String purchasedCoin) {
        Log.d(TAG, "updateCoin: " + purchasedCoin);

        ApiCallWork apiCallWork = new ApiCallWork(this);
        apiCallWork.addCoin(sessionManager.getUser().getToken(), Integer.parseInt(purchasedCoin));
        apiCallWork.setOnResponseListnear(new ApiCallWork.OnResponseListnear() {
            @Override
            public void onSuccess() {
                JanuWork januWork = new JanuWork();
                boolean added = januWork.addCoinLocal(PlanListActivity.this, Integer.parseInt(purchasedCoin));
                Log.d(TAG, "onSuccess: coin added " + added);
            }

            @Override
            public void onFailure() {
//
            }
        });
    }




    public void onBillingError(int errorCode, Throwable error) {
        purchasedCoin = "";
        if(error != null) {
            Log.d(TAG, "onBillingError: google err " + error.getMessage());
        }
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
}