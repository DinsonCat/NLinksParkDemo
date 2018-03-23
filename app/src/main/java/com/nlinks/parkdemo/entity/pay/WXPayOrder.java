package com.nlinks.parkdemo.entity.pay;

import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 微信下单返回结果
 * Created by Dell on 2017/04/27.
 */
public class WXPayOrder {

	/**
	 * package : Sign=WXPay
	 * appid : wx3498e08e2b8bf537
	 * sign : 8422429C81EB80BFC6D261164D158408
	 * result_code : SUCCESS
	 * orderCode : 201704271827031107278310
	 * return_msg : OK
	 * partnerid : 1406980602
	 * prepayid : wx20170427182704742258cc5f0429408335
	 * return_code : SUCCESS
	 * noncestr : QQCIcgI48nL9lHTj
	 * timestamp : 1493288824
	 */

	@SerializedName("package")
	private String packageX;
	private String appid;
	private String sign;
	private String result_code;
	private String orderCode;
	private String return_msg;
	private String partnerid;
	private String prepayid;
	private String return_code;
	private String noncestr;
	private String timestamp;

	public String getPackageX() {
		try {
			return URLDecoder.decode(packageX, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return packageX;
	}

	public void setPackageX(String packageX) {
		this.packageX = packageX;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
