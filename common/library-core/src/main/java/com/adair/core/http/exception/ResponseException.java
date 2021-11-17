package com.adair.core.http.exception;

import androidx.annotation.Nullable;

/**
 * 统一的异常类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class ResponseException extends Exception {

    private int code;
    private Throwable cause;
    private String message;

    public ResponseException(int code, Throwable cause) {
        this.code = code;
        this.cause = cause;
    }

    public ResponseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Nullable
    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
