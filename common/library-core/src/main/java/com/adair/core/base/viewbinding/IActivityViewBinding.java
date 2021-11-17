package com.adair.core.base.viewbinding;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public interface IActivityViewBinding<VB extends ViewBinding> {

    /**
     * 创建ViewBinding对象
     *
     * @param inflater 布局加载器
     * @return ViewBinding对象
     */
    VB createViewBinding(@NonNull LayoutInflater inflater);

    /**
     * 使用反射创建ViewBinding对象,当createViewBinding(LayoutInflater inflater) 返回null时，自动调用
     *
     * @param inflater 布局加载器
     * @return ViewBinding对象
     */
    VB createViewBindingByReflect(@NonNull LayoutInflater inflater);


}
