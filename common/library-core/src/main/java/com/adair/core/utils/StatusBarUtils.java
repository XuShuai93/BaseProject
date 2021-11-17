package com.adair.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/8
 */

@SuppressWarnings("RedundantSuppression")
public class StatusBarUtils {
	/*
	 *
	 * android的状态栏大致经历了以下几个阶段：
	 *  在android4.4以下就不要想着对状态栏做什么文章了，现在app的适配一般也是在android4.4以上了。
	 *  在android4.4—android5.0可以实现状态栏的变色，但是效果还不是很好，
	 *  主要实现方式是通过FLAG_TRANSLUCENT_STATUS这个属性设置状态栏为透明并且为全屏模式，
	 *  然后通过添加一个与StatusBar 一样大小的View，将View 设置为我们想要的颜色，从而来实现状态栏变色。
	 *  在android5.0—android6.0系统才真正的支持状态栏变色，系统加入了一个重要的属性和方法 android:statusBarColor （
	 *     对应方法为 setStatusBarColor），通过这个属性可以直接设置状态栏的颜色。
	 *  在android6.0上主要就是添加了一个功能可以修改状态栏上内容和图标的颜色（黑色和白色）
	 *
	 *  在4.4-5.0版本主要通过FLAG_TRANSLUCENT_STATUS这个属性实现状态栏变色，
	 *  当使用这个flag时SYSTEM_UI_FLAG_LAYOUT_STABLE和SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN会被自动添加
	 *    1.对于4.4的机型，小米和魅族是透明色，而其它系统上就只是黑色到透明色的渐变。
	 *    2.对于5.x的机型，大部分是使背景色半透明，小米和魅族以及其它少数机型会全透明
	 *    3.对于6.0以上的机型，设置此flag会使得StatusBar完全透明
	 *    4.对于7.0以上的机型，设置此flag会使得StatusBar半透明
	 *  在代码中设置：activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	 *  在主题中设置对应代码<item name="android:windowTranslucentStatus">true</item>
	 *
	 *  android5.0是android的一次重大更新，很多api都是在这个版本上添加的（真想最低适配android5.0），setStatusBarColor是专门用来设置状态栏颜色的，
	 *  但是让这个方法生效有一个前提条件：你必须给window添加FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS并且取消FLAG_TRANSLUCENT_STATUS，
	 *  前面已经说过了FLAG_TRANSLUCENT_STATUS会让状态栏透明，那FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS这个属性又是干嘛的呢？
	 *  设置了FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,表明会Window负责系统bar的background 绘制，绘制透明背景的系统bar（状态栏和导航栏），
	 *  然后用getStatusBarColor()和getNavigationBarColor()的颜色填充相应的区域。这就是Android 5.0 以上实现沉浸式导航栏的原理。
	 *
	 *  添加方式也有两种：
	 *   1.在代码中设置：
	 *     getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	 *     getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	 *     getWindow().setStatusBarColor(ContextCompat.getColor(this,android.R.color.holo_blue_dark));
	 *  2.在布局文件中设置（因为是android5.0新添加的属性，所以在添加到values-v21文件夹下的主题中）：
	 *    <item name="android:windowTranslucentStatus">false</item>
	 *    <item name="android:windowDrawsSystemBarBackgrounds">true</item>
	 *    <item name="android:statusBarColor">@android:color/holo_blue_dark</item>
	 *
	 *
	 *    使用沉浸式的时候会遇到一个问题，那就是Android 系统状态栏的字色和图标颜色为白色，
	 *    当我的主题色或者图片接近白色或者为浅色的时候，状态栏上的内容就看不清了。
	 *    这个问题在Android 6.0的时候得到了解决。Android 6.0 新添加了一个属性SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
	 *
	 *      添加方式同样是两种：
	 *      在代码中设置：
	 *          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
	 *              getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
	 *                  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
	 *          }
	 *      在布局文件中设置（因为是android5.0新添加的属性，所以在添加到values-v23文件夹下的主题中）：
	 *          <item name="android:windowLightStatusBar">true</item>
	 *
	 */

