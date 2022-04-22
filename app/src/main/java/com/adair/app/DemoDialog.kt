package com.adair.app

import android.os.Bundle
import android.view.View
import com.adair.core2.base.fragment.BaseDialogFragment

/**
 *
 *
 *
 * @author xushuai
 * @date   2022/3/26-18:44
 * @email  466911254@qq.com
 */
class DemoDialog : BaseDialogFragment() {

    companion object {
        fun newInstance(): DemoDialog {
            val args = Bundle()

            val fragment = DemoDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getContentLayoutId(): Int {
        return R.layout.dialog_demo
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}