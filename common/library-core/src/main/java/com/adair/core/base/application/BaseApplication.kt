package com.adair.core.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.adair.core.activity.ActivityStackManager
import com.adair.core.crash.CrashHandler
import com.adair.core.utils.AppInject
import com.adair.core.utils.toast.ToastUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * 基础Application
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 9:58
 */
open class BaseApplication : Application() {

    private val mComponentAppClassNameList = mutableListOf<String>()
    private val mHelper = ComponentApplicationHelper()


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        addComponentApplication(mComponentAppClassNameList)
        mHelper.initAppObjects(mComponentAppClassNameList)
        mHelper.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppInject.init(this)
        ToastUtils.init(this)
        Logger.addLogAdapter(AndroidLogAdapter())
        ActivityStackManager.init(this)
        CrashHandler.init(this)
        mHelper.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mHelper.onTerminate(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mHelper.onLowMemory(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        mHelper.onTrimMemory(this, level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mHelper.onConfigurationChanged(this, newConfig)
    }

    open fun addComponentApplication(classNames: MutableList<String>) {

    }
}