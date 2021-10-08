package com.donews.core.http.strategy;

import android.app.Application;

import com.donews.core.http.HttpConstant;
import com.donews.core.http.interceptor.BaseUrlInterceptor;
import com.donews.core.utils.ApplicationInject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 默认实现的Http配置策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class DefaultHttpStrategy implements IHttpStrategy {
    @Override
    public OkHttpClient getOkHttpClient() {
        Application application = ApplicationInject.getInstance().getApplication();
        File cacheFile = new File(application.getCacheDir(), "OkHttpCache");
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        Long cacheSize = HttpConstant.CACHE_SIZE;
        Cache cache = new Cache(cacheFile, cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(HttpConstant.TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(HttpConstant.TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(HttpConstant.TIMEOUT_WRITE, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new BaseUrlInterceptor())
                .cache(cache);
        return builder.build();
    }

    @Override
    public Converter.Factory getRetrofitConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Override
    public CallAdapter.Factory getRetrofitCallAdapterFactory(){
        return null;
    }
}
