package com.nlinks.parkdemo.entity.park;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  停车记录
 */
public class ParkRecord implements Parcelable {

    /**
     * staffCode : CS002
     * inTime : 2017-06-19 15:50:49
     * couponMoney : 0.0
     * hasPay : 18.0
     * parkRecordId : ca0aa69876cd4780bfafe92a574cb824
     * outTime : 2017-06-20 09:30:15
     * parkStatus : 2
     * payTime : 2017-06-19 17:36:41
     * parkSeconds : 63566
     * recordStatus : 4
     * consume : 22.0
     * stallCode : 35050506
     * parkName : 法制广场停车场
     * stallName : null
     * payStatus : 4
     * payType : 2
     * address : 福建省泉州市丰泽区丰海路
     * waitPay : 4.0
     * "waitOutTime": "2017-09-15 10:59:52",
     * longitude : 118.624781
     * latitude : 24.883042
     * chargeStandard : 停放1小时内（含1小时）每辆次3元，超过1小时后每30分钟加收2元（不足30分钟的按30分钟计），同一辆车当天停放最高收费30元。
     * plateNum : 京A01234
     * "parkCode": "test001",
     */

    private String staffCode="";
    private String inTime="";
    private double couponMoney;
    private double hasPay;
    private String parkRecordId="";
    private String outTime="";
    private int parkStatus;
    private String payTime="";
    private long parkSeconds;
    private int recordStatus;
    private double consume;
    private String stallCode="";
    private String parkName="";
    private String stallName="";
    private int payStatus;
    private int payType;
    private String address="";
    private double waitPay;
    private double longitude;
    private double latitude;
    private String chargeStandard="";
    private String plateNum="";
    private String waitOutTime="";
    private String parkCode="";
    /**
     * latitude : 24.878191
     * consume : 2.0
     * hasPay : 2.0
     * parkSeconds : 57120
     * waitLeaveMin : 15
     * couponMoney : 0.99
     * isShare : 0
     * waitPay : 0.0
     * waitOutTime : null
     * outTime : null
     * longitude : 118.643088
     */

    private int waitLeaveMin;


    protected ParkRecord(Parcel in) {
        staffCode = in.readString();
        inTime = in.readString();
        couponMoney = in.readDouble();
        hasPay = in.readDouble();
        parkRecordId = in.readString();
        outTime = in.readString();
        parkStatus = in.readInt();
        payTime = in.readString();
        parkSeconds = in.readLong();
        recordStatus = in.readInt();
        consume = in.readDouble();
        stallCode = in.readString();
        parkName = in.readString();
        stallName = in.readString();
        payStatus = in.readInt();
        payType = in.readInt();
        address = in.readString();
        waitPay = in.readDouble();
        longitude = in.readDouble();
        latitude = in.readDouble();
        chargeStandard = in.readString();
        plateNum = in.readString();
        waitOutTime = in.readString();
        parkCode = in.readString();
        waitLeaveMin = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(staffCode);
        dest.writeString(inTime);
        dest.writeDouble(couponMoney);
        dest.writeDouble(hasPay);
        dest.writeString(parkRecordId);
        dest.writeString(outTime);
        dest.writeInt(parkStatus);
        dest.writeString(payTime);
        dest.writeLong(parkSeconds);
        dest.writeInt(recordStatus);
        dest.writeDouble(consume);
        dest.writeString(stallCode);
        dest.writeString(parkName);
        dest.writeString(stallName);
        dest.writeInt(payStatus);
        dest.writeInt(payType);
        dest.writeString(address);
        dest.writeDouble(waitPay);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(chargeStandard);
        dest.writeString(plateNum);
        dest.writeString(waitOutTime);
        dest.writeString(parkCode);
        dest.writeInt(waitLeaveMin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParkRecord> CREATOR = new Creator<ParkRecord>() {
        @Override
        public ParkRecord createFromParcel(Parcel in) {
            return new ParkRecord(in);
        }

        @Override
        public ParkRecord[] newArray(int size) {
            return new ParkRecord[size];
        }
    };

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getWaitOutTime() {
        return waitOutTime;
    }

    public void setWaitOutTime(String waitOutTime) {
        this.waitOutTime = waitOutTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }




    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(double couponMoney) {
        this.couponMoney = couponMoney;
    }

    public double getHasPay() {
        return hasPay;
    }

    public void setHasPay(double hasPay) {
        this.hasPay = hasPay;
    }

    public String getParkRecordId() {
        return parkRecordId;
    }

    public void setParkRecordId(String parkRecordId) {
        this.parkRecordId = parkRecordId;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public int getParkStatus() {
        return parkStatus;
    }

    public void setParkStatus(int parkStatus) {
        this.parkStatus = parkStatus;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public long getParkSeconds() {
        return parkSeconds;
    }

    public void setParkSeconds(long parkSeconds) {
        this.parkSeconds = parkSeconds;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public double getConsume() {
        return consume;
    }

    public void setConsume(double consume) {
        this.consume = consume;
    }

    public String getStallCode() {
        return stallCode;
    }

    public void setStallCode(String stallCode) {
        this.stallCode = stallCode;
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

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWaitPay() {
        return waitPay;
    }

    public void setWaitPay(double waitPay) {
        this.waitPay = waitPay;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(String chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }


    public int getWaitLeaveMin() {
        return waitLeaveMin;
    }

    public void setWaitLeaveMin(int waitLeaveMin) {
        this.waitLeaveMin = waitLeaveMin;
    }

}
