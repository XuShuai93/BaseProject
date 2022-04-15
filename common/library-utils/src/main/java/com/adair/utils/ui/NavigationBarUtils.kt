package com.adair.utils.ui

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

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
        fun hideNavigationBar(window: Window?) {
            window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    insetsController?.let {
                        val controllerCompat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(it)
                        controllerCompat.hide(WindowInsetsCompat.Type.navigationBars())
                        controllerCompat.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                } else {
                    val options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    decorView.systemUiVisibility = decorView.systemUiVisibility or options
                }
            }
        }

        /**
         * 显示导航栏
         */
        @JvmStatic
        fun showNavigationBar(window: Window?) {
            window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    insetsController?.let {
                        val controllerCompat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(it)
                        controllerCompat.show(WindowInsetsCompat.Type.navigationBars())
                    }
                } else {
                    val options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    decorView.systemUiVisibility = decorView.systemUiVisibility and options.inv()
                }
            }
        }

        /**
         * 沉浸式导航栏(内容侵入导航栏显示)
         */
        @JvmStatic
        fun setImmersionNavigationBar(window: Window) {
           val controllerCompat =  ViewCompat.getWindowInsetsController(window.decorView)

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