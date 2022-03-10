package com.adair.utils

import android.app.Application
import com.adair.utils.activity.ActivityStackManager
import com.adair.utils.crash.CrashHandler
import com.adair.utils.toast.ToastUtils

/**
 * 工具library 管理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/10 15:58
 */
class UtilsManager private constructor() {

    companion object {
        @JvmStatic
        fun init(application: Application) {
            AppInject.init(application)
            CrashHandler.init(application)
            ToastUtils.init(application)
            ActivityStackManager.init(application)
        }
    }
}