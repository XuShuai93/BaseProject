package com.adair.core2.base.dialog

import android.content.Context
import android.content.DialogInterface
import com.adair.core2.base.dialog.DialogInterfaceProxyDialog

/**
 * 基础Dialog基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/22 17:26
 */
class BaseDialog : DialogInterfaceProxyDialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(
        context,
        cancelable,
        cancelListener
    )
}