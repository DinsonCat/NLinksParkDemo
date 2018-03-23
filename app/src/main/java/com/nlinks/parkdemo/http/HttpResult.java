package com.nlinks.parkdemo.http;

import java.io.Serializable;

/**
 * 网络请求通用基本对象
 */
public class HttpResult<T> implements Serializable {
    private int statusCode;
    private String statusMsg;
    private int total;
    private T data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "statusCode=" + statusCode +
                ", statusMsg='" + statusMsg + '\'' +
                ", total=" + total +
                '}';
    }
}
