package com.adair.core.http.retrofit;

/**
 * retrofit client 接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public interface IRetrofitClient {

    /**
     * 生成server对象
     *
     * @param service 接口对象类
     * @return
     */
    <T> T create(Class<T> service);
}
