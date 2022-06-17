package com.adair.utilstest.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.adair.core.base.ui.viewbinding.BaseVbDialogFragment
import com.adair.utilstest.databinding.DialogTestStyleBinding
import com.gyf.immersionbar.ImmersionBar

/**
 *
 * 测试Style的弹出框
 *
 * @author xushuai
 * @date   2022/5/15-15:52
 * @email  466911254@qq.com
 */
class TestStyleDialog : BaseVbDialogFragment<DialogTestStyleBinding>() {

    companion object {
        fun newInstance(): TestStyleDialog {
            val args = Bundle()
            val fragment = TestStyleDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen = true
        backgroundDimEnable = true
        backgroundDimAmount = 0.6f
        windowGravity = Gravity.CENTER
        windowSize = intArrayOf(-2, -2)
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ImmersionBar.destroy(this)
    }
}