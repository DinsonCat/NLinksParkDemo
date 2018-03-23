package com.nlinks.parkdemo.module.usercenter.monthly.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.nlinks.parkdemo.entity.pay.PlatformActivity;

import java.util.List;

/**
 * 包月申请-包月订单传递数据类
 */
public class MonthlyPay implements Parcelable {
    private String parkName;
    private String parkRuleName;
    private String plateNum;
    private String startTime;
    private String endTime;
    private String price;
    private int timeCount;
    private String monthlyRuleId;
    private String lastMoney;//订单最终价格（扣除各种各样的活动）
    private List<PlatformActivity> activityLists;

    private double couponMoney = 0;//优惠金额


    public MonthlyPay(String parkName, String parkRuleName, String plateNum, String startTime, String endTime,
                      String price, int timeCount, String monthlyRuleId) {
        this.parkName = parkName;
        this.parkRuleName = parkRuleName;
        this.plateNum = plateNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.timeCount = timeCount;
        this.monthlyRuleId = monthlyRuleId;
    }


    protected MonthlyPay(Parcel in) {
        parkName = in.readString();
        parkRuleName = in.readString();
        plateNum = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        price = in.readString();
        timeCount = in.readInt();
        monthlyRuleId = in.readString();
        lastMoney = in.readString();
        activityLists = in.createTypedArrayList(PlatformActivity.CREATOR);
        couponMoney = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parkName);
        dest.writeString(parkRuleName);
        dest.writeString(plateNum);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(price);
        dest.writeInt(timeCount);
        dest.writeString(monthlyRuleId);
        dest.writeString(lastMoney);
        dest.writeTypedList(activityLists);
        dest.writeDouble(couponMoney);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MonthlyPay> CREATOR = new Creator<MonthlyPay>() {
        @Override
        public MonthlyPay createFromParcel(Parcel in) {
            return new MonthlyPay(in);
        }

        @Override
        public MonthlyPay[] newArray(int size) {
            return new MonthlyPay[size];
        }
    };

    @Override
    public String toString() {
        return "MonthlyPay{" +
            "parkName='" + parkName + '\'' +
            ", parkRuleName='" + parkRuleName + '\'' +
            ", plateNum='" + plateNum + '\'' +
            ", startTime='" + startTime + '\'' +
            ", endTime='" + endTime + '\'' +
            ", price='" + price + '\'' +
            ", timeCount='" + timeCount + '\'' +
            ", monthlyRuleId='" + monthlyRuleId + '\'' +
            ", lastMoney='" + lastMoney + '\'' +
            ", activityLists=" + activityLists +
            ", couponMoney=" + couponMoney +
            '}';
    }

    public double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(double couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(String lastMoney) {
        this.lastMoney = lastMoney;
    }

    public List<PlatformActivity> getActivityLists() {
        return activityLists;
    }

    public void setActivityLists(List<PlatformActivity> activityLists) {
        this.activityLists = activityLists;
    }

    public String getMonthlyRuleId() {
        return monthlyRuleId;
    }

    public void setMonthlyRuleId(String monthlyRuleId) {
        this.monthlyRuleId = monthlyRuleId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkRuleName() {
        return parkRuleName;
    }

    public void setParkRuleName(String parkRuleName) {
        this.parkRuleName = parkRuleName;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(int timeCount) {
        this.timeCount = timeCount;
    }
}
