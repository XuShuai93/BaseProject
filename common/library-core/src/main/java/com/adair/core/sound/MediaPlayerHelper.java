package com.adair.core.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.orhanobut.logger.Logger;


/**
 * BookEditor 播放音频工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2020/11/23
 */
public class MediaPlayerHelper implements LifecycleObserver {


	public interface MediaPlayerHelperCallback {
		/**
		 * 播放完成回调
		 *
		 * @param mp 播放器
		 */
		void onPlayCompletion(MediaPlayer mp);

		/**
		 * 播放错误回调
		 *
		 * @param mp        播放器
		 * @param throwable 错误信息
		 */
		void onPlayError(MediaPlayer mp, Throwable throwable);

		/**
		 * 播发完成或者错误都会回调此方法
		 *
		 * @param mp 播放器
		 */
		default void onPlayFinish(MediaPlayer mp) {
		}
	}


	/** 生命周期被观察者 */
	private LifecycleOwner mLifecycleOwner;
	/** 注销的时候是否release */
	private boolean mUnsubscribeRelease = true;
	/** 音频播放器 */
	private MediaPlayer mMediaPlayer;
	/** 当前播放的文件路径 */
	private String mFilePath;
	/** 当前音频播放循环次数，-1无限循环 */
	private int mLoop;
	/** 当前音频已经播放的次数 */
	private int mPlayCompletedCount;
	/** 播放回调 */
	private MediaPlayerHelperCallback mCallback;
	/** 音频资源是否准备好 */
	private boolean mPrepared = false;
	/** 进入onResume生命周期的时候是否需要继续播放 */
	private boolean mResumePlay;

	/** 上下文对象 */
	private Context mContext;
	/** 后期添加音频焦点管理 */
	private AudioManager mAudioManager;


	public MediaPlayerHelper() {
		mMediaPlayer = new MediaPlayer();
		setupMediaPlayer();
	}

