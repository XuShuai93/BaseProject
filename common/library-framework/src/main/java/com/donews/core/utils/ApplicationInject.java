package com.donews.core.utils;

import android.app.Application;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class ApplicationInject {
    private static final ApplicationInject ourInstance = new ApplicationInject();

    public static ApplicationInject getInstance() {
        return ourInstance;
    }

    private ApplicationInject() {
    }

    private Application mApplication;


    public void init(Application application) {
        this.mApplication = application;
    }

    public Application getApplication() {
        if (mApplication == null) {
            throw new IllegalArgumentException("application is null,please call init() method");
        }
        return mApplication;
    }
}
