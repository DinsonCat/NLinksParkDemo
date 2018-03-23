package com.nlinks.parkdemo.entity.msgpush;

/**
 * 请求首页小红点
 * 不要问我为什么，我也很绝望
 */

public class UnReadCount {


    /**
     * statusMsg : 操作成功
     * unreadTotal : 0
     * statusCode : 200
     */

    private String statusMsg;
    private int unreadTotal;
    private int statusCode;

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public int getUnreadTotal() {
        return unreadTotal;
    }

    public void setUnreadTotal(int unreadTotal) {
        this.unreadTotal = unreadTotal;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
