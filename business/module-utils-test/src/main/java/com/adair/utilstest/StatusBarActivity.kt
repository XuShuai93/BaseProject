package com.adair.utilstest

import android.graphics.Color
import android.os.Bundle
import com.adair.core2.base.viewbinding.ui.BaseViewBindingActivity
import com.adair.utils.toast.ToastUtils
import com.adair.utils.ui.StatusBarUtils
import com.adair.utilstest.databinding.ActivityStatusBarBinding

class StatusBarActivity : BaseViewBindingActivity<ActivityStatusBarBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        ToastUtils.init(this)

        mBinding.btnStatusBarShow.setOnClickListener {
            StatusBarUtils.showStatusBar(window)
        }
        mBinding.btnStatusBarHide.setOnClickListener {
            StatusBarUtils.hideStatusBar(window)
        }
        mBinding.btnStatusBarImm.setOnClickListener {
            StatusBarUtils.setImmersionStatusBar(window, false)
        }
        mBinding.btnStatusBarImm2.setOnClickListener {
            StatusBarUtils.setImmersionStatusBar(window, true)
        }
        mBinding.btnStatusBarBlackFont.setOnClickListener {
            StatusBarUtils.setStatusBarLightFont(window, true)
        }

        mBinding.btnStatusBarWhiteFont.setOnClickListener {
            StatusBarUtils.setStatusBarLightFont(window, false)
        }

        mBinding.btnStatusBarColor.setOnClickListener {
            StatusBarUtils.setStatusBarColor(window, Color.RED)
        }

        mBinding.btnStatusBarFitFalse.setOnClickListener {
            StatusBarUtils.setFirSystemWindows(window, false)
        }
        mBinding.btnStatusBarFitTrue.setOnClickListener {
            StatusBarUtils.setFirSystemWindows(window, true)
        }

        mBinding.btnStatusBarIsShow.setOnClickListener {
            ToastUtils.showToast("状态栏显示:${StatusBarUtils.isShowStatusBar(window)}")
        }
    }
}