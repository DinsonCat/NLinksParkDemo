package com.nlinks.parkdemo.module.login.presenter;

import com.nlinks.parkdemo.entity.user.LoginResultData;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.module.login.model.LoginModelImpl;
import com.nlinks.parkdemo.module.login.model.OnLoginListener;
import com.nlinks.parkdemo.module.login.model.OnPlateNullListener;
import com.nlinks.parkdemo.module.login.view.ILoginView;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.UIUtils;


/**
 *
 */
public class LoginPresenterImpl implements ILoginPresenter {

    private ILoginView loginView;
    private final LoginModelImpl loginModel;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModelImpl(mLoginlistener);
    }

    private OnLoginListener mLoginlistener = new OnLoginListener() {
        @Override
        public void loginSuccess(LoginResultData user, String phone) {
            if (user == null) {
                loginView.onLoginResult(false, "验证码发送成功,请稍后");
                loginView.countDown();
            } else {


                GlobalApplication.userId = user.getUserId();
                GlobalApplication.token = user.getToken();
                GlobalApplication.membership = user.getMembership();
                GlobalApplication.plateNum = user.getPlateNum();

                SPUtils.putUserId(UIUtils.getContext(), user.getUserId());
                SPUtils.putToken(UIUtils.getContext(), user.getToken());
                SPUtils.putUserPlate(UIUtils.getContext(), user.getPlateNum());
                SPUtils.putMembership(UIUtils.getContext(), user.getMembership());


                LogUtils.e("login success: "+GlobalApplication.userId+" <> "+SPUtils.getUserId(UIUtils.getContext())
                +"  "+GlobalApplication.token+" <> "+SPUtils.getToken(UIUtils.getContext(),""));

                //登陆完成后需要获取车牌信息
                loginModel.getPlate(user.getUserId(), new OnPlateNullListener() {
                    @Override
                    public void onCallBack(boolean isNull) {
                        if (isNull){
                            //车牌为空时跳转添加车牌界面
                            loginView.junp2addPlateNum();
                        }else{
                            loginView.onLoginResult(true, "");
                        }
                       
                    }
                });
            }
            loginView.onSetProgressBarVisibility(false);
            SPUtils.putLastPhone(UIUtils.getContext(), phone);
        }

        @Override
        public void loginFailed(String msg) {
            loginView.onLoginResult(false, msg);
            loginView.onSetProgressBarVisibility(false);
        }
    };


    @Override
    public void doLoginBySms(String name, String sms) {
        loginView.onSetProgressBarVisibility(true);
        loginModel.loginBySms(name, sms);
    }

    @Override
    public void doLoginByPwd(String name, String pwd) {
        loginView.onSetProgressBarVisibility(true);
        loginModel.loginByPwd(name, pwd);
    }

    @Override
    public void getSmsCode(String phone) {
        loginModel.getSmsCode(phone);
    }

}
