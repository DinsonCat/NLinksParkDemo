package com.nlinks.parkdemo.entity._req;

/**
 * 注册和忘记密码共用
 */

public class RegisterAndForgetPwd {
    private String smsCode;   //手机短信验证码

    private String phoneNo;   //注册手机号码

    private String password;  //注册密码

    public RegisterAndForgetPwd(String smsCode, String phoneNo, String password) {
        this.smsCode = smsCode;
        this.phoneNo = phoneNo;
        this.password = password;
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
