package com.donews.core.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * <p>
 * 屏幕密度换算
 * </p>
 * created at 2019/7/11 15:10
 *
 * @author XuShuai
 * @version v1.0
 */
public class DensityUtils {

	/**
	 * dpi 转像素值
	 *
	 * @param context 上下文对象
	 * @param dpValue dp值
	 *
	 * @return 像素值
	 */
	public static int dp2Px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * sp 转 px
	 *
	 * @param context 上下文对象
	 * @param spValue sp值
	 *
	 * @return 像素值
	 */
	public static int sp2Px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * px转dp
	 *
	 * @param context 上下文对象
	 * @param pxValue 像素值
	 *
	 * @return dp值
	 */
	public static int px2Dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * px转sp
	 *
	 * @param context 上下文对象
	 * @param pxValue 像素值
	 *
	 * @return sp值
	 */
	public static int px2Sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 手机Density
	 */
	public static float getDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.density;
	}


	/** 手机字体缩放Density */
	public static float getScaleDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.scaledDensity;
	}

	/**
	 * 手机dpi,注意，此方法获取的DensityDpi不是手机系统设置的dpi，可能是App自己修改过的Dpi
	 */
	public static int getDensityDpi(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.densityDpi;
	}

	/**
	 * 这是手机真实的Dpi，不会随App的修改变动
	 *
	 * @param context 上下文对象
	 *
	 * @return 真实的手机dpi
	 */
	public static int getRealDensityDpi(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			context.getDisplay().getRealMetrics(metrics);
		} else {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			display.getRealMetrics(metrics);
		}
		return metrics.densityDpi;
	}


	/**
	 * 根据xxhpi标准的480dpi计算输入的像素在手机上的实际显示像素(以480dpi为参考的像素点)
	 *
	 * @param pixel 在480dpi上的标准像素尺寸
	 *
	 * @return 在当前手机上应该显示的尺寸
	 */
	public static float getScalePixelByXXHdpi(Context context, float pixel) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float scale = dm.densityDpi / 480f;
		return pixel * scale;
	}

	/**
	 * 获取放大缩小值
	 */
	public static float getDensityScaleByXXHdpi(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.densityDpi / 480f;
	}


}
