@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.adair.core2.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.adair.core.sound.MediaPlayerHelper.MediaPlayerHelperCallback

/**
 *
 * MediaPlayer 音频播放工具类
 *
 * @author xushuai
 * @date   2022/5/23-23:49
 * @email  466911254@qq.com
 */
class MediaPlayerHelper : LifecycleEventObserver {

    /** 播放器对象 */
    private var mMediaPlayer: MediaPlayer? = null

    /** 当前播放的文件路径  */
    private var mFilePath: String? = null

    /** 当前音频播放循环次数，-1无限循环  */
    private var mLoop = 0

    /** 当前音频已经播放的次数  */
    private var mPlayCompletedCount = 0

    /** 播放回调  */
    private var mCallback: MediaPlayerHelperCallback? = null

    /** 音频资源是否准备好  */
    private var mPrepared = false

    /** 进入onResume生命周期的时候是否需要继续播放  */
    private var mResumePlay = false

    /** 生命周期被观察者  */
    private var mLifecycleOwner: LifecycleOwner? = null

    /** 注销的时候是否release  */
    private var mUnsubscribeRelease = true

    init {
        mMediaPlayer = MediaPlayer()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                try {
                    if (mMediaPlayer != null && mResumePlay) {
                        mMediaPlayer!!.start()
                        mResumePlay = false
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            }
            Lifecycle.Event.ON_PAUSE -> {
                try {
                    if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                        mMediaPlayer!!.pause()
                        mResumePlay = true
                    }
                } catch (e: java.lang.IllegalStateException) {
                    e.printStackTrace()
                }
            }
            Lifecycle.Event.ON_DESTROY -> {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer!!.isPlaying || mResumePlay) if (mCallback != null) {
                        mCallback!!.onPlayError(
                            mMediaPlayer,
                            Throwable("MediaPlayer error:Lifecycle is OnDestroy,mUnsubscribeRelease = $mUnsubscribeRelease")
                        )
                        mCallback!!.onPlayFinish(mMediaPlayer)
                    }
                }
                stop()
                if (mUnsubscribeRelease) {
                    release()
                }
                unsubscribe()
            }
            else -> {}
        }
    }


    /**
     * 开始播放,用在暂停后继续播放
     */
    fun start() {
        try {
            mMediaPlayer?.let {
                if (!it.isPlaying) {
                    it.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 暂停播放
     */
    fun pause() {
        try {
            mMediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        try {
            mMediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mPrepared = false
    }

    fun reset() {
        try {
            mMediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
            }
            mPlayCompletedCount = 0
            mPrepared = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 播放音频
     *
     * @param path 音频路径
     * @param loop 是否循环播放
     */
    fun play(path: String, leftVolume: Float = 1.0f, rightVolume: Float = 1.0f, loop: Int = 0) {
        try {
            mFilePath = path
            mLoop = loop
            mPlayCompletedCount = 0
            mMediaPlayer?.run {
                reset()
                setDataSource(path)
                isLooping = false
                setVolume(leftVolume, rightVolume)
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playAssets(context: Context, path: String, leftVolume: Float = 1.0f, rightVolume: Float = 1.0f, loop: Int = 0) {

        try {
            mFilePath = path
            mLoop = loop
            mPlayCompletedCount = 0
            mMediaPlayer?.run {
                reset()
                val manager = context.assets
                val afd = manager.openFd(path)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                isLooping = false
                setVolume(leftVolume, rightVolume)
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 设置声音大小
     *
     * @param leftVolume  左声道声音
     * @param rightVolume 右声道声音
     */
    fun setVolume(leftVolume: Float, rightVolume: Float) {
        try {
            mMediaPlayer?.setVolume(leftVolume, rightVolume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 是否正在播放改路径的音频
     *
     * @param path 音频路径
     *
     * @return true 正在播放此路径音频
     */
    fun isPlayingSameFile(path: String): Boolean {
        var result = false
        try {
            val sameFile = mFilePath != null && mFilePath == path
            result = sameFile && (mMediaPlayer?.isPlaying ?: false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 是否正在播放中
     *
     * @return true 正在播放
     */
    fun isPlaying(): Boolean {
        try {
            return mMediaPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getLoop(): Int {
        return mLoop
    }

    fun setLoop(loop: Int) {
        mLoop = loop
    }

    fun isPrepared(): Boolean {
        return mPrepared
    }

    private fun release() {
        mMediaPlayer?.release()
        mMediaPlayer = null
        mPrepared = false
    }

    private fun setupMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
        mMediaPlayer?.apply {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val attr = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
                setAudioAttributes(attr)
            } else {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
            }
            setOnErrorListener { mp, what, extra ->
                mPrepared = false
                mp.reset()
                mCallback?.onPlayError(mp, Throwable("MediaPlay Error:what = $what , extra = $extra"))
                mCallback?.onPlayFinish(mp)
                return@setOnErrorListener true
            }

            setOnPreparedListener {
                mPrepared = true
                it.start()
            }

            setOnCompletionListener {
                mPlayCompletedCount++
                when (mLoop) {
                    -1 -> {
                        it.start()
                    }

                    0 -> {
                        mCallback?.onPlayCompletion(it)
                        mCallback?.onPlayFinish(it)
                    }

                    else -> {
                        if ((mPlayCompletedCount - 1) == mLoop) {
                            mCallback?.onPlayCompletion(it)
                            mCallback?.onPlayFinish(it)
                        } else {
                            it.start()
                        }
                    }
                }
            }
        }
    }

    /**
     * 注册生命周期
     *
     * @param lifecycleOwner     生命周期对象
     * @param unsubscribeRelease 注销生命周期监听时，是否需要释放MediaPlayer
     */
    fun subscribeLifecycle(lifecycleOwner: LifecycleOwner, unsubscribeRelease: Boolean = false) {
        if (mLifecycleOwner !== lifecycleOwner && mLifecycleOwner != null) {
            mLifecycleOwner?.lifecycle?.removeObserver(this)
        }
        mLifecycleOwner = lifecycleOwner
        mLifecycleOwner?.lifecycle?.addObserver(this)
        mUnsubscribeRelease = unsubscribeRelease
    }

    /**
     * 取消监控
     */
    fun unsubscribe() {
        mLifecycleOwner?.lifecycle?.removeObserver(this)
        mLifecycleOwner = null
    }
}


interface MediaPlayerHelpCallback {

    /**
     * 播放完成回调
     * @param player MediaPlayer 播放器对象
     */
    fun onPlayCompletion(player: MediaPlayer)

    /**
     * 播放错误回调
     * @param player MediaPlayer 播放器对象
     * @param throwable Throwable 播放错误信息
     */
    fun onPlayError(player: MediaPlayer, throwable: Throwable)

    /**
     * 播放完成,无论播放成功还是失败都会回调
     * @param player MediaPlayer 播放器对象
     */
    fun onPlayFinish(player: MediaPlayer)
}