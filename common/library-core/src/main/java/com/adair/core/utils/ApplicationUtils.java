package com.adair.core.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 应用Application相关工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/7
 */
public class ApplicationUtils {

    /**
     * <p>
     * created at 2018/6/15 17:23
     *
     * @param context 上下文对象
     * @return java.lang.String App名称
     */
    public static String getAppName(Context context) {
        String appName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName = context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    /**
     * 获取应用程序版本名称信息
     * <p>
     * created at 2018/6/15 17:23
     *
     * @param context 上下文对象
     * @return java.lang.String App当前版本名称
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取App版本号
     * <p>
     * created at 2018/6/15 17:24
     *
     * @param context 上下文对象
     * @return int App当前版本号
     */
    public static long getVersionCode(Context context) {
        long versionCode = -1;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
