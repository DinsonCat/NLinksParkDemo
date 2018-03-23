package com.nlinks.parkdemo.entity.appointment;

import java.io.Serializable;

/**
 * Created by Dell on 2017/05/02.
 */

public class AppointmentRecord implements Serializable {

	/**
	 * createTime : 2017-05-10 17:51:11
	 * status : 1
	 * deviceCode : 03000000
	 * parkStatus : 4
	 * chargeOut : 0
	 * parkSharingId : 63b6261675ce4784b7e73e5ec40515d8
	 * id : 3e1100f982a848b1b7aaf4a371d5c177
	 * charge : 0
	 * timeOut : 0
	 * address : 福建省泉州市丰泽区后厝路
	 * actualTime : 2017-05-10 17:54:40
	 * name : 南威停车场
	 * leaveTime : 2017-05-10 17:50:53
	 * money : 12
	 * enterTime : 2017-05-10 17:54:28
	 * plateNum : 闽C213K8
	 */

	private String createTime;
	private String status;
	private String deviceCode;
	private String parkStatus;
	private double chargeOut;
	private String parkSharingId;
	private String id;
	private double charge;
	private int timeOut;
	private String address;
	private String actualTime;
	private String name;
	private String leaveTime;
	private double money;
	private String enterTime;
	private String plateNum;
	private double longitude;
	private double latitude;

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getParkStatus() {
		return parkStatus;
	}

	public void setParkStatus(String parkStatus) {
		this.parkStatus = parkStatus;
	}

	public double getChargeOut() {
		return chargeOut;
	}

	public void setChargeOut(double chargeOut) {
		this.chargeOut = chargeOut;
	}

	public String getParkSharingId() {
		return parkSharingId;
	}

	public void setParkSharingId(String parkSharingId) {
		this.parkSharingId = parkSharingId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}

	public String getPlateNum() {
		return plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
}
