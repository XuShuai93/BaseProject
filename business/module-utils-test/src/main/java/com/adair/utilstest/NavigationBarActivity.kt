package com.adair.utilstest

import android.app.AlertDialog
import android.os.Bundle
import com.adair.core2.base.viewbinding.ui.BaseViewBindingActivity
import com.adair.utils.ui.NavigationBarUtils
import com.adair.utilstest.databinding.ActivityNavigationBarBinding

class NavigationBarActivity : BaseViewBindingActivity<ActivityNavigationBarBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mBinding.btnNaviBarShow.setOnClickListener {
            NavigationBarUtils.showNavigationBar(window)
        }

        mBinding.btnNaviBarHide.setOnClickListener {
            NavigationBarUtils.hideNavigationBar(window)
        }

        mBinding.btnNaviBarImm.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("1111")
                .setMessage("2222222")
                .show()
        }

        mBinding.btnNaviBarWhiteFont.setOnClickListener {

        }

        mBinding.btnNaviBaBlackFont.setOnClickListener {

        }
        mBinding.btnNaviBarIsShow.setOnClickListener {

        }

        mBinding.btnNaviBarColor.setOnClickListener {

        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) {
//            NavigationBarUtils.hideNavigationBar(window)
//        }
    }
}