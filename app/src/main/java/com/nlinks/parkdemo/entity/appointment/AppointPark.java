package com.nlinks.parkdemo.entity.appointment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nlinks.parkdemo.entity.pay.PlatformActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是预约车位，下单后返回的参数。下单后返回的参数还需要用到下一级支付中
 * Created by Dell on 2017/05/26.
 */
public class AppointPark implements Parcelable {

	/**
	 * randomOffMoney : 0.0
	 * couponMoney : 0
	 * appointMoney : 2.93
	 * realMoney : 2.9327666666666667
	 * activityList : []
	 * stallCode : 35050501
	 */
	private String appointTime;
	private String orderCode;
	private String appointMoney;
	private String realMoney;
	private String stallCode;//预约的车位编号
	private ArrayList<PlatformActivity> activityList;


	protected AppointPark(Parcel in) {
		appointTime = in.readString();
		orderCode = in.readString();
		appointMoney = in.readString();
		realMoney = in.readString();
		stallCode = in.readString();
		activityList = in.createTypedArrayList(PlatformActivity.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(appointTime);
		dest.writeString(orderCode);
		dest.writeString(appointMoney);
		dest.writeString(realMoney);
		dest.writeString(stallCode);
		dest.writeTypedList(activityList);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<AppointPark> CREATOR = new Creator<AppointPark>() {
		@Override
		public AppointPark createFromParcel(Parcel in) {
			return new AppointPark(in);
		}

		@Override
		public AppointPark[] newArray(int size) {
			return new AppointPark[size];
		}
	};

	public String getStallCode() {
		return stallCode;
	}

	public void setStallCode(String stallCode) {
		this.stallCode = stallCode;
	}
	public String getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(String appointTime) {
		this.appointTime = appointTime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getAppointMoney() {
		return appointMoney;
	}

	public void setAppointMoney(String appointMoney) {
		this.appointMoney = appointMoney;
	}

	public String getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(String realMoney) {
		this.realMoney = realMoney;
	}

	public ArrayList<PlatformActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<PlatformActivity> activityList) {
		this.activityList = activityList;
	}
}
