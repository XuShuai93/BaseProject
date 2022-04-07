package com.adair.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * 状态栏View
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/7 13:47
 */
class StatusBarView : View {
    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

    }
}