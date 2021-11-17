package com.adair.core.widget;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;

/**
 * 自定义AnimationDrawable，可以监听播放完成的监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/6/21
 */
public class CustomAnimationDrawable extends AnimationDrawable {

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private OnAnimationDrawableListener mOnAnimationDrawableListener;

    /** 重复播放次数 */
    private int mRepeatCount;
    /** 已经播放次数 */
    private int mRepeatPlayCount;
    private boolean mRepeat = false;
    /** 动画执行一次的时长 */
    private long mTotalTime;

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isOneShot()) {
                if (mRepeatCount != 0) {
                    if (mRepeatCount > mRepeatPlayCount) {
                        mRepeatPlayCount++;
                        mRepeat = true;
                        setVisible(true, true);
                        start();
                        mHandler.postAtTime(this, SystemClock.uptimeMillis() + mTotalTime);
                        if (mOnAnimationDrawableListener != null) {
                            mOnAnimationDrawableListener.onAnimationRepeat(CustomAnimationDrawable.this);
                        }
                    } else {
                        if (mOnAnimationDrawableListener != null) {
                            mOnAnimationDrawableListener.onAnimationFinish(CustomAnimationDrawable.this);
                        }
                    }
                } else {
                    if (mOnAnimationDrawableListener != null) {
                        mOnAnimationDrawableListener.onAnimationFinish(CustomAnimationDrawable.this);
                    }
                }
            } else {
                mHandler.postAtTime(this, SystemClock.uptimeMillis() + mTotalTime);
                if (mOnAnimationDrawableListener != null) {
                    mOnAnimationDrawableListener.onAnimationRepeat(CustomAnimationDrawable.this);
                }
            }
        }
    };

    public CustomAnimationDrawable(AnimationDrawable animationDrawable) {
        mTotalTime = 0L;
        int numberOfFrames = animationDrawable.getNumberOfFrames();
        for (int i = 0; i < numberOfFrames; i++) {
            addFrame(animationDrawable.getFrame(i), animationDrawable.getDuration(i));
        }
        setOneShot(animationDrawable.isOneShot());
    }


    @Override
    public void addFrame(@NonNull Drawable frame, int duration) {
        super.addFrame(frame, duration);
        mTotalTime += duration;
    }

    @Override
    public void start() {
        super.start();
        if (!mRepeat) {
            mHandler.postAtTime(mRunnable, SystemClock.uptimeMillis() + mTotalTime);
            if (mOnAnimationDrawableListener != null) {
                mOnAnimationDrawableListener.onAnimationStart(this);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        mHandler.removeCallbacksAndMessages(null);
        mRepeatPlayCount = 0;
        mRepeat = false;
        if (mOnAnimationDrawableListener != null) {
            mOnAnimationDrawableListener.onAnimationStop(this);
        }
    }

    public void recycle() {
        if (isRunning()) {
            stop();
        }
        int numberOfFrames = getNumberOfFrames();
        for (int i = 0; i < numberOfFrames; i++) {
            Drawable frame = getFrame(i);
            if (frame instanceof BitmapDrawable) {
                ((BitmapDrawable) frame).getBitmap().recycle();
            }
            frame.setCallback(null);
        }
        setCallback(null);
    }

    public int getRepeatCount() {
        return mRepeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        mRepeatCount = repeatCount;
        if (!isOneShot()) {
            setOneShot(true);
        }
    }

    public OnAnimationDrawableListener getOnAnimationDrawableListener() {
        return mOnAnimationDrawableListener;
    }

    public void setOnAnimationDrawableListener(OnAnimationDrawableListener onAnimationDrawableListener) {
        mOnAnimationDrawableListener = onAnimationDrawableListener;
    }

    public interface OnAnimationDrawableListener {
        void onAnimationStart(CustomAnimationDrawable drawable);

        void onAnimationStop(CustomAnimationDrawable drawable);

        void onAnimationRepeat(CustomAnimationDrawable drawable);

        void onAnimationFinish(CustomAnimationDrawable drawable);
    }


    public static class SimpleAnimationDrawableListener implements OnAnimationDrawableListener {

        @Override
        public void onAnimationStart(CustomAnimationDrawable drawable) {

        }

        @Override
        public void onAnimationStop(CustomAnimationDrawable drawable) {

        }


        @Override
        public void onAnimationRepeat(CustomAnimationDrawable drawable) {

        }

        @Override
        public void onAnimationFinish(CustomAnimationDrawable drawable) {

        }
    }
}
