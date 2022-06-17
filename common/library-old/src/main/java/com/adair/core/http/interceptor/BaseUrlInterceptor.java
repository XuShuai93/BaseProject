package com.adair.core.http.interceptor;

import com.adair.core.http.HttpConstant;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 地址拦截，替换为其他地址
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/7
 */
public class BaseUrlInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        //获取请求信息
        Request oldRequest = chain.request();
        //获取urlName的请求头信息
        List<String> urlNameHeaders = oldRequest.headers(HttpConstant.NEW_URL_HEADER);

        if (!urlNameHeaders.isEmpty()) {
            //重新建立请求Builder
            Request.Builder newRequestBuilder = oldRequest.newBuilder();

            //移除urlName请求头信息
            newRequestBuilder.removeHeader(HttpConstant.NEW_URL_HEADER);

            String newUrlString = urlNameHeaders.get(0);

            //解析url成为HttpUrl对象，如果解析错误,则直接使用原有请求)
            HttpUrl parseUrl = HttpUrl.parse(newUrlString);
            if (parseUrl == null) {
                Logger.log(Logger.INFO, HttpConstant.LOG_TAG, "newUrlString.toHttpUrlOrNull() is null", null);
                return chain.proceed(oldRequest);
            }
            //重新配置新的请求地址信息
            HttpUrl oldUrl = oldRequest.url();
            HttpUrl newRequestUrl = oldUrl.newBuilder().scheme(parseUrl.scheme())
                    .host(parseUrl.host())
                    .port(parseUrl.port())
                    .build();
            return chain.proceed(newRequestBuilder.url(newRequestUrl).build());
        }
        return chain.proceed(oldRequest);
    }
}
