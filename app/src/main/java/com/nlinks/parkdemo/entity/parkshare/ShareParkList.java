package com.nlinks.parkdemo.entity.parkshare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 共享车位
 */
public class ShareParkList implements Parcelable {


    /**
     * deviceCode : 03000000
     * deviceStatus : 2
     * sharingStatus : 3
     * endTime : 2017-07-06 09:06:12
     * parkSharingId : 0aa26260e5164d358aafbae9e2e0959e
     * appointTime : 2017-06-06 09:07:18
     * phoneNo : 15280067034
     * startTime : 2017-06-06 09:06:07
     * auditStatus : 3
     * charge : 5
     * parkName : 3号
     * stallName : 3
     * leaveTime : 2017-05-27 18:55:09
     * money : 3.0
     * parkinglockId : 58281a8dabbf44bbb68b153e7d82b1fd
     * plateNum : 闽AET155
     */

    private String deviceCode;
    private int deviceStatus;
    private int sharingStatus;
    private String endTime;
    private String parkSharingId;
    private String appointTime;
    private String phoneNo;
    private String startTime;
    private int auditStatus;
    private String parkName;
    private String stallName;
    private String leaveTime;
    private double money;
    private double charge;
    private String parkinglockId;
    private String plateNum;

    public ShareParkList() {
    }

    protected ShareParkList(Parcel in) {
        deviceCode = in.readString();
        deviceStatus = in.readInt();
        sharingStatus = in.readInt();
        endTime = in.readString();
        parkSharingId = in.readString();
        appointTime = in.readString();
        phoneNo = in.readString();
        startTime = in.readString();
        auditStatus = in.readInt();
        parkName = in.readString();
        stallName = in.readString();
        leaveTime = in.readString();
        money = in.readDouble();
        charge = in.readDouble();
        parkinglockId = in.readString();
        plateNum = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceCode);
        dest.writeInt(deviceStatus);
        dest.writeInt(sharingStatus);
        dest.writeString(endTime);
        dest.writeString(parkSharingId);
        dest.writeString(appointTime);
        dest.writeString(phoneNo);
        dest.writeString(startTime);
        dest.writeInt(auditStatus);
        dest.writeString(parkName);
        dest.writeString(stallName);
        dest.writeString(leaveTime);
        dest.writeDouble(money);
        dest.writeDouble(charge);
        dest.writeString(parkinglockId);
        dest.writeString(plateNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShareParkList> CREATOR = new Creator<ShareParkList>() {
        @Override
        public ShareParkList createFromParcel(Parcel in) {
            return new ShareParkList(in);
        }

        @Override
        public ShareParkList[] newArray(int size) {
            return new ShareParkList[size];
        }
    };

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public int getSharingStatus() {
        return sharingStatus;
    }

    public void setSharingStatus(int sharingStatus) {
        this.sharingStatus = sharingStatus;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParkSharingId() {
        return parkSharingId;
    }

    public void setParkSharingId(String parkSharingId) {
        this.parkSharingId = parkSharingId;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getParkinglockId() {
        return parkinglockId;
    }

    public void setParkinglockId(String parkinglockId) {
        this.parkinglockId = parkinglockId;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }
}
