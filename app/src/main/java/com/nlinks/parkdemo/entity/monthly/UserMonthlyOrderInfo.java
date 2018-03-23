package com.nlinks.parkdemo.entity.monthly;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户错峰包月记录
 */
public class UserMonthlyOrderInfo implements Parcelable {
    /**
     * chareFee : 0.01
     * interTime : 2
     * parkName : 南威地下车库
     * payTime : null
     * ruleTime : 10:00:00-12:00:00,13:00:00-18:00:00
     * remark : 南威软件停车
     * plateNum : 闽C55669
     * userId : 19ff049d11e944cdbd1ccd4f163f6475
     * monthlyRecordId : 73a4dbf4ba434ada992e3285b305857c
     * ruleName : 南威软件停车套餐
     * startTime : 2017-09-11 00:00:00
     * payChannel : null
     * orderCode : 111222333
     * endTime : 2017-11-11 23:59:59
     * status : 2
     */
    private String chareFee;
    private int interTime;
    private String parkName;
    private String payTime;
    private String ruleTime;
    private String remark;
    private String plateNum;
    private String userId;
    private String monthlyRecordId;
    private String ruleName;
    private String startTime;
    private String payChannel;
    private String orderCode;
    private String endTime;
    private int status;

    protected UserMonthlyOrderInfo(Parcel in) {
        chareFee = in.readString();
        interTime = in.readInt();
        parkName = in.readString();
        payTime = in.readString();
        ruleTime = in.readString();
        remark = in.readString();
        plateNum = in.readString();
        userId = in.readString();
        monthlyRecordId = in.readString();
        ruleName = in.readString();
        startTime = in.readString();
        payChannel = in.readString();
        orderCode = in.readString();
        endTime = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chareFee);
        dest.writeInt(interTime);
        dest.writeString(parkName);
        dest.writeString(payTime);
        dest.writeString(ruleTime);
        dest.writeString(remark);
        dest.writeString(plateNum);
        dest.writeString(userId);
        dest.writeString(monthlyRecordId);
        dest.writeString(ruleName);
        dest.writeString(startTime);
        dest.writeString(payChannel);
        dest.writeString(orderCode);
        dest.writeString(endTime);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserMonthlyOrderInfo> CREATOR = new Creator<UserMonthlyOrderInfo>() {
        @Override
        public UserMonthlyOrderInfo createFromParcel(Parcel in) {
            return new UserMonthlyOrderInfo(in);
        }

        @Override
        public UserMonthlyOrderInfo[] newArray(int size) {
            return new UserMonthlyOrderInfo[size];
        }
    };

    public String getChareFee() {
        return chareFee;
    }

    public void setChareFee(String chareFee) {
        this.chareFee = chareFee;
    }

    public int getInterTime() {
        return interTime;
    }

    public void setInterTime(int interTime) {
        this.interTime = interTime;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getRuleTime() {
        return ruleTime;
    }

    public void setRuleTime(String ruleTime) {
        this.ruleTime = ruleTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMonthlyRecordId() {
        return monthlyRecordId;
    }

    public void setMonthlyRecordId(String monthlyRecordId) {
        this.monthlyRecordId = monthlyRecordId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
