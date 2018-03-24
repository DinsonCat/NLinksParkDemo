package com.nlinks.parkdemo.module.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ThirdLoginApi;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity._req.ThirdAuthLogin;
import com.nlinks.parkdemo.entity._req.ThirdAuthLoginByQQ;
import com.nlinks.parkdemo.entity.thirdlogin.LoginBundle;
import com.nlinks.parkdemo.entity.thirdlogin.LoginResult;
import com.nlinks.parkdemo.entity.user.SmsCode;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.usercenter.UserCenterActivity;
import com.nlinks.parkdemo.module.usercenter.managecar.AddCarPlateActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.RegexUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.utils.avalidations.EditTextValidator;
import com.nlinks.parkdemo.utils.avalidations.ValidationModel;
import com.nlinks.parkdemo.utils.avalidations.utils.LoginCodeValidation;
import com.nlinks.parkdemo.utils.avalidations.utils.UserPhoneValidation;

import java.util.Locale;

import io.reactivex.Observable;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_LOGIN_REQ = "LOGIN_REQ";
    private static final String EXTRA_LOGIN_MODE = "LOGIN_MODE";
    private LoginBundle mAuthBean;
    private ThirdLoginApi mThirdLoginApi;
    private ThirdLoginMode mLoginMode;
    private EditTextValidator mValidator;
    private TextView mTvGetCode;
    private EditText mEtUsername;
    private EditText mEtSms;
    private UserApi mUserApi;

    public enum ThirdLoginMode {
        WX, ALI, QQ
    }

    public static void start(Context context, LoginBundle bean, ThirdLoginMode loginMode) {
        Intent starter = new Intent(context, BindPhoneActivity.class);
        starter.putExtra(EXTRA_LOGIN_REQ, bean);
        starter.putExtra(EXTRA_LOGIN_MODE, loginMode);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras == null || extras.size() == 0) finish();
        setContentView(R.layout.activity_bind_phone);

        mAuthBean = extras.getParcelable(EXTRA_LOGIN_REQ);
        mLoginMode = (ThirdLoginMode) extras.getSerializable(EXTRA_LOGIN_MODE);
        mThirdLoginApi = HttpHelper.getRetrofit().create(ThirdLoginApi.class);
        mUserApi = HttpHelper.getRetrofit().create(UserApi.class);


        initUI();

    }

    private void initUI() {
        View btn_commit = findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);

        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtSms = (EditText) findViewById(R.id.et_sms);
        mTvGetCode = (TextView) findViewById(R.id.tv_getCode);
        mTvGetCode.setOnClickListener(this);

        mValidator = new EditTextValidator(this)
            .setButton(btn_commit)
            .add(new ValidationModel(new UserPhoneValidation(mEtUsername)))
            .add(new ValidationModel(new LoginCodeValidation(mEtSms)))
            .execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (!mValidator.validate()) return;
                post2Server(createObservable());
                break;
            case R.id.tv_getCode:
                String phoneStr = formatEt(mEtUsername);
                if (RegexUtils.isMobilePhone(phoneStr)) {
                    getCode(phoneStr);
                } else {
                    UIUtils.showToast("请输入正确的手机号");
                }

                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param phoneStr
     */
    private void getCode(String phoneStr) {
        mTvGetCode.setEnabled(false);
        mUserApi.getCode(new SmsCode(phoneStr) )
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                countDownTimer.start();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mTvGetCode.setEnabled(true);
            }
        });
    }

    private Observable createObservable() {
        String phone = formatEt(mEtUsername);
        String smsCode = formatEt(mEtSms);
        String appKey = GlobalApplication.appKey;
        int appSecret = GlobalApplication.appSecret;
        String cid = SPUtils.getCid(this, "");
        switch (mLoginMode) {
            case WX:
                ThirdAuthLogin entity = new ThirdAuthLogin(appKey, appSecret, mAuthBean.getAuthCode(), phone, smsCode, cid);
                LogUtils.i(entity.toString());
                return mThirdLoginApi.weixinAuthLogin(entity);
            case QQ:
                ThirdAuthLoginByQQ thirdAuthLoginByQQ = new ThirdAuthLoginByQQ(mAuthBean.getAccessToken(),
                    appKey, String.valueOf(appSecret), cid, mAuthBean.getOpenid(), phone, mAuthBean.getQqappid(), smsCode);
                LogUtils.i(thirdAuthLoginByQQ.toString());
                return mThirdLoginApi.QQAuthLogin(thirdAuthLoginByQQ);
            case ALI:
                ThirdAuthLogin ali = new ThirdAuthLogin(appKey, appSecret, mAuthBean.getAuthCode(), phone, smsCode, cid);
                LogUtils.i(ali.toString());
                return mThirdLoginApi.aliAuthLogin(ali);
        }
        return null;
    }

    private void post2Server(Observable observable) {
        observable.compose(RxSchedulers.io_main()).subscribe(new BaseObserver<LoginResult>(this) {
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
                    AddCarPlateActivity.startForResult(BindPhoneActivity.this, true, REQUEST_CODE_PLATE);
                } else {
                    UserCenterActivity.start(BindPhoneActivity.this);
                }
            }
        });
    }

    private static final int REQUEST_CODE_PLATE = 165;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PLATE) {
            UserCenterActivity.start(this);
        }

    }

    private long sendLeftTime = 59 * 1000;// 重新发送验证码时间,1分钟

    private CountDownTimer countDownTimer = new CountDownTimer(sendLeftTime, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long remainedSecs = millisUntilFinished / 1000;
            mTvGetCode.setEnabled(false);
            mTvGetCode.setText(String.format(Locale.CHINA, "%02d秒后重发", remainedSecs % 60));
        }

        @Override
        public void onFinish() {
            mTvGetCode.setText("获取验证码");
            mTvGetCode.setEnabled(true);
            cancel();
        }
    };

    private String formatEt(EditText et) {
        return et.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
