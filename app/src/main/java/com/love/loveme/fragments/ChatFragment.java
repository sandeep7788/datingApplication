package com.love.loveme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.love.loveme.R;
import com.love.loveme.actvites.ChatActivity;
import com.love.loveme.actvites.MainActivity;
import com.love.loveme.databinding.FragmentChatBinding;
import com.love.loveme.models.MessageUserRoot;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends Fragment {
    FragmentChatBinding binding;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.lytloder.tvdes2.setText("Please wait\nwe find perfect girl for you");

        binding.lytloder.lytbackground.setVisibility(View.GONE);
        Call<MessageUserRoot> call = RetrofitBuilder.create().getMessageUserList(Const.DEV_KEY);
        call.enqueue(new Callback<MessageUserRoot>() {
            @Override
            public void onResponse(Call<MessageUserRoot> call, Response<MessageUserRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    Collections.shuffle(response.body().getData());
                    MessageUserRoot.DataItem girl = response.body().getData().get(0);

                    new Handler().postDelayed(() -> {
                        if (getActivity() != null) {
                            startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("girl", new Gson().toJson(girl)));
                            new Handler().postDelayed(() -> {
                                Log.i("TAG", "onResponse: ");
                                ((MainActivity) getActivity()).resetPostion();
                            }, 500);
                        }
                    }, 5000);

                }

            }

            @Override
            public void onFailure(Call<MessageUserRoot> call, Throwable t) {
//
            }
        });
    }
}