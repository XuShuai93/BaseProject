package com.adair.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * 网络相关工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/10 16:35
 */
class NetworkUtils private constructor() {

    companion object {
        /**
         * 打开网络设置界面
         *
         * @param activity    上下文对象
         * @param requestCode 请求code
         */
        @JvmStatic
        fun openNetworkSetting(activity: Activity, requestCode: Int) {
            val intent = Intent()
            val cm = ComponentName("com.android.settings", "com.android.settings.WirelessSettings")
            intent.component = cm
            intent.action = "android.intent.action.VIEW"
            activity.startActivityForResult(intent, requestCode)
        }

        /**
         * 使用经典方法ping的地址，检查网络是否连通,默认ping www.baidu.com
         *
         *
         * 移动数据连通下：0
         * 可用wifi:2
         * 需要网页认证的wifi:1
         * wifi打开但没有网络，移动数据也不可用：2
         * 不可用wifi:2
         */
        @JvmStatic
        fun ping(ip: String): Boolean {
            var useIp = ip
            val runtime = Runtime.getRuntime()
            if (useIp.isBlank()) {
                useIp = "www.baidu.com"
            }
            try {
                val process = runtime.exec("ping -c 3 -w 100 $ip")
                return process.waitFor() == 0
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        /**
         * 判断当前是否有网络连接
         * @param context Context 上下文对象
         * @return Boolean true 当前连接到网络
         */
        @JvmStatic
        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null) {
                    return networkInfo.isAvailable
                }
            } else {
                val network = cm.activeNetwork ?: return false
                val status = cm.getNetworkCapabilities(network) ?: return false

                return status.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                        status.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            return false
        }

        /**
         * 判断是否是wifi网络连接
         * @param context Context 上下文对象
         * @return Boolean true 当前网络时wifi连接
         */
        @JvmStatic
        fun isWifiConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                val wifiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                if (wifiNetworkInfo != null) {
                    return wifiNetworkInfo.isAvailable
                }
            } else {
                val network = cm.activeNetwork ?: return false
                val status = cm.getNetworkCapabilities(network) ?: return false
                if (status.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                }
            }
            return false
        }


        /**
         * 是否是移动网络连接
         * @param context Context 上下文对象
         * @return Boolean true 移动网络
         */
        @JvmStatic
        fun isMobileConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                val mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                if (mobileNetworkInfo != null) {
                    return mobileNetworkInfo.isAvailable
                }
            } else {
                val network = cm.activeNetwork ?: return false
                val status = cm.getNetworkCapabilities(network) ?: return false
                if (status.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                }
            }
            return false
        }
    }
}