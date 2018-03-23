package com.nlinks.parkdemo.entity.park;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 停车记录 缴费 传值 实体类
 * Created by Dell on 2017/06/06.
 */
public class ParkRecordPay implements Parcelable {

    private String parkName="";//停车场名称
    private String parkCode="";//停车场code
    private String spendTime="";//停车时长
    private String carCode="";//车牌号
    private double shouldPay;//需付金额
    private double discount;//代金券/折扣券金额
    private double actualPay;//实际支付
    private String orderCode="";//订单号
    private String couponId="";//优惠券id
    private int payType;//支付类型
    private double lastMoney;

    public ParkRecordPay() {
    }

    protected ParkRecordPay(Parcel in) {
        parkName = in.readString();
        parkCode = in.readString();
        spendTime = in.readString();
        carCode = in.readString();
        shouldPay = in.readDouble();
        discount = in.readDouble();
        actualPay = in.readDouble();
        orderCode = in.readString();
        couponId = in.readString();
        payType = in.readInt();
        lastMoney = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parkName);
        dest.writeString(parkCode);
        dest.writeString(spendTime);
        dest.writeString(carCode);
        dest.writeDouble(shouldPay);
        dest.writeDouble(discount);
        dest.writeDouble(actualPay);
        dest.writeString(orderCode);
        dest.writeString(couponId);
        dest.writeInt(payType);
        dest.writeDouble(lastMoney);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParkRecordPay> CREATOR = new Creator<ParkRecordPay>() {
        @Override
        public ParkRecordPay createFromParcel(Parcel in) {
            return new ParkRecordPay(in);
        }

        @Override
        public ParkRecordPay[] newArray(int size) {
            return new ParkRecordPay[size];
        }
    };

    public double getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(double lastMoney) {
        this.lastMoney = lastMoney;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }


    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(double shouldPay) {
        this.shouldPay = shouldPay;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getActualPay() {
        return actualPay;
    }

    public void setActualPay(double actualPay) {
        this.actualPay = actualPay;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCouponId() {
        return couponId;
    }
    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
