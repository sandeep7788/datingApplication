package com.love.loveme.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.love.loveme.R;
import com.love.loveme.databinding.ItemEmojiHorizontolBinding;
import com.love.loveme.models.GiftRoot;
import com.love.loveme.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class EmojiAdapterHorizontol extends RecyclerView.Adapter<EmojiAdapterHorizontol.EmojiViewHolder> {
    private Context context;
    private List<GiftRoot.DataItem> gifts = new ArrayList<>();
    OnEmojiClickListnear onEmojiClickListnear;
    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EmojiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji_horizontol, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        Glide.with(context).load(Const.IMAGE_URL + gifts.get(position).getGiftMedia()).circleCrop().into(holder.binding.imgEmoji);
        holder.binding.tvCoin.setText(gifts.get(position).getCoins());
        holder.binding.itememoji.setOnClickListener(v -> onEmojiClickListnear.onEmojiClick(null, gifts.get(position).getCoins()));
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public void addData(List<GiftRoot.DataItem> data) {
        for(int i = 0; i < data.size(); i++) {
            gifts.add(data.get(i));
            notifyItemInserted(gifts.size() - 1);
        }
    }

    public OnEmojiClickListnear getOnEmojiClickListnear() {
        return onEmojiClickListnear;
    }

    public void setOnEmojiClickListnear(OnEmojiClickListnear onEmojiClickListnear) {
        this.onEmojiClickListnear = onEmojiClickListnear;
    }

    public interface OnEmojiClickListnear {
        void onEmojiClick(Bitmap bitmap, String coin);
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        ItemEmojiHorizontolBinding binding;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemEmojiHorizontolBinding.bind(itemView);
        }
    }
}
