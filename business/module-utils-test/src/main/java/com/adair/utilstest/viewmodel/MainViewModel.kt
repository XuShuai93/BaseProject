package com.adair.utilstest.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.adair.core.base.viewmodel.BaseViewModel
import com.adair.core.utils.toast.ToastUtils

/**
 *
 *  主界面ViewModel
 *
 * @author xushuai
 * @date   2022/5/12-22:56
 * @email  466911254@qq.com
 */
class MainViewModel : BaseViewModel() {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }


    fun toast() {
        ToastUtils.show("11111111")
    }

}