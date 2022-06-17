package com.adair.core.base.ui.base

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
    var backgroundDimEnable: Boolean = true

    /**设置背景变暗的透明度，如果为-1表示默认不设置*/
    var backgroundDimAmount: Float = DEFAULT_DIM_AMOUNT

    /**是否全屏*/
    var fullScreen: Boolean = false

    /** 设置Window的宽高，可以设置内容是否全屏铺满 */
    var windowSize: IntArray = intArrayOf(-2, -2)

    /** 设置Window内容在界面上的位置 */
    var windowGravity: Int = Gravity.CENTER

    /** 设置window的padding值 */
    var windowPadding: IntArray = intArrayOf(0, 0, 0, 0)

    /**window 显示隐藏动画*/
    @StyleRes
    var windowAnimation: Int = DEFAULT_WINDOW_ANIM

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
        beforeInitView(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        this.dialog?.window?.apply {
            decorView.setPadding(windowPadding[0], windowPadding[1], windowPadding[2], windowPadding[3])
            val lp = attributes
            if (fullScreen) {
                lp.width = -1
                lp.height = -1
            } else {
                lp.width = windowSize[0]
                lp.height = windowSize[1]
            }
            attributes = lp
            setGravity(windowGravity)
            if (windowAnimation != DEFAULT_WINDOW_ANIM) {
                setWindowAnimations(windowAnimation)
            }

            if (!backgroundDimEnable) {
                setDimAmount(0f)
            }

            if (backgroundDimEnable && backgroundDimAmount != DEFAULT_DIM_AMOUNT) {
                setDimAmount(backgroundDimAmount)
            }
        }
    }


    protected open fun getStyle(): Int {
        return R.style.BaseDialogStyle
    }


    /**
     * 设置Window显示宽高
     * @param width Int 宽度 默认 WindowManager.LayoutParams.WRAP_CONTENT
     * @param height Int 高度 默认 WindowManager.LayoutParams.WRAP_CONTENT
     */
    fun setWindowSize(width: Int, height: Int) {
        windowSize[0] = width
        windowSize[1] = height
    }

    /**
     * 设置Window 周围padding，已经默认设置为0
     *  @param left Int
     *  @param top Int
     *  @param right Int
     *  @param bottom Int
     */
    fun setWindowPadding(left: Int, top: Int, right: Int, bottom: Int) {
        windowPadding[0] = left
        windowPadding[1] = top
        windowPadding[2] = right
        windowPadding[3] = bottom
    }

    /**返回布局Layout*/
    @LayoutRes
    abstract fun getContentLayoutId(): Int

    open fun beforeInitView(view: View, savedInstanceState: Bundle?) {}

    /** 初始化View */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 使用此方法显示弹出框，可以避免生命周期状态错误导致的异常(Can not perform this action after onSaveInstanceState)
     * @param manager FragmentManager
     * @param tag String?
     */
    fun showAllowingStateLoss(manager: FragmentManager, tag: String? = this::class.java.simpleName) {
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