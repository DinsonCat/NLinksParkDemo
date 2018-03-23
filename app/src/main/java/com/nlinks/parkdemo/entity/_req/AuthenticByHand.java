package com.nlinks.parkdemo.entity._req;

/**
 * @author Dinson - 2017/8/2
 */
public class AuthenticByHand {
    /**
     * carNo : string
     * carUserName : string
     * carUserPhone : string
     * engineNo : string
     * userId : string
     * vinNo : string
     */

    private String carNo;
    private String carUserName;
    private String carUserPhone;
    private String engineNo;
    private String userId;
    private String vinNo;


    @Override
    public String toString() {
        return "AuthenticByHand{" +
                "carNo='" + carNo + '\'' +
                ", carUserName='" + carUserName + '\'' +
                ", carUserPhone='" + carUserPhone + '\'' +
                ", engineNo='" + engineNo + '\'' +
                ", userId='" + userId + '\'' +
                ", vinNo='" + vinNo + '\'' +
                '}';
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarUserName() {
        return carUserName;
    }

    public void setCarUserName(String carUserName) {
        this.carUserName = carUserName;
    }

    public String getCarUserPhone() {
        return carUserPhone;
    }

    public void setCarUserPhone(String carUserPhone) {
        this.carUserPhone = carUserPhone;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVinNo() {
        return vinNo;
    }

    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }
}
