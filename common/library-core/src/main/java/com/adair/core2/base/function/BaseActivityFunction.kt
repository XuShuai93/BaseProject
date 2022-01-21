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
open class BaseActivityFunction {

    fun onNewIntent(activity: Activity, intent: Intent?) {

    }

    fun onCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    fun onRestarted(activity: Activity) {

    }

    fun onStarted(activity: Activity) {

    }

    fun onResumed(activity: Activity) {

    }

    fun onActivityResult(activity: Activity,requestCode: Int, resultCode: Int, data: Intent?) {

    }

    fun onPaused(activity: Activity) {

    }

    fun onStopped(activity: Activity) {

    }

    fun onSaveInstanceState(activity: Activity, outState: Bundle) {

    }

    fun onRestoreInstanceState(activity: Activity, savedInstanceState: Bundle) {

    }

    fun onDestroyed(activity: Activity) {

    }

    fun onFinish(activity: Activity) {

    }

    fun onWindowFocusChanged(activity: Activity, hasFocus: Boolean) {

    }
}