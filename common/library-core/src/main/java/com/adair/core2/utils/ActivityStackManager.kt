package com.adair.core2.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

/**
 * App 的全局Activity 对象配置
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/27 16:26
 */
class ActivityStackManager {
    companion object {
        private var mInit = false

        @JvmStatic
        private val mActivityStack = Stack<Activity>()

        private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                addActivity(activity)
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
                removeActivity(activity)
            }
        }

        @JvmStatic
        fun init(application: Application) {
            if (!mInit) {
                application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
                mInit = true
            }
        }

        @JvmStatic
        fun addActivity(activity: Activity?) {
            activity?.let {
                mActivityStack.add(activity)
            }
        }

        @JvmStatic
        fun removeActivity(activity: Activity?) {
            activity?.let {
                mActivityStack.remove(activity)
            }
        }

        @JvmStatic
        fun getSize(): Int {
            return mActivityStack.size
        }

        @JvmStatic
        fun isEmpty(): Boolean {
            return mActivityStack.isEmpty()
        }

        @JvmStatic
        fun isNotEmpty(): Boolean {
            return mActivityStack.isNotEmpty()
        }

        @JvmStatic
        fun topActivity(): Activity? {
            if (isNotEmpty()) {
                return mActivityStack.lastElement()
            }
            return null
        }

        @JvmStatic
        fun getActivity(cls: Class<*>): Activity? {
            if (isNotEmpty()) {
                for (activity in mActivityStack) {
                    if (activity.javaClass == cls) {
                        return activity
                    }
                }
            }
            return null
        }


        @JvmStatic
        fun finishActivity(activity: Activity?) {
            activity?.let {
                if (!it.isFinishing) {
                    it.finish()
                }
            }
        }

        @JvmStatic
        fun finishTopActivity() {
            finishActivity(topActivity())
        }

        /**结束指定类名的Activity*/
        @JvmStatic
        fun finishActivity(cls: Class<*>) {
            finishActivity(getActivity(cls))
        }

        fun finishAllActivity() {
            if (isNotEmpty()) {
                for (activity in mActivityStack) {
                    finishActivity(activity)
                }
            }
        }
    }
}