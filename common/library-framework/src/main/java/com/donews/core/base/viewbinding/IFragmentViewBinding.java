package com.donews.core.base.viewbinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

/**
 * 实现ViewBinding接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public interface IFragmentViewBinding<VB extends ViewBinding> {

    /**
     * 创建ViewBinding对象
     *
     * @param inflater  布局加载器
     * @param container 父布局容器
     * @return ViewBinding对象
     */
    VB createViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);


    /**
     * 使用反射创建ViewBinding对象,当createViewBinding(LayoutInflater inflater) 返回null时，自动调用
     *
     * @param inflater  布局加载器
     * @param container 父布局容器
     * @return ViewBinding对象
     */
    VB createViewBindingByReflect(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 初始化View
     */
    void initView(@Nullable Bundle savedInstanceState);
}