	/**
	 * 获取状态栏高度
	 */
	@SuppressWarnings("unused")
	public static int getStatusBarHeight(Context context) {
		int height = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			height = context.getResources().getDimensionPixelSize(resourceId);
		}
		return height;
	}


	/**
	 * 设置状态栏颜色
	 */
	@SuppressLint("ObsoleteSdkInt")
	@SuppressWarnings("unused")
	public static void setStatusBarColor(Activity activity, @ColorInt int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			if (window != null) {
				//取消状态栏透明
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				//添加Flag把状态栏设为可绘制模式
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				//设置状态栏颜色
				window.setStatusBarColor(color);
			}
		}
	}

	/**
	 * 透明状态栏，状态栏覆盖在界面上
	 */
	@SuppressLint("ObsoleteSdkInt")
	public static void transparentStatusBar(Activity activity) {
		Window window = activity.getWindow();
		if (window != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				//取消状态栏透明
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				//添加Flag把状态栏设为可绘制模式
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				//设置状态栏颜色
				window.setStatusBarColor(Color.TRANSPARENT);
				//设置window的状态栏不可见,添加此2个flag，状态栏覆盖在界面内容之上
				View decorView = window.getDecorView();

				int options = decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
						View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
				decorView.setSystemUiVisibility(options);
			} else {
				window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
		}
	}

	/**
	 * @param activity  目标activity
	 * @param lightFont font is black when true or is white when false
	 */
	public static void setAndroidNativeLightStatusBar(Activity activity, boolean lightFont) {
		Window window = activity.getWindow();
		if (window != null) {
			// 设置浅色状态栏时的界面显示
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				View decorView = window.getDecorView();
				int options = decorView.getSystemUiVisibility();
				if (lightFont) {
					options = options | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
				} else {
					options = options & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
				}
				decorView.setSystemUiVisibility(options);
			}
		}
	}

	/**
	 * Set light status bar
	 *
	 * @param activity  目标activity
	 * @param lightFont font is black when true or is white when false
	 */
	public static void setLightStatusBar(Activity activity, boolean lightFont) {
		setAndroidNativeLightStatusBar(activity, lightFont);
	}

	/**
	 * Is light status bar
	 *
	 * @param activity 上下文对象
	 *
	 * @return true 文字颜色为黑色
	 */
	public static boolean isLightStatusBar(Activity activity) {
		Window window = activity.getWindow();
		if (window != null) {
			// 设置浅色状态栏时的界面显示
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				View decorView = window.getDecorView();
				int options = decorView.getSystemUiVisibility();
				return (options & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
			}
		}
		return false;
	}


	/**
	 * 隐藏虚拟按钮
	 */
	public static void hideNavigationBar(@NonNull Activity activity) {
		Window window = activity.getWindow();
		if (window != null) {
			View decorView = window.getDecorView();
			int currentVersionCode = Build.VERSION.SDK_INT;
			if (currentVersionCode > Build.VERSION_CODES.HONEYCOMB && currentVersionCode < Build.VERSION_CODES.KITKAT) {
				//此方法会导致状态栏一起隐藏
				decorView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
			} else if (currentVersionCode >= Build.VERSION_CODES.KITKAT && currentVersionCode < Build.VERSION_CODES.R) {
				int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
				decorView.setSystemUiVisibility(uiOptions);
				decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
					@Override
					public void onSystemUiVisibilityChange(int visibility) {
						if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
							decorView.setSystemUiVisibility(uiOptions);
						}
					}
				});
			} else if (currentVersionCode >= Build.VERSION_CODES.R) {
				WindowInsetsController controller = window.getInsetsController();
				if (controller != null) {
					controller.hide(WindowInsets.Type.statusBars());
					controller.hide(WindowInsets.Type.navigationBars());
				}
			}
		}
	}

	/**
	 * 隐藏弹出框的虚拟按钮和状态栏
	 *
	 * @param dialogFragment {@link DialogFragment} 弹出框对象
	 */
	public static void hideNavigationBar(@NonNull DialogFragment dialogFragment) {
		Dialog dialog = dialogFragment.getDialog();
		if (dialog != null) {
			Window window = dialog.getWindow();
			if (window != null) {
				View decorView = window.getDecorView();
				int currentVersionCode = Build.VERSION.SDK_INT;
				if (currentVersionCode > Build.VERSION_CODES.HONEYCOMB && currentVersionCode < Build.VERSION_CODES.KITKAT) {
					//此方法会导致状态栏一起隐藏
					decorView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
				} else if (currentVersionCode >= Build.VERSION_CODES.KITKAT && currentVersionCode < Build.VERSION_CODES.R) {
					int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
					decorView.setSystemUiVisibility(uiOptions);
					decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
						@Override
						public void onSystemUiVisibilityChange(int visibility) {
							if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
								decorView.setSystemUiVisibility(uiOptions);
							}
						}
					});
				} else if (currentVersionCode >= Build.VERSION_CODES.R) {
					WindowInsetsController controller = window.getInsetsController();
					if (controller != null) {
						controller.hide(WindowInsets.Type.navigationBars());
					}
				}
			}
		}
	}
}
