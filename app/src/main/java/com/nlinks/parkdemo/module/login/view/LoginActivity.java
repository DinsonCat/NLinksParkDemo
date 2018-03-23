package com.nlinks.parkdemo.module.login.view;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ThirdLoginApi;
import com.nlinks.parkdemo.entity.thirdlogin.AuthResult;
import com.nlinks.parkdemo.entity.thirdlogin.LoginBundle;
import com.nlinks.parkdemo.entity.thirdlogin.LoginResult;
import com.nlinks.parkdemo.entity.thirdlogin.SignInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.login.presenter.LoginPresenterImpl;
import com.nlinks.parkdemo.module.login.util.LoginUtils;
import com.nlinks.parkdemo.module.usercenter.UserCenterActivity;
import com.nlinks.parkdemo.module.usercenter.managecar.AddCarPlateActivity;
import com.nlinks.parkdemo.utils.APPUtil;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.RegexUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.utils.avalidations.EditTextValidator;
import com.nlinks.parkdemo.utils.avalidations.ValidationModel;
import com.nlinks.parkdemo.utils.avalidations.utils.LoginCodeValidation;
import com.nlinks.parkdemo.utils.avalidations.utils.UserPhoneValidation;
import com.nlinks.parkdemo.utils.avalidations.utils.UserPwdValidation;
import com.nlinks.parkdemo.widget.CustomProgressDialog;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText et_username, et_password, et_sms;
    private LoginPresenterImpl presenter;
    private Button btn_login;
    private RelativeLayout rl_login_sms;
    private RadioGroup rg_login_mode;
    private long sendLeftTime = 59 * 1000;// 重新发送验证码时间,1分钟

    private static final int REQUEST_PLATE = 742;


    private CountDownTimer countDownTimer = new CountDownTimer(sendLeftTime, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long remainedSecs = millisUntilFinished / 1000;
            tv_getCode.setEnabled(false);
            tv_getCode.setText(String.format("%02d", remainedSecs % 60) + "秒后重发");
        }

        @Override
        public void onFinish() {
            tv_getCode.setText("获取验证码");
            tv_getCode.setEnabled(true);
            cancel();
        }
    };
    private TextView tv_getCode;
    private EditTextValidator mPwdValidator;//账号密码验证
    private EditTextValidator mCodeValidator;
    private View mLoginByWechat, mLoginByQQ, mLoginByAli;
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private ThirdLoginApi mThirdLoginApi;
    private CustomProgressDialog mPd;

    //private LinearLayout ll_protocol;
    //private CheckBox mCbProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenterImpl(this);
        mPd = new CustomProgressDialog(this);

        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(AppConst.QQ_APP_ID, this.getApplicationContext());
        mThirdLoginApi = HttpHelper.getRetrofit().create(ThirdLoginApi.class);

        initUI();
    }

    private void initUI() {
        btn_login = findViewById(R.id.btn_login);
        tv_getCode = findViewById(R.id.tv_getCode);
        et_sms = findViewById(R.id.et_sms);
        et_username = findViewById(R.id.et_username);
        et_username.setText(SPUtils.getLastPhone(this, ""));
        et_password = findViewById(R.id.et_password);
        rl_login_sms = findViewById(R.id.rl_login_sms);
        rg_login_mode = findViewById(R.id.rg_login_mode);
        RadioButton rb_pwd = findViewById(R.id.rb_pwd);

        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        mLoginByWechat = findViewById(R.id.loginByWechat);
        mLoginByWechat.setOnClickListener(this);
        mLoginByQQ = findViewById(R.id.loginByQQ);
        mLoginByQQ.setOnClickListener(this);
        mLoginByAli = findViewById(R.id.loginByAli);
        mLoginByAli.setOnClickListener(this);

        //ll_protocol = (LinearLayout) findViewById(R.id.ll_protocol);
        //mCbProtocol = (CheckBox) findViewById(R.id.cb_protocol);
        //TextView tv_protocol =  findViewById(R.id.tv_protocol);
        //SpannableString string = new SpannableString(UIUtils.getString(R.string.register_protocol));
        //string.setSpan(new ForegroundColorSpan(Color.parseColor("#4187ff")), 7, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //tv_protocol.setText(string);
        //tv_protocol.setOnClickListener(this);

        findViewById(R.id.tv_forget_pwd).setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
        rg_login_mode.setOnCheckedChangeListener(this);
        rb_pwd.setChecked(true);

        mPwdValidator = new EditTextValidator(this)
            .setButton(btn_login)
            .add(new ValidationModel(new UserPhoneValidation(et_username)))
            .add(new ValidationModel(new UserPwdValidation(et_password)))
            .execute();
        mCodeValidator = new EditTextValidator(this)
            .setButton(btn_login)
            .add(new ValidationModel(new UserPhoneValidation(et_username)))
            .add(new ValidationModel(new LoginCodeValidation(et_sms)))
            .execute();


        View rootView = findViewById(R.id.rootView);
        addLayoutListener(rootView, btn_login);


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

    @Override
    public void onLoginResult(Boolean result, String msg) {
        if (result) {
            setResult(RESULT_OK);
            finish();
        } else
            UIUtils.showToast(msg);
    }

    @Override
    public void onSetProgressBarVisibility(boolean visibility) {
        btn_login.setText(visibility ? "正在登录" : "登录");
    }

    /**
     * 发送短信剩余时间计时
     */
    @Override
    public void countDown() {
        countDownTimer.start();
    }

    @Override
    public void junp2addPlateNum() {
        AddCarPlateActivity.startForResult(this, true, REQUEST_PLATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PLATE) {
                setResult(RESULT_OK);
                finish();
            }
        }
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (rg_login_mode.getCheckedRadioButtonId() == R.id.rb_pwd) {
                    if (mPwdValidator.validate())
                        presenter.doLoginByPwd(formatEt(et_username), formatEt(et_password));
                } else {
                    if (mCodeValidator.validate())
                        presenter.doLoginBySms(formatEt(et_username), formatEt(et_sms));
                }
                break;
            case R.id.tv_getCode:
                String phone = formatEt(et_username);
                if (RegexUtils.isMobilePhone(phone)) {
                    presenter.getSmsCode(formatEt(et_username));
                } else {
                    UIUtils.showToast("请输入正确的电话号码");
                }
                break;
            case R.id.tv_register:
                RegisterActivity.start(this);
                break;
            case R.id.tv_forget_pwd:
                ForgetPwdActivity.start(this);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.loginByWechat:
                if (mPd != null) mPd.show();
                wxLogin();
                break;
            case R.id.loginByQQ:
                if (mPd != null) mPd.show();
                if (APPUtil.isAvilible(this, "com.tencent.mobileqq")) {
                    qqLogin();
                } else {
                    UIUtils.showToast("您未安装QQ客户端哟~");
                }
                break;
            case R.id.loginByAli:
                if (mPd != null) mPd.show();
                if (APPUtil.isAvilible(this, "com.eg.android.AlipayGphone")) {
                    aliLogin();
                } else {
                    UIUtils.showToast("您未安装支付宝客户端哟~");
                }
                break;
        }
    }

    private void aliLogin() {
        final String[] authCode = {""};
        final Observable<HttpResult<SignInfo>> observable = mThirdLoginApi.authLoginInfo();
        observable
            .map(new Function<HttpResult<SignInfo>, Map<String, String>>() {
                @Override
                public Map<String, String> apply(HttpResult<SignInfo> signInfoHttpResult) throws Exception {
                    // 构造AuthTask 对象
                    AuthTask authTask = new AuthTask(LoginActivity.this);
                    // 调用授权接口，获取授权结果
                    return authTask.authV2(signInfoHttpResult.getData().getSignInfo(), true);
                }
            })
            .flatMap(new Function<Map<String, String>, ObservableSource<HttpResult<LoginResult>>>() {
                @Override
                public ObservableSource<HttpResult<LoginResult>> apply(Map<String, String> stringStringMap) throws Exception {
                    AuthResult authResult = new AuthResult(stringStringMap, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        authCode[0] = authResult.getAuthCode();
                        return mThirdLoginApi.checkAlipayLogin(authCode[0], SPUtils.getCid(LoginActivity.this, ""));
                    } else {
                        // 其他状态值则为授权失败
                        return Observable.just(new HttpResult<LoginResult>());
                    }
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<HttpResult<LoginResult>>() {
                @Override
                public void accept(HttpResult<LoginResult> bean) throws Exception {
                    LogUtils.e(bean.toString());
                    if (mPd != null)
                        mPd.cancel();
                    switch (bean.getStatusCode()) {
                        case 0:
                            UIUtils.showToast("授权失败");
                            break;
                        case 42004:
                            LoginBundle bundle = new LoginBundle();
                            bundle.setAuthCode(authCode[0]);
                            BindPhoneActivity.start(LoginActivity.this, bundle, BindPhoneActivity.ThirdLoginMode.ALI);
                            break;
                        case 200:
                            LoginResult user = bean.getData();

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
                                junp2addPlateNum();
                            } else {
                                UserCenterActivity.start(LoginActivity.this);
                                finish();
                            }

                            break;
                    }
                }
            });
    }


    /**
     * 第三方微信登录
     */
    private void wxLogin() {
        mLoginByWechat.setEnabled(false);
        IWXAPI wxApi = LoginUtils.getInstance().getWxApi();
        if (!wxApi.isWXAppInstalled()) {
            UIUtils.showToast("您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        if (!wxApi.sendReq(req))
            UIUtils.showToast("打开微信登录失败!");
        mLoginByWechat.setEnabled(true);
        if (mPd != null && mPd.isShowing()) mPd.dismiss();
    }

    private void qqLogin() {
        mLoginByQQ.setEnabled(false);
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(this, "all", mIUiListener);
        mLoginByQQ.setEnabled(true);
        if (mPd != null && mPd.isShowing()) mPd.dismiss();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_pwd:
                setPwdMode(true);
                break;
            case R.id.rb_sms:
                setPwdMode(false);
                break;
        }
    }

    private void setPwdMode(boolean b) {
        rl_login_sms.setVisibility(b ? View.GONE : View.VISIBLE);
        //ll_protocol.setVisibility(b ? View.GONE : View.VISIBLE);
        et_password.setVisibility(b ? View.VISIBLE : View.GONE);
        et_password.setText("");
        et_sms.setText("");
    }

    private String formatEt(EditText et) {
        return et.getText().toString().trim().replaceAll(" ", "");
    }

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            LogUtils.e("response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                final String openID = obj.getString("openid");
                final String accessToken = obj.getString("access_token");
                String cid = SPUtils.getCid(LoginActivity.this, "");

                mThirdLoginApi.checkQQLogin(openID, accessToken, AppConst.QQ_APP_ID, cid)
                    .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<LoginResult>(mPd) {
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
                            junp2addPlateNum();
                        } else {
                            UserCenterActivity.start(LoginActivity.this);
                            finish();
                        }
                    }

                    @Override
                    public void onHandleError(int errorCode, String message) {
                        super.onHandleError(errorCode, message);
                        if (errorCode == 42004) {
                            LoginBundle bundle = new LoginBundle();
                            bundle.setAccessToken(accessToken);
                            bundle.setOpenid(openID);
                            bundle.setQqappid(AppConst.QQ_APP_ID);
                            BindPhoneActivity.start(LoginActivity.this, bundle, BindPhoneActivity.ThirdLoginMode.QQ);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            UIUtils.showToast("授权失败");
        }

        @Override
        public void onCancel() {
            UIUtils.showToast("授权取消");
        }
    }
}
