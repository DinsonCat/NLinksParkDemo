package com.nlinks.parkdemo.entity._req;

/**
 * 登录用户
 */
public class Login  {
    private String appKey;  //security.oauth2.client.client-id

    private int appSecret;  //security.oauth2.client.client-secret

    private String phoneNo;   //登录手机号

    private String password;  //登录密码

    private String smsCode;   //手机验证码

    private String cid;//个推id

    /**
     * 账号密码登录上传对象
     *
     * @param appKey
     * @param appSecret
     * @param phoneNo
     * @param password
     * @param cid
     */
    public Login(String appKey, int appSecret, String phoneNo, String password, String cid) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.phoneNo = phoneNo;
        this.password = password;
        this.cid = cid;
    }

    /**
     * 短信验证码上传对象
     *
     * @param appKey
     * @param appSecret
     * @param phoneNo
     * @param password
     * @param smsCode
     * @param cid
     */
    public Login(String appKey, int appSecret, String phoneNo, String password, String smsCode, String cid) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.phoneNo = phoneNo;
        this.password = password;
        this.smsCode = smsCode;
        this.cid = cid;
    }


    @Override
    public String toString() {
        return "Login{" +
            "appKey='" + appKey + '\'' +
            ", appSecret=" + appSecret +
            ", phoneNo='" + phoneNo + '\'' +
            ", password='" + password + '\'' +
            ", smsCode='" + smsCode + '\'' +
            ", cid='" + cid + '\'' +
            '}';
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(int appSecret) {
        this.appSecret = appSecret;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }


}
