package com.nlinks.parkdemo.entity.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息实体类
 */
public class UserInfo implements Parcelable {
    /**
     * phoneNo : 15880897090
     * canSignin : 1
     * sex : 0
     * membership : 0
     * couponCount : 0
     * points : 0
     * monthCardCount : 0
     * displayName :
     * photoUrl : http://59.61.216.123:14106/v1/tfs/T1UAxgBjxT1RyJcC6K
     * plateNum :
     * walletMoney : 0.0
     */
    private String phoneNo;//电话号码
    private int canSignin;
    private int sex;//0未设置  1男  2女
    private int membership;//会员等级 0普通会员 1蜻蜓会员
    private int couponCount;//优惠券
    private int points;//积分
    private int monthCardCount;//月卡
    private String displayName;//昵称
    private String photoUrl;
    private String plateNum;
    private double walletMoney;//钱包余额

    public boolean isMemberShip() {
        return membership == 1;

    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "phoneNo='" + phoneNo + '\'' +
            ", sex=" + sex +
            ", membership=" + membership +
            ", displayName='" + displayName + '\'' +
            '}';
    }

    protected UserInfo(Parcel in) {
        phoneNo = in.readString();
        canSignin = in.readInt();
        sex = in.readInt();
        membership = in.readInt();
        couponCount = in.readInt();
        points = in.readInt();
        monthCardCount = in.readInt();
        displayName = in.readString();
        photoUrl = in.readString();
        plateNum = in.readString();
        walletMoney = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNo);
        dest.writeInt(canSignin);
        dest.writeInt(sex);
        dest.writeInt(membership);
        dest.writeInt(couponCount);
        dest.writeInt(points);
        dest.writeInt(monthCardCount);
        dest.writeString(displayName);
        dest.writeString(photoUrl);
        dest.writeString(plateNum);
        dest.writeDouble(walletMoney);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getCanSignin() {
        return canSignin;
    }

    public void setCanSignin(int canSignin) {
        this.canSignin = canSignin;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getMembership() {
        return membership;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMonthCardCount() {
        return monthCardCount;
    }

    public void setMonthCardCount(int monthCardCount) {
        this.monthCardCount = monthCardCount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public double getWalletMoney() {
        return walletMoney;
    }

    public void setWalletMoney(double walletMoney) {
        this.walletMoney = walletMoney;
    }


}
