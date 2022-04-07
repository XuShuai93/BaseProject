package com.adair.utils.ui

import android.content.Context

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
    }
}