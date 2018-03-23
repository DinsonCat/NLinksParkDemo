package com.nlinks.parkdemo.entity._req;

/**
 * 错峰包月支付上传类
 */

public class MonthlyOrderReq {


    /**
     * monthlyRecordId : string
     * payType : 0
     * userId : string
     * "transactType": 0,
     */


    private String monthlyRecordId;
    private final int transactType = 1;//交易码。app固定填1
    private final int payType = 8;//交易类型
    private String userId;
    /**
     * couponDetailIds : string
     */

    private String couponDetailIds;

    public MonthlyOrderReq(String monthlyRecordId, String userId, String couponDetailIds) {
        this.monthlyRecordId = monthlyRecordId;
        this.userId = userId;
        this.couponDetailIds = couponDetailIds;
    }


    @Override
    public String toString() {
        return "MonthlyOrderReq{" +
            "monthlyRecordId='" + monthlyRecordId + '\'' +
            ", transactType=" + transactType +
            ", payType=" + payType +
            ", userId='" + userId + '\'' +
            ", couponDetailIds='" + couponDetailIds + '\'' +
            '}';
    }

    public int getPayType() {
        return payType;
    }


    public String getMonthlyRecordId() {
        return monthlyRecordId;
    }

    public void setMonthlyRecordId(String monthlyRecordId) {
        this.monthlyRecordId = monthlyRecordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponDetailIds() {
        return couponDetailIds;
    }

    public void setCouponDetailIds(String couponDetailIds) {
        this.couponDetailIds = couponDetailIds;
    }
}
