package com.adair.core.base.ui.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.adair.core.base.ui.function.BaseFunctionActivity
import java.lang.reflect.ParameterizedType

/**
 * 自动实现ViewBinding功能的Activity基础类,VB也可以使用DataBinding
 *
 *
 * @author xushuai
 * @date   2022/3/26-16:47
 * @email  466911254@qq.com
 */
abstract class BaseVbActivity<VB : ViewBinding> : BaseFunctionActivity(), IActivityViewBinding<VB> {

    protected lateinit var mBinding: VB

    override fun beforeSetContentView(savedInstanceState: Bundle?) {
        super.beforeSetContentView(savedInstanceState)
        var viewBinding = createViewBinding(layoutInflater)
        if (viewBinding == null) {
            viewBinding = createViewBindingByReflect(layoutInflater)
        }

        checkNotNull(viewBinding) { "rootViewBinding 初始化失败" }

        mBinding = viewBinding
    }

    override fun getContentLayoutId(): Int {
        return 0
    }

    override fun getContentView(): View? {
        return mBinding.root
    }

    override fun createViewBinding(inflater: LayoutInflater): VB? {
        return null
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewBindingByReflect(inflater: LayoutInflater): VB? {
        try {
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType) {
                val vbClass = type.actualTypeArguments.filterIsInstance<Class<VB>>()
                val method = vbClass[0].getDeclaredMethod("inflate", LayoutInflater::class.java)
                return method.invoke(null, inflater) as VB
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}