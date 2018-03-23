package com.nlinks.parkdemo.entity.monthly;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 错峰包月套餐
 */
public class MonthlyParkInfo implements Parcelable{

    /**
     * chareFee : 1
     * ruleTime : 14:00:00-16:00:00,18:00:00-20:00:00
     * ruleName : 测试3
     * remark : 包月测试3
     * monthlyRuleId : ca78a1e98e97427192bb1031510c8d51
     * maxMonth : 2
     */

    private String chareFee;
    private String ruleTime;
    private String ruleName;
    private String remark;
    private String monthlyRuleId="";
    private int maxMonth;

    /**
     * chareFee : 0.01
     * periodType : 1
     * monthlyType : 1
     * backPic : null
     * status : 2
     */

    private int periodType;//周期类型       1每日 2每月
    private int monthlyType;//错峰包月类型    1错峰 2月卡
    private String backPic;//月卡的背景图片
    private int status;//套餐状态           1启用，可购买 2关注状态

    public MonthlyParkInfo() {
    }

    protected MonthlyParkInfo(Parcel in) {
        chareFee = in.readString();
        ruleTime = in.readString();
        ruleName = in.readString();
        remark = in.readString();
        monthlyRuleId = in.readString();
        maxMonth = in.readInt();
        periodType = in.readInt();
        monthlyType = in.readInt();
        backPic = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chareFee);
        dest.writeString(ruleTime);
        dest.writeString(ruleName);
        dest.writeString(remark);
        dest.writeString(monthlyRuleId);
        dest.writeInt(maxMonth);
        dest.writeInt(periodType);
        dest.writeInt(monthlyType);
        dest.writeString(backPic);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MonthlyParkInfo> CREATOR = new Creator<MonthlyParkInfo>() {
        @Override
        public MonthlyParkInfo createFromParcel(Parcel in) {
            return new MonthlyParkInfo(in);
        }

        @Override
        public MonthlyParkInfo[] newArray(int size) {
            return new MonthlyParkInfo[size];
        }
    };

    public String getChareFee() {
        return chareFee;
    }

    public void setChareFee(String chareFee) {
        this.chareFee = chareFee;
    }

    public String getRuleTime() {
        return ruleTime;
    }

    public void setRuleTime(String ruleTime) {
        this.ruleTime = ruleTime;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMonthlyRuleId() {
        return monthlyRuleId;
    }

    public void setMonthlyRuleId(String monthlyRuleId) {
        this.monthlyRuleId = monthlyRuleId;
    }

    public int getMaxMonth() {
        return maxMonth;
    }

    public void setMaxMonth(int maxMonth) {
        this.maxMonth = maxMonth;
    }



    public int getPeriodType() {
        return periodType;
    }

    public void setPeriodType(int periodType) {
        this.periodType = periodType;
    }

    public int getMonthlyType() {
        return monthlyType;
    }

    public void setMonthlyType(int monthlyType) {
        this.monthlyType = monthlyType;
    }

    public String getBackPic() {
        return backPic;
    }

    public void setBackPic(String backPic) {
        this.backPic = backPic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
