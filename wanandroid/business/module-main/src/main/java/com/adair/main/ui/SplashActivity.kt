package com.adair.main.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.adair.core.base.ui.viewbinding.BaseVbActivity
import com.adair.main.databinding.MainActivitySplashBinding
import com.adair.middle.router.RouterPath
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar

/**
 *
 * WanAndroid 启屏页
 *
 * @author xushuai
 * @date   2022/5/19-23:29
 * @email  466911254@qq.com
 */
@Route(path = RouterPath.MainRouter.SPLASH_ACTIVITY)
class SplashActivity : BaseVbActivity<MainActivitySplashBinding>() {

    private val mHandler = Handler(Looper.getMainLooper())

    override fun beforeInitView(savedInstanceState: Bundle?) {
        super.beforeInitView(savedInstanceState)
        ImmersionBar.with(this)
            .hideBar(BarHide.FLAG_HIDE_BAR)
            .fullScreen(true)
            .init()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mHandler.postDelayed({
            ARouter.getInstance().build(RouterPath.MainRouter.MAIN_ACTIVITY).navigation()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}