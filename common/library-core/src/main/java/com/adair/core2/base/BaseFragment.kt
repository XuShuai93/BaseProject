package com.adair.core2.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * 基础Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:25
 */
abstract class BaseFragment : Fragment() {

    private var mInitView: Boolean = false
    private var mLazy = false

    private var mResume: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazy()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getContentLayoutId() != 0) {
            return inflater.inflate(getContentLayoutId(), container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        mInitView = true
    }

    override fun onResume() {
        super.onResume()
        mResume = true
        lazy()
    }

    override fun onPause() {
        super.onPause()
        mResume = false
    }

    private fun lazy() {
        if (userVisibleHint && mInitView && !mLazy) {
            mLazy = true
            onFirstVisible()
        }
    }

    /**返回布局Layout*/
    @LayoutRes
    abstract fun getContentLayoutId(): Int

    /** 初始化View */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /** 该fragment 第一次被显示时调用,可用作懒加载 */
    open fun onFirstVisible() {}

    /**
     * 是否在Resume显示期间
     *
     * @return true 在Resume期间
     */
    fun isResume(): Boolean {
        return mResume
    }


    /** 隐藏键盘 */
    protected fun hideKeyboardFrom(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}