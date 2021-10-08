package com.donews.core.http.retrofit;

import com.donews.core.http.HttpManager;

import retrofit2.Retrofit;

/**
 * 默认的RetrofitClient，可以自定义
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class DefaultRetrofitClient implements IRetrofitClient {
    private static final DefaultRetrofitClient ourInstance = new DefaultRetrofitClient();

    public static DefaultRetrofitClient getInstance() {
        return ourInstance;
    }


    private final Retrofit mRetrofit;

    private DefaultRetrofitClient() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(HttpManager.getInstance().getRetrofitUrl())
                .client(HttpManager.getInstance().getHttpStrategy().getOkHttpClient())
                .addConverterFactory(HttpManager.getInstance().getHttpStrategy().getRetrofitConverterFactory());
        if (HttpManager.getInstance().getHttpStrategy().getRetrofitCallAdapterFactory() != null) {
            builder.addCallAdapterFactory(HttpManager.getInstance().getHttpStrategy().getRetrofitCallAdapterFactory());
        }

        mRetrofit = builder.build();
    }

    @Override
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
