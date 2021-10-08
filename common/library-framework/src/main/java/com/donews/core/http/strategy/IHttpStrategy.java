package com.donews.core.http.strategy;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * http配置策略接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public interface IHttpStrategy {
    /**
     * 返回一个自定义的OkHttpClient对象
     */
    OkHttpClient getOkHttpClient();

    /**
     * 返回一个Retrofit Converter.Factory对象，适应不同的Converter
     */
    Converter.Factory getRetrofitConverterFactory();

    /**
     * 返回一个Retrofit CallAdapter.Factory对象，适应不同的Converter
     */
    CallAdapter.Factory getRetrofitCallAdapterFactory();
}
