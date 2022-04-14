package com.adair.mmkv

import android.content.Context
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * MMKV 管理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/14 9:59
 */
object MMKVManager {

    @JvmStatic
    fun initMMKV(
        context: Context,
        rootDir: String? = null,
        loader: MMKV.LibLoader? = null,
        logLevel: MMKVLogLevel? = MMKVLogLevel.LevelInfo
    ) {
        val root = rootDir ?: context.filesDir.absolutePath + "/mmkv"
        val realLogLevel = logLevel ?: MMKVLogLevel.LevelInfo
        MMKV.initialize(context, root, loader, realLogLevel)
    }


    private const val DEFAULT_MMKV = "defaultMMKV"
    private val mmkvMap = HashMap<String, MMKV?>()

    fun getMMKV(mmapID: String): MMKV {
        var value = mmkvMap[mmapID]
        if (value == null) {
            value = MMKV.mmkvWithID(mmapID)
            mmkvMap[mmapID] = value
        }
        return value!!
    }

    fun getDefault(): MMKV {
        var value = mmkvMap[DEFAULT_MMKV]
        if (value == null) {
            value = MMKV.defaultMMKV()
            mmkvMap[DEFAULT_MMKV] = value
        }
        return value!!
    }
}