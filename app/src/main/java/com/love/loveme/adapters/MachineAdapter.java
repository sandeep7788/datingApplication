package com.love.loveme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.love.loveme.R;
import com.love.loveme.databinding.ItemMachineBinding;

import java.util.ArrayList;
import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {
    private Context context;
    private List<Integer> girllist1 = new ArrayList<>();

    public MachineAdapter(List<Integer> girllist1) {

        this.girllist1 = girllist1;
    }

    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MachineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_machine, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class MachineViewHolder extends RecyclerView.ViewHolder {
        ItemMachineBinding binding;

        public MachineViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMachineBinding.bind(itemView);
        }

        public void setData(int position) {

            Glide.with(context).load(girllist1.get(position)).circleCrop().into(binding.imageview);
        }
    }
}
