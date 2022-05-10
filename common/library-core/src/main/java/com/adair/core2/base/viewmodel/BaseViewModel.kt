package com.adair.core2.base.viewmodel

import androidx.lifecycle.*

/**
 *
 * 基础ViewModel对象
 *
 * @author xushuai
 * @date   2022/5/5-21:21
 * @email  466911254@qq.com
 */
abstract class BaseViewModel : ViewModel(), LifecycleEventObserver {

    companion object {
        @JvmStatic
        fun createViewModelFactory(viewModel: ViewModel): ViewModelFactory {
            return ViewModelFactory(viewModel)
        }
    }
}

/**
 * ViewModel 工厂，这样可以外部传递参数给ViewModel
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val viewModel: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
