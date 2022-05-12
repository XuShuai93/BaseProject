package com.adair.utilstest

import android.graphics.Color
import android.os.Bundle
import com.adair.core2.base.ui.viewbinding.BaseVbActivity
import com.adair.utils.display.DisplayUtils
import com.adair.utils.toast.ToastUtils
import com.adair.utils.ui.StatusBarUtils
import com.adair.utilstest.databinding.ActivityStatusBarBinding
import com.orhanobut.logger.Logger
import java.lang.StringBuilder

class StatusBarActivity : BaseVbActivity<ActivityStatusBarBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        ToastUtils.init(this)

        mBinding.btnStatusBarShow.setOnClickListener {
            StatusBarUtils.showStatusBar(window)
            logDisplayData()
        }
        mBinding.btnStatusBarHide.setOnClickListener {
            StatusBarUtils.hideStatusBar(window)
            logDisplayData()
        }
        mBinding.btnStatusBarImm.setOnClickListener {
            StatusBarUtils.setImmersionStatusBar(window, false)
            logDisplayData()
        }
        mBinding.btnStatusBarImm2.setOnClickListener {
            StatusBarUtils.setImmersionStatusBar(window, true)
            logDisplayData()
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
            logDisplayData()
        }
        mBinding.btnStatusBarFitTrue.setOnClickListener {
            StatusBarUtils.setFirSystemWindows(window, true)
            logDisplayData()
        }

        mBinding.btnStatusBarIsShow.setOnClickListener {
            ToastUtils.showToast("状态栏显示:${StatusBarUtils.isShowStatusBar(window)}")
        }
    }


    private fun logDisplayData() {
        val builder = StringBuilder()
        builder.append("v1 --- ${getSizeString(DisplayUtils.getDisplaySizeV1(this))}").append("\n")
        builder.append("v2 --- ${getSizeString(DisplayUtils.getDisplaySizeV2(this))}").append("\n")
        builder.append("v3 --- ${getSizeString(DisplayUtils.getDisplaySizeV3(this))}").append("\n")
        builder.append("v4 --- ${getSizeString(DisplayUtils.getDisplaySizeV4(this))}").append("\n")
        builder.append("v5 --- ${getSizeString(DisplayUtils.getDisplaySizeV5(this))}").append("\n")
        Logger.d(builder)
        mBinding.root.post {
            Logger.d("content width:${mBinding.root.width},height = ${mBinding.root.height}")
        }
    }

    private fun getSizeString(data: IntArray): String {
        return "width = ${data[0]},height =${data[1]}"
    }
}