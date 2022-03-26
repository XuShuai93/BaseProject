package com.adair.app

import android.os.Bundle
import com.adair.app.databinding.ActivityMainBinding
import com.adair.core2.base.viewbinding.ui.BaseViewBindingActivity
import com.adair.utils.density.dp2Px

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mBinding.btnDialog1.setOnClickListener {
            showDialog()
        }
    }


    private fun showDialog() {
        val dialog = DemoDialog.newInstance().apply {
            setFullScreen(true)
            setHidNavigationBar(false)
            setBackgroundDimEnable(true)
            setWindowSize(-1, -1)
            setBackgroundDimAmount(0.7f)
            setWindowPadding(20.dp2Px, 20.dp2Px, 20.dp2Px, 20.dp2Px)
            show(supportFragmentManager, "DemoDialog")
        }
    }

    override fun isHideStatusBar(): Boolean {
        return true
    }

    override fun isHideNavigationBar(): Boolean {
        return true
    }

    override fun isLandscape(): Boolean {
        return true
    }
}