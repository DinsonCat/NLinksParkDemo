package com.nlinks.parkdemo.entity._req;

/**
 * 错峰包月支付上传类
 */

public class MonthlyParkReq {


    /**
     * monthlyRuleId : string
     * months : 0
     * plateNum : string
     * startTime : string
     * userId : string
     */

    private String monthlyRuleId;
    private String months;
    private String plateNum;
    private String startTime;
    private String userId;


    @Override
    public String toString() {
        return "MonthlyParkReq{" +
            "monthlyRuleId='" + monthlyRuleId + '\'' +
            ", months='" + months + '\'' +
            ", plateNum='" + plateNum + '\'' +
            ", startTime='" + startTime + '\'' +
            ", userId='" + userId + '\'' +
            '}';
    }

    public MonthlyParkReq(String monthlyRuleId, String months, String plateNum, String startTime,
                          String userId) {
        this.monthlyRuleId = monthlyRuleId;
        this.months = months;
        this.plateNum = plateNum;
        this.startTime = startTime;
        this.userId = userId;
    }

    public String getMonthlyRuleId() {
        return monthlyRuleId;
    }

    public void setMonthlyRuleId(String monthlyRuleId) {
        this.monthlyRuleId = monthlyRuleId;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
