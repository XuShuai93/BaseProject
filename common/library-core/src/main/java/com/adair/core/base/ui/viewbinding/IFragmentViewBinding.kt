package com.adair.core.base.ui.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 *
 * Fragment实现ViewBinding功能接口
 *
 * @author xushuai
 * @date   2022/3/26-16:44
 * @email  466911254@qq.com
 */
interface IFragmentViewBinding<VB : ViewBinding> {

    /**
     * 创建ViewBinding对象
     *
     * @param inflater  布局加载器
     * @param container 父布局容器
     * @return ViewBinding对象
     */
    fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB?


    /**
     * 使用反射创建ViewBinding对象,当createViewBinding(LayoutInflater inflater) 返回null时，自动调用
     *
     * @param inflater  布局加载器
     * @param container 父布局容器
     * @return ViewBinding对象
     */
    fun createViewBindingByReflect(inflater: LayoutInflater, container: ViewGroup?): VB?
}