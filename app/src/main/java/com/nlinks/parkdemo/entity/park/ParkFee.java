package com.nlinks.parkdemo.entity.park;

/**
 * 停车记录支付记录
 */
public class ParkFee {


    /**
     * randomMoney : 0
     * createTime : 2018-01-18 19:13:42
     * parkingrecordid : 1111111111111111173
     * couponMoney : 0.99
     * payChannel : 2
     * realMoney : 0.01
     * payState : 2
     * couponId : [{"couponId":"0aeee44038c3497d8a9b2c59fcebc36f","couponMoney":0.99,"couponType":1}]
     */

    private double randomMoney;
    private String createTime;
    private double couponMoney;
    private int payChannel;
    private double realMoney;

    public double getRandomMoney() {
        return randomMoney;
    }

    public void setRandomMoney(double randomMoney) {
        this.randomMoney = randomMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(double couponMoney) {
        this.couponMoney = couponMoney;
    }

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

}
