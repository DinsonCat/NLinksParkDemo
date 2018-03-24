package com.nlinks.parkdemo.module.login.model;

import android.support.annotation.NonNull;

import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity._req.Login;
import com.nlinks.parkdemo.entity.plate.PlateInfo;
import com.nlinks.parkdemo.entity.user.LoginResultData;
import com.nlinks.parkdemo.entity.user.SmsCode;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.MD5;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * 登录
 */
public class LoginModelImpl implements ILoginModel {

    private OnLoginListener mListener;
    private final UserApi mUserApi;

    public LoginModelImpl(OnLoginListener loginListener) {
        this.mListener = loginListener;
        mUserApi = HttpHelper.getRetrofit().create(UserApi.class);
    }

    @Override
    public void loginBySms(final String username, String sms) {
        Login entity = new Login(GlobalApplication.appKey,
            GlobalApplication.appSecret, username, null, sms, SPUtils.getCid(UIUtils.getContext(), ""));
        LogUtils.e(entity.toString());
        mUserApi.LoginBySms(entity)
            .compose(RxSchedulers.io_main()).subscribe(new LoginObserver<LoginResultData>() {
            @Override
            public void onHandleSuccess(LoginResultData data) {
                mListener.loginSuccess(data, username);
            }
        });
    }

    @Override
    public void loginByPwd(final String username, String pwd) {
        Login entity = new Login(GlobalApplication.appKey,
            GlobalApplication.appSecret, username, MD5.encode(pwd), SPUtils.getCid(UIUtils.getContext(), ""));
        LogUtils.e(entity.toString());
        mUserApi.loginByPwd(entity)
            .compose(RxSchedulers.io_main()).subscribe(new LoginObserver<LoginResultData>() {
            @Override
            public void onHandleSuccess(LoginResultData data) {
                mListener.loginSuccess(data, username);
            }
        });
    }

    @Override
    public void getSmsCode(final String phone) {

        mUserApi.getCode(new SmsCode(phone))
            .compose(RxSchedulers.io_main()).subscribe(new LoginObserver<Void>() {
            @Override
            public void onHandleSuccess(Void aVoid) {
                mListener.loginSuccess(null, phone);
            }
        });

        /*mUserApi.getCode(new SmsCode(phone))
            .compose(RxSchedulers.io_main()).subscribe(new LoginObserver<Void>() {
            @Override
            public void onHandleSuccess(Void aVoid) {
                mListener.loginSuccess(null, phone);
            }
        });*/
    }

    @Override
    public void getPlate(String userId, final OnPlateNullListener listener) {
        HttpHelper.getRetrofit().create(PlateNumAPI.class).getPlateList(userId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<PlateInfo>>() {
            @NonNull
            @Override
            public void onHandleSuccess(ArrayList<PlateInfo> plateInfos) {
                String plates = "";
                for (PlateInfo plateInfo : plateInfos) {
                    plates += plateInfo.getCarNo() + ",";
                }
                if (StringUtils.isEmpty(plates)) {
                    listener.onCallBack(true);
                } else {
                    //存sp
                    listener.onCallBack(false);
                    SPUtils.putUserPlate(UIUtils.getContext(), plates);
                }
            }
        });
    }

    abstract class LoginObserver<T> extends BaseObserver<T> {
        @Override
        public void onHandleError(int code, String message) {
            mListener.loginFailed(message);
        }
    }
}
