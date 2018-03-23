package com.nlinks.parkdemo.entity._req;

/**
 * 上传车牌
 */
public class PlateNum {
    private String userId;

    private String carNo;

    private String brand;

    @Override
    public String toString() {
        return "PlateNum{" +
                "userId='" + userId + '\'' +
                ", carNo='" + carNo + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public PlateNum(String userId, String carNo) {
        this.userId = userId;
        this.carNo = carNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
