package com.nlinks.parkdemo.entity.favoritepark;

import android.os.Parcel;
import android.os.Parcelable;

public class FavorParkInfo implements Parcelable {

    /**
     * unUsedStallNum : 19
     * isAppoint : 1
     * address : 福建省泉州市丰泽区丰海路
     * name : 法制广场停车场
     * longitude : 118.624781
     * latitude : 24.883042
     * type : 1
     * stallNum : 25
     * appointCount : 0
     * parkCode : 35050102
     */

    private int unUsedStallNum;
    private int isAppoint;
    private String address;
    private String name;
    private double longitude;
    private double latitude;
    private int type;
    private int stallNum;
    private int appointCount;
    private String parkCode;

    protected FavorParkInfo(Parcel in) {
        unUsedStallNum = in.readInt();
        isAppoint = in.readInt();
        address = in.readString();
        name = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        type = in.readInt();
        stallNum = in.readInt();
        appointCount = in.readInt();
        parkCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(unUsedStallNum);
        dest.writeInt(isAppoint);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeInt(type);
        dest.writeInt(stallNum);
        dest.writeInt(appointCount);
        dest.writeString(parkCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavorParkInfo> CREATOR = new Creator<FavorParkInfo>() {
        @Override
        public FavorParkInfo createFromParcel(Parcel in) {
            return new FavorParkInfo(in);
        }

        @Override
        public FavorParkInfo[] newArray(int size) {
            return new FavorParkInfo[size];
        }
    };

    public int getUnUsedStallNum() {
        return unUsedStallNum;
    }

    public void setUnUsedStallNum(int unUsedStallNum) {
        this.unUsedStallNum = unUsedStallNum;
    }

    public int getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(int isAppoint) {
        this.isAppoint = isAppoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStallNum() {
        return stallNum;
    }

    public void setStallNum(int stallNum) {
        this.stallNum = stallNum;
    }

    public int getAppointCount() {
        return appointCount;
    }

    public void setAppointCount(int appointCount) {
        this.appointCount = appointCount;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }
}