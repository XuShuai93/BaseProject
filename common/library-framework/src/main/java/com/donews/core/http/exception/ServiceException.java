package com.donews.core.http.exception;

/**
 * 服务器异常
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class ServiceException extends ApiException {

    private static final long serialVersionUID = -1313192420618683873L;

    public ServiceException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
