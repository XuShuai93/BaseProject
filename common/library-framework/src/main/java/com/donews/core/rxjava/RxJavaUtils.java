package com.donews.core.rxjava;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 线程切换
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/20
 */
public class RxJavaUtils {

	/**
	 * 线程切换
	 */
	public static <T> ObservableTransformer<T, T> io_main() {
		return upstream -> upstream.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
