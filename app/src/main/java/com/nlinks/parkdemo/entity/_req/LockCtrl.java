package com.nlinks.parkdemo.entity._req;

/**
 * Created by Dell on 2017/05/05.
 */

public class LockCtrl {
	/**
	 * ctrlCmd : string
	 * deviceCode : string
	 * msgCode : string
	 */

	private String ctrlCmd;
	private String deviceCode;
	private String msgCode;

	public static LockCtrl up(String deviceCode){
		return new LockCtrl("up", deviceCode);
	}

	public static LockCtrl down(String deviceCode){
		return new LockCtrl("down", deviceCode);
	}

	public LockCtrl() {}

	public LockCtrl(String ctrlCmd, String deviceCode) {
		this(ctrlCmd, deviceCode, "12101");
	}

	public LockCtrl(String ctrlCmd, String deviceCode, String msgCode) {
		this.ctrlCmd = ctrlCmd;
		this.deviceCode = deviceCode;
		this.msgCode = msgCode;
	}

	public String getCtrlCmd() {
		return ctrlCmd;
	}

	public void setCtrlCmd(String ctrlCmd) {
		this.ctrlCmd = ctrlCmd;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
}
