package com.adair.httpv2.strategy

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 *
 * 构建Retrofit的策略模式
 *
 * @author xushuai
 * @date   2021/11/10-22:14
 * @email  466911254@qq.com
 */
interface IRetrofitBuildStrategy {

    /** 构建Retrofit 的OkHttpClient */
    fun okHttpClient(): OkHttpClient

    /** 构建Retrofit 的Converter.Factory */
    fun converterFactory(): Converter.Factory?

    /** 构建Retrofit 的CallAdapter.Factory */
    fun callAdapterFactory(): CallAdapter.Factory?
}