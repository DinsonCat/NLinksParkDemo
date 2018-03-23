package com.nlinks.parkdemo.entity.monthly;


import com.nlinks.parkdemo.entity.pay.PlatformActivity;

import java.util.List;

/**
 * 错峰包月提交订单返回实体
 */
public class MonthlyOrderResponse {
    /**
     * orderMoney : 0.01
     * couponMoney : 0
     * orderCode : 20171120093334160007607
     * realMoney : 0.01
     */

    private String orderMoney;
    private String orderCode;
    private String realMoney;

    /**
     * randomOffMoney : 0.0
     * orderMoney : 1
     * realMoney : 1.0
     * activityList : [{"randomOffMoney":0,"activityValue":"随机立减","activityName":"平台活动"}]
     */
    private List<PlatformActivity> activityList;

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }


    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(String realMoney) {
        this.realMoney = realMoney;
    }


    public List<PlatformActivity> getPlatformActivityList() {
        return activityList;
    }

    public void setPlatformActivityList(List<PlatformActivity> activityList) {
        this.activityList = activityList;
    }
}
