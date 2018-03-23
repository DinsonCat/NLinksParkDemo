package com.nlinks.parkdemo.module.login.model;

/**
 *
 */
public interface ILoginModel {
    void loginBySms(String username, String sms);

    void loginByPwd(String username, String pwd);

    void getSmsCode(String phone);

    void getPlate(String userId, OnPlateNullListener listener);

}
