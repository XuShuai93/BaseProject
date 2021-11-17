package com.adair.core.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

/**
 * 计时器工具,精确到毫秒
 * <p>
 * 可以设置最小精度，默认是1秒，如果精度过小，可能导致不准
 * SystemClock.uptimeMillis() 使用此系统时间计时，因为Handle使用的是此时间
 * 使用此时间可能导致当手机进入深度睡眠以后，计算的时间变得不准确
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/5/20
 */
public class Timer {

    //region 计时刷新精度
    /** 秒精度 */
    private static final long SECOND_ACCURACY = 1000L;
    /** 分组精度 */
    private static final long MINUTE_ACCURACY = 60 * 1000L;
    //endregion

    //region Handler 固定消息
    private static final int MSG = 1;
    //endregion

    //region 计时器状态
    private static final int STATUS_INIT = 1;
    private static final int STATUS_RUNNING = 2;
    private static final int STATUS_PAUSE = 3;
    private static final int STATUS_COMPLETE = 4;

    @IntDef({STATUS_INIT, STATUS_RUNNING, STATUS_PAUSE, STATUS_COMPLETE})
    public @interface TimeStatus {
    }
    //endregion

    //region 私有属性
    /** 刷新精度 默认1秒刷新一次，最小为0 */
    private long mRefreshAccuracy;
    /** 是否是倒计时计时器 */
    private boolean mCountdown;
    /** 倒计时总时长 */
    private long mCountdownTotalTime;
    /**
     * 倒计时 增加时长和减少时长标准，
     * 默认false 增加时间和减少时间都是对当前计时进行操作
     * 当为true是，增加时间和减少时间都是对倒计时总时长进行操作
     */
    private boolean mCountdownCalculateRevert;

    /** 开始计时时系统的时间 */
    private long mStartSystemTime;
    /** 暂停的时候的时间点 */
    private long mPauseSystemTime;
    /** 发送计时消息时间 */
    private long mSendSystemTime;
    /** 暂停总时长 */
    private long mPauseTotalTime;
    /** 总的扣除时间（用于游戏时需要扣除时间） */
    private long mSubTime;
    /** 总的添加时长（用于游戏时需要增加时间） */
    private long mAddTime;
    /** 已经计时的时间 */
    private long mElapsedTime;

    /** 计时器状态 */
    @TimeStatus
    private int mTimeStatus;

    /** 监听器 */
    private OnTimeListener mOnTimeListener;

