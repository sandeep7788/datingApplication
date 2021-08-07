package com.love.loveme.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.love.loveme.R;
import com.love.loveme.databinding.ItemProductImageBinding;
import com.love.loveme.models.BrandingImageRoot;
import com.love.loveme.retrofit.Const;

import java.util.List;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.MyViewHolder> {
    private List<BrandingImageRoot.DataItem> productImage;


    public ProductImageAdapter(List<BrandingImageRoot.DataItem> productImage) {


        this.productImage = productImage;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BrandingImageRoot.DataItem image = productImage.get(position);
        Glide.with(holder.binding.getRoot().getContext())
                .load(Const.IMAGE_URL + image.getImage())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.binding.pBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.binding.ivProduct);

//ll
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_image, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return productImage.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductImageBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductImageBinding.bind(itemView);
        }
    }
}

