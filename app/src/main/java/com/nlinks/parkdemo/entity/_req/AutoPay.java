package com.nlinks.parkdemo.entity._req;

/**
 * @author Dinson - 2017/7/8
 */
public class AutoPay {


    /**
     * carNoId : string
     * isAutoPay : 0
     */

    private String carNoId;
    private int isAutoPay;
    @Override
    public String toString() {
        return "AutoPay{" +
                "carNoId='" + carNoId + '\'' +
                ", isAutoPay=" + isAutoPay +
                '}';
    }

    public AutoPay(String carNoId, int isAutoPay) {
        this.carNoId = carNoId;
        this.isAutoPay = isAutoPay;
    }
    public String getCarNoId() {
        return carNoId;
    }

    public void setCarNoId(String carNoId) {
        this.carNoId = carNoId;
    }

    public int getIsAutoPay() {
        return isAutoPay;
    }

    public void setIsAutoPay(int isAutoPay) {
        this.isAutoPay = isAutoPay;
    }
}
