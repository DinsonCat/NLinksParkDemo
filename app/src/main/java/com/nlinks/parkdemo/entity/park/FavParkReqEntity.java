package com.nlinks.parkdemo.entity.park;

/**
 * @author lzhixing@linewell.com
 *         Created at 11:05 16/7/28
 */
public class FavParkReqEntity {

    private String phoneNo;  //用户

    private String parkCode;  //停车场编码

    public FavParkReqEntity(String phoneNo, String parkCode) {
        this.phoneNo = phoneNo;
        this.parkCode = parkCode;
    }

    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getParkCode() {
        return this.parkCode;
    }


    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }
}
