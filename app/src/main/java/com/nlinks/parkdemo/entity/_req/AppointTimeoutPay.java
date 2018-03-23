package com.nlinks.parkdemo.entity._req;

/**
 * 预定车位超时支付请求
 * Created by Dell on 2017/05/06.
 */
public class AppointTimeoutPay {
	@Override
	public String toString() {
		return "AppointTimeoutPay{" +
			"appointRecordId='" + appointRecordId + '\'' +
			", userId='" + userId + '\'' +
			'}';
	}

	public AppointTimeoutPay(String appointRecordId, String userId) {
		this.appointRecordId = appointRecordId;
		this.userId = userId;
	}

	/**
	 * appointRecordId : string
	 * userId : string
	 */

	private String appointRecordId;
	private String userId;

	public String getAppointRecordId() {
		return appointRecordId;
	}

	public void setAppointRecordId(String appointRecordId) {
		this.appointRecordId = appointRecordId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
