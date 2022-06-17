package com.adair.core.http.model;

import com.adair.core.http.exception.ServiceException;

/**
 * 基础网络请求数据结构
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/7
 */
public class ApiResponse<T> {

    protected static final int SUCCESS_CODE = 0;

    private int errorCode;
    private String errorMessage;
    private T data;

    public boolean isSuccess() {
        return errorCode == SUCCESS_CODE;
    }

    public T coverData() throws ServiceException {
        if (isSuccess()) {
            return data;
        }
        throw new ServiceException(errorCode, errorMessage);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
