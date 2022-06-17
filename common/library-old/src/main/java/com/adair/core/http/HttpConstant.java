package com.adair.core.http;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class HttpConstant {
    public static final Long TIMEOUT_CONNECT = 15L;
    public static final Long TIMEOUT_READ = 30L;
    public static final Long TIMEOUT_WRITE = 30L;
    public static final Long CACHE_SIZE = 50L * 1024L * 1024L; //缓存大小50M

    /**
     * 当接口需要其他url时，配置header, value 为 新的url路径
     */
    public static final String NEW_URL_HEADER = "newUrl";
    public static final String LOG_TAG = "library_network";
}
