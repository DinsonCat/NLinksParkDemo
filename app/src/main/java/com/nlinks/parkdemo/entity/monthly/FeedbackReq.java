package com.nlinks.parkdemo.entity.monthly;

import android.content.Context;

import com.nlinks.parkdemo.utils.SPUtils;

/**
 * 错峰包月-关注
 */

public class FeedbackReq {


    /**
     * monthlyRuleId : string
     * monthlyRuleName : string
     * parkCode : string
     * userId : string
     */

    private String monthlyRuleId = "";
    private String monthlyRuleName = "";
    private String parkCode = "";
    private String userId = "";

    public FeedbackReq(Context context) {
        this.userId = SPUtils.getUserId(context);
    }

    @Override
    public String toString() {
        return "FeedbackReq{" +
            "monthlyRuleId='" + monthlyRuleId + '\'' +
            ", monthlyRuleName='" + monthlyRuleName + '\'' +
            ", parkCode='" + parkCode + '\'' +
            ", userId='" + userId + '\'' +
            '}';
    }

    public String getMonthlyRuleId() {
        return monthlyRuleId;
    }

    public void setMonthlyRuleId(String monthlyRuleId) {
        this.monthlyRuleId = monthlyRuleId;
    }

    public String getMonthlyRuleName() {
        return monthlyRuleName;
    }

    public void setMonthlyRuleName(String monthlyRuleName) {
        this.monthlyRuleName = monthlyRuleName;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }
}
