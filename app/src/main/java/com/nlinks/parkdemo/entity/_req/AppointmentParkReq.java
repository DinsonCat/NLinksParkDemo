package com.nlinks.parkdemo.entity._req;

/**
 * 预定车位请求
 * Created by Dell on 2017/05/06.
 */
public class AppointmentParkReq {
	@Override
	public String toString() {
		return "AppointmentParkReq{" +
			"couponDetailIds='" + couponDetailIds + '\'' +
			", leaveTime='" + leaveTime + '\'' +
			", parkSharingId='" + parkSharingId + '\'' +
			", plateNum='" + plateNum + '\'' +
			", userId='" + userId + '\'' +
			", transactType=" + transactType +
			'}';
	}

	/**
	 * couponDetailIds : string
	 * leaveTime : string
	 * parkSharingId : string
	 * plateNum : string
	 * userId : string
	 */


	private String couponDetailIds;
	private String leaveTime;
	private String parkSharingId;
	private String plateNum;
	private String userId;
	private final int transactType = 1;

	public String getCouponDetailIds() {
		return couponDetailIds;
	}

	public void setCouponDetailIds(String couponDetailIds) {
		this.couponDetailIds = couponDetailIds;
	}

	public String getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getParkSharingId() {
		return parkSharingId;
	}

	public void setParkSharingId(String parkSharingId) {
		this.parkSharingId = parkSharingId;
	}

	public String getPlateNum() {
		return plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
