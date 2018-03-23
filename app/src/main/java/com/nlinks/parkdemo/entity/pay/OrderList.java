package com.nlinks.parkdemo.entity.pay;

import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

import java.io.Serializable;

/**
 * 停车缴费列表
 */
public class OrderList implements Serializable,Visitable {
    /**
     * createTime : 2017-05-11 16:44:09
     * orderState : 1
     * payFlowId :
     * coordinateX : 24.880256
     * coordinateY : 118.635992
     * orderCode : null
     * parkingStartTime : 2017-05-24 08:17:11
     * transactDesc : null
     * couponMoney : 0.0
     * realMoney : 36.0
     * preMoney : 0.0
     * parkingRecordId : cfd3d6e5bfb5442c8e46646c969f35c7
     * parkingEndTime : 2017-05-24 18:30:11
     * couponId :
     * payChannel : null
     * parkName : 丰海路（停车场）
     * parkAddress : 福建省泉州市丰泽区石头街271号
     * userId : c099d3b62dc24f94b6b6eee18a95bab9
     * parkingSeconds : 36780
     * tranId : null
     * orderMoney : 36.0
     * plateNum : 京A00112
     * orderId : 2793c4d4484e4b038f61a2fc3b4af1af
     */

    private String createTime;
    private int orderState;
    private String payFlowId;
    private double coordinateX;
    private double coordinateY;
    private String orderCode;
    private String parkingStartTime;
    private String transactDesc;
    private double couponMoney;
    private double realMoney;
    private double preMoney;
    private String parkingRecordId;
    private String parkingEndTime;
    private String couponId;
    private String payChannel;
    private String parkName;
    private String parkAddress;
    private String userId;
    private long parkingSeconds;
    private String tranId;
    private double orderMoney;
    private String plateNum;
    private String orderId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getPayFlowId() {
        return payFlowId;
    }

    public void setPayFlowId(String payFlowId) {
        this.payFlowId = payFlowId;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getParkingStartTime() {
        return parkingStartTime;
    }

    public void setParkingStartTime(String parkingStartTime) {
        this.parkingStartTime = parkingStartTime;
    }

    public String getTransactDesc() {
        return transactDesc;
    }

    public void setTransactDesc(String transactDesc) {
        this.transactDesc = transactDesc;
    }

    public double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(double couponMoney) {
        this.couponMoney = couponMoney;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public double getPreMoney() {
        return preMoney;
    }

    public void setPreMoney(double preMoney) {
        this.preMoney = preMoney;
    }

    public String getParkingRecordId() {
        return parkingRecordId;
    }

    public void setParkingRecordId(String parkingRecordId) {
        this.parkingRecordId = parkingRecordId;
    }

    public String getParkingEndTime() {
        return parkingEndTime;
    }

    public void setParkingEndTime(String parkingEndTime) {
        this.parkingEndTime = parkingEndTime;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkAddress() {
        return parkAddress;
    }

    public void setParkAddress(String parkAddress) {
        this.parkAddress = parkAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getParkingSeconds() {
        return parkingSeconds;
    }

    public void setParkingSeconds(long parkingSeconds) {
        this.parkingSeconds = parkingSeconds;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public double getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(double orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }


//    old
//    private String createTime;
//    private String payFlowId;
//    private String orderId;
//    private String userId;
//    private String parkingRecordId;
//    private String couponId;
//    private String plateNum;
//    private String orderCode;
//    private double coordinateX;
//    private double coordinateY;
//    private int status;//订单状态(1 待付款 2 已付款)
//    private String parkingStartTime;//停车入场时间
//    private String parkingEndTime;//停车结束时间
//    private double parkingHours;//停车时长
//    private double realMoney;//实际金额
//    private double consume;//订单金额
//    private double couponMoney;
//    private double prePayMoney;
//    private String parkName;
//    private String parkAddress;
//    private int payWay;//支付方式(1 账户 2 支付宝 3 微信 4 巡检员收缴 5 其他)
//    private String remark;//备注信息


}
