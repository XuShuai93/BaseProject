package com.adair.core2.base.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.adair.core.utils.StatusBarUtils


/**
 * 基础activity类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:01
 */
abstract class BaseActivity : AppCompatActivity() {

    private var mResume: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuperOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        beforeSetContentView(savedInstanceState)
        if (getContentLayoutId() != 0) {
            setContentView(getContentLayoutId())
        } else if (getContentView() != null) {
            setContentView(getContentView())
        }
        if (isHideStatusBar()) {
            fullScreen()
        }
        if (isLandscape()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
        afterSetContentView(savedInstanceState)
        initView(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        mResume = true
        if (isHideNavigationBar()) {
            hideNavigationBar()
        }
    }

    override fun onPause() {
        super.onPause()
        mResume = false
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (isHideNavigationBar()) {
            hideNavigationBar()
        }
    }

    /**
     * 此方法需要在setContentView()之后调用
     */
    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun hideNavigationBar() {
        StatusBarUtils.hideNavigationBar(this)
    }

    /**执行在super.onCreate(savedInstanceState)之前*/
    open fun beforeSuperOnCreate(savedInstanceState: Bundle?) {}

    /**在setContentView之前执行*/
    open fun beforeSetContentView(savedInstanceState: Bundle?) {
        if (isHideStatusBar()) {
            //隐藏标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    /**
     * 是否在Resume显示期间
     *
     * @return true 在Resume期间
     */
    fun isResume(): Boolean {
        return mResume
    }

    /**
     * 隐藏状态栏
     *
     * @return true 隐藏状态栏,false 显示状态栏
     */
    open fun isHideStatusBar(): Boolean {
        return false
    }


    /**
     * 隐藏导航栏
     *
     * @return true 隐藏导航栏,false 显示导航栏
     */
    open fun isHideNavigationBar(): Boolean {
        return false
    }

    /**
     * 设置横屏
     *
     * @return true 横屏显示,false 默认竖屏显示
     */
    open fun isLandscape(): Boolean {
        return false
    }

    /**返回布局Layout*/
    @LayoutRes
    abstract fun getContentLayoutId(): Int

    /**返回ContentView*/
    abstract fun getContentView(): View?

    /**在setContentView之后执行*/
    open fun afterSetContentView(savedInstanceState: Bundle?) {}

    /**初始化View*/
    abstract fun initView(savedInstanceState: Bundle?)
}