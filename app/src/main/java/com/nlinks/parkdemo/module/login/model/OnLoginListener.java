package com.nlinks.parkdemo.module.login.model;

import com.nlinks.parkdemo.entity.user.LoginResultData;

/**
 * 登录完成监听
 */
public interface OnLoginListener {
    void loginSuccess(LoginResultData user, String phone);

    void loginFailed(String msg);
}

