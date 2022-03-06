package com.adair.core2.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import java.lang.Exception

/**
 *
 * 组件Application辅助类
 *
 * @author xushuai
 * @date   2022/3/6-13:43
 * @email  466911254@qq.com
 */
internal class ComponentApplicationHelper : IComponentApplication {

    private val mComponentAppObjects = mutableListOf<IComponentApplication>()

    fun initAppObjects(classNames: List<String>) {
        mComponentAppObjects.clear()
        for (className in classNames) {
            try {
                val clazz = Class.forName(className)
                val obj = clazz.newInstance()
                if (obj is IComponentApplication) {
                    mComponentAppObjects.add(obj)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun attachBaseContext(context: Context) {
        for (app in mComponentAppObjects) {
            app.attachBaseContext(context)
        }
    }

    override fun onCreate(application: Application) {
        for (app in mComponentAppObjects) {
            app.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        for (app in mComponentAppObjects) {
            app.onTerminate(application)
        }
    }

    override fun onLowMemory(application: Application) {
        for (app in mComponentAppObjects) {
            app.onLowMemory(application)
        }
    }

    override fun onTrimMemory(application: Application, level: Int) {
        for (app in mComponentAppObjects) {
            app.onTrimMemory(application, level)
        }
    }

    override fun onConfigurationChanged(application: Application, newConfig: Configuration) {
        for (app in mComponentAppObjects) {
            app.onConfigurationChanged(application, newConfig)
        }
    }
}