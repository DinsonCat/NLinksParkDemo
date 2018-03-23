package com.nlinks.parkdemo.entity.common;

/**
 * Created by Dell on 2017/05/24.
 */

public class LatestVersion {
	/**
	 * createTime : 1467685166000
	 * downloadAddress : http://218.5.135.5:22122/v1/tfs/T1paJTBKdT1RygfCrK.apk
	 * versionCode : 5
	 * versionName : 1.0.4
	 * updateInfo : 修复一些问题
	 */

	private long createTime;
	private String downloadAddress;
	private int versionCode;
	private String versionName;
	private String updateInfo;

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}
}
