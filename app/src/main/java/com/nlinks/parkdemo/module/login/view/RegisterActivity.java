package com.nlinks.parkdemo.module.login.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity._req.RegisterAndForgetPwd;
import com.nlinks.parkdemo.entity.user.RegisterResult;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewNormalActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.MD5;
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


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnAction;
    private CheckBox mCbProtocol;
    private EditTextValidator mCodeValidator;//表单校验
    private EditText mEtUsername, mEtSms, mEtPassword, mEtPasswordConfirm;
    private UserApi mUserApi;
    private long sendLeftTime = 59 * 1000;// 重新发送验证码时间,1分钟
    private TextView mTvGetCode;
    private View mLayoutStep1, mLayoutStep2;

    enum MODE {
        //下一步，完成
        NEXT, FINISH
    }

    private MODE mCurrentMode = MODE.NEXT;//当前的模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setContentView(R.layout.activity_register);

        mUserApi = HttpHelper.getRetrofit().create(UserApi.class);

        initUI();

        initLayout();

        View rootView = findViewById(R.id.rootView);
        addLayoutListener(rootView, mBtnAction);
    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    private void initUI() {

        //底部跳转文本
        TextView tv_login = (TextView) findViewById(R.id.tv_login);
        SpannableString span = new SpannableString("已有账号，立即登录");
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }
        }, 5, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_login.setText(span);
        tv_login.setMovementMethod(LinkMovementMethod.getInstance());
        //用户协议
        TextView tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        SpannableString protocolStr = new SpannableString("我已同意《出行停车停车服务协议》");
        protocolStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                WebViewNormalActivity.start(RegisterActivity.this, "用户协议", AppConst.URL_USERAGREEMENT);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }
        }, 4, protocolStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_protocol.setText(protocolStr);
        tv_protocol.setMovementMethod(LinkMovementMethod.getInstance());

        mTvGetCode = (TextView) findViewById(R.id.tv_getCode);
        mTvGetCode.setOnClickListener(this);
        mBtnAction = (Button) findViewById(R.id.btn_action);
        mBtnAction.setOnClickListener(this);
        mCbProtocol = (CheckBox) findViewById(R.id.cb_protocol);

        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtSms = (EditText) findViewById(R.id.et_sms);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPasswordConfirm = (EditText) findViewById(R.id.et_password_confirm);

        mLayoutStep1 = findViewById(R.id.layoutStep1);
        mLayoutStep2 = findViewById(R.id.layoutStep2);

        findViewById(R.id.iv_back).setOnClickListener(this);//返回

      /*  findViewById(R.id.tv_next).setOnClickListener(this);
        et_username = (EditText) findViewById(R.id.et_username);
        cb_protocol = (CheckBox) findViewById(R.id.cb_protocol);
        TextView tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        SpannableString string = new SpannableString(UIUtils.getString(R.string.register_protocol));
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#4187ff")), 7, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_protocol.setText(string);
        tv_protocol.setOnClickListener(this);*/

      /*  mMode = getIntent().getIntExtra(EXTRA_MODE, GlobalConfig.MODE_REGIST);
        if (mMode == GlobalConfig.MODE_FORGET) {
            findViewById(R.id.tv_mode_register).setVisibility(View.GONE);
            findViewById(R.id.ll_mode_register).setVisibility(View.GONE);
            CommonTitleBar titlebar = (CommonTitleBar) findViewById(R.id.titlebar);
            titlebar.setTitleText("找回密码");
        }*/
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        switch (mCurrentMode) {
            case NEXT:
                mBtnAction.setText("下一步");
                mLayoutStep1.setVisibility(View.VISIBLE);
                mLayoutStep2.setVisibility(View.INVISIBLE);
                mCodeValidator = new EditTextValidator(this).setButton(mBtnAction).add(new ValidationModel(new UserPhoneValidation(mEtUsername))).add(new ValidationModel(new LoginCodeValidation(mEtSms))).execute();
                break;
            case FINISH:
                mLayoutStep1.setVisibility(View.INVISIBLE);
                mLayoutStep2.setVisibility(View.VISIBLE);

                mBtnAction.setText("注册");
                mCodeValidator = new EditTextValidator(this).setButton(mBtnAction).add(new ValidationModel(new UserPwdValidation(mEtPassword))).add(new ValidationModel(new UserPwdValidation(mEtPasswordConfirm))).execute();
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_action://下一步
                if (!mCodeValidator.validate()) return;
                if (!mCbProtocol.isChecked()) {
                    UIUtils.showToast("请同意服务协议方可注册");
                    return;
                }
                if (mCurrentMode == MODE.NEXT) {
                    checkMsmCode();//教研验证码，通过执行下一步
                } else if (mCurrentMode == MODE.FINISH) {
                    String pwd = formatEt(mEtPassword);
                    if (!formatEt(mEtPasswordConfirm).equals(pwd)) {
                        UIUtils.showToast("两次密码不一致");
                        return;
                    }
                    doRegister(new RegisterAndForgetPwd(formatEt(mEtSms), formatEt(mEtUsername), MD5.encode(pwd)));
                }
                break;
            case R.id.tv_getCode:
                String phoneNum = mEtUsername.getText().toString();
                if (RegexUtils.isMobilePhone(phoneNum)) {
                    checkExist(phoneNum);
                } else {
                    UIUtils.showToast("请输入正确的手机号");
                    mEtUsername.requestFocus();
                }
                break;
            case R.id.iv_back:
                onBackPressed();//返回
                break;
        }
    }

    /**
     * 注册
     *
     * @param req
     */
    private void doRegister(RegisterAndForgetPwd req) {
        HttpHelper.getRetrofit().create(UserApi.class).register(req)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<RegisterResult>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(RegisterResult httpResult) {
                UIUtils.showToast("注册成功，请登录");
                finish();
            }

            @Override
            public void onHandleError(int code, String message) {
                super.onHandleError(code, message);
                mCurrentMode = MODE.NEXT;
                initLayout();
            }
        });
    }

    /**
     * 检验验证码
     */
    private void checkMsmCode() {
        mBtnAction.setEnabled(false);
        mUserApi.checkSmsCodeValid(formatEt(mEtUsername), formatEt(mEtSms))
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                mCurrentMode = MODE.FINISH;
                initLayout();
            }

            @Override
            public void onComplete() {
                mBtnAction.setEnabled(true);
                super.onComplete();
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
                    UIUtils.showToast("用户已注册");
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

    private String formatEt(EditText et) {
        return et.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    /**
     * addLayoutListener方法如下
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                int screenHeight = main.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了

                LogUtils.e("mainInvisibleHeight:" + mainInvisibleHeight + " screenHeight:" + screenHeight);
                if (mainInvisibleHeight > screenHeight / 4) {
                    main.scrollTo(0, 0);
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度

                    LogUtils.w("location[1]:" + location[1] + " scroll.getHeight():" + scroll.getHeight() + " rect.bottom:" + rect.bottom);

                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    //5､让界面整体上移键盘的高度

                    LogUtils.i("srollHeight:" + srollHeight);

                    main.scrollTo(0, srollHeight);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
            }
        });
    }

}
