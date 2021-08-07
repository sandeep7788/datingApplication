package com.love.loveme.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.love.loveme.R;
import com.love.loveme.SessionManager;
import com.love.loveme.actvites.ChatActivity;
import com.love.loveme.databinding.ItemChatGirlBinding;
import com.love.loveme.databinding.ItemChatUserBinding;
import com.love.loveme.models.MessageUserRoot;
import com.love.loveme.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ROBOT_TYPE = 1;
    private static final int USER_TYPE = 2;
    SessionManager sessionManager;
    MediaPlayer mp = new MediaPlayer();
    private List<ChatActivity.MyChat> list = new ArrayList<>();
    private Context context;
    private MessageUserRoot.DataItem girl;
    private String userImage;
    OnChatAdapterListnear onChatAdapterListnear;

    public List<ChatActivity.MyChat> getList() {
        return list;
    }

    public void setList(List<ChatActivity.MyChat> list) {
        this.list = list;
    }

    public OnChatAdapterListnear getOnChatAdapterListnear() {
        return onChatAdapterListnear;
    }

    public void setOnChatAdapterListnear(OnChatAdapterListnear onChatAdapterListnear) {
        this.onChatAdapterListnear = onChatAdapterListnear;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isRobot()) {
            return ROBOT_TYPE;
        } else {
            return USER_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManager = new SessionManager(context);
        userImage = sessionManager.getStringValue(Const.PROFILE_IMAGE);
        if (viewType == ROBOT_TYPE) {
            return new RobotViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_girl, parent, false));
        } else {
            return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RobotViewHolder) {
            ((RobotViewHolder) holder).setData(position);
        } else {
            ((UserViewHolder) holder).setData(position);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(ChatActivity.MyChat myChat) {

        list.add(myChat);
        notifyDataSetChanged();
    }

    public void setGirl(MessageUserRoot.DataItem girl) {

        this.girl = girl;
    }


    public interface OnChatAdapterListnear {
        void onAudioMsgClick(ChatActivity.MyChat myChat, int position, ItemChatGirlBinding binding);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ItemChatUserBinding binding;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemChatUserBinding.bind(itemView);
        }

        public void setData(int position) {
            binding.imageview.setVisibility(View.GONE);
            binding.tvText.setVisibility(View.GONE);
            binding.lytaudio.setVisibility(View.GONE);
            binding.lytgift.setVisibility(View.GONE);

            Glide.with(context).load(userImage).circleCrop().into(binding.imgGirl);


            ChatActivity.MyChat myChat = list.get(position);
            if (myChat.getType().equals("1")) { //image

            } else if (myChat.getType().equals("2")) {  //audio
                binding.lytaudio.setVisibility(View.VISIBLE);


            } else if (myChat.getType().equals("3")) {
                binding.tvText.setVisibility(View.VISIBLE);
                binding.tvText.setText(myChat.getMessageFileText());
            } else {
                binding.lytgift.setVisibility(View.VISIBLE);
                binding.tvmsg.setText(myChat.getMessageFileText());
                binding.tvcoins.setText(myChat.getContent());

                Glide.with(context).load(Const.IMAGE_URL + myChat.getBitmap()).circleCrop().into(binding.imggift);
            }
        }
    }

    public class RobotViewHolder extends RecyclerView.ViewHolder {
        ItemChatGirlBinding binding;

        public RobotViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        void setData(int position) {
            binding.lytimage.setVisibility(View.GONE);
            binding.tvText.setVisibility(View.GONE);
            binding.lytaudio.setVisibility(View.GONE);
            Glide.with(context).load(Const.IMAGE_URL + girl.getImage()).circleCrop().into(binding.imgGirl);

            ChatActivity.MyChat myChat = list.get(position);
            if (myChat.getType().equals("1")) {  //image
                Glide.with(context).load(Const.IMAGE_URL + myChat.getMessageFileText()).centerCrop().into(binding.imageview);
                binding.lytimage.setVisibility(View.VISIBLE);
                binding.tvimgmsg.setText(myChat.getContent());
                if (myChat.getContent() != null && !myChat.getContent().equals("")) {
                    binding.tvimgmsg.setVisibility(View.VISIBLE);
                } else {
                    binding.tvimgmsg.setVisibility(View.GONE);
                }
            } else if (myChat.getType().equals("2")) {
                binding.lytaudio.setVisibility(View.VISIBLE);
                binding.imgPlay.setOnClickListener(v -> onChatAdapterListnear.onAudioMsgClick(myChat, position, binding));

            } else {
                binding.tvText.setVisibility(View.VISIBLE);
                binding.tvText.setText(myChat.getMessageFileText());
            }
        }
    }
}
