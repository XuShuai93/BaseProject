package com.adair.core2.base.ui.vbvm

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.adair.core2.base.ui.viewbinding.BaseVbFragment
import com.adair.core2.base.viewmodel.BaseViewModel
import com.adair.core2.utils.ClassUtils
import java.lang.reflect.Modifier

/**
 *  使用ViewBinding和ViewModel的 Fragment
 *
 *
 * @author xushuai
 * @date   2022/5/12-20:56
 * @email  466911254@qq.com
 */
abstract class BaseVbVmFragment<VB : ViewBinding, VM : BaseViewModel> : BaseVbFragment<VB>() {

    /** 泛型中的默认ViewModel对象 */
    protected lateinit var mViewModel: VM

    override fun beforeInitView(view: View, savedInstanceState: Bundle?) {
        super.beforeInitView(view, savedInstanceState)
        val vm = createViewModel()
        mViewModel = if (vm != null) {
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm)).get(vm::class.java)
        } else {
            val clazz: Class<VM>? = ClassUtils.getGenericClassByClass(this, ViewModel::class.java)
            if (clazz == null || clazz == ViewModel::class.java) {
                return
            }
            //判断此VM是否有abstract标记,有则无法初始化
            val isAbstract = Modifier.isAbstract(clazz.modifiers)
            if (isAbstract) {
                return
            }
            ViewModelProvider(this).get(clazz)
        }
    }

    /**
     * 创建ViewModel对象,直接new 一个对象供ViewModelFactory使用，如果返回null，则尝试通过反射初始化无参数ViewModel
     * @return VM 泛型的ViewModel对象,
     */
    protected open fun createViewModel(): VM? {
        return null
    }
}