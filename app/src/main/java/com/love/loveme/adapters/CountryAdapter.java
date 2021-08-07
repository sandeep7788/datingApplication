package com.love.loveme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.love.loveme.R;
import com.love.loveme.databinding.ItemCountryBinding;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    int selectPos = 0;
    private Context context;
    OnCountryClickListnear onCountryClickListnear;
    private List<CountryRoot.DataItem> list = new ArrayList<>();

    public OnCountryClickListnear getOnCountryClickListnear() {
        return onCountryClickListnear;
    }

    public void setOnCountryClickListnear(OnCountryClickListnear onCountryClickListnear) {
        this.onCountryClickListnear = onCountryClickListnear;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryRoot.DataItem data = list.get(position);
        holder.binding.tvTab.setText(data.getCountry());
        Glide.with(context).load(Const.IMAGE_URL + data.getCountryMedia()).circleCrop().into(holder.binding.imagetab);
        if(position == selectPos) {
            holder.binding.lytcountry.setBackground(ContextCompat.getDrawable(context, R.drawable.tab_layout_fg));
        } else {
            holder.binding.lytcountry.setBackground(ContextCompat.getDrawable(context, R.drawable.tab_layout_bg));
        }


        holder.itemView.setOnClickListener(v -> {
            onCountryClickListnear.onCountryClick(data);
            selectPos = position;
            notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<CountryRoot.DataItem> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnCountryClickListnear {
        void onCountryClick(CountryRoot.DataItem dataItem);
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {
        ItemCountryBinding binding;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCountryBinding.bind(itemView);
        }
    }
}
