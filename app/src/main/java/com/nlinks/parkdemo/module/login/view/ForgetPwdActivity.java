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
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity._req.RegisterAndForgetPwd;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.MD5;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.RegexUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.utils.avalidations.EditTextValidator;
import com.nlinks.parkdemo.utils.avalidations.ValidationModel;
import com.nlinks.parkdemo.utils.avalidations.utils.LoginCodeValidation;
import com.nlinks.parkdemo.utils.avalidations.utils.UserPhoneValidation;
import com.nlinks.parkdemo.utils.avalidations.utils.UserPwdValidation;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    private EditTextValidator mValidator;
    private EditText mEtUsername, mEtPwdConfirm, mEtPwd;
    private EditText mEtSms;
    private TextView mTvGetCode;
    private long sendLeftTime = 59 * 1000;// 重新发送验证码时间,1分钟
    private UserApi mUserApi;

    public static void start(Context context) {
        Intent starter = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        mUserApi = HttpHelper.getRetrofit().create(UserApi.class);

        initUI();

    }

    private void initUI() {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtSms = (EditText) findViewById(R.id.et_sms);
        mEtPwd = (EditText) findViewById(R.id.et_password);
        mEtPwdConfirm = (EditText) findViewById(R.id.et_password_confirm);


        View btnCommit = findViewById(R.id.btn_commit);
        mValidator = new EditTextValidator(this)
            .setButton(btnCommit)
            .add(new ValidationModel(new UserPhoneValidation(mEtUsername)))
            .add(new ValidationModel(new LoginCodeValidation(mEtSms)))
            .add(new ValidationModel(new UserPwdValidation(mEtPwd)))
            .add(new ValidationModel(new UserPwdValidation(mEtPwdConfirm)))
            .execute();

        btnCommit.setOnClickListener(this);
        //mEtUsername.setOnFocusChangeListener(this);
        mTvGetCode = (TextView) findViewById(R.id.tv_getCode);
        mTvGetCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (!mEtPwd.getText().toString().equals(mEtPwdConfirm.getText().toString())) {
                    UIUtils.showToast("两次密码不一致");
                    return;
                }
                if (mValidator.validate()) {
                    String sms = mEtSms.getText().toString();
                    String pwd = mEtPwd.getText().toString();
                    String PhoneNum = mEtUsername.getText().toString();
                    push2Server(new RegisterAndForgetPwd(sms, PhoneNum, MD5.encode(pwd)));
                }
                break;
            case R.id.tv_getCode:
                String phoneNum = mEtUsername.getText().toString();
                if (RegexUtils.isMobilePhone(phoneNum)) {
                    checkExist(phoneNum);
                } else {
                    UIUtils.showToast("请输入正确的手机号码");
                    mEtUsername.requestFocus();
                }
                break;
        }
    }

    /**
     * 忘记密码
     */
    private void push2Server(RegisterAndForgetPwd req) {
        mUserApi.forgetPwd(req)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void t) {
                UIUtils.showToast("密码已重置");
                finish();
            }
        });
    }

    /**
     * 获取验证码
     */
    private void checkExist(final String number) {

        mUserApi.checkExist(number).compose(RxSchedulers.io_main())
            .subscribe(new BaseObserver<Void>() {
                @NonNull
                @Override
                public void onHandleSuccess(Void aVoid) {
                    getCode(number);
                }

                @Override
                public void onHandleError(int code, String message) {
                    UIUtils.showToast("用户未注册");
                    mEtUsername.requestFocus();
                }
            });
    }

    private void getCode(String number) {
        mUserApi.getCode(number, AppConst.ACCESSKEY).compose(RxSchedulers.io_main())
            .subscribe(new BaseObserver<Void>() {
                @NonNull
                @Override
                public void onHandleSuccess(Void aVoid) {
                    countDownTimer.start();
                }
            });
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }


}
