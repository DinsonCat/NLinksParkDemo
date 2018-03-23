package com.nlinks.parkdemo.entity.appointment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 停车场共享车位列表
 * Created by Dell on 2017/05/05.
 */
public class AppointStall implements Parcelable {

	/**
	 * startTime : 2017-05-05 19:41:37
	 * deviceCode : 03000000
	 * money : 10
	 * endTime : 2017-05-05 21:41:40
	 * parkSharingId : 7A4F724236EA6B544372533E2E33AB5F
	 * stallCode : HL0311
	 */

	private String startTime;
	private String deviceCode;
	private double money;
	private String endTime;
	private String parkSharingId;
	private String stallCode;

	public AppointStall() {
	}

	protected AppointStall(Parcel in) {
		startTime = in.readString();
		deviceCode = in.readString();
		money = in.readDouble();
		endTime = in.readString();
		parkSharingId = in.readString();
		stallCode = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(startTime);
		dest.writeString(deviceCode);
		dest.writeDouble(money);
		dest.writeString(endTime);
		dest.writeString(parkSharingId);
		dest.writeString(stallCode);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<AppointStall> CREATOR = new Creator<AppointStall>() {
		@Override
		public AppointStall createFromParcel(Parcel in) {
			return new AppointStall(in);
		}

		@Override
		public AppointStall[] newArray(int size) {
			return new AppointStall[size];
		}
	};

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getParkSharingId() {
		return parkSharingId;
	}

	public void setParkSharingId(String parkSharingId) {
		this.parkSharingId = parkSharingId;
	}

	public String getStallCode() {
		return stallCode;
	}

	public void setStallCode(String stallCode) {
		this.stallCode = stallCode;
	}
}
