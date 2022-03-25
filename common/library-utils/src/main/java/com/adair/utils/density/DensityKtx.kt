package com.adair.utils.density

import android.content.res.Resources
import android.util.TypedValue

/**
 *
 * Density 相关扩展类,px 和dp sp 的互相转换
 *
 * dp和sp转为px默认返回四射五入的 Int值
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/25 18:29
 */

val Float.dp2Px: Int
    get() = (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    ) + 0.5f).toInt()

val Int.dp2Px: Int
    get() = (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ) + 0.5f).toInt()

val Float.sp2Px: Int
    get() = (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    ) + 0.5f).toInt()

val Int.sp2Px: Int
    get() = (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ) + 0.5f).toInt()

val Int.px2Dp: Float
    get() = this / Resources.getSystem().displayMetrics.density

val Float.px2Dp: Float
    get() = this / Resources.getSystem().displayMetrics.density

val Int.px2Sp: Float
    get() = this / Resources.getSystem().displayMetrics.scaledDensity

val Float.px2Sp: Float
    get() = this / Resources.getSystem().displayMetrics.scaledDensity