package com.nlinks.parkdemo.entity._req;

/**
 * 发布共享车位
 */

public class PublishPark {
    /**
     * endTime : string
     * money : string
     * parkSharingId : string
     * startTime : string
     */

    private String endTime;
    private String money;
    private String parkSharingId;
    private String startTime;

    public PublishPark(String startTime,String endTime, String money, String parkSharingId) {
        this.endTime = endTime;
        this.money = money;
        this.parkSharingId = parkSharingId;
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getParkSharingId() {
        return parkSharingId;
    }

    public void setParkSharingId(String parkSharingId) {
        this.parkSharingId = parkSharingId;
    }

    public String getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "PublishPark{" +
                "endTime='" + endTime + '\'' +
                ", money='" + money + '\'' +
                ", parkSharingId='" + parkSharingId + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    public void setStartTime(String startTime) {


        this.startTime = startTime;
    }
}
