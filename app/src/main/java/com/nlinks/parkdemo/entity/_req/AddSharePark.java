package com.nlinks.parkdemo.entity._req;

/**
 * Created by Dinson on 2017/5/5.
 */

public class AddSharePark {
//    "parkName": "string",
//            "phoneNo": "string",
//            "stallName": "string",
//            "userId": "string"
    private String parkName;
    private String phoneNo;
    private String stallName;
    private String userId;

    public AddSharePark(String parkName, String phoneNo, String stallName, String userId) {
        this.parkName = parkName;
        this.phoneNo = phoneNo;
        this.stallName = stallName;
        this.userId = userId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
