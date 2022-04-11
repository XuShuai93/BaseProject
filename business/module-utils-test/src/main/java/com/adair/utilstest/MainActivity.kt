package com.adair.utilstest

import android.content.Intent
import android.os.Bundle
import com.adair.core2.base.viewbinding.ui.BaseViewBindingActivity
import com.adair.utilstest.databinding.ActivityMainBinding

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.btnStatusBar.setOnClickListener {
            startActivity(Intent(this, StatusBarActivity::class.java))
        }
        mBinding.btnNavigationBar.setOnClickListener {
            startActivity(Intent(this, NavigationBarActivity::class.java))
        }
    }
}