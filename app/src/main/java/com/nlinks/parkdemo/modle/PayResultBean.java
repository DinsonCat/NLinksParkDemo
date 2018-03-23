package com.nlinks.parkdemo.modle;

/**
 * 支付完成实体
 */
public class PayResultBean {
    private boolean isSuccess;
    private String message;

    public PayResultBean(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    @Override
    public String toString() {
        return "PayResultBean{" +
            "isSuccess=" + isSuccess +
            ", message='" + message + '\'' +
            '}';
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
