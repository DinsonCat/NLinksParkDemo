package com.nlinks.parkdemo.entity.park;

import android.os.Parcel;
import android.os.Parcelable;

import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

/**
 * 优惠券
 * Created by Dell on 2017/3/31.
 */
public class ParkingCoupon implements Parcelable, Visitable {

    /**
     * application : 7
     * couponName : 停车代金券
     * couponAmount : 10
     * status : 3
     * couponType : 1
     * couponCode : 201603231500
     * couponId : zcdoocodo
     * couponDiscount : null
     * isSuperposition : 0
     * id : c074ddc09107424fb3e6ed452e04cf7c
     * sentTime : 2016-03-23 15:02:42
     * couponUseLimit : 30
     * userId : 19ff049d11e944cdbd1ccd4f163f6475
     * couponEndTime : 2017-03-31 15:00:35
     * couponRemark : 满30减10元
     * couponBeginTime : 2016-03-20 15:00:31
     * useTime : null
     */


    private String application = "";
    private String couponName = "";
    private double couponAmount;
    private int status;
    private int couponType;
    private String couponCode = "";
    private String couponId = "";
    private double couponDiscount;
    private double isSuperposition;
    private String id = "";
    private String sentTime = "";
    private double couponUseLimit;
    private String userId = "";
    private String couponEndTime = "";
    private String couponRemark = "";
    private String couponBeginTime = "";
    private String useTime = "";
    private String parkCode = ""; //支持的停车场code

    public ParkingCoupon() {
    }

    protected ParkingCoupon(Parcel in) {
        application = in.readString();
        couponName = in.readString();
        couponAmount = in.readDouble();
        status = in.readInt();
        couponType = in.readInt();
        couponCode = in.readString();
        couponId = in.readString();
        couponDiscount = in.readDouble();
        isSuperposition = in.readDouble();
        id = in.readString();
        sentTime = in.readString();
        couponUseLimit = in.readDouble();
        userId = in.readString();
        couponEndTime = in.readString();
        couponRemark = in.readString();
        couponBeginTime = in.readString();
        useTime = in.readString();
        parkCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(application);
        dest.writeString(couponName);
        dest.writeDouble(couponAmount);
        dest.writeInt(status);
        dest.writeInt(couponType);
        dest.writeString(couponCode);
        dest.writeString(couponId);
        dest.writeDouble(couponDiscount);
        dest.writeDouble(isSuperposition);
        dest.writeString(id);
        dest.writeString(sentTime);
        dest.writeDouble(couponUseLimit);
        dest.writeString(userId);
        dest.writeString(couponEndTime);
        dest.writeString(couponRemark);
        dest.writeString(couponBeginTime);
        dest.writeString(useTime);
        dest.writeString(parkCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParkingCoupon> CREATOR = new Creator<ParkingCoupon>() {
        @Override
        public ParkingCoupon createFromParcel(Parcel in) {
            return new ParkingCoupon(in);
        }

        @Override
        public ParkingCoupon[] newArray(int size) {
            return new ParkingCoupon[size];
        }
    };

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public double getIsSuperposition() {
        return isSuperposition;
    }

    public void setIsSuperposition(double isSuperposition) {
        this.isSuperposition = isSuperposition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public double getCouponUseLimit() {
        return couponUseLimit;
    }

    public void setCouponUseLimit(double couponUseLimit) {
        this.couponUseLimit = couponUseLimit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getCouponRemark() {
        return couponRemark;
    }

    public void setCouponRemark(String couponRemark) {
        this.couponRemark = couponRemark;
    }

    public String getCouponBeginTime() {
        return couponBeginTime;
    }

    public void setCouponBeginTime(String couponBeginTime) {
        this.couponBeginTime = couponBeginTime;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }




}
