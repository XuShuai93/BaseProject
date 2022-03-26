package com.adair.core2.base.viewbinding

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * Activity实现ViewBinding功能接口
 *
 *
 * @author xushuai
 * @date   2022/3/26-16:37
 * @email  466911254@qq.com
 */
interface IActivityViewBinding<VB : ViewBinding> {

    /**
     * 创建ViewBinding对象
     *
     * @param inflater 布局加载器
     * @return ViewBinding对象
     */
    fun createViewBinding(inflater: LayoutInflater): VB?

    /**
     * 使用反射创建ViewBinding对象,当createViewBinding(LayoutInflater inflater) 返回null时，自动调用
     *
     * @param inflater 布局加载器
     * @return ViewBinding对象
     */
    fun createViewBindingByReflect(inflater: LayoutInflater): VB?
}