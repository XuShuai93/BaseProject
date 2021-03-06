package com.adair.utils.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


/**
 * 状态栏相关工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/6 16:32
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class StatusBarUtils {

    companion object {

        private const val STATUS_BAR_VIEW_TAG = "StatusBarView"

        /**
         * 获取状态栏高度
         */
        @JvmStatic
        fun getStatusBarHeight(context: Context): Int {
            var height = 0
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                height = context.resources.getDimensionPixelSize(resourceId)
            }
            return height
        }

        /**
         * 隐藏状态栏
         * @param window Window? activity和dialog对应不同的window
         */
        @JvmStatic
        fun hideStatusBar(window: Window?) {
            window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    insetsController?.let {
                        val controllerCompat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(it)
                        controllerCompat.hide(WindowInsetsCompat.Type.statusBars())
                        controllerCompat.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                } else {
                    addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    val statusBarView = decorView.findViewWithTag<StatusBarView>(STATUS_BAR_VIEW_TAG)
                    statusBarView?.visibility = View.GONE
                }
            }
        }

        /**
         * 显示状态栏
         * @param window Window? activity和dialog对应不同的window
         */
        @JvmStatic
        fun showStatusBar(window: Window?) {
            window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    insetsController?.show(WindowInsets.Type.statusBars())
                } else {
                    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    val statusBarView = decorView.findViewWithTag<StatusBarView>(STATUS_BAR_VIEW_TAG)
                    statusBarView?.visibility = View.VISIBLE
                }
            }
        }


        /**
         * 判断状态栏是否显示
         *
         * @param window Window?
         * @return Boolean
         */
        @JvmStatic
        fun isShowStatusBar(window: Window?): Boolean {
            return window?.let {

                val content = it.findViewById<ViewGroup>(android.R.id.content)

                val contentView: ViewGroup = content.parent.parent as ViewGroup
                val contentViewTop = contentView.y != 0f
                if (contentViewTop) {
                    return true
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val insetsCompat = ViewCompat.getRootWindowInsets(content)
                    return insetsCompat?.isVisible(WindowInsetsCompat.Type.statusBars()) ?: false
                } else {
                    val params = it.attributes
                    val flag = params.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                    val flagShow = flag != params.flags
                    if (flagShow) {
                        return true
                    }
                }
                return false
            } ?: false
        }


        /**
         * 设置沉浸式状态栏 ，状态栏覆盖在内容上面
         * @param window Window?
         */
        @JvmStatic
        fun setImmersionStatusBar(window: Window?, fitsSystemWindows: Boolean = false) {
            showStatusBar(window)
            window?.apply {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        WindowCompat.setDecorFitsSystemWindows(this, fitsSystemWindows)
                        statusBarColor = Color.TRANSPARENT
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                        //取消状态栏透明
                        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        //添加Flag把状态栏设为可绘制模式
                        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        //设置状态栏颜色
                        statusBarColor = Color.TRANSPARENT

                        val options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        if (!fitsSystemWindows) {
                            //设置window的状态栏不可见,添加此2个flag，状态栏覆盖在界面内容之上
                            decorView.systemUiVisibility = decorView.systemUiVisibility or options
                        } else {
                            decorView.systemUiVisibility = decorView.systemUiVisibility and options.inv()
                        }
                        //刷新界面,防止界面不生效
                        decorView.requestApplyInsets()
                    }
                    else -> {
                        addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        val statusBarView = decorView.findViewWithTag<StatusBarView>(STATUS_BAR_VIEW_TAG)
                        statusBarView?.setBackgroundColor(Color.TRANSPARENT)
                        setFitSystemWindows(fitsSystemWindows)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                            decorView.requestApplyInsets()
                        } else {
                            decorView.requestFitSystemWindows()
                        }
                    }
                }
            }
        }


        /**
         * 设置状态栏颜色
         * @param color Int 状态栏颜色值
         */
        @JvmStatic
        fun setStatusBarColor(window: Window?, @ColorInt color: Int) {
            window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //取消状态栏透明
                    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    //添加Flag把状态栏设为可绘制模式
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    //设置状态栏颜色
                    statusBarColor = color
                } else {
                    addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    //获取decorView
                    val decorView: ViewGroup = decorView as ViewGroup
                    var statusBarView = decorView.findViewWithTag<StatusBarView>(STATUS_BAR_VIEW_TAG)
                    if (statusBarView != null) {
                        statusBarView.setBackgroundColor(color)
                    } else {
                        statusBarView = StatusBarView(context).apply {
                            val params = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                getStatusBarHeight(context)
                            )
                            layoutParams = params
                            tag = STATUS_BAR_VIEW_TAG
                            setBackgroundColor(color)
                        }
                        decorView.addView(statusBarView)
                    }
                    setFitSystemWindows(true)
                }
            }
        }

        /**
         * 给Content 设置状态栏高度的padding
         * @param window Window?
         * @param fitsSystemWindows Boolean
         */
        fun setFirSystemWindows(window: Window?, fitsSystemWindows: Boolean) {
            window?.setFitSystemWindows(fitsSystemWindows)
        }


        /**
         * 给布局添加FitsSystemWindows 标记，为状态栏留出位置
         * @receiver Window
         * @param fitsSystemWindows Boolean
         */
        private fun Window.setFitSystemWindows(fitsSystemWindows: Boolean) {
            val contentView = findViewById<ViewGroup>(android.R.id.content)
            if (fitsSystemWindows) {
                contentView.setPadding(0, getStatusBarHeight(context), 0, 0)
            } else {
                contentView.setPadding(0, 0, 0, 0)
            }
        }


        /**
         * @param activity  目标activity
         * @param lightFont font is black when true or is white when false
         */
        @JvmStatic
        fun setStatusBarLightFont(window: Window?, lightFont: Boolean) {
            window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    insetsController?.let {
                        val controllerCompat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(it)
                        controllerCompat.isAppearanceLightStatusBars = lightFont
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val options = if (lightFont) {
                        decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    }
                    decorView.systemUiVisibility = options
                }
            }
        }

        /**
         * Is light status bar
         *
         * @param activity 上下文对象
         * @return true 文字颜色为黑色,false 文字颜色白色
         */
        @JvmStatic
        fun isStatusBarLightFont(window: Window?): Boolean {
            return window?.let {
                // 设置浅色状态栏时的界面显示
                return when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        it.insetsController?.let { controller ->
                            val controllerCompat =
                                WindowInsetsControllerCompat.toWindowInsetsControllerCompat(controller)
                            controllerCompat.isAppearanceLightStatusBars
                        } ?: false
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        it.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                    else -> {
                        false
                    }
                }
            } ?: false
        }

    }
}