package com.adair.core2.base.function

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle

/**
 * activity生命周期功能
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 10:03
 */
open class BaseActivityFunction : Application.ActivityLifecycleCallbacks {

    fun onNewIntent(intent: Intent) {}

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    fun onFinish(activity: Activity) {}

    fun onBackPressed(activity: Activity): Boolean {
        return false
    }

    fun onWindowFocusChanged(activity: Activity) {}
}