package com.adair.app

import android.graphics.Color
import android.os.Bundle
import com.adair.app.databinding.ActivityMainBinding
import com.adair.core2.base.viewbinding.ui.BaseViewBindingActivity
import com.adair.utils.density.dp2Px
import com.adair.utils.ui.SystemUiUtils

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mBinding.btnDialog1.setOnClickListener {
            showDialog()
        }

        mBinding.btn1.setOnClickListener {
            SystemUiUtils.showStatusBar(window)
        }
        mBinding.btn2.setOnClickListener {
            SystemUiUtils.hideStatusBar(window)
        }
        mBinding.btn3.setOnClickListener {
            SystemUiUtils.setTransparentStatusBar(window)
        }
        mBinding.btn4.setOnClickListener {
            SystemUiUtils.setStatusBarLightFont(this, true)
        }
        mBinding.btn5.setOnClickListener {
            SystemUiUtils.setStatusBarLightFont(this, false)
        }

        mBinding.btn6.setOnClickListener {
            SystemUiUtils.setStatusBarColor(window, Color.RED)
        }
        mBinding.btn7.setOnClickListener {
            SystemUiUtils.restoreStatusBar(window)
        }
    }


    private fun showDialog() {
        val dialog = DemoDialog.newInstance().apply {
            setFullScreen(true)
            setHidNavigationBar(true)
            setBackgroundDimEnable(true)
            setWindowSize(-1, -1)
            setBackgroundDimAmount(0.7f)
//            setWindowPadding(20.dp2Px, 20.dp2Px, 20.dp2Px, 20.dp2Px)
            show(supportFragmentManager, "DemoDialog")
        }
    }

    override fun isHideStatusBar(): Boolean {
        return true
    }

    override fun isHideNavigationBar(): Boolean {
        return false
    }

    override fun isLandscape(): Boolean {
        return false
    }
}