package com.adair.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * app 相关信息获取工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/24 10:47
 */
class AppInfoUtils private constructor() {

    companion object {

        /**
         * 获取当前App 名称
         * @param context Context 上下文对象
         * @return String App 名称,发生错误返回 空字符串
         */
        @JvmStatic
        fun getAppName(context: Context): String {
            var appName = ""
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                val labelRes = packageInfo.applicationInfo.labelRes
                appName = context.resources.getString(labelRes)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return appName
        }

        /**
         * 获取当前App 版本名称
         * @param context Context 上下文对象
         * @return String App版本名称
         */
        @JvmStatic
        fun getVersionName(context: Context): String {
            var versionName = ""
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                versionName = packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionName
        }

        /**
         * 获取当前App 版本号
         * @param context Context 上下文对象
         * @return Long App版本code,发生错误时返回 -1
         */
        @JvmStatic
        fun getVersionCode(context: Context): Long {
            var versionCode: Long = -1L
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode
                } else {
                    packageInfo.versionCode.toLong()
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionCode
        }
    }
}