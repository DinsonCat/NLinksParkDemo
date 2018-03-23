package com.nlinks.parkdemo.entity._req;

import android.os.Parcel;
import android.os.Parcelable;

import com.nlinks.parkdemo.utils.StringUtils;

/**
 * 支付订单上传类（包括钱包支付，微信支付，支付宝支付）
 */

public class PayOrderReq implements Parcelable {
	/**
	 * couponDetailIds : string
	 * couponMoney : 0
	 * orderAttach : string
	 * orderCode : string
	 * orderDesc : string
	 * orderDetail : string
	 * payType : 0
	 * totalFee : 0
	 * userId : string
	 */

	private String couponDetailIds;
	private String couponMoney ;
	private String orderAttach;
	private String orderCode;
	private String orderDesc;
	private String orderDetail;
	private int payType; //1充值支付   2订单支付
	private String totalFee;
	private String userId;

	public PayOrderReq() {}

	public PayOrderReq(String couponDetailIds, double couponMoney, String orderAttach
			, String orderCode, String orderDesc, String orderDetail, int payType
			, double totalFee, String userId) {
		this(couponDetailIds, StringUtils.formatMoney(couponMoney), orderAttach, orderCode
				, orderDesc, orderDetail, payType, StringUtils.formatMoney(totalFee), userId);
	}

	public PayOrderReq(String couponDetailIds, String couponMoney, String orderAttach
			, String orderCode, String orderDesc, String orderDetail, int payType
			, String totalFee, String userId) {
		this.couponDetailIds = couponDetailIds;
		this.couponMoney = couponMoney;
		this.orderAttach = orderAttach;
		this.orderCode = orderCode;
		this.orderDesc = orderDesc;
		this.orderDetail = orderDetail;
		this.payType = payType;
		this.totalFee = totalFee;
		this.userId = userId;
	}

	protected PayOrderReq(Parcel in) {
		couponDetailIds = in.readString();
		couponMoney = in.readString();
		orderAttach = in.readString();
		orderCode = in.readString();
		orderDesc = in.readString();
		orderDetail = in.readString();
		payType = in.readInt();
		totalFee = in.readString();
		userId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(couponDetailIds);
		dest.writeString(couponMoney);
		dest.writeString(orderAttach);
		dest.writeString(orderCode);
		dest.writeString(orderDesc);
		dest.writeString(orderDetail);
		dest.writeInt(payType);
		dest.writeString(totalFee);
		dest.writeString(userId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PayOrderReq> CREATOR = new Creator<PayOrderReq>() {
		@Override
		public PayOrderReq createFromParcel(Parcel in) {
			return new PayOrderReq(in);
		}

		@Override
		public PayOrderReq[] newArray(int size) {
			return new PayOrderReq[size];
		}
	};

	@Override
	public String toString() {
		return "PayOrderReq{" +
				"couponDetailIds='" + couponDetailIds + '\'' +
				", couponMoney='" + couponMoney + '\'' +
				", orderAttach='" + orderAttach + '\'' +
				", orderCode='" + orderCode + '\'' +
				", orderDesc='" + orderDesc + '\'' +
				", orderDetail='" + orderDetail + '\'' +
				", payType=" + payType +
				", totalFee='" + totalFee + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}

	public String getCouponDetailIds() {
		return couponDetailIds;
	}

	public void setCouponDetailIds(String couponDetailIds) {
		this.couponDetailIds = couponDetailIds;
	}

	public String getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(double couponMoney) {
		this.couponMoney = StringUtils.formatMoney(couponMoney);
	}

	public void setCouponMoney(String couponMoney) {
		this.couponMoney = couponMoney;
	}

	public String getOrderAttach() {
		return orderAttach;
	}

	public void setOrderAttach(String orderAttach) {
		this.orderAttach = orderAttach;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(double totalFee) {
		this.totalFee = StringUtils.formatMoney(totalFee);
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
