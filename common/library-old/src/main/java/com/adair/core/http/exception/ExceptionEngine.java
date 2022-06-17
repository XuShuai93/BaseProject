package com.adair.core.http.exception;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * 异常归类引擎
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class ExceptionEngine {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int TOKEN_FAILED = 409;

    /**
     * 将请求异常进行分类封装
     *
     * @param e 原始异常
     * @return 封装后的异常
     */
    public static ResponseException handleException(Throwable e) {
        ResponseException ex;
        //HTTP错误
        if (e instanceof HttpException) {
            //均视为网络错误
            HttpException httpException = (HttpException) e;
            ex = new ResponseException(ErrorCode.HTTP_ERROR, e);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.setMessage("未验证");
                    break;
                case FORBIDDEN:
                    ex.setMessage("服务器禁止访问");
                    break;
                case NOT_FOUND:
                    ex.setMessage("服务器不存在");
                    break;
                case REQUEST_TIMEOUT:
                    ex.setMessage("请求超时");
                    break;
                case GATEWAY_TIMEOUT:
                    ex.setMessage("网关超时");
                    break;
                case BAD_GATEWAY:
                    ex.setMessage("网关错误");
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.setMessage("服务器内部错误");
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.setMessage("服务器不可用");
                    break;
                case TOKEN_FAILED:
                    ex.setMessage("请求冲突");
                    break;
                default:
                    ex.setMessage("请求错误");
                    break;
            }
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ex = new ResponseException(ErrorCode.PARSE_ERROR, e);
            ex.setMessage("解析错误");
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            ex = new ResponseException(ErrorCode.NETWORK_ERROR, e);
            ex.setMessage("网络连接错误");
        } else if (e instanceof SSLHandshakeException) {
            ex = new ResponseException(ErrorCode.SSL_ERROR, e);
            ex.setMessage("ssl证书验证失败");
        } else if (e instanceof ApiException) {
            ex = new ResponseException(ErrorCode.API_ERROR, e);
            ex.setMessage(e.getMessage());
        } else {
            //未知错误
            ex = new ResponseException(ErrorCode.UNKNOWN, e);
            ex.setMessage("未知错误");
        }
        return ex;
    }
}
