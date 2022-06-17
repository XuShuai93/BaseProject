package com.adair.core.sound

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.annotation.RawRes
import com.adair.core.utils.AppInject

/**
 * 使用SoundPool播放音频的工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/5/23 9:58
 * @email 466911254@qq.com
 */
object SoundPoolHelper : SoundPool.OnLoadCompleteListener {

    private const val MAX_STREAMS = 5

    /** 保存已经加载的声音 */
    private val mSoundIdMap = LinkedHashMap<String, Int>()

    private val mNeedPlaySoundId = HashMap<Int, SoundInfo>()

    private var mSoundPool: SoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .build()
        SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(MAX_STREAMS)
            .build()
    } else {
        SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
    }

    init {
        mSoundPool.setOnLoadCompleteListener(this)
    }

    override fun onLoadComplete(soundPool: SoundPool, sampleId: Int, status: Int) {
        if (mNeedPlaySoundId.size > 0 && mNeedPlaySoundId.containsKey(sampleId)) {
            val soundInfo = mNeedPlaySoundId[sampleId]
            soundInfo?.let {
                if (it.soundId == sampleId) {
                    playSound(sampleId, it.leftVolume, it.rightVolume, 1, it.loop, it.rate)
                }
            }
            mNeedPlaySoundId.remove(sampleId)
        }
    }


    /**
     * 加载Assets的音频文件,
     * @param filePath String 文件在assets的路径
     * @return Int 加载的soundId,0 为加载失败
     */
    fun loadAssetsFile(filePath: String): Int {
        var soundId = 0
        try {
            if (mSoundIdMap.containsKey(filePath)) {
                soundId = mSoundIdMap[filePath] ?: 0
                if (soundId != 0) {
                    return soundId
                }
            }
            val afd = AppInject.getApp().assets.openFd(filePath)
            soundId = mSoundPool.load(afd, 1)
            if (soundId != 0) {
                mSoundIdMap[filePath] = soundId
                return soundId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return soundId
    }

    /**
     * 加载本地文件
     * @param filePath String 文件路径
     * @return Int soundId
     */
    fun loadFile(filePath: String): Int {
        var soundId = 0
        try {
            if (mSoundIdMap.containsKey(filePath)) {
                soundId = mSoundIdMap[filePath] ?: 0
                if (soundId != 0) {
                    return soundId
                }
            }
            soundId = mSoundPool.load(filePath, 1)
            if (soundId != 0) {
                mSoundIdMap[filePath] = soundId
                return soundId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return soundId
    }

    /**
     * 加载Raw文件
     * @param resId Int Raw的文件id
     * @return Int 加载的soundId,0 为加载失败
     */
    fun loadRaw(@RawRes resId: Int): Int {
        val soundName = resId.toString()
        var soundId = 0
        try {
            if (mSoundIdMap.containsKey(soundName)) {
                soundId = mSoundIdMap[soundName] ?: 0
                if (soundId != 0) {
                    return soundId
                }
            }
            soundId = mSoundPool.load(AppInject.getApp(), resId, 1)
            if (soundId != 0) {
                mSoundIdMap[soundName] = soundId
                return soundId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return soundId
    }

    /**
     * 播放音频
     * @param soundId Int 音频加载id
     * @param leftVolume Float 左声道大小
     * @param rightVolume Float 右声道大小
     * @param priority Int 播放优先级
     * @param loop Int  循环次数,0不循环，-1无限循环,其他为循环次数
     * @param rate Float  播放速度(1.0 = normal playback, range 0.5 to 2.0)
     * @return Int 返回此次播放的流id,用此id，可以控制播放暂停,继续，关闭,0播放失败
     */
    fun playSound(
        soundId: Int,
        leftVolume: Float = 1.0f,
        rightVolume: Float = 1.0f,
        priority: Int = 1,
        loop: Int = 0,
        rate: Float = 1.0f
    ): Int {
        return mSoundPool.play(soundId, leftVolume, rightVolume, priority, loop, rate)
    }

    /**
     * 播放Raw文件
     * @param resId Int raw资源id
     * @param leftVolume Float 左声道大小 0-1.0f
     * @param rightVolume Float 右声道大小 0-1.0f
     * @param loop Int  循环播放,0不循环，-1无限循环,正数循环次数
     * @param rate Float 播放速率 0.5f -2.0f
     */
    @Synchronized
    fun playRaw(
        @RawRes resId: Int,
        leftVolume: Float = 1.0f,
        rightVolume: Float = 1.0f,
        loop: Int = 0,
        rate: Float = 1f
    ) {
        val soundName = resId.toString()
        var soundId = foundSoundIdBySoundName(soundName)
        if (soundId == 0) {
            soundId = mSoundPool.load(AppInject.getApp(), resId, 1)
            if (soundId != 0) {
                mSoundIdMap[soundName] = soundId
                mNeedPlaySoundId[soundId] =
                    SoundInfo(soundName, soundId, leftVolume, rightVolume, loop, rate)
            }
        } else {
            playSound(soundId, leftVolume, rightVolume, 1, loop, rate)
        }
    }

    /**
     * 直接播放Assets中的音频文件
     * @param filePath String assets文件路径
     * @param leftVolume Float 左声道大小 0-1.0f
     * @param rightVolume Float 右声道大小 0-1.0f
     * @param loop Int  循环播放,0不循环，-1无限循环,正数循环次数
     * @param rate Float 播放速率 0.5f -2.0f
     */
    fun playAssetsFile(
        filePath: String,
        leftVolume: Float = 1.0f,
        rightVolume: Float = 1.0f,
        loop: Int = 0,
        rate: Float = 1f
    ) {
        var soundId = foundSoundIdBySoundName(filePath)
        if (soundId == 0) {
            val afd = AppInject.getApp().assets.openFd(filePath)
            soundId = mSoundPool.load(afd, 1)
            if (soundId != 0) {
                mSoundIdMap[filePath] = soundId
                mNeedPlaySoundId[soundId] =
                    SoundInfo(filePath, soundId, leftVolume, rightVolume, loop, rate)
            }
        } else {
            playSound(soundId, leftVolume, rightVolume, 1, loop, rate)
        }
    }

    /**
     * 直接播放本地文件中的音频文件，外部文件需要读写权限
     * @param filePath String assets文件路径
     * @param leftVolume Float 左声道大小 0-1.0f
     * @param rightVolume Float 右声道大小 0-1.0f
     * @param loop Int  循环播放,0不循环，-1无限循环,正数循环次数
     * @param rate Float 播放速率 0.5f -2.0f
     */
    fun playFile(
        filePath: String,
        leftVolume: Float = 1.0f,
        rightVolume: Float = 1.0f,
        loop: Int = 0,
        rate: Float = 1f
    ) {
        var soundId = foundSoundIdBySoundName(filePath)
        if (soundId == 0) {
            soundId = mSoundPool.load(filePath, 1)
            if (soundId != 0) {
                mSoundIdMap[filePath] = soundId
                mNeedPlaySoundId[soundId] =
                    SoundInfo(filePath, soundId, leftVolume, rightVolume, loop, rate)
            }
        } else {
            playSound(soundId, leftVolume, rightVolume, 1, loop, rate)
        }
    }

    /** 通过name查找SoundId */
    fun foundSoundIdBySoundName(soundName: String): Int {
        var result = 0
        if (mSoundIdMap.containsKey(soundName)) {
            val value = mSoundIdMap[soundName]
            if (value == null || value == 0) {
                mSoundIdMap.remove(soundName)
            } else {
                result = value
            }
        }
        return result
    }

    class SoundInfo(
        val soundName: String,
        val soundId: Int,
        val leftVolume: Float,
        val rightVolume: Float,
        val loop: Int,
        val rate: Float
    )
}