package com.adair.core2.base.ui.activity.vbvm

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.adair.core2.base.ui.activity.viewbinding.BaseVbActivity
import com.adair.core2.base.viewmodel.BaseViewModel

/**
 *
 * 使用ViewBinding和ViewModel的Activity
 *
 * @author xushuai
 * @date   2022/5/11-0:47
 * @email  466911254@qq.com
 */
abstract class BaseVbVmActivity<VB : ViewBinding, VM : BaseViewModel> : BaseVbActivity<VB>() {

    /** 泛型中的默认ViewModel对象 */
    protected lateinit var mViewModel: VM

    override fun beforeInitView(savedInstanceState: Bundle?) {
        super.beforeInitView(savedInstanceState)
        val vm = createViewModel()
        mViewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm)).get(vm::class.java)
    }

    /**
     * 创建ViewModel对象
     */

    /**
     * 创建ViewModel对象
     * @return VM 泛型的ViewModel对象,直接new 一个对象供ViewModelFactory使用
     */
    protected abstract fun createViewModel(): VM
}