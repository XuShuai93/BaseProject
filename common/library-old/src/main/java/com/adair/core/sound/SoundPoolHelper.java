package com.adair.core.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * SoundPool 播放音频辅助类
 * <p>
 * 添加了LifecycleObserver监听，可以添加生命周期监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2020/11/24
 */
public class SoundPoolHelper implements LifecycleObserver, SoundPool.OnLoadCompleteListener {

    public final static int TYPE_MUSIC = AudioManager.STREAM_MUSIC;
    public final static int TYPE_ALARM = AudioManager.STREAM_ALARM;
    public final static int TYPE_RING = AudioManager.STREAM_RING;


    @IntDef({TYPE_MUSIC, TYPE_ALARM, TYPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    public interface OnLoadCompleteListener {

        /**
         * Called when a sound has completed loading.
         *
         * @param soundPool SoundPool object from the load() method
         * @param sampleId  the sample ID of the sound loaded.
         * @param status    the status of the load operation (0 = success)
         *
         * @return true 已经完成回调，可以移除回调接口了
         */
        boolean onLoadComplete(SoundPool soundPool, int sampleId, int status);
    }


    /**
     * 生命周期被观察者
     */
    private LifecycleOwner mLifecycleOwner;
    /**
     * 注销的时候是否release
     */
    private boolean mUnsubscribeRelease = true;


    private SoundPool mSoundPool;
    private int mMaxStream;

    private final List<OnLoadCompleteListener> mOnLoadCompleteListeners = new ArrayList<>();
    /**
     * 用来保存已经加载过的声音id
     */
    private final HashMap<String, Integer> mSoundIdMap = new HashMap<>();


    public SoundPoolHelper() {
        this(1, TYPE_MUSIC);
    }

    public SoundPoolHelper(int maxStream) {
        this(maxStream, TYPE_MUSIC);
    }

    public SoundPoolHelper(int maxStream, int streamType) {
        this.mMaxStream = maxStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setLegacyStreamType(streamType)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(maxStream)
                    .build();
        } else {
            mSoundPool = new SoundPool(maxStream, AudioAttributes.CONTENT_TYPE_MUSIC, 0);
        }
        mSoundPool.setOnLoadCompleteListener(this);
    }

    /**
     * Load the sound from an asset file descriptor.
     *
     * @param soundName a String name, this value can avoid duplicate loads
     * @param afd       an asset file descriptor
     * @param priority  the priority of the sound. Currently has no effect. Use
     *                  a value of 1 for future compatibility.
     *
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(String soundName, AssetFileDescriptor afd, int priority) {
        if (mSoundPool != null) {
            int soundId = foundSoundIdByName(soundName);
            if (soundId == 0) {
                soundId = mSoundPool.load(afd, priority);
                if (soundId != 0) {
                    mSoundIdMap.put(soundName, soundId);
                }
            }
            return soundId;
        } else {
            return 0;
        }
    }

    /**
     * Load the sound from the specified APK resource.
     *
     * @param context  the application context
     * @param resId    the resource ID
     * @param priority the priority of the sound. Currently has no effect. Use
     *                 a value of 1 for future compatibility.
     *
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(String soundName, Context context, int resId, int priority) {
        if (mSoundPool != null) {
            int soundId = foundSoundIdByName(soundName);
            if (soundId == 0) {
                soundId = mSoundPool.load(context, resId, priority);
                if (soundId != 0) {
                    mSoundIdMap.put(soundName, soundId);
                }
            }
            return soundId;
        } else {
            return 0;
        }
    }

    /**
     * Load the sound from the specified path.
     *
     * @param path     the path to the audio file
     * @param priority the priority of the sound. Currently has no effect. Use
     *                 a value of 1 for future compatibility.
     *
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(String soundName, String path, int priority) {
        if (mSoundPool != null) {
            int soundId = foundSoundIdByName(soundName);
            if (soundId == 0) {
                soundId = mSoundPool.load(path, priority);
                if (soundId != 0) {
                    mSoundIdMap.put(soundName, soundId);
                }
            }
            return soundId;
        } else {
            return 0;
        }
    }

    /**
     * Load the sound from a FileDescriptor.
     *
     * @param fd       a FileDescriptor object
     * @param offset   offset to the start of the sound
     * @param length   length of the sound
     * @param priority the priority of the sound. Currently has no effect. Use
     *                 a value of 1 for future compatibility.
     *
     * @return a sound ID. This value can be used to play or unload the sound.
     */
    public int load(String soundName, FileDescriptor fd, long offset, long length, int priority) {
        if (mSoundPool != null) {
            int soundId = foundSoundIdByName(soundName);
            if (soundId == 0) {
                soundId = mSoundPool.load(fd, offset, length, priority);
                if (soundId != 0) {
                    mSoundIdMap.put(soundName, soundId);
                }
            }
            return soundId;
        } else {
            return 0;
        }
    }


    /**
     * 播放
     *
     * @param soundId     a soundID returned by the load() function
     * @param leftVolume  left volume value (range = 0.0 to 1.0)
     * @param rightVolume right volume value (range = 0.0 to 1.0)
     * @param priority    stream priority (0 = lowest priority)
     * @param loop        loop mode (0 = no loop, -1 = loop forever)
     * @param rate        playback rate (1.0 = normal playback, range 0.5 to 2.0)
     *
     * @return non-zero streamID if successful, zero if failed
     */
    public int play(int soundId, float leftVolume, float rightVolume, int priority, int loop, float rate) {
        if (mSoundPool != null) {
            return mSoundPool.play(soundId, leftVolume, rightVolume, priority, loop, rate);
        } else {
            return 0;
        }
    }


    /**
     * 恢复播放
     *
     * @param streamId a streamID returned by the play() function
     */
    public void resume(int streamId) {
        if (mSoundPool != null) {
            mSoundPool.resume(streamId);
        }
    }

    /**
     * 暂停播放
     *
     * @param streamId a streamID returned by the play() function
     */
    public void pause(int streamId) {
        if (mSoundPool != null) {
            mSoundPool.pause(streamId);
        }
    }

    /**
     * 恢复所有正在播放的音频
     */
    public void autoResume() {
        if (mSoundPool != null) {
            mSoundPool.autoResume();
        }
    }

    /**
     * 暂停所有正在播放的音频
     */
    public void autoPause() {
        if (mSoundPool != null) {
            mSoundPool.autoPause();
        }
    }

    /**
     * 停止播放
     *
     * @param streamId a streamID returned by the play() function
     */
    public void stop(int streamId) {
        if (mSoundPool != null) {
            mSoundPool.stop(streamId);
        }
    }


    /**
     * 设置音频播放次数
     *
     * @param streamId a streamID returned by the play() function
     * @param loop     循环次数,-1表示无限循环
     */
    public void setLoop(int streamId, int loop) {
        if (mSoundPool != null) {
            mSoundPool.setLoop(streamId, loop);
        }
    }

    /**
     * 设置音频播放优先级
     *
     * @param streamId a streamID returned by the play() function
     * @param priority 优先级，优先级高的先播放
     */
    public void setPriority(int streamId, int priority) {
        if (mSoundPool != null) {
            mSoundPool.setPriority(streamId, priority);
        }
    }

    /**
     * 设置音频播放速率
     *
     * @param streamId a streamID returned by the play() function
     * @param rate     播放速率 ,(1.0 = normal playback, range 0.5 to 2.0)
     */
    public void setRate(int streamId, float rate) {
        if (mSoundPool != null) {
            mSoundPool.setRate(streamId, rate);
        }
    }

    /**
     * Set stream volume.
     * <p>
     * Sets the volume on the stream specified by the streamID.
     * This is the value returned by the play() function. The
     * value must be in the range of 0.0 to 1.0. If the stream does
     * not exist, it will have no effect.
     *
     * @param streamId    a streamID returned by the play() function
     * @param leftVolume  left volume value (range = 0.0 to 1.0)
     * @param rightVolume right volume value (range = 0.0 to 1.0)
     */
    public void setVolume(int streamId, float leftVolume, float rightVolume) {
        if (mSoundPool != null) {
            mSoundPool.setVolume(streamId, leftVolume, rightVolume);
        }
    }

    /**
     * 卸载音频
     *
     * @param soundId returned by the load() function
     *
     * @return true if just unloaded, false if previously unloaded
     */
    public boolean unload(int soundId) {
        if (mSoundPool != null) {
            return mSoundPool.unload(soundId);
        } else {
            return false;
        }
    }

    /**
     * 添加加载完成监听
     *
     * @param loadCompleteListener 文件加载回调
     */
    public void addOnLoadCompleteListener(OnLoadCompleteListener loadCompleteListener) {
        synchronized (this) {
            mOnLoadCompleteListeners.add(loadCompleteListener);
        }
    }

    /**
     * 移除加载完成监听
     *
     * @param loadCompleteListener 加载完成监听
     */
    public void removeOnLoadCompleteListener(OnLoadCompleteListener loadCompleteListener) {
        synchronized (this) {
            mOnLoadCompleteListeners.remove(loadCompleteListener);
        }
    }


    public int getMaxStream() {
        return mMaxStream;
    }

    public void setMaxStream(int maxStream) {
        mMaxStream = maxStream;
    }

    public void release() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        mOnLoadCompleteListeners.clear();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        synchronized (this) {
            Iterator<OnLoadCompleteListener> iterator = mOnLoadCompleteListeners.iterator();
            while (iterator.hasNext()) {
                OnLoadCompleteListener listener = iterator.next();
                if (listener.onLoadComplete(soundPool, sampleId, status)) {
                    iterator.remove();
                }
            }
        }
    }


    public void subscribeLifecycle(@NonNull LifecycleOwner lifecycleOwner, boolean unsubscribeRelease) {
        if (mLifecycleOwner != lifecycleOwner && mLifecycleOwner != null) {
            mLifecycleOwner.getLifecycle().removeObserver(this);
        }
        mLifecycleOwner = lifecycleOwner;
        mLifecycleOwner.getLifecycle().addObserver(this);
        this.mUnsubscribeRelease = unsubscribeRelease;
    }


    public void unsubscribe() {
        if (mLifecycleOwner != null) {
            mLifecycleOwner.getLifecycle().removeObserver(this);
            mLifecycleOwner = null;
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        autoResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        autoPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        autoPause();
        if (mUnsubscribeRelease) {
            release();
        }
        unsubscribe();
    }


    public int foundSoundIdByName(String soundName) {
        int result = 0;
        if (mSoundIdMap.containsKey(soundName)) {
            Integer value = mSoundIdMap.get(soundName);
            if (value == null || value == 0) {
                mSoundIdMap.remove(soundName);
            } else {
                result = value;
            }
        }
        return result;
    }
}
