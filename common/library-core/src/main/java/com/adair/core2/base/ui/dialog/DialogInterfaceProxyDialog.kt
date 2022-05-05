package com.adair.core2.base.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

/**
 * 代理监听器dialog,对应监听器使用弱引用,防止内存泄漏
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/20 18:11
 */
open class DialogInterfaceProxyDialog : Dialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(
        context,
        cancelable,
        cancelListener
    )

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(DialogInterfaceProxy.proxy(listener))
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(DialogInterfaceProxy.proxy(listener))
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(DialogInterfaceProxy.proxy(listener))
    }
}