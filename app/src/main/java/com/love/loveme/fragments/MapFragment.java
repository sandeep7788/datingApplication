package com.love.loveme.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.love.loveme.R;
import com.love.loveme.databinding.FragmentMapBinding;

import java.util.Random;

public class MapFragment extends Fragment implements Runnable {
    private Random rand = new Random();
    FragmentMapBinding binding;
    private MapFragmentListnear mapFragmentListnear;
    Handler handler = new Handler();
    int count = 0;

    public MapFragment(MapFragmentListnear mapFragmentListnear) {

        this.mapFragmentListnear = mapFragmentListnear;
    }

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setImageLoader();


        binding.tvcount
                .setInterpolator(new AccelerateInterpolator())
                .setAnimationDuration(3000)
                .countAnimation(0, rand.nextInt(300 - 135 + 1) + 135);

        initListnear();

    }

    private void initListnear() {
        binding.tvmatch.setOnClickListener(v -> mapFragmentListnear.onMatchDone());
    }

    private void setImageLoader() {
        new Thread(() -> handler.postDelayed(this, 0)).start();
    }

    @Override
    public void run() {
        Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.pop);
        if (count == 0) {
            count++;
            binding.iv1.setVisibility(View.VISIBLE);
            binding.iv1.startAnimation(pop);
            new Thread(() -> handler.postDelayed(this, 2000)).start();
        } else if (count == 1) {
            count++;
            binding.iv1.clearAnimation();
            binding.iv2.setVisibility(View.VISIBLE);
            binding.iv2.startAnimation(pop);
            new Thread(() -> handler.postDelayed(this, 2000)).start();


            binding.tvcount
                    .setInterpolator(new AccelerateInterpolator())
                    .setAnimationDuration(2000)
                    .countAnimation(Integer.parseInt(binding.tvcount.getText().toString()), rand.nextInt(500 - 300 + 1) + 300);

        } else if (count == 2) {
            binding.iv2.clearAnimation();
            binding.iv3.setVisibility(View.VISIBLE);
            binding.iv3.startAnimation(pop);

            binding.tvcount
                    .setInterpolator(new AccelerateInterpolator())
                    .setAnimationDuration(1500)
                    .countAnimation(Integer.parseInt(binding.tvcount.getText().toString()), rand.nextInt(650 - 500 + 1) + 500);

            new Handler().postDelayed(() -> {
                binding.iv3.clearAnimation();
                binding.tvmatch.startAnimation(pop);
                binding.tvmatch.setVisibility(View.VISIBLE);
            }, 1500);

        }
    }

    public interface MapFragmentListnear {
        void onMatchDone();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(this);
    }
}