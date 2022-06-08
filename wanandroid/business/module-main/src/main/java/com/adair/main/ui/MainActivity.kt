package com.adair.main.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.adair.core2.base.ui.viewbinding.BaseVbActivity
import com.adair.main.R
import com.adair.main.databinding.MainActivityMainBinding
import com.adair.middle.router.RouterPath
import com.adair.middle.viewmodel.MainShareViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar

/**
 *
 * 主界面Activity
 *
 * @author xushuai
 * @date   2022/5/21-11:48
 * @email  466911254@qq.com
 */
@Route(path = RouterPath.MainRouter.MAIN_ACTIVITY)
class MainActivity : BaseVbActivity<MainActivityMainBinding>() {

    private lateinit var mMainShareViewModel: MainShareViewModel

    override fun beforeInitView(savedInstanceState: Bundle?) {
        super.beforeInitView(savedInstanceState)
        ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .fitsSystemWindows(true)
            .init()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mMainShareViewModel = ViewModelProvider(this).get(MainShareViewModel::class.java)
        lifecycle.addObserver(mMainShareViewModel)

    }
}