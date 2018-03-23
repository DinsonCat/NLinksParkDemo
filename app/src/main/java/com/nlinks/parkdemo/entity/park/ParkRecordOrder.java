package com.nlinks.parkdemo.entity.park;

import com.nlinks.parkdemo.entity.pay.PlatformActivity;

import java.util.ArrayList;

/**
 * 停车记录订单详情
 */
public class ParkRecordOrder {
    /**
     * orderCode : 201706061535421466476896
     * couponMoney : 0
     * realMoney : 0.10000000000000009
     * orderMoney : 0.10000000000000009
     */

    private String orderCode;
    private double couponMoney;
    private double realMoney;
    private double orderMoney;

    public ArrayList<PlatformActivity> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<PlatformActivity> activityList) {
        this.activityList = activityList;
    }

    /**
     * randomOffMoney : 0.0
     * orderMoney : 6
     * couponMoney : 0
     * realMoney : 6.0
     * activityList : []
     */

    private ArrayList<PlatformActivity> activityList;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(double couponMoney) {
        this.couponMoney = couponMoney;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public double getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(double orderMoney) {
        this.orderMoney = orderMoney;
    }

}
