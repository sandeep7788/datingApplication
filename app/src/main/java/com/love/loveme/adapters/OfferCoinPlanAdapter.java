package com.love.loveme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.love.loveme.R;
import com.love.loveme.databinding.ItemCoinplansBinding;
import com.love.loveme.models.OfferCoinPackageRoot;

import java.util.ArrayList;
import java.util.List;

public class OfferCoinPlanAdapter extends RecyclerView.Adapter<OfferCoinPlanAdapter.CoinPlanViewHolder> {
    private Context context;
    private List<OfferCoinPackageRoot.DataItem> data = new ArrayList<>();
    private OnOfferCoinPlanClickListnear onOfferCoinPlanClickListnear;


    public OfferCoinPlanAdapter(List<OfferCoinPackageRoot.DataItem> data,
                                OnOfferCoinPlanClickListnear onOfferCoinPlanClickListnear) {
        this.data = data;

        this.onOfferCoinPlanClickListnear = onOfferCoinPlanClickListnear;
    }

    @Override
    public void onBindViewHolder(@NonNull CoinPlanViewHolder holder, int position) {
        OfferCoinPackageRoot.DataItem plan = data.get(position);
        holder.binding.lytmain.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_white_pinklight_10dp));
        if (position % 2 == 0) {
            holder.binding.tvamount.setTextColor(ContextCompat.getColor(context, R.color.black2));
            holder.binding.tvcoin.setTextColor(ContextCompat.getColor(context, R.color.black2));
            holder.binding.tvcoin.setText(plan.getCoinAmount());
            holder.binding.tvamount.setText("$" + plan.getPrice());
        } else {
            holder.binding.tvcoin.setText(plan.getCoinAmount());
            holder.binding.tvamount.setText("$" + plan.getPrice());
        }

        holder.itemView.setOnClickListener(v -> onOfferCoinPlanClickListnear.onOfferPlanClick(plan));
    }

    @NonNull
    @Override
    public CoinPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CoinPlanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coinplans, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnOfferCoinPlanClickListnear {
        void onOfferPlanClick(OfferCoinPackageRoot.DataItem amount);
    }

    public class CoinPlanViewHolder extends RecyclerView.ViewHolder {
        ItemCoinplansBinding binding;

        public CoinPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCoinplansBinding.bind(itemView);
        }
    }
}
