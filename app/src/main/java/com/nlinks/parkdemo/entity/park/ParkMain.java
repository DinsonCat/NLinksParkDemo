package com.nlinks.parkdemo.entity.park;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 主页的停车场（待统一）
 */
public class ParkMain implements Parcelable {
    /**
     * stallNum : 325
     * code : nw001
     * address : 福建省泉州市丰泽区丰海路南威软件大厦
     * appointCount : 0
     * distance : 25.32
     * parkImage : http://120.76.199.51:7500/v1/tfs/T1btETBvVT1RXrhCrK
     * latitude : 24.879267
     * type : 2
     * parkImageList : ["http://120.76.199.51:7500/v1/tfs/T1btETBvVT1RXrhCrK"]
     * isEnable : 1
     * unuedStallNum : 118
     * name : 南威软件园区停车场
     * parklevel : 3
     * chargeStandard : 临时停放车辆2小时（含2小时）免费，临时停放车辆3小时以内（含3小时）每辆次5元，停放时间每超过1小时加收3元，以此类推，停车24小时内收费不超过30元。
     * isAppoint : 1
     * longitude : 118.643298
     */

    private String name="";//停车场名称
    private String code="";//停车场编码
    private String address="";//停车场地址
    private double distance;//距离
    private double longitude;//经度
    private double latitude;//纬度
    private int stallNum;//总车位
    private int unuedStallNum;//空余车位
    private int type;//类型 1 路内
    private int isAppoint;//是否可预约： 0支持预约 1不支持预约
    private int appointCount;//可预约车位数
    private String parkImage="";//停车场图片/**/
    private List<String> parkImageList;
    /**
     * distance : 26.25
     * latitude : 24.87951
     * isEnable : null
     * parklevel : 1
     * chargeStandard : 停放1小时内（含1小时）每辆次3元，超过1小时后每30分钟加收2元（不足30分钟的按30分钟计），同一辆车当天停放最高收费30元。
     * parkicon : null
     * longitude : 118.643407
     */
    private int parklevel=0;//区分停车场级别1：蓝蜻蜓，2：绿蜻蜓，3：金蜻蜓
    private String parkicon = "";

    private boolean isBaiduPark=false;

    protected ParkMain(Parcel in) {
        name = in.readString();
        code = in.readString();
        address = in.readString();
        distance = in.readDouble();
        longitude = in.readDouble();
        latitude = in.readDouble();
        stallNum = in.readInt();
        unuedStallNum = in.readInt();
        type = in.readInt();
        isAppoint = in.readInt();
        appointCount = in.readInt();
        parkImage = in.readString();
        parkImageList = in.createStringArrayList();
        parklevel = in.readInt();
        parkicon = in.readString();
        isBaiduPark = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(address);
        dest.writeDouble(distance);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeInt(stallNum);
        dest.writeInt(unuedStallNum);
        dest.writeInt(type);
        dest.writeInt(isAppoint);
        dest.writeInt(appointCount);
        dest.writeString(parkImage);
        dest.writeStringList(parkImageList);
        dest.writeInt(parklevel);
        dest.writeString(parkicon);
        dest.writeByte((byte) (isBaiduPark ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParkMain> CREATOR = new Creator<ParkMain>() {
        @Override
        public ParkMain createFromParcel(Parcel in) {
            return new ParkMain(in);
        }

        @Override
        public ParkMain[] newArray(int size) {
            return new ParkMain[size];
        }
    };

    public boolean isBaiduPark() {
        return isBaiduPark;
    }

    public void setBaiduPark(boolean baiduPark) {
        isBaiduPark = baiduPark;
    }

    public ParkMain() {
    }


    public int getStallNum() {
        return stallNum;
    }

    public void setStallNum(int stallNum) {
        this.stallNum = stallNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAppointCount() {
        return appointCount;
    }

    public void setAppointCount(int appointCount) {
        this.appointCount = appointCount;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getParkImage() {
        return parkImage;
    }

    public void setParkImage(String parkImage) {
        this.parkImage = parkImage;
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

    public int getUnuedStallNum() {
        return unuedStallNum;
    }

    public void setUnuedStallNum(int unuedStallNum) {
        this.unuedStallNum = unuedStallNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(int isAppoint) {
        this.isAppoint = isAppoint;
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


    public int getParklevel() {
        return parklevel;
    }

    public void setParklevel(int parklevel) {
        this.parklevel = parklevel;
    }


    public String getParkicon() {
        return parkicon;
    }

    public void setParkicon(String parkicon) {
        this.parkicon = parkicon;
    }

}