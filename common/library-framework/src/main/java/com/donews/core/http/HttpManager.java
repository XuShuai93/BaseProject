package com.donews.core.http;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.donews.core.http.strategy.DefaultHttpStrategy;
import com.donews.core.http.strategy.IHttpStrategy;
import com.donews.core.utils.ApplicationInject;

/**
 * 网络管理器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class HttpManager {
	private static final HttpManager ourInstance = new HttpManager();

	public static HttpManager getInstance() {
		return ourInstance;
	}

	private IHttpStrategy httpStrategy;
	private String retrofitUrl;

	private HttpManager() {
		Application application = ApplicationInject.getInstance().getApplication();

		try {
			ApplicationInfo appInfo = application.getPackageManager()
					.getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
			retrofitUrl = appInfo.metaData.getString("retrofit_url", "");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(retrofitUrl)) {
			throw new IllegalArgumentException("retrofit_url value 获取失败,请检查是否配置mate_value ");
		}
		httpStrategy = new DefaultHttpStrategy();
	}

	public void init(IHttpStrategy strategy) {
		if (strategy == null) {
			strategy = new DefaultHttpStrategy();
		}
		this.httpStrategy = strategy;
	}

	public IHttpStrategy getHttpStrategy() {
		return httpStrategy;
	}

	public String getRetrofitUrl() {
		return retrofitUrl;
	}
}
