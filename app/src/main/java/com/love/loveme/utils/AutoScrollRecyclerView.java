package com.love.loveme.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class AutoScrollRecyclerView extends RecyclerView {

    private static final String TAG = AutoScrollRecyclerView.class.getSimpleName();
    private static final int SPEED = 50;
    /**
     * Sliding estimator
     */
    private UniformSpeedInterpolator mInterpolator;
    /**
     * Dx and dy between units
     */
    private int mSpeedDx;
    private int mSpeedDy;
    /**
     * Sliding speed, default 100
     */
    private int mCurrentSpeed = SPEED;
    /**
     * Whether to display the list infinitely
     */
    private boolean mLoopEnabled;
    /**
     * Whether to slide backwards
     */
    private boolean mReverse;
    /**
     * Whether to turn on automatic sliding
     */
    private boolean mIsOpenAuto;
    /**
     * Whether the user can manually slide the screen
     */
    private boolean mCanTouch = true;
    /**
     * Whether the user clicks on the screen
     */
    private boolean mPointTouch;
    /**
     * Are you ready for data?
     */
    private boolean mReady;
    /**
     * Whether initialization is complete
     */
    private boolean mInflate;

    /**
     * Whether to stop scroll
     */
    private boolean isStopAutoScroll = false;

    public AutoScrollRecyclerView(Context context) {
        this(context, null);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInterpolator = new UniformSpeedInterpolator();
        mReady = false;
    }

    /**
     * Start sliding
     */
    public void startAutoScroll() {
        isStopAutoScroll = false;
        openAutoScroll(mCurrentSpeed, false);
    }

    /**
     * Start sliding
     *
     * @param speed   Sliding distance (determining the sliding speed)
     * @param reverse Whether to slide backwards
     */
    public void openAutoScroll(int speed, boolean reverse) {
        mReverse = reverse;
        mCurrentSpeed = speed;
        mIsOpenAuto = true;
        notifyLayoutManager();
        startScroll();
    }

    /**
     * Is it possible to manually slide when swiping automatically?
     */
    public void setCanTouch(boolean b) {
        mCanTouch = b;
    }

    public boolean canTouch() {
        return mCanTouch;
    }

    /**
     * Whether to slide infinitely
     */
    public boolean isLoopEnabled() {
        return mLoopEnabled;
    }

    /**
     * Set whether to display the list infinitely
     */
    public void setLoopEnabled(boolean loopEnabled) {
        this.mLoopEnabled = loopEnabled;

        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
            startScroll();
        }
    }

    /**
     * @param isStopAutoScroll
     */
    public void pauseAutoScroll(boolean isStopAutoScroll) {
        this.isStopAutoScroll = isStopAutoScroll;
    }

    public boolean getReverse() {
        return mReverse;
    }

    /**
     * Set whether to reverse
     */
    public void setReverse(boolean reverse) {
        mReverse = reverse;
        notifyLayoutManager();
        startScroll();
    }

    /**
     * Start scrolling
     */
    private void startScroll() {
        Log.d(TAG, "startScroll: mopenauto " + mIsOpenAuto);
        if (getScrollState() == SCROLL_STATE_SETTLING) {
            Log.d(TAG, "startScroll: is setling");
            return;
        }
        if (mInflate && mReady) {
            mSpeedDx = mSpeedDy = 0;
            smoothScroll();
        }
    }

    private void smoothScroll() {
        if (!isStopAutoScroll) {
            Log.d(TAG, "smoothScroll:reverce " + mReverse);
            int absSpeed = Math.abs(mCurrentSpeed);
            int d = mReverse ? -absSpeed : absSpeed;
            Log.d(TAG, "smoothScroll: speed==" + d);
            smoothScrollBy(d, d, mInterpolator);
        }
    }

    private void notifyLayoutManager() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {

            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
            linearLayoutManager.setReverseLayout(mReverse);

        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
            if (staggeredGridLayoutManager != null) {
                staggeredGridLayoutManager.setReverseLayout(mReverse);
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mCanTouch) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPointTouch = true;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mIsOpenAuto) {
                        return true;
                    }
                    break;
                default:
            }
            return super.onInterceptTouchEvent(e);
        } else return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mCanTouch) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mIsOpenAuto) {
                        mPointTouch = false;
                        smoothScroll();
                        return true;
                    }
                    break;
                default:
            }
            return super.onTouchEvent(e);
        } else return true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startScroll();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInflate = true;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (mPointTouch) {
            mSpeedDx = 0;
            mSpeedDy = 0;
            return;
        }
        boolean vertical;
        if (dx == 0) {//Vertical scrolling
            mSpeedDy += dy;
            vertical = true;
        } else {//Horizontal scrolling
            mSpeedDx += dx;
            vertical = false;
        }

        if (vertical) {
            if (Math.abs(mSpeedDy) >= Math.abs(mCurrentSpeed)) {
                mSpeedDy = 0;
                smoothScroll();
            }
        } else {
            if (Math.abs(mSpeedDx) >= Math.abs(mCurrentSpeed)) {
                mSpeedDx = 0;
                smoothScroll();
            }
        }
    }


    /**
     * Custom estimator
     * Swipe the list at a constant speed
     */
    private static class UniformSpeedInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            return input;
        }
    }

    /**
     * Customize the Adapter container so that the list can be displayed in an infinite loop
     */
}
