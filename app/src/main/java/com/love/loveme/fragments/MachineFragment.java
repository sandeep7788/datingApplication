package com.love.loveme.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.love.loveme.JanuWork;
import com.love.loveme.R;
import com.love.loveme.adapters.MachineAdapter;
import com.love.loveme.databinding.FragmentMachineBinding;

import java.util.List;
import java.util.Random;

public class MachineFragment extends Fragment {


    FragmentMachineBinding binding;
    MachineAdapter machineAdapter1;
    MachineAdapter machineAdapter2;
    MachineAdapter machineAdapter3;
    private LinearLayoutManager layoutManager1;
    private LinearLayoutManager layoutManager2;
    private LinearLayoutManager layoutManager3;
    private MachineFragentClickListaer machineFragentClickListaer;
    private Random rand = new Random();

    public MachineFragment() {

    }

    public MachineFragment(MachineFragentClickListaer machineFragentClickListaer) {

        this.machineFragentClickListaer = machineFragentClickListaer;
    }

    private int speedScroll = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_machine, container, false);
        return binding.getRoot();

    }

    private void initListnear() {
        binding.bt1startmatching.setOnClickListener(v -> machineFragentClickListaer.onStartMatchingClick());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Integer> girllist1 = JanuWork.setGirlsPhotos1();
        List<Integer> girllist2 = JanuWork.setGirlsPhotos2();
        List<Integer> girllist3 = JanuWork.setGirlsPhotos3();
        machineAdapter1 = new MachineAdapter(girllist1);
        machineAdapter2 = new MachineAdapter(girllist2);
        machineAdapter3 = new MachineAdapter(girllist3);
        binding.rvmachine1.setAdapter(machineAdapter1);
        binding.rvmachine2.setAdapter(machineAdapter2);
        binding.rvmachine3.setAdapter(machineAdapter3);


        initView();
        initListnear();


        binding.countAnimationTextView
                .setInterpolator(new AccelerateInterpolator())
                .setAnimationDuration(5000)
                .countAnimation(0, rand.nextInt(7000 - 4000 + 1) + 4000);


    }

    final Runnable runnable = new Runnable() {
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;

        @Override
        public void run() {
            if (layoutManager1.findFirstVisibleItemPosition() >= machineAdapter1.getItemCount() / 2 && layoutManager1.findFirstVisibleItemPosition() > 1) {
                machineAdapter1.notifyItemMoved(0, machineAdapter1.getItemCount() - 1);
            }
            if (layoutManager3.findFirstVisibleItemPosition() >= machineAdapter3.getItemCount() / 2 && layoutManager3.findFirstVisibleItemPosition() > 1) {
                machineAdapter3.notifyItemMoved(0, machineAdapter3.getItemCount() - 1);
            }
            binding.rvmachine1.smoothScrollToPosition(count1++);
            binding.rvmachine3.smoothScrollToPosition(count3++);

            if (layoutManager2.findFirstVisibleItemPosition() >= machineAdapter2.getItemCount() / 2 && layoutManager2.findFirstVisibleItemPosition() > 1) {
                machineAdapter2.notifyItemMoved(0, machineAdapter2.getItemCount() - 1);
            }
            binding.rvmachine2.smoothScrollToPosition(count2++);
            handler.postDelayed(this, speedScroll);

        }
    };
    final Handler handler = new Handler();

    private void initView() {


        layoutManager1 = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                if (getActivity() != null) {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return 5.0f;
                        }
                    };

                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                }


            }

        };
        layoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, true) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                if (getActivity() != null) {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return 5.0f;
                        }
                    };

                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                }


            }

        };
        layoutManager3 = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                if (getActivity() != null) {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return 5.0f;
                        }
                    };

                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                }


            }

        };

        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvmachine1.setLayoutManager(layoutManager1);
        binding.rvmachine1.setDrawingCacheEnabled(true);
        binding.rvmachine1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        binding.rvmachine1.setHasFixedSize(true);
        binding.rvmachine1.setItemViewCacheSize(machineAdapter1.getItemCount());
        binding.rvmachine1.setAdapter(machineAdapter1);

        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvmachine2.setLayoutManager(layoutManager2);
        binding.rvmachine3.setDrawingCacheEnabled(true);
        binding.rvmachine2.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        binding.rvmachine3.setHasFixedSize(true);
        binding.rvmachine2.setItemViewCacheSize(machineAdapter2.getItemCount());
        binding.rvmachine2.setAdapter(machineAdapter2);

        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvmachine3.setLayoutManager(layoutManager3);
        binding.rvmachine3.setDrawingCacheEnabled(true);
        binding.rvmachine3.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        binding.rvmachine1.setHasFixedSize(true);
        binding.rvmachine1.setItemViewCacheSize(machineAdapter3.getItemCount());
        binding.rvmachine3.setAdapter(machineAdapter3);


        autoScroll();


    }

    public void autoScroll() {

        handler.postDelayed(runnable, speedScroll);


    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        autoScroll();
    }

    public interface MachineFragentClickListaer {
        void onStartMatchingClick();
    }

}