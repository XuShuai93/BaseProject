package com.adair.middleware.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.adair.core.base.viewmodel.BaseViewModel

/**
 *
 * 主界面共享的ViewModel，各个模块可以共享的数据
 *
 * @author xushuai
 * @date   2022/5/29-18:47
 * @email  466911254@qq.com
 */
class MainShareViewModel : BaseViewModel() {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }
}