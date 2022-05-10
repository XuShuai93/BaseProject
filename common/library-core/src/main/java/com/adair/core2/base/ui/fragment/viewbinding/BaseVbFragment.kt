package com.adair.core2.base.ui.fragment.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.adair.core.base.viewbinding.IFragmentViewBinding
import com.adair.core2.base.ui.fragment.function.BaseFunctionFragment
import java.lang.reflect.ParameterizedType

/**
 *
 * 集成ViewBinding功能的Fragment基类
 *
 * @author xushuai
 * @date   2022/3/26-17:04
 * @email  466911254@qq.com
 */
abstract class BaseVbFragment<VB : ViewBinding> : BaseFunctionFragment(), IFragmentViewBinding<VB> {

    private var _mBinding: VB? = null

    protected val mBinding = _mBinding!!

    override fun getContentLayoutId(): Int {
        return 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _mBinding = createViewBinding(layoutInflater, container)
        if (_mBinding == null) {
            _mBinding = createViewBindingByReflect(layoutInflater, container)
        }
        checkNotNull(_mBinding) { "rootViewBinding 初始化失败" }
        return _mBinding!!.root
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB? {
        return null
    }

    override fun createViewBindingByReflect(inflater: LayoutInflater, container: ViewGroup?): VB? {
        try {
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType) {
                val clazz = type.actualTypeArguments[0] as Class<VB>
                val method = clazz.getDeclaredMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.javaPrimitiveType
                )
                return method.invoke(null, inflater, container, false) as VB
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onDestroyView() {
        _mBinding = null
        super.onDestroyView()
    }
}