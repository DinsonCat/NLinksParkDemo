package com.nlinks.parkdemo.entity.pay;

/**
 * 钱包明细  http 返回结果
 */
public class WalletMoneyDetail {

    //
//    private String id;  //流水id
//    private String createTime; //创建时间
//    private double amountMoney;  //金额(+值是收入 -值是支出)
//    private String userId; //用户id
//    private int linkBusiness; //关联业务(0 未知；1 订单；2 充值；3 订单退款)
//    private String linkContent;//关联内容(eg. "充值100元")
//    private String code; //流水编号
//    private String orderId; //关联订单
//    private String creator;
    /**
     * payState : 2
     * createTime : 2017-05-15 10:04:29
     * serialNo : 4008402001201705150899657731
     * transactMoney : 0.01
     * transactDesc : 1
     * ordercode : 20170515100420679311714
     * payChannel : 2
     */

    private String payState;//payState:支付状态(1等待支付、2交易完成、3交易关闭、4交易失败、5全额退款)
    private String createTime;
    private String serialNo;//serialNo:支付流水号
    private double transactMoney;
    private int transactDesc;//transactDesc:交易描述(1钱包充值、2停车缴费、3预约缴费、4预约超时缴费、5停车预缴、6路外停车缴费、7会员升级)
    private String ordercode;//ordercode:商家订单号
    private int payChannel;//payChannel:支付渠道(1钱包支付、2微信支付，3支付宝、4线下支付)

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public double getTransactMoney() {
        return transactMoney;
    }

    public void setTransactMoney(double transactMoney) {
        this.transactMoney = transactMoney;
    }

    public int getTransactDesc() {
        return transactDesc;
    }

    public void setTransactDesc(int transactDesc) {
        this.transactDesc = transactDesc;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }





}
