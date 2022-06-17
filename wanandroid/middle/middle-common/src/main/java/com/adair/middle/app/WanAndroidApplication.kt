package com.adair.middle.app

import com.adair.core.base.application.BaseApplication
import com.alibaba.android.arouter.launcher.ARouter

/**
 *
 *  主要Application
 *
 * @author xushuai
 * @date   2022/5/21-11:52
 * @email  466911254@qq.com
 */
class WanAndroidApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }
}