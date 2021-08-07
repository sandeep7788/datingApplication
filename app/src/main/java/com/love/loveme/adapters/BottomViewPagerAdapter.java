package com.love.loveme.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.love.loveme.R;
import com.love.loveme.databinding.ItemEmojiviewpagerBinding;
import com.love.loveme.models.GiftCategoryRoot;
import com.love.loveme.models.GiftRoot;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomViewPagerAdapter extends PagerAdapter {
    EmojiListnerViewPager emojiListnerViewPager;
    private List<GiftCategoryRoot.DataItem> giftCategories = new ArrayList<>();

    public BottomViewPagerAdapter(List<GiftCategoryRoot.DataItem> giftCategories) {

        this.giftCategories = giftCategories;
    }


    public EmojiListnerViewPager getEmojiListnerViewPager() {
        return emojiListnerViewPager;
    }

    public void setEmojiListnerViewPager(EmojiListnerViewPager emojiListnerViewPager) {
        this.emojiListnerViewPager = emojiListnerViewPager;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_emojiviewpager, container, false);

        Log.d("TAG", "instantiateItem: ");
        RecyclerView recyclerView = view.findViewById(R.id.rvEmojiSheet);
        EmojiAdapter emojiAdapter = new EmojiAdapter();
        recyclerView.setAdapter(emojiAdapter);

        int giftStart = 0;
        ItemEmojiviewpagerBinding binding = DataBindingUtil.bind(view);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.rvEmojiSheet.setVisibility(View.GONE);
        Call<GiftRoot> call = RetrofitBuilder.create().getGifts(Const.DEV_KEY, giftCategories.get(position).getGiftCatId(), giftStart, Const.COUNT);
        call.enqueue(new Callback<GiftRoot>() {
            @Override
            public void onResponse(Call<GiftRoot> call, Response<GiftRoot> response) {
                if (response.code() == 200) {
                    Log.d("TAG", "onResponse: true");
                    if (response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {

                        binding.shimmer.setVisibility(View.GONE);
                        binding.rvEmojiSheet.setVisibility(View.VISIBLE);
                        emojiAdapter.addData(response.body().getData());
                    }
                }
            }


            @Override
            public void onFailure(Call<GiftRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        emojiAdapter.setOnEmojiClickListnear((image, coin) -> emojiListnerViewPager.emojilistnerViewpager(image, coin));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);

    }

    @Override
    public int getCount() {
        return giftCategories.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public interface EmojiListnerViewPager {
        void emojilistnerViewpager(String bitmap, String coin);
    }
}
