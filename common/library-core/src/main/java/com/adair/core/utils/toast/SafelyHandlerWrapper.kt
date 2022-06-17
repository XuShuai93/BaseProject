package com.adair.core.utils.toast

import android.os.Handler
import android.os.Message

/**
 * 解决Toast 在Android 7.1.1版本可能出现的错误
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/11 14:17
 */
class SafelyHandlerWrapper(private val handler: Handler) : Handler() {

    override fun dispatchMessage(msg: Message) {
        try {
            super.dispatchMessage(msg)
        } catch (e: Exception) {
        }
    }

    override fun handleMessage(msg: Message) {
        handler.handleMessage(msg)
    }
}