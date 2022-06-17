package com.adair.utilstest

import android.content.Intent
import android.os.Bundle
import com.adair.core.base.ui.vbvm.BaseVbVmActivity
import com.adair.utils.UtilsManager
import com.adair.utilstest.databinding.ActivityMainBinding
import com.adair.utilstest.dialog.TestStyleDialog
import com.adair.utilstest.viewmodel.MainViewModel

class MainActivity : BaseVbVmActivity<ActivityMainBinding, MainViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        UtilsManager.init(application = application)

        mBinding.btnStatusBar.setOnClickListener {
//            mViewModel.toast()
//            startActivity(Intent(this, StatusBarActivity::class.java))

            val dialog = TestStyleDialog.newInstance().apply {
//                setFullScreen(true)
//                setBackgroundDimEnable(true)
//                setBackgroundDimAmount(0f)
//                setWindowSize(-1, -1)
//                setWindowPadding(0,0,0,0)
            }
            dialog.showAllowingStateLoss(supportFragmentManager)
        }
        mBinding.btnNavigationBar.setOnClickListener {
            startActivity(Intent(this, NavigationBarActivity::class.java))
        }
    }
}