	public void reset() {
		try {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
				}
				mMediaPlayer.reset();
			}
			mPlayCompletedCount = 0;
			mPrepared = false;
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper pause error");
		}
	}

	/**
	 * 播放音频
	 *
	 * @param path 音频路径
	 * @param loop 是否循环播放
	 */
	public void play(String path, float leftVolume, float rightVolume, int loop) {
		try {
			mFilePath = path;
			mLoop = loop;
			mPlayCompletedCount = 0;
			if (mMediaPlayer != null) {
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(path);
				mMediaPlayer.setLooping(false);
				mMediaPlayer.setVolume(leftVolume, rightVolume);
				mMediaPlayer.prepareAsync();
			}
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper play error");
		}
	}

	public void playAssets(Context context, String path, float leftVolume, float rightVolume, int loop) {
		try {
			mFilePath = path;
			mLoop = loop;
			mPlayCompletedCount = 0;
			if (mMediaPlayer != null) {
				mMediaPlayer.reset();
				AssetManager manager = context.getAssets();
				AssetFileDescriptor afd = manager.openFd(path);
				mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				mMediaPlayer.setLooping(false);
				mMediaPlayer.setVolume(leftVolume, rightVolume);
				mMediaPlayer.prepareAsync();
			}
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper play error");
		}
	}


	/**
	 * 开始播放,用在暂停后继续播放
	 */
	public void start() {
		try {
			if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
				mMediaPlayer.start();
			}
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper start error");
		}
	}

	/**
	 * 暂停播放
	 */
	public void pause() {
		try {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
			}
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper pause error");
		}
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		try {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper pause error");
		}
		mPrepared = false;
	}

	private void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		mPrepared = false;
	}


	/**
	 * 设置声音大小
	 *
	 * @param leftVolume  左声道声音
	 * @param rightVolume 右声道声音
	 */
	public void setVolume(float leftVolume, float rightVolume) {
		try {
			if (mMediaPlayer != null) {
				mMediaPlayer.setVolume(leftVolume, rightVolume);
			}
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper setVolume error");
		}
	}


	/**
	 * 是否正在播放改路径的音频
	 *
	 * @param path 音频路径
	 *
	 * @return true 正在播放此路径音频
	 */
	public boolean isPlayingSameFile(String path) {
		boolean result = false;

		try {
			boolean sameFile = (mFilePath != null) && mFilePath.equals(path);
			result = sameFile && mMediaPlayer.isPlaying();
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper isPlayingSameFile error");
		}
		return result;
	}

	/**
	 * 是否正在播放中
	 *
	 * @return true 正在播放
	 */
	public boolean playing() {
		try {
			return mMediaPlayer.isPlaying();
		} catch (Exception e) {
			Logger.e(e, "MediaPlayerHelper isPlayingSameFile error");
		}
		return false;
	}

	public int getLoop() {
		return mLoop;
	}

	public void setLoop(int loop) {
		mLoop = loop;
	}

	public boolean isPrepared() {
		return mPrepared;
	}

	public boolean isPlaying() {
		if (mMediaPlayer == null) {
			return false;
		}
		return mMediaPlayer.isPlaying();
	}

	public MediaPlayerHelperCallback getCallback() {
		return mCallback;
	}

	public void setCallback(MediaPlayerHelperCallback callback) {
		mCallback = callback;
	}

	private void setupMediaPlayer() {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
		}
		AudioAttributes attr = new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_MEDIA)
				.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
				.setLegacyStreamType(AudioManager.STREAM_MUSIC)
				.build();
		mMediaPlayer.setAudioAttributes(attr);
		mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				mPrepared = false;
				Logger.e("mediaPlayer play error: what = %d, extra = %d", what, extra);
				mp.reset();
				if (mCallback != null) {
					mCallback.onPlayError(mp, new Throwable("MediaPlay Error:what = " + what + " , extra = " + extra));
					mCallback.onPlayFinish(mp);
				}
				return true;
			}
		});
		mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mPrepared = true;
				mp.start();
			}
		});
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mPlayCompletedCount++;
				if (mLoop == -1) {
					mp.start();
				} else if (mLoop == 0) {
					if (mCallback != null) {
						mCallback.onPlayCompletion(mp);
						mCallback.onPlayFinish(mp);
					}
				} else {
					//循环次数 = 完成次数-1，则表示已经完成循环 循环次数的播放
					if ((mPlayCompletedCount - 1) == mLoop) {
						if (mCallback != null) {
							mCallback.onPlayCompletion(mp);
							mCallback.onPlayFinish(mp);
						}
					} else {
						mp.start();
					}
				}
			}
		});
	}


	/**
	 * 注册生命周期
	 *
	 * @param lifecycleOwner     生命周期对象
	 * @param unsubscribeRelease 注销生命周期监听时，是否需要释放MediaPlayer
	 */
	public void subscribeLifecycle(@NonNull LifecycleOwner lifecycleOwner, boolean unsubscribeRelease) {
		if (mLifecycleOwner != lifecycleOwner && mLifecycleOwner != null) {
			mLifecycleOwner.getLifecycle().removeObserver(this);
		}
		mLifecycleOwner = lifecycleOwner;
		mLifecycleOwner.getLifecycle().addObserver(this);
		this.mUnsubscribeRelease = unsubscribeRelease;
	}

	/**
	 * 取消监控
	 */
	public void unsubscribe() {
		if (mLifecycleOwner != null) {
			mLifecycleOwner.getLifecycle().removeObserver(this);
			mLifecycleOwner = null;
		}
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	public void onResume() {
		try {
			if (mMediaPlayer != null && mResumePlay) {
				mMediaPlayer.start();
				mResumePlay = false;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	public void onPause() {
		try {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mResumePlay = true;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	public void onStop() {

	}


	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	public void onDestroy() {
		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying() || mResumePlay)
				if (mCallback != null) {
					mCallback.onPlayError(mMediaPlayer, new Throwable("MediaPlayer error:Lifecycle is OnDestroy,mUnsubscribeRelease = " + mUnsubscribeRelease));
					mCallback.onPlayFinish(mMediaPlayer);
				}
		}
		stop();
		if (mUnsubscribeRelease) {
			release();
		}
		unsubscribe();
		mContext = null;
	}


	public static class SimpleMediaPlayerHelperCallback implements MediaPlayerHelperCallback {

		@Override
		public void onPlayCompletion(MediaPlayer mp) {

		}

		@Override
		public void onPlayError(MediaPlayer mp, Throwable throwable) {
//			Logger.e(throwable, "");
		}
	}
}


