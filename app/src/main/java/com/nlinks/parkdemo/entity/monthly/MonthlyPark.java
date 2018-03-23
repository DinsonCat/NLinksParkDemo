package com.nlinks.parkdemo.entity.monthly;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 错峰包月停车场
 */

public class MonthlyPark implements Parcelable {

    /**
     * chareFee : 100
     * address : 福建省泉州市丰泽区后厝路
     * latitude : 24.879617
     * name : 南威地下车库
     * parkCode : nw001
     * type : 2
     * parkImageList : ["http://59.61.216.123:14106/v1/tfs/T1ZKKgByWg1R4bAZ6K","http://59.61.216.123:14106/v1/tfs/T1ZKKgByWg1R4bAZ6K"]
     * longitude : 118.643717
     */
    /**
     * 套餐
     * chareFee : 99
     */
    private String chareFee;
    private String address;
    private double latitude;
    private String name;
    private String parkCode;
    private int type;
    private double longitude;
    private List<String> parkImageList;
    private String monthlyRuleId="";//月卡套餐id

    public MonthlyPark() {
    }

    public MonthlyPark(String address, double latitude, String name, String parkCode, int type,
                       double longitude, List<String> parkImageList) {
        this.address = address;
        this.latitude = latitude;
        this.name = name;
        this.parkCode = parkCode;
        this.type = type;
        this.longitude = longitude;
        this.parkImageList = parkImageList;
    }


    protected MonthlyPark(Parcel in) {
        chareFee = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        name = in.readString();
        parkCode = in.readString();
        type = in.readInt();
        longitude = in.readDouble();
        parkImageList = in.createStringArrayList();
        monthlyRuleId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chareFee);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeString(name);
        dest.writeString(parkCode);
        dest.writeInt(type);
        dest.writeDouble(longitude);
        dest.writeStringList(parkImageList);
        dest.writeString(monthlyRuleId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MonthlyPark> CREATOR = new Creator<MonthlyPark>() {
        @Override
        public MonthlyPark createFromParcel(Parcel in) {
            return new MonthlyPark(in);
        }

        @Override
        public MonthlyPark[] newArray(int size) {
            return new MonthlyPark[size];
        }
    };

    public String getMonthlyRuleId() {
        return monthlyRuleId;
    }

    public void setMonthlyRuleId(String monthlyRuleId) {
        this.monthlyRuleId = monthlyRuleId;
    }

    public String getChareFee() {
        return chareFee;
    }

    public void setChareFee(String chareFee) {
        this.chareFee = chareFee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getParkImageList() {
        return parkImageList;
    }

    public void setParkImageList(List<String> parkImageList) {
        this.parkImageList = parkImageList;
    }


}