    private final Handler mTimeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            synchronized (Timer.this) {
                long nowTime = SystemClock.uptimeMillis();
                //抹除发送消息到处理消息之间的时间
                long sendMessageDuration = nowTime - mSendSystemTime;
                long curTime = nowTime - mStartSystemTime - mPauseTotalTime - sendMessageDuration + mElapsedTime;
                if (!mCountdown) {
                    curTime = curTime + mAddTime - mSubTime;
                    if (mOnTimeListener != null) {
                        mOnTimeListener.onTimeChange(curTime, curTime);
                    }
                    mSendSystemTime += mRefreshAccuracy;
                    Timer.this.sendMessage();
                } else {
                    long realCurTime;
                    long realTotalTime;
                    if (!mCountdownCalculateRevert) {
                        curTime = curTime - mAddTime + mSubTime;
                        realCurTime = mCountdownTotalTime - curTime;
                        realTotalTime = mCountdownTotalTime;
                    } else {
                        realTotalTime = mCountdownTotalTime + mAddTime - mSubTime;
                        realCurTime = realTotalTime - curTime;
                    }
                    if (realCurTime <= 0) {
                        mTimeStatus = STATUS_COMPLETE;
                        if (mOnTimeListener != null) {
                            mOnTimeListener.onTimeChange(0, realTotalTime);
                            mOnTimeListener.onCountDownComplete();
                        }
                    } else {
                        if (mOnTimeListener != null) {
                            mOnTimeListener.onTimeChange(realCurTime, realTotalTime);
                        }
                        mSendSystemTime += mRefreshAccuracy;
                        Timer.this.sendMessage();
                    }
                }
            }
        }
    };
    //endregion

    //region 构造方法
    public Timer() {
        this(false, 0);
    }

    public Timer(long countdownTotalTime) {
        this(true, countdownTotalTime);
    }

    private Timer(boolean countdown, long countdownTotalTime) {
        mCountdown = countdown;
        mCountdownTotalTime = countdownTotalTime;
        mCountdownCalculateRevert = false;
        mRefreshAccuracy = SECOND_ACCURACY;
        mTimeStatus = STATUS_INIT;
        mStartSystemTime = 0;
        mPauseSystemTime = 0;
        mSendSystemTime = 0;
        mPauseTotalTime = 0;
        mElapsedTime = 0;
        mSubTime = 0;
        mAddTime = 0;
    }
    //endregion

    //region 公共方法

    /** 开始计时 */
    public synchronized void start() {
        if (mRefreshAccuracy <= 0) {
            if (mOnTimeListener != null) {
                mOnTimeListener.onError(new Throwable("刷新精度不能等于或者小于0"));
            }
            return;
        }
        if (mTimeStatus == STATUS_COMPLETE) {
            if (mOnTimeListener != null) {
                mOnTimeListener.onError(new Throwable("Time status is countdown Complete! please call reset() Method"));
            }
            return;
        }
        if (mTimeStatus == STATUS_RUNNING) {
            return;
        }
        clearMessage();
        long nowTime = SystemClock.uptimeMillis();
        if (mTimeStatus == STATUS_INIT) {
            mStartSystemTime = nowTime;
            mSendSystemTime = mStartSystemTime + mRefreshAccuracy;
        } else {
            //从暂停状态恢复
            long pauseTime = nowTime - mPauseSystemTime;
            mPauseTotalTime += pauseTime;
            //下一次发送消息时间需要加上暂停时间
            mSendSystemTime += pauseTime;
        }
        mTimeStatus = STATUS_RUNNING;
        if (mOnTimeListener != null) {
            mOnTimeListener.onStart();
        }
        refreshTime(nowTime);
        sendMessage();
    }


    /** 暂停计时 */
    public synchronized void pause() {
        if (mTimeStatus == STATUS_INIT) {
            if (mOnTimeListener != null) {
                mOnTimeListener.onError(new Throwable("Time status is Init! please call start() Method"));
            }
            return;
        }
        if (mTimeStatus == STATUS_COMPLETE) {
            if (mOnTimeListener != null) {
                mOnTimeListener.onError(new Throwable("Time status is countdown Complete! please call reset() Method"));
            }
            return;
        }

        if (mTimeStatus == STATUS_PAUSE) {
            return;
        }
        clearMessage();
        mTimeStatus = STATUS_PAUSE;
        mPauseSystemTime = SystemClock.uptimeMillis();
        if (mOnTimeListener != null) {
            mOnTimeListener.onPause();
        }
    }

    /** 重置计时器 */
    public synchronized void reset() {
        clearMessage();
        mTimeStatus = STATUS_INIT;
        mStartSystemTime = 0;
        mPauseSystemTime = 0;
        mSendSystemTime = 0;
        mPauseTotalTime = 0;
        mElapsedTime = 0;
        mSubTime = 0;
        mAddTime = 0;
        if (mOnTimeListener != null) {
            mOnTimeListener.onReset();
        }
    }

    /** 添加时间 */
    public synchronized void addTime(long duration) {
        mAddTime += duration;
        refreshTime(SystemClock.uptimeMillis());
    }

    /** 扣除时间 */
    public synchronized void subTime(long duration) {
        mSubTime += duration;
        refreshTime(SystemClock.uptimeMillis());
    }

    public synchronized void setElapsedTime(long time) {
        if (mTimeStatus != STATUS_INIT) {
            if (mOnTimeListener != null) {
                mOnTimeListener.onError(new Throwable("Time status is countdown Complete! please call reset() Method"));
            }
            return;
        }
        mElapsedTime = time;
        refreshTime(SystemClock.uptimeMillis());
    }
    //endregion

    //region 私有方法
    private synchronized void refreshTime(long nowTime) {
        if (mTimeStatus == STATUS_COMPLETE) {
            if (mOnTimeListener != null) {
                mOnTimeListener.onError(new Throwable("Time status is countdown Complete! please call reset() Method"));
            }
            return;
        }
        if (mTimeStatus == STATUS_PAUSE) {
            long pauseTime = nowTime - mPauseSystemTime;
            mPauseTotalTime += pauseTime;
            mSendSystemTime += pauseTime;
            mPauseSystemTime = nowTime;
        }
        //抹除发送消息到处理消息之间的时间(这里要计算上一次发送消息的时间，多减去一个刷新间隔)
        long curTime;
        if (mTimeStatus != STATUS_INIT) {
            long sendMessageDuration = nowTime - mSendSystemTime + mRefreshAccuracy;
            curTime = nowTime - mStartSystemTime - mPauseTotalTime - sendMessageDuration + mElapsedTime;
        } else {
            curTime = mElapsedTime;
        }
        if (!mCountdown) {
            curTime = curTime + mAddTime - mSubTime;
            if (mOnTimeListener != null) {
                mOnTimeListener.onTimeChange(curTime, curTime);
            }
        } else {
            long realCurTime;
            long realTotalTime;
            if (!mCountdownCalculateRevert) {
                curTime = curTime - mAddTime + mSubTime;
                realCurTime = mCountdownTotalTime - curTime;
                realTotalTime = mCountdownTotalTime;
            } else {
                realTotalTime = mCountdownTotalTime + mAddTime - mSubTime;
                realCurTime = realTotalTime - curTime;
            }
            if (realCurTime <= 0) {
                mTimeStatus = STATUS_COMPLETE;
                if (mOnTimeListener != null) {
                    mOnTimeListener.onTimeChange(0, realTotalTime);
                    mOnTimeListener.onCountDownComplete();
                }
            } else {
                if (mOnTimeListener != null) {
                    mOnTimeListener.onTimeChange(realCurTime, realTotalTime);
                }
            }
        }
    }

    private void sendMessage() {
        mTimeHandler.sendEmptyMessageAtTime(MSG, mSendSystemTime);
    }

    private void clearMessage() {
        mTimeHandler.removeCallbacksAndMessages(null);
    }
    //endregion

    //region getter and setter
    public long getRefreshAccuracy() {
        return mRefreshAccuracy;
    }

    public void setRefreshAccuracy(long refreshAccuracy) {
        mRefreshAccuracy = refreshAccuracy;
    }

    public boolean isCountdown() {
        return mCountdown;
    }

    public void setCountdown(boolean countdown) {
        mCountdown = countdown;
    }

    public long getCountdownTotalTime() {
        return mCountdownTotalTime;
    }

    public void setCountdownTotalTime(long countdownTotalTime) {
        mCountdownTotalTime = countdownTotalTime;
    }

    public boolean isCountdownCalculateRevert() {
        return mCountdownCalculateRevert;
    }

    public void setCountdownCalculateRevert(boolean countdownCalculateRevert) {
        mCountdownCalculateRevert = countdownCalculateRevert;
    }

    public OnTimeListener getOnTimeListener() {
        return mOnTimeListener;
    }

    public void setOnTimeListener(OnTimeListener onTimeListener) {
        mOnTimeListener = onTimeListener;
    }
    //endregion

    //region 计时器监听接口及默认实现
    public interface OnTimeListener {
        void onStart();

        void onTimeChange(long time, long totalTime);

        void onPause();

        void onReset();

        void onCountDownComplete();

        void onError(Throwable throwable);
    }

    public static abstract class DefaultOnTimeListener implements OnTimeListener {
        @Override
        public void onStart() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onReset() {

        }

        @Override
        public void onCountDownComplete() {

        }

        @Override
        public void onError(Throwable throwable) {

        }
    }
    //endregion
}
