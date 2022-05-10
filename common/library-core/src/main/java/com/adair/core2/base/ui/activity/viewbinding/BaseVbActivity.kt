package com.adair.core2.base.ui.activity.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.adair.core2.base.ui.activity.function.BaseFunctionActivity
import com.adair.core2.base.viewbinding.IActivityViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 自动实现ViewBinding功能的Activity基础类
 *
 *
 * @author xushuai
 * @date   2022/3/26-16:47
 * @email  466911254@qq.com
 */
abstract class BaseVbActivity<VB : ViewBinding> : BaseFunctionActivity(), IActivityViewBinding<VB> {

    lateinit var mBinding: VB

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

    override fun createViewBindingByReflect(inflater: LayoutInflater): VB? {
        try {
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType) {
                val clazz = type.actualTypeArguments[0] as Class<VB>
                val method = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
                return method.invoke(null, inflater) as VB
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}