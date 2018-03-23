package com.nlinks.parkdemo.entity.park;

import com.nlinks.parkdemo.entity.monthly.MonthlyPark;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 停车详情Bean
 *
 * @author lzhixing@linewell.com
 *         Created at 2015/12/22 14:54
 */
public class ParkDetail implements Serializable {


    public MonthlyPark conversion() {
        return new MonthlyPark(getAddress(), getLatitude(), getName(), getCode(), getType(), getLongitude(),
                               getParkImageList());

    }


    private String name;//停车场名称
    private String code;//停车场编码
    private String address;//停车场地址
    private String instruction;//停车场收费标准
    private String parkImage;//停车场图片路径
    private int unuedStallNum;//空余车位
    private int stallNumber;//总车位
    private int type;//类型 1代表路外停车场，2代表路内停车场
    private int isAppoint, appointCount;//2017年5月15日 新增2个和预约相关的字段
    private ArrayList<String> parkImageList = new ArrayList<String>();//2017年9月28日 新增停车场图片，多图

    /**
     * latitude : 24.87951
     * end_time : 21:30:00
     * begin_time : 07:30:00
     * areaCode : 35050501
     * longitude : 118.643407
     */
    private double latitude;
    private String end_time;
    private String begin_time;
    private String areaCode;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getParkImage() {
        return parkImage;
    }

    public void setParkImage(String parkImage) {
        this.parkImage = parkImage;
    }

    public int getUnuedStallNum() {
        return unuedStallNum;
    }

    public void setUnuedStallNum(int unuedStallNum) {
        this.unuedStallNum = unuedStallNum;
    }

    public int getStallNumber() {
        return stallNumber;
    }

    public void setStallNumber(int stallNumber) {
        this.stallNumber = stallNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(int isAppoint) {
        this.isAppoint = isAppoint;
    }

    public int getAppointCount() {
        return appointCount;
    }

    public void setAppointCount(int appointCount) {
        this.appointCount = appointCount;
    }

    public ArrayList<String> getParkImageList() {
        return parkImageList;
    }

    public void setParkImageList(ArrayList<String> parkImageList) {
        this.parkImageList = parkImageList;
    }


}
