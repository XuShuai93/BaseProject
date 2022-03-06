package com.adair.httpv2.strategy

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 *
 * 默认的Retrofit构建策略
 *
 * @author xushuai
 * @date   2021/11/10-22:35
 * @email  466911254@qq.com
 */
class DefaultRetrofitBuildStrategy : IRetrofitBuildStrategy {
    override fun okHttpClient(): OkHttpClient {

    }

    override fun converterFactory(): Converter.Factory? {

    }

    override fun callAdapterFactory(): CallAdapter.Factory? {

    }
}