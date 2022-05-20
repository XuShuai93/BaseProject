package com.cdyfnts.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.orhanobut.logger.Logger
import kotlin.math.abs

/**
 * 可拖动的ConstraintLayout
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/5/13 16:51
 */
class DragConstraintLayout : ConstraintLayout {

    private var mDragging = false
    private var mClick = false

    private var mParentHeight = 0
    private var mParentWidth = 0
    private var mLastX = 0f
    private var mLastY = 0f
    private var mStartX = 0f
    private var mStartY = 0f

    private var touchSlop = 0

    private var mPullOverAnim: ObjectAnimator? = null

    var dragListener: OnDragListener? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (parent != null) {
            mParentWidth = (parent as ViewGroup).width
            mParentHeight = (parent as ViewGroup).height
            Logger.d("$mParentWidth,$mParentHeight")
        }
        val rawX = ev.rawX
        val rawY = ev.rawY
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = rawX
                mLastY = rawY
                mStartX = rawX
                mStartY = rawY
                mPullOverAnim?.cancel()
                mDragging = false
                mClick = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(rawX - mStartX) < touchSlop && abs(rawY - mStartY) < touchSlop) {
                    //小于滑动的最小距离,不拦截
                    mDragging = false
                    return false
                } else {
                    //拦截滑动
                    mDragging = true
                }
            }
            MotionEvent.ACTION_UP -> {
                mDragging = false
            }
        }
        return mDragging
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rawX = event.rawX
        val rawY = event.rawY
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                if (mParentWidth <= 0 || mParentHeight <= 0) {
                    return true
                } else {
                    val dx = rawX - mLastX
                    val dy = rawY - mLastY
                    //距离小于
                    var dragX = x + dx
                    if (dragX < 0) {
                        dragX = 0f
                    } else if (dragX > mParentWidth - width) {
                        dragX = (mParentWidth - width).toFloat()
                    }

                    var dragY = y + dy
                    if (dragY < 0) {
                        dragY = 0f
                    } else if (dragY > mParentHeight - height) {
                        dragY = (mParentHeight - height).toFloat()
                    }
                    x = dragX
                    y = dragY
                    mLastX = rawX
                    mLastY = rawY
                    dragListener?.onDragging()
                }
            }
            MotionEvent.ACTION_UP -> {
                isPressed = false
                if (abs(rawX - mStartX) < touchSlop && abs(rawY - mStartY) < touchSlop) {
                    mClick = true
                    performClick()
                }
                if (!(x <= 0f || x >= (mParentWidth - width).toFloat())) {
                    pullOver()
                }
            }
        }
        return true
    }


    override fun performClick(): Boolean {
        return if (mClick) {
            super.performClick()
        } else {
            false
        }
    }


    private fun pullOver() {
        mPullOverAnim?.cancel()
        if (isLeft()) {
            mPullOverAnim = ObjectAnimator.ofFloat(this, "x", 0f).apply {
                interpolator = DecelerateInterpolator()
                duration = 500
                addUpdateListener {
                    dragListener?.onDragging()
                }
                start()
            }
        } else {
            mPullOverAnim = ObjectAnimator.ofFloat(this, "x", (mParentWidth - width).toFloat()).apply {
                interpolator = DecelerateInterpolator()
                duration = 500
                addUpdateListener {
                    dragListener?.onDragging()
                }
                start()
            }
        }
    }

    /**
     * 当前View是否正在滑动
     * @return Boolean
     */
    fun isDrag(): Boolean {
        return mDragging
    }

    /**
     * 当前是否在父布局左边
     * @return Boolean
     */
    fun isLeft(): Boolean {
        return x <= mParentWidth / 2f
    }

    interface OnDragListener {
        fun onDragging()
    }
}