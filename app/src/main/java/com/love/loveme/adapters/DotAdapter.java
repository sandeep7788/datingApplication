package com.love.loveme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.love.loveme.R;
import com.love.loveme.databinding.ItemDotBinding;
import com.love.loveme.models.BrandingImageRoot;

import java.util.List;

public class DotAdapter extends RecyclerView.Adapter<DotAdapter.DotViewHolder> {
    private Context context;
    private int seleccted = 0;
    private List<BrandingImageRoot.DataItem> productImage;

    public DotAdapter(List<BrandingImageRoot.DataItem> productImage) {

        this.productImage = productImage;
    }

    @NonNull
    @Override
    public DotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DotViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DotViewHolder holder, int position) {

        if (seleccted == position) {
            holder.binding.imgdot.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_gredent_pinkround));
        } else {
            holder.binding.imgdot.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_white_round));
        }
    }

    @Override
    public int getItemCount() {
        return productImage.size();
    }

    public void changeDot(int scrollPosition) {
        seleccted = scrollPosition;
        notifyDataSetChanged();
    }

    public class DotViewHolder extends RecyclerView.ViewHolder {
        ItemDotBinding binding;

        public DotViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemDotBinding.bind(itemView);
        }
    }
}

