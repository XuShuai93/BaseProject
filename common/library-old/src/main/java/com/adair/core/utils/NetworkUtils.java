package com.adair.core.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

/**
 * 网络工具
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/7
 */
public class NetworkUtils {

    /**
     * 打开网络设置界面
     *
     * @param activity    上下文对象
     * @param requestCode 请求code
     */
    public static void openNetworkSetting(Activity activity, int requestCode) {
        Intent intent = new Intent();
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取可用网络信息 ，API 29过期
     *
     * @param context 上下文对象
     * @return 信息
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    @Deprecated()
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }
        return null;
    }

    /**
     * 判断是否有可用网络，wifi或者移动数据
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    return networkInfo.isAvailable();
                }
            } else {
                Network network = cm.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities nc = cm.getNetworkCapabilities(network);
                    if (nc != null) {
                        return nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 使用经典方法ping的地址，检查网络是否连通,默认ping www.baidu.com
     * <p>
     * 移动数据连通下：0
     * 可用wifi:2
     * 需要网页认证的wifi:1
     * wifi打开但没有网络，移动数据也不可用：2
     * 不可用wifi:2
     */
    public static boolean ping(String ip) {
        Runtime runtime = Runtime.getRuntime();
        if (TextUtils.isEmpty(ip)) {
            ip = "www.baidu.com";
        }
        try {
            Process process = runtime.exec("ping -c 3 -w 100 " + ip);
            return process.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
