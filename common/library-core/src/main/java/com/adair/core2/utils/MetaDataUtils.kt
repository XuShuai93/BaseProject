package com.adair.core2.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * App 读取MetaData相关工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/24 10:21
 */
class MetaDataUtils {

    companion object {

        /**
         * 读取 配置的MetaData 指定key的值
         * @param context Context 上下文对象
         * @param key String metaData的 name
         * @return String? 值，可能为null
         */
        @JvmStatic
        fun getMetaData(context: Context, key: String): String? {
            if (key.isBlank()) {
                return null
            }

            var value: String? = null
            try {
                val packageManager = context.packageManager
                val applicationInfo =
                    packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                if (applicationInfo.metaData != null) {
                    value = applicationInfo.metaData.getString(key)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }
    }
}