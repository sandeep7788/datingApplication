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
import com.love.loveme.databinding.ItemEmojiBinding;
import com.love.loveme.models.GiftRoot;
import com.love.loveme.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    OnEmojiClickListnear onEmojiClickListnear;

    private Context contect;


    public OnEmojiClickListnear getOnEmojiClickListnear() {
        return onEmojiClickListnear;
    }

    public void setOnEmojiClickListnear(OnEmojiClickListnear onEmojiClickListnear) {
        this.onEmojiClickListnear = onEmojiClickListnear;
    }

    private List<GiftRoot.DataItem> gifts = new ArrayList<>();

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        contect = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        holder.binding.itememoji.setBackground(ContextCompat.getDrawable(contect, R.drawable.bg_pinkline10dp));

        Glide.with(contect).load(Const.IMAGE_URL + gifts.get(position).getGiftMedia()).circleCrop().into(holder.binding.imgEmoji);
        holder.binding.tvCoin.setText(gifts.get(position).getCoins());

        holder.binding.itememoji.setOnClickListener(v -> onEmojiClickListnear.onEmojiClick(gifts.get(position).getGiftMedia(), gifts.get(position).getCoins()));
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

    public interface OnEmojiClickListnear {
        void onEmojiClick(String image, String coin);
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        ItemEmojiBinding binding;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemEmojiBinding.bind(itemView);
        }
    }
}
