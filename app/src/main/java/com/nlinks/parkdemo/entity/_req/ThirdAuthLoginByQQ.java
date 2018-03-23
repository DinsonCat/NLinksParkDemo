package com.nlinks.parkdemo.entity._req;

/**
 * @author Dinson - 2017/8/28
 */
public class ThirdAuthLoginByQQ {

    /**
     * accessToken : string
     * appKey : string
     * appSecret : string
     * cid : string
     * openid : string
     * phoneNo : string
     * qqappid : string
     * smsCode : string
     */

    private String accessToken;
    private String appKey;
    private String appSecret;
    private String cid;
    private String openid;
    private String phoneNo;
    private String qqappid;
    private String smsCode;

    @Override
    public String toString() {
        return "ThirdAuthLoginByQQ{" +
            "accessToken='" + accessToken + '\'' +
            ", appKey='" + appKey + '\'' +
            ", appSecret='" + appSecret + '\'' +
            ", cid='" + cid + '\'' +
            ", openid='" + openid + '\'' +
            ", phoneNo='" + phoneNo + '\'' +
            ", qqappid='" + qqappid + '\'' +
            ", smsCode='" + smsCode + '\'' +
            '}';
    }

    public ThirdAuthLoginByQQ(String accessToken, String appKey, String appSecret, String cid, String openid, String phoneNo, String qqappid, String smsCode) {
        this.accessToken = accessToken;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.cid = cid;
        this.openid = openid;
        this.phoneNo = phoneNo;
        this.qqappid = qqappid;
        this.smsCode = smsCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getQqappid() {
        return qqappid;
    }

    public void setQqappid(String qqappid) {
        this.qqappid = qqappid;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
