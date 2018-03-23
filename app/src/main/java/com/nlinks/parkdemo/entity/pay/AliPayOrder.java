package com.nlinks.parkdemo.entity.pay;

/**
 * 支付宝下单返回结果
 * Created by Dell on 2017/04/27.
 */
public class AliPayOrder {
	String orderInfo, orderCode;


	@Override
	public String toString() {
		return "AliPayOrder{" +
			"orderInfo='" + orderInfo + '\'' +
			", orderCode='" + orderCode + '\'' +
			'}';
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}
