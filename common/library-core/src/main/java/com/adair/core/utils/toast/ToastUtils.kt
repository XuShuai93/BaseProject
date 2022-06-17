package com.adair.core.utils.toast

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.widget.Toast
import java.lang.reflect.Field

/**
 *  Toast显示
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 11:31
 */
@SuppressLint("StaticFieldLeak")
object ToastUtils {

    private var toast: Toast? = null
    private var sField_TN: Field? = null
    private var sField_TN_Handler: Field? = null

    private lateinit var mContext: Context

    @JvmStatic
    fun init(context: Context) {
        mContext = context.applicationContext
    }

    @JvmStatic
    fun show(charSequence: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        if (toast == null) {
            toast = Toast.makeText(mContext, charSequence, duration)
        } else {
            toast?.setText(charSequence)
            toast?.duration = duration
        }
        hook(toast)
        toast?.show()
    }

    @JvmStatic
    fun cancel() {
        toast?.let {
            it.cancel()
            toast = null
        }
    }

    /** 修复版本7.1.1 toast显示错误bug */
    @SuppressLint("DiscouragedPrivateApi")
    private fun hook(toast: Toast?) {
        toast?.let {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    if (sField_TN == null) {
                        sField_TN = Toast::class.java.getDeclaredField("mTN")
                        sField_TN?.isAccessible = true
                        sField_TN_Handler = sField_TN?.type?.getDeclaredField("mHandler")
                        sField_TN_Handler?.isAccessible = true
                    }
                    val tn = sField_TN!!.get(it)
                    val preHandler: Handler = sField_TN_Handler!!.get(tn) as Handler
                    sField_TN_Handler!!.set(tn, SafelyHandlerWrapper(preHandler))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}