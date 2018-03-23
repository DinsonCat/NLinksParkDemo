package com.nlinks.parkdemo.entity._req;

/**
 * 停车记录提交生成订单
 */
public class ParkRecordReq {
//     "couponDetailIds": "string",
//             "parkRecordId": "string",
//             "userId": "string"


    private String couponDetailIds;
    private String parkRecordId;
    private String userId;
    private int payType;
    private final int transactType = 1;


    public ParkRecordReq(String couponDetailIds, String parkRecordId, String userId, int payType) {
        this.couponDetailIds = couponDetailIds;
        this.parkRecordId = parkRecordId;
        this.userId = userId;
        this.payType = payType;
    }


    @Override
    public String toString() {
        return "ParkRecordReq{" +
            "couponDetailIds='" + couponDetailIds + '\'' +
            ", parkRecordId='" + parkRecordId + '\'' +
            ", userId='" + userId + '\'' +
            ", payType=" + payType +
            ", transactType=" + transactType +
            '}';
    }

    public String getCouponDetailIds() {
        return couponDetailIds;
    }

    public void setCouponDetailIds(String couponDetailIds) {
        this.couponDetailIds = couponDetailIds;
    }

    public String getParkRecordId() {
        return parkRecordId;
    }

    public void setParkRecordId(String parkRecordId) {
        this.parkRecordId = parkRecordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

}
