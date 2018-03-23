package com.nlinks.parkdemo.module.login.presenter;

/**
 *
 */
public interface ILoginPresenter {
    void doLoginBySms(String name, String sms);
    void doLoginByPwd(String name, String pwd);
    void getSmsCode(String phone);


}
