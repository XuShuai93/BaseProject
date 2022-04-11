package com.adair.utils.ui

import android.content.Context
import android.graphics.Color
import android.view.Window
import androidx.annotation.ColorInt

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/6 16:59
 */
class NavigationBarUtils {
    companion object {

        /**
         * 获取导航栏高度
         */
        @JvmStatic
        fun getNavigationBarHeight(context: Context): Int {
            var height = 0
            val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                height = context.resources.getDimensionPixelSize(resourceId)
            }
            return height
        }


        /**
         * 隐藏导航栏
         */
        @JvmStatic
        fun hideNavigationBar(window: Window) {

        }

        /**
         * 显示导航栏
         */
        @JvmStatic
        fun showNavigationBar(window: Window) {

        }

        /**
         * 沉浸式导航栏(内容侵入导航栏显示)
         */
        @JvmStatic
        fun setImmersionNavigationBar(window: Window) {

        }

        /**
         * 设置导航栏颜色
         */
        @JvmStatic
        fun setNavigationBarColor(window: Window, @ColorInt color: Int) {


        }

        /**
         * 设置导航栏亮色文字
         */
        @JvmStatic
        fun setNavigationBarLightFont(window: Window?, lightFont: Boolean) {

        }
    }
}