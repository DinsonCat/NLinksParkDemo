package com.nlinks.parkdemo.entity.user;

import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.utils.MD5;

/**
 * 发送验证码
 */
public class SmsCode {
    private String phoneNo;
    private String accessKey = AppConst.CODE_KEY;
    private String accessToken;

    public SmsCode(String phoneNo) {
        this.phoneNo = phoneNo;
        this.accessToken = MD5.getSmsToken(phoneNo, accessKey);
    }

    @Override
    public String toString() {
        return "SmsCode{" +
            "phoneNo='" + phoneNo + '\'' +
            ", accessKey='" + accessKey + '\'' +
            ", accessToken='" + accessToken + '\'' +
            '}';
    }
}
