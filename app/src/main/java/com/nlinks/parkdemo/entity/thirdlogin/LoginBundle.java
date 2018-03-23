package com.nlinks.parkdemo.entity.thirdlogin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 登录传值
 *
 * @author Dinson - 2017/9/1
 */
public class LoginBundle implements Parcelable {
    //qq登录传值
    private String accessToken;
    private String openid;
    private String qqappid;

    //微信，支付宝传值
    private String authCode;

    public LoginBundle() {
    }

    protected LoginBundle(Parcel in) {
        accessToken = in.readString();
        openid = in.readString();
        qqappid = in.readString();
        authCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(openid);
        dest.writeString(qqappid);
        dest.writeString(authCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginBundle> CREATOR = new Creator<LoginBundle>() {
        @Override
        public LoginBundle createFromParcel(Parcel in) {
            return new LoginBundle(in);
        }

        @Override
        public LoginBundle[] newArray(int size) {
            return new LoginBundle[size];
        }
    };

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getQqappid() {
        return qqappid;
    }

    public void setQqappid(String qqappid) {
        this.qqappid = qqappid;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
