package com.love.loveme.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.facebook.ads.NativeAd;
import com.google.gson.Gson;
import com.love.loveme.R;
import com.love.loveme.SessionManager;
import com.love.loveme.actvites.ChatActivity;
import com.love.loveme.actvites.LiveActivity;
import com.love.loveme.adapters.CardAdapter;
import com.love.loveme.adapters.CountryAdapter;
import com.love.loveme.databinding.FragmentStrimmingBinding;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.CountryVideoListRoot;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.MultipleCustomNativeAds;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StrimmingFragment extends Fragment {

    FragmentStrimmingBinding binding;
    CountryAdapter countryAdapter = new CountryAdapter();
    CardAdapter girlsAdapter = new CardAdapter();
    private int start = 0;
    SessionManager sessionManager;
    private Call<CountryVideoListRoot> call;

    public StrimmingFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_strimming, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            sessionManager = new SessionManager(getActivity());


            initView();
            getCountryData();

            binding.imgMessage.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChatActivity.class)));
        }
    }


    private void getCountryData() {
        binding.shimmerCountry.startShimmer();
        binding.shimmerCountry.setVisibility(View.VISIBLE);
        List<CountryRoot.DataItem> countries = sessionManager.getCountry();
        if (countries != null) {
            countryAdapter.addData(countries);
            getGirlsList(countries.get(0));
            initListnear();
            binding.shimmerCountry.setVisibility(View.GONE);
        }
    }

    private void initListnear() {
        countryAdapter.setOnCountryClickListnear(dataItem -> {
            girlsAdapter.clearData();
            getGirlsList(dataItem);
        });
    }

    private void getGirlsList(CountryRoot.DataItem dataItem) {
        binding.shimmercard.setVisibility(View.VISIBLE);
        binding.shimmercard.startShimmer();
        Log.d("TAG", "getGirlsList: " + dataItem.getCountry());


        if (call != null) {
            call.cancel();
        }
        call = RetrofitBuilder.create().getGirlsList(Const.DEV_KEY, dataItem.getCountryId(), start, Const.COUNT);
        call.enqueue(new Callback<CountryVideoListRoot>() {
            @Override
            public void onResponse(Call<CountryVideoListRoot> call, Response<CountryVideoListRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                        girlsAdapter.setCountry(dataItem);
                        girlsAdapter.addData(response.body().getData());
                        binding.shimmercard.setVisibility(View.GONE);
                        girlsAdapter.setOnCardClickLinstear((pos, imagegirl) -> {
                            nextActivity(pos, imagegirl);
                            Log.d("TAG", "onResponse: " + imagegirl);
                        });
                        loadNativeAds();
                    } else {
                        Log.d("TAG", "onResponse: data is 1null");
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryVideoListRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: country" + t.getMessage());
            }
        });

    }

    private void nextActivity(int pos, ImageView imagegirl) {

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(imagegirl, String.valueOf(pos));
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(new Intent(getActivity(), LiveActivity.class)
                        .putExtra("girl", new Gson().toJson(girlsAdapter.getList().get(pos)))
                        .putExtra("position", String.valueOf(pos))
                , activityOptions.toBundle());


    }

    private void loadNativeAds() {
        new MultipleCustomNativeAds(getActivity(), (adsData, position) -> {
            if (girlsAdapter != null) {
                if (adsData instanceof com.google.android.gms.ads.nativead.NativeAd) {
                    girlsAdapter.addNewAds(position, (com.google.android.gms.ads.nativead.NativeAd) adsData);
                } else if (adsData instanceof NativeAd) {
                    girlsAdapter.addFBAds(position, (NativeAd) adsData);
                }
                return position < girlsAdapter.getList().size();
            }
            return true;
        }, 2);
    }


    private void initView() {
        binding.rvCountry.setAdapter(countryAdapter);


        CardStackView cardStackView = binding.cardStackView;
        CardStackLayoutManager manager = new CardStackLayoutManager(getActivity());

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(2);
        manager.setTranslationInterval(1f);
        manager.setScaleInterval(1f);
        manager.setSwipeThreshold(0f);
        manager.setMaxDegree(30.0f);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);

        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .build();

        manager.setSwipeAnimationSetting(setting);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(girlsAdapter);
    }


}