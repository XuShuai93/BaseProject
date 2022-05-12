package com.adair.core2.base.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.adair.core.R
import com.adair.core.utils.StatusBarUtils
import com.adair.core2.ktx.isShowing
import java.lang.reflect.Field

/**
 * 基础dialogFragment 封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 13:40
 */
abstract class BaseDialogFragment : DialogFragment() {

    companion object {
        const val DEFAULT_DIM_AMOUNT = -1f

        const val DEFAULT_WINDOW_ANIM = 0
    }

    /**背景是否变暗*/
    private var mBackgroundDimEnable: Boolean = true

    /**设置背景变暗的透明度，如果为-1表示默认不设置*/
    private var mBackgroundDimAmount: Float = DEFAULT_DIM_AMOUNT

    /**是否全屏*/
    private var mFullScreen: Boolean = false

    /** 设置Window的宽高，可以设置内容是否全屏铺满 */
    private val mWindowSize: IntArray = intArrayOf(-2, -2)

    /** 设置Window内容在界面上的位置 */
    private var mWindowGravity: Int = Gravity.CENTER

    /** 设置window的padding值 */
    private var mWindowPadding: IntArray = intArrayOf(0, 0, 0, 0)

    /**window 显示隐藏动画*/
    @StyleRes
    private var mWindowAnimation: Int = DEFAULT_WINDOW_ANIM

    /** 是否隐藏导航栏 */
    private var mHidNavigationBar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, getStyle())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DialogInterfaceProxyDialog(requireContext(), theme)
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
    }

    override fun onStart() {
        super.onStart()
        this.dialog?.window?.apply {
            decorView.setPadding(mWindowPadding[0], mWindowPadding[1], mWindowPadding[2], mWindowPadding[3])
            val lp = attributes
            lp.width = mWindowSize[0]
            lp.height = mWindowSize[1]
            attributes = lp
            setGravity(mWindowGravity)
            if (mWindowAnimation != DEFAULT_WINDOW_ANIM) {
                setWindowAnimations(mWindowAnimation)
            }

            if (!mBackgroundDimEnable) {
                setDimAmount(0f)
            }

            if (mBackgroundDimEnable && mBackgroundDimAmount != DEFAULT_DIM_AMOUNT) {
                setDimAmount(mBackgroundDimAmount)
            }
        }

        if (mHidNavigationBar) {
            StatusBarUtils.hideNavigationBar(this)
        }
    }


    private fun getStyle(): Int {
        return if (mFullScreen) {
            R.style.BaseDialogStyle_FullScreen
        } else {
            R.style.BaseDialogStyle
        }
    }

    /**
     * 设置界面显示时，背景是否变暗
     * @param enable Boolean true 会变暗，false，不会变暗
     */
    fun setBackgroundDimEnable(enable: Boolean) {
        mBackgroundDimEnable = enable
    }

    /**
     * 设置背景变暗时，背景透明度
     *
     * @param dimAmount Float, 当为0时，完全透明，1时完全不透明
     */
    fun setBackgroundDimAmount(dimAmount: Float) {
        mBackgroundDimAmount = dimAmount
    }

    /**
     * 设置Window显示宽高
     * @param width Int 宽度 默认 WindowManager.LayoutParams.WRAP_CONTENT
     * @param height Int 高度 默认 WindowManager.LayoutParams.WRAP_CONTENT
     */
    fun setWindowSize(width: Int, height: Int) {
        mWindowSize[0] = width
        mWindowSize[1] = height
    }

    /**
     * 设置Window 周围padding，已经默认设置为0
     *  @param left Int
     *  @param top Int
     *  @param right Int
     *  @param bottom Int
     */
    fun setWindowPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mWindowPadding[0] = left
        mWindowPadding[1] = top
        mWindowPadding[2] = right
        mWindowPadding[3] = bottom
    }

    /**
     * 设置Window在屏幕的什么位置 ,默认在屏幕中间
     * @param gravity Int
     */
    fun setWindowGravity(gravity: Int) {
        mWindowGravity = gravity
    }

    /**
     * 设置Window 显示和隐藏动画
     *
     * Style中分别添加:
     *   <item name="android:windowEnterAnimation"></item>
     *   <item name="android:windowExitAnimation"></item>
     *
     * @param animStyle style
     */

    fun setWindowAnimation(@StyleRes animStyle: Int) {
        mWindowAnimation = animStyle
    }

    /**
     * 是否Window全屏显示,当全屏时,内容会延伸到状态栏,会隐藏状态栏
     * @param fullScreen 是否全屏显示
     */
    fun setFullScreen(fullScreen: Boolean) {
        mFullScreen = fullScreen
    }

    /**
     * 是否隐藏导航栏
     * @param hide 隐藏导航栏
     */
    fun setHidNavigationBar(hide: Boolean) {
        mHidNavigationBar = hide
    }

    /**返回布局Layout*/
    @LayoutRes
    abstract fun getContentLayoutId(): Int

    /** 初始化View */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 使用此方法显示弹出框，可以避免生命周期状态错误导致的异常(Can not perform this action after onSaveInstanceState)
     * @param manager FragmentManager
     * @param tag String?
     */
    fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        try {
            val dismissed: Field = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)
            val shown: Field = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.isAccessible = true
            shown.set(this, true)
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}