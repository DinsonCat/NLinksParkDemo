package com.nlinks.parkdemo.entity._req;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Dinson - 2017/8/28
 */
public class ThirdAuthLogin implements Parcelable{
    /**
     * appKey : string
     * appSecret : string
     * authCode : string
     * phoneNo : string
     * smsCode : string
     */
    private String appKey;
    private int appSecret;
    private String authCode;
    private String phoneNo;
    private String smsCode;
    private String cid;

    @Override
    public String toString() {
        return "ThirdAuthLogin{" +
            "appKey='" + appKey + '\'' +
            ", appSecret=" + appSecret +
            ", authCode='" + authCode + '\'' +
            ", phoneNo='" + phoneNo + '\'' +
            ", smsCode='" + smsCode + '\'' +
            ", cid='" + cid + '\'' +
            '}';
    }

    protected ThirdAuthLogin(Parcel in) {
        appKey = in.readString();
        appSecret = in.readInt();
        authCode = in.readString();
        phoneNo = in.readString();
        smsCode = in.readString();
        cid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appKey);
        dest.writeInt(appSecret);
        dest.writeString(authCode);
        dest.writeString(phoneNo);
        dest.writeString(smsCode);
        dest.writeString(cid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThirdAuthLogin> CREATOR = new Creator<ThirdAuthLogin>() {
        @Override
        public ThirdAuthLogin createFromParcel(Parcel in) {
            return new ThirdAuthLogin(in);
        }

        @Override
        public ThirdAuthLogin[] newArray(int size) {
            return new ThirdAuthLogin[size];
        }
    };


    public ThirdAuthLogin(String appKey, int appSecret, String authCode, String phoneNo, String smsCode, String cid) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.authCode = authCode;
        this.phoneNo = phoneNo;
        this.smsCode = smsCode;
        this.cid = cid;
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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public boolean thirdLoginCheck() {
        if (this.appSecret == 0 || this.cid.equals("") || this.smsCode.equals("")
            || this.phoneNo.equals("") || this.appKey.equals("")||this.authCode.equals("")) return false;
        return true;
    }
}
