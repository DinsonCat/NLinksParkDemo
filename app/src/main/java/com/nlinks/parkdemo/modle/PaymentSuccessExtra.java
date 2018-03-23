package com.nlinks.parkdemo.modle;

import android.os.Parcel;
import android.os.Parcelable;

import com.nlinks.parkdemo.entity.pay.PlatformActivity;

import java.util.List;

/**
 * 前往支付成功界面的数据传递类
 */

public class PaymentSuccessExtra implements Parcelable {

    private String payMoney;
    private String attention;
    private List<PlatformActivity> PlatformActivityList;


    public PaymentSuccessExtra() {
    }

    protected PaymentSuccessExtra(Parcel in) {
        payMoney = in.readString();
        attention = in.readString();
        PlatformActivityList = in.createTypedArrayList(PlatformActivity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payMoney);
        dest.writeString(attention);
        dest.writeTypedList(PlatformActivityList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentSuccessExtra> CREATOR = new Creator<PaymentSuccessExtra>() {
        @Override
        public PaymentSuccessExtra createFromParcel(Parcel in) {
            return new PaymentSuccessExtra(in);
        }

        @Override
        public PaymentSuccessExtra[] newArray(int size) {
            return new PaymentSuccessExtra[size];
        }
    };

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public List<PlatformActivity> getPlatformActivityList() {
        return PlatformActivityList;
    }

    public void setPlatformActivityList(List<PlatformActivity> platformActivityList) {
        PlatformActivityList = platformActivityList;
    }
}
