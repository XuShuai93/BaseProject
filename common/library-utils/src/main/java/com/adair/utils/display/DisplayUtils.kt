package com.adair.utils.display

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics

/**
 * 新安所相关信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/15 10:00
 */
class DisplayUtils {

    companion object {


        /**
         * 获取显示区域狂歌
         * @param activity Activity
         */
        @JvmStatic
        fun getDisplaySizeV1(activity: Activity): IntArray {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.display
            } else {
                activity.windowManager.defaultDisplay
            }
            val point = Point()
            display?.getSize(point)
            val result = intArrayOf(0, 0)
            result[0] = point.x
            result[1] = point.y
            return result
        }

        @JvmStatic
        fun getDisplaySizeV2(activity: Activity): IntArray {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.display
            } else {
                activity.windowManager.defaultDisplay
            }
            val rect = Rect()
            display?.getRectSize(rect)
            val result = intArrayOf(0, 0)
            result[0] = if (rect.right - rect.left >= 0) {
                rect.right - rect.left
            } else {
                0
            }
            result[1] = if (rect.bottom - rect.top >= 0) {
                rect.bottom - rect.top
            } else {
                0
            }
            return result
        }

        @JvmStatic
        fun getDisplaySizeV3(activity: Activity): IntArray {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.display
            } else {
                activity.windowManager.defaultDisplay
            }
            val metrics = DisplayMetrics()
            display?.getMetrics(metrics)
            val result = intArrayOf(0, 0)
            result[0] = metrics.widthPixels
            result[1] = metrics.heightPixels
            return result
        }

        @JvmStatic
        fun getDisplaySizeV4(activity: Activity): IntArray {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.display
            } else {
                activity.windowManager.defaultDisplay
            }
            val point = Point()
            display?.getRealSize(point)
            val result = intArrayOf(0, 0)
            result[0] = point.x
            result[1] = point.y
            return result
        }

        @JvmStatic
        fun getDisplaySizeV5(activity: Activity): IntArray {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.display
            } else {
                activity.windowManager.defaultDisplay
            }
            val metrics = DisplayMetrics()
            display?.getRealMetrics(metrics)
            val result = intArrayOf(0, 0)
            result[0] = metrics.widthPixels
            result[1] = metrics.heightPixels
            return result
        }
    }
}