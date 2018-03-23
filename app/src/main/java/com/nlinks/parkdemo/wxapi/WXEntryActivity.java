package com.nlinks.parkdemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ThirdLoginApi;
import com.nlinks.parkdemo.entity.thirdlogin.LoginBundle;
import com.nlinks.parkdemo.entity.thirdlogin.LoginResult;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.login.util.LoginUtils;
import com.nlinks.parkdemo.module.login.view.BindPhoneActivity;
import com.nlinks.parkdemo.module.usercenter.UserCenterActivity;
import com.nlinks.parkdemo.module.usercenter.managecar.AddCarPlateActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * @author Dinson - 2017/8/24
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_wx_entry);
        //如果没回调onResp，八成是这句没有写
        LoginUtils.getInstance().getWxApi().handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

        LogUtils.e("onReq  < Type:" + req.getType() + " checkArgs:" + req.checkArgs()
            + " openId:" + req.openId + " transaction:" + req.transaction);

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e("wechat error code:" + resp.errCode + " errStr:" + resp.errStr);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) UIUtils.showToast("分享失败");
                else UIUtils.showToast("登录失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        LogUtils.e("code = " + code);
                        checkWeiXinLogin(code);
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        UIUtils.showToast("微信分享成功");
                        finish();
                        break;
                }
                break;
        }
    }

    private void checkWeiXinLogin(final String code) {
         HttpHelper.getRetrofit().create(ThirdLoginApi.class).checkWeixinLogin(code,SPUtils.getCid(this,""))
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<LoginResult>(this,false) {
            @NonNull
            @Override
            public void onHandleSuccess(LoginResult user) {
                GlobalApplication.userId = user.getUserId();
                GlobalApplication.token = user.getToken();
                GlobalApplication.membership = user.getMembership();
                GlobalApplication.plateNum = user.getPlateNum();

                SPUtils.putUserId(UIUtils.getContext(), user.getUserId());
                SPUtils.putToken(UIUtils.getContext(), user.getToken());
                SPUtils.putUserPlate(UIUtils.getContext(), user.getPlateNum());
                SPUtils.putMembership(UIUtils.getContext(), user.getMembership());

                LogUtils.e("login success: " + GlobalApplication.userId + " <> " + SPUtils.getUserId(UIUtils.getContext())
                    + "  " + GlobalApplication.token + " <> " + SPUtils.getToken(UIUtils.getContext(), ""));

                if (StringUtils.isEmpty(user.getPlateNum())) {
                    AddCarPlateActivity.startForResult(WXEntryActivity.this, true, REQUEST_CODE_PLATE);
                } else {
                    finish();
                    UserCenterActivity.start(WXEntryActivity.this);
                }
            }

            @Override
            public void onHandleError(int errorCode, String message) {
                super.onHandleError(errorCode, message);
                if (errorCode == 42004) {
                    LoginBundle bundle = new LoginBundle();
                    bundle.setAuthCode(code);
                    BindPhoneActivity.start(WXEntryActivity.this, bundle, BindPhoneActivity.ThirdLoginMode.WX);
                    finish();
                }
            }
        });
    }

    private static final int REQUEST_CODE_PLATE = 568;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PLATE) {
            finish();
            UserCenterActivity.start(this);
        }
    }/**
     * 状态栏透明之后，标题栏会顶上去。所以标题栏的大小要加大
     */
    public void setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
