package com.nlinks.parkdemo.entity._req;

/**
 * @author Dinson - 2017/7/13
 */
public class SubmitMemOrder {

    /**
     * couponDetailIds : string
     * payType : 0
     * userId : string
     */

    private String couponDetailIds;
    private int payType;
    private String userId;

    public String getCouponDetailIds() {
        return couponDetailIds;
    }

    public void setCouponDetailIds(String couponDetailIds) {
        this.couponDetailIds = couponDetailIds;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
