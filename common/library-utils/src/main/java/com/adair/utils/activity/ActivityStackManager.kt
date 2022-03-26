package com.adair.utils.activity

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
object ActivityStackManager {
    private var mInit = false
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
    private fun addActivity(activity: Activity?) {
        activity?.let {
            mActivityStack.add(activity)
        }
    }

    @JvmStatic
    private fun removeActivity(activity: Activity?) {
        activity?.let {
            mActivityStack.remove(activity)
        }
    }

    /**
     * 获取栈中Activity数量
     * @return Int 当前栈中Activity数量
     */
    @JvmStatic
    fun getSize(): Int {
        return mActivityStack.size
    }

    /**
     * 返回当前栈是否是null的
     * @return Boolean true 当前栈的activity的数量为0,false 数量>0
     */
    @JvmStatic
    fun isEmpty(): Boolean {
        return mActivityStack.isEmpty()
    }

    /**
     * 返回当前栈是否不为空
     * @return Boolean true 当前栈不为空，false 当前栈为空
     */
    @JvmStatic
    fun isNotEmpty(): Boolean {
        return mActivityStack.isNotEmpty()
    }

    /**
     * 获取当前栈顶层activity,即当前显示的activity
     * @return Activity? 顶层activity
     */
    @JvmStatic
    fun topActivity(): Activity? {
        if (isNotEmpty()) {
            return mActivityStack.lastElement()
        }
        return null
    }

    /**
     * 当前栈中查找指定class的activity
     * @param cls Class<*> 指定的class对象
     * @return Activity? 找到的activity对象
     */
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

    /**
     * 根据指定的类名查找栈中是否有activity对象
     * @param clazzName String 类名字符串，注意：类名包含包名
     * @return Activity? 找到的activity对象
     */
    @JvmStatic
    fun getActivity(clazzName: String): Activity? {
        if (isNotEmpty()) {
            for (activity in mActivityStack) {
                if (activity.javaClass.name == clazzName) {
                    return activity
                }
            }
        }
        return null
    }


    /**
     * 关闭指定界面
     * @param activity Activity?
     */
    @JvmStatic
    fun finishActivity(activity: Activity?) {
        activity?.let {
            if (!it.isFinishing) {
                it.finish()
            }
        }
    }

    /**
     * 关闭当前界面
     */
    @JvmStatic
    fun finishTopActivity() {
        finishActivity(topActivity())
    }

    /**结束指定类名的Activity*/
    @JvmStatic
    fun finishActivity(cls: Class<*>) {
        finishActivity(getActivity(cls))
    }

    /**
     * 结束指定类名的activity
     * @param clazzName String 指定的activity类名，包含类的包名
     */
    @JvmStatic
    fun finishActivity(clazzName: String) {
        finishActivity(getActivity(clazzName))
    }

    /**
     * 关闭所有界面
     */
    @JvmStatic
    fun finishAllActivity() {
        if (isNotEmpty()) {
            for (activity in mActivityStack) {
                finishActivity(activity)
            }
        }
    }
}