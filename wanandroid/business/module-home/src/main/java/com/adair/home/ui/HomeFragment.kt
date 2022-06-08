package com.adair.home.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.adair.core2.base.ui.vbvm.BaseVbVmFragment
import com.adair.home.databinding.HomeFragHomeBinding
import com.adair.home.viewmodel.HomeViewModel
import com.adair.middle.viewmodel.MainShareViewModel

/**
 *
 * 主页Fragment页面
 *
 * @author xushuai
 * @date   2022/5/29-18:59
 * @email  466911254@qq.com
 */
class HomeFragment : BaseVbVmFragment<HomeFragHomeBinding, HomeViewModel>() {

    private lateinit var mMainShareViewModel: MainShareViewModel

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun initObserver(view: View, savedInstanceState: Bundle?) {
        mMainShareViewModel = ViewModelProvider(requireActivity()).get(MainShareViewModel::class.java)
    }

}