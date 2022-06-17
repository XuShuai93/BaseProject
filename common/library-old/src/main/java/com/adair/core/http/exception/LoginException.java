package com.adair.core.http.exception;

/**
 * 登录异常
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
class LoginException extends ApiException {

    private static final long serialVersionUID = 3457962530000349926L;

    public LoginException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
