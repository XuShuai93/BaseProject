package com.adair.utils

import android.os.Build

/**
 *  手机系统判断
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/8 10:59
 */
class OSUtils private constructor() {

    companion object {

        /**
         * 判断是否是小米手机
         * @return Boolean true 小米手机，false 不是小米手机
         */
        @JvmStatic
        fun isXiaoMi(): Boolean {
            return Build.MANUFACTURER.lowercase().contains("xiaomi")
        }

        /**
         * 判断是否是华为手机
         * @return Boolean true 华为系统的手机，false 非华为系统手机
         */
        @JvmStatic
        fun isHuaWei(): Boolean {
            return Build.MANUFACTURER.lowercase().contains("huawei")
        }


        /**
         * 判断是否是Oppo
         * @return Boolean
         */
        @JvmStatic
        fun isOppo(): Boolean {
            return Build.MANUFACTURER.lowercase().contains("oppo")
        }

        /**
         * 是否是Vivo手机 Is vivo boolean.
         * @return Boolean
         */
        @JvmStatic
        fun isViVo(): Boolean {
            return Build.MANUFACTURER.lowercase().contains("vivo")
        }

        /**
         * 是否是三星手机
         * @return Boolean
         */
        @JvmStatic
        fun isSamsung(): Boolean {
            return Build.MANUFACTURER.lowercase().contains("samsung")
        }

        /**
         * 是否是联想手机
         * @return Boolean
         */
        @JvmStatic
        fun isLenovo(): Boolean {
            return Build.MANUFACTURER.lowercase().contains("lenovo")
        }

        /**
         * 是否是魅族手机
         * @return Boolean
         */
        @JvmStatic
        fun isMeizu(): Boolean {
            return Build.MANUFACTURER.lowercase().contains("meizu")
        }
    }
}