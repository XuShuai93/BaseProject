package com.adair.core.http.exception;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class ErrorCode {

    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 证书出错
     */
    public static final int SSL_ERROR = 1005;

    /**
     * 协议出错,请求返回code不为200
     */
    public static final int HTTP_ERROR = 1003;

    /**
     * 请求成功,返回的数据状态不对
     */
    public static final int API_ERROR = 1006;
}
