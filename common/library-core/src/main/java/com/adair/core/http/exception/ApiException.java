package com.adair.core.http.exception;

/**
 * 请求接口错误异常
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class ApiException extends Exception {
    private static final long serialVersionUID = 6657901183080961278L;

    private int errorCode;
    private String errorMessage;
    private Throwable throwable;

    public ApiException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ApiException(int errorCode, String errorMessage, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.throwable = throwable;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", throwable=" + throwable +
                '}';
    }
}
