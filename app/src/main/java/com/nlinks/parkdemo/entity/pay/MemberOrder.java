package com.nlinks.parkdemo.entity.pay;

/**
 * @author Dinson - 2017/7/13
 */
public class MemberOrder {

    /**
     * orderCode : 201707131553321236846499
     * couponMoney : 0
     * realMoney : 188
     * orderMoney : 188
     */

    private String orderCode;
    private int couponMoney;
    private int realMoney;
    private int orderMoney;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(int couponMoney) {
        this.couponMoney = couponMoney;
    }

    public int getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(int realMoney) {
        this.realMoney = realMoney;
    }

    public int getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(int orderMoney) {
        this.orderMoney = orderMoney;
    }
}
