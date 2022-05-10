package com.adair.utils.display

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Android Density单位转换
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/25 18:25
 */
@Suppress("DEPRECATION")
class DensityUtils {
    companion object {

        /**
         * dpi 转像素值
         *
         * @param context 上下文对象
         * @param dpValue dp值
         *
         * @return 像素值
         */
        @JvmStatic
        fun dp2Px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * sp 转 px
         *
         * @param context 上下文对象
         * @param spValue sp值
         *
         * @return 像素值
         */
        @JvmStatic
        fun sp2Px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * px转dp
         *
         * @param context 上下文对象
         * @param pxValue 像素值
         *
         * @return dp值
         */
        @JvmStatic
        fun px2Dp(context: Context, pxValue: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxValue / scale
        }


        /**
         * px转sp
         *
         * @param context 上下文对象
         * @param pxValue 像素值
         *
         * @return sp值
         */
        @JvmStatic
        fun px2Sp(context: Context, pxValue: Float): Float {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return pxValue / fontScale
        }

        /**
         * 手机Density
         */
        @JvmStatic
        fun getDensity(context: Context): Float {
            val dm = context.resources.displayMetrics
            return dm.density
        }


        /** 手机字体缩放Density  */
        @JvmStatic
        fun getScaleDensity(context: Context): Float {
            val dm = context.resources.displayMetrics
            return dm.scaledDensity
        }

        /**
         * 手机dpi,注意，此方法获取的DensityDpi不是手机系统设置的dpi，可能是App自己修改过的Dpi
         */
        @JvmStatic
        fun getDensityDpi(context: Context): Int {
            val dm = context.resources.displayMetrics
            return dm.densityDpi
        }

        /**
         * 这是手机真实的Dpi，不会随App的修改变动
         *
         * @param context 上下文对象
         *
         * @return 真实的手机dpi
         */
        @JvmStatic
        fun getRealDensityDpi(context: Context): Int {
            val metrics = DisplayMetrics()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.display!!.getRealMetrics(metrics)
            } else {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = wm.defaultDisplay
                display.getRealMetrics(metrics)
            }
            return metrics.densityDpi
        }
    }
}