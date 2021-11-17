package com.adair.core.base;

import android.app.Application;

import com.adair.core.crash.CrashHandler;
import com.adair.core.utils.ApplicationInject;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * 基础Application基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ApplicationInject.getInstance().init(this);
		Logger.addLogAdapter(new AndroidLogAdapter());
		CrashHandler.getInstance().init(this);
	}
}
