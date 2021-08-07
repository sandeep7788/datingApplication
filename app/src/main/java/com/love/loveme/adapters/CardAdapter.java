package com.love.loveme.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.ads.NativeAd;
import com.love.loveme.R;
import com.love.loveme.databinding.ItemGirlsBinding;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.CountryVideoListRoot;
import com.love.loveme.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

import static com.love.loveme.retrofit.Const.AD_FB_TYPE;
import static com.love.loveme.retrofit.Const.AD_TYPE;
import static com.love.loveme.retrofit.Const.POST_TYPE;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<Object> list = new ArrayList<>();
    OnCardClickLinstear onCardClickLinstear;
    private CountryVideoListRoot.DataItem datum;
    private CountryRoot.DataItem country;

    public OnCardClickLinstear getOnCardClickLinstear() {
        return onCardClickLinstear;
    }

    public void setOnCardClickLinstear(OnCardClickLinstear onCardClickLinstear) {
        this.onCardClickLinstear = onCardClickLinstear;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == AD_TYPE) {
            View unifiedNativeLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.admob_cat_cateroies,
                    parent, false);
            return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);

        } else if (viewType == AD_FB_TYPE) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.fb_cat_categories, parent, false);
            return new AdHolder(view);

        } else {
            return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_girls, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UnifiedNativeAdViewHolder) {
            UnifiedNativeAdViewHolder viewHolder = (UnifiedNativeAdViewHolder) holder;
            com.google.android.gms.ads.nativead.NativeAd nativeAd = (com.google.android.gms.ads.nativead.NativeAd) list.get(position);
            viewHolder.populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
        } else if (holder instanceof AdHolder) {
            AdHolder adHolder = (AdHolder) holder;
            adHolder.adChoicesContainer.removeAllViews();
            NativeAd ad = (NativeAd) list.get(position);
            adHolder.showAds(ad);
        } else if (holder instanceof CardViewHolder) {
            CardViewHolder holder1 = (CardViewHolder) holder;

            holder1.setdata(position);
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<CountryVideoListRoot.DataItem> data) {
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i));
            notifyItemInserted(list.size() - 1);
        }
    }


    public void addNewAds(int index, com.google.android.gms.ads.nativead.NativeAd ad) {
        if (!list.isEmpty() && index < list.size()) {
            list.add(index, ad);
            notifyItemInserted(index);
        }
    }

    public void addFBAds(int index, NativeAd nextNativeAd) {
        if (!list.isEmpty() && index < list.size()) {
            list.add(index, nextNativeAd);
            notifyItemInserted(index);
        }
    }

    public List<Object> getList() {
        return list;
    }

    public void setCountry(CountryRoot.DataItem country) {

        this.country = country;
    }

    public void clearData() {
        list.clear();
        list = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof com.google.android.gms.ads.nativead.NativeAd) {
            return AD_TYPE;
        } else if (list.get(position) instanceof NativeAd) {
            return AD_FB_TYPE;
        } else {
            return POST_TYPE;
        }
    }

    public interface OnCardClickLinstear {
        void onCardClick(int pos, ImageView imagegirl);
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        ItemGirlsBinding binding;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemGirlsBinding.bind(itemView);
        }

        public void setdata(int position) {
            if (list.get(position) instanceof CountryVideoListRoot.DataItem) {
                datum = (CountryVideoListRoot.DataItem) list.get(position);
                datum.setCountry(country);
                binding.imagegirl.setTransitionName(String.valueOf(position));
                Log.d("TAG", "setdata: adapter " + datum.getThumbImg());
                Glide.with(context).load(Const.IMAGE_URL + datum.getThumbImg()).circleCrop().into(binding.imagegirl);
                Glide.with(context).load(Const.IMAGE_URL + country.getCountryMedia()).circleCrop().into(binding.imgcountry);
                binding.tvName.setText(datum.getName());
                binding.tvcountryName.setText(datum.getCountry().getCountry());


                binding.imagegirl.setOnClickListener(v -> onCardClickLinstear.onCardClick(position, binding.imagegirl));
                binding.item.setOnClickListener(v -> onCardClickLinstear.onCardClick(position, binding.imagegirl));
                binding.lytdetail.setOnClickListener(v -> onCardClickLinstear.onCardClick(position, binding.imagegirl));
            }
        }
    }
}
