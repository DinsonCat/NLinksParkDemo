package com.nlinks.parkdemo.entity.plate;

/**
 * Created by PC-DINSON on 2017/4/12.
 */
public class PlateInfo {
    /**
     * id : 1af477388cbd49189ce015072afa9ca1
     * carNo : 川P5Q5Q5
     * status : 0
     * isAutoPay : 0
     * brand : null
     * isAuthentic : 0
     */

    private String id;
    private String carNo;
    private int status;
    private int isAutoPay;
    private String brand;
    private int isAuthentic;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsAutoPay() {
        return isAutoPay;
    }

    public void setIsAutoPay(int isAutoPay) {
        this.isAutoPay = isAutoPay;
    }


    public int getIsAuthentic() {
        return isAuthentic;
    }

    public void setIsAuthentic(int isAuthentic) {
        this.isAuthentic = isAuthentic;
    }





}
