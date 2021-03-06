package com.adair.utils

import android.content.Context
import android.provider.Settings

/**
 * 系统信息相关工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/24 10:55
 */
class SystemUtils private constructor() {

    companion object {
        /**
         * 获取AndroidId
         * <p>
         * 注意：刷机、root、恢复出厂设置等会使得 Android ID 改变
         * Android 8.0之后，Android ID的规则发生了变化：
         * 对于升级到8.0之前安装的应用，ANDROID_ID会保持不变。如果卸载后重新安装的话，ANDROID_ID将会改变。
         * 对于安装在8.0系统的应用来说，ANDROID_ID根据应用签名和用户的不同而不同。ANDROID_ID的唯一决定于应用签名、用户和设备三者的组合。
         * 两个规则导致的结果就是：
         * 第一，如果用户安装APP设备是8.0以下，后来卸载了，升级到8.0之后又重装了应用，Android ID不一样；
         * 第二，不同签名的APP，获取到的Android ID不一样。*
         *
         * @return ANDROID_ID
         */
        @JvmStatic
        fun getAndroidId(context: Context): String {
            var value = ""
            try {
                value = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }
    }
}