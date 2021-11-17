package com.adair.core.base.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * 实现ViewModel接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public interface IViewModel<VM extends ViewModel> {

	/**
	 * 手动初始化ViewModel
	 *
	 * @return 返回ture，则不会再调用createViewModel()方法，并且不会自动初始化view model
	 */
	boolean initViewModel();

	/**
	 * 創建一個ViewModel對象,直接new就行
	 *
	 * @return ViewModel對象
	 */
	VM createViewModel();

	/**
	 * 绑定ViewModel中LiveData监听器
	 */
	void bindObserver();

	/**
	 * 需要初始化数据
	 */
	void initData();
}
