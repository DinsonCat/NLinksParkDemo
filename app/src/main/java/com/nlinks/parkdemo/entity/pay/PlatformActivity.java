package com.nlinks.parkdemo.entity.pay;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 支付订单平台活动
 */

public class PlatformActivity implements Parcelable {
    /**
     * randomOffMoney : 0.0
     * activityValue : 随机立减
     * activityName : 平台活动
     */

    private String randomOffMoney;
    private String activityValue;
    private String activityName;

    public PlatformActivity(String randomOffMoney, String activityValue, String activityName) {
        this.randomOffMoney = randomOffMoney;
        this.activityValue = activityValue;
        this.activityName = activityName;
    }

    protected PlatformActivity(Parcel in) {
        randomOffMoney = in.readString();
        activityValue = in.readString();
        activityName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(randomOffMoney);
        dest.writeString(activityValue);
        dest.writeString(activityName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlatformActivity> CREATOR = new Creator<PlatformActivity>() {
        @Override
        public PlatformActivity createFromParcel(Parcel in) {
            return new PlatformActivity(in);
        }

        @Override
        public PlatformActivity[] newArray(int size) {
            return new PlatformActivity[size];
        }
    };

    public String getRandomOffMoney() {
        return randomOffMoney;
    }

    public void setRandomOffMoney(String randomOffMoney) {
        this.randomOffMoney = randomOffMoney;
    }

    public String getActivityValue() {
        return activityValue;
    }

    public void setActivityValue(String activityValue) {
        this.activityValue = activityValue;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
