package com.adair.core.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * toast 管理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/8/12 17:12
 */
public class ToastUtils {

	private static Toast sToast;


	public static void show(Context context, String message) {
		if (sToast == null) {
			sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		} else {
			sToast.setText(message);
		}
		sToast.show();
	}

	public static void show(Context context, @StringRes int resId) {
		if (sToast == null) {
			sToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		} else {
			sToast.setText(resId);
		}
		sToast.show();
	}


	public static void cancelToast() {
		if (sToast != null) {
			sToast.cancel();
		}
	}
}
