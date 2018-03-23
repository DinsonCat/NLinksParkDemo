package com.nlinks.parkdemo.module.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.RedeemCodeApi;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity._req.RedeemBuyGoldCode;
import com.nlinks.parkdemo.entity.other.WebActionBean;
import com.nlinks.parkdemo.entity.park.ParkRecordPay;
import com.nlinks.parkdemo.entity.user.UserInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.module.home.GuideActivity;
import com.nlinks.parkdemo.module.home.view.MainActivity;
import com.nlinks.parkdemo.module.login.view.LoginActivity;
import com.nlinks.parkdemo.module.park.ParkingPaymentActivity;
import com.nlinks.parkdemo.utils.ColorUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NetworkUtils;
import com.nlinks.parkdemo.utils.RegexUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.SoftHideKeyBoardUtil;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.OnScrollCallbackWebView;
import com.nlinks.parkdemo.widget.OnScrollCallbackWebView.OnscrollChangeCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.nlinks.parkdemo.module.base.BaseActivity.LOGIN_CODE;
import static com.nlinks.parkdemo.module.base.WebViewPromotionActivity.TitleMode.NoTransparent;

public class WebViewPromotionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int mScrollTitleHight = 200;//滚动渐变完全高度

    public static final String EXTRA_HTML = "html_string";
    public static final String EXTRA_ISFROMSPLASH = "isFromSplash";
    private OnScrollCallbackWebView wv_content;
    private boolean mIsFromSplash;
    private View mTitleBar;
    private TextView mTvActionCancel, mTvActionBack, mTvTitleText;
    private String mTitleColor;

    public static void start(Context context, String url) {
        start(context, url, false);
    }

    public static void start(Context context, String url, boolean isFromSplash) {
        Intent starter = new Intent(context, WebViewPromotionActivity.class);
        starter.putExtra(EXTRA_HTML, url);
        starter.putExtra(EXTRA_ISFROMSPLASH, isFromSplash);
        context.startActivity(starter);
    }

    public static final int PAYMENT_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_web_view);
        SoftHideKeyBoardUtil.assistActivity(this);
        initUI();
    }

    private void initUI() {
        //设置title
        mIsFromSplash = getIntent().getBooleanExtra(EXTRA_ISFROMSPLASH, false);

        mTvActionCancel = (TextView) findViewById(R.id.action_cancel);
        mTvActionBack = (TextView) findViewById(R.id.action_back);
        mTvActionCancel.setOnClickListener(this);
        mTvActionBack.setOnClickListener(this);
        mTitleBar = findViewById(R.id.wbTitleBar);
        mTvTitleText = (TextView) findViewById(R.id.tv_title_text);

        initWebView();
    }

    private void initWebView() {
        String htmlStr = getIntent().getStringExtra(EXTRA_HTML);

        if (StringUtils.isNotEmpty(SPUtils.getUserId(this))) {
            htmlStr += "?userId=" + SPUtils.getUserId(this);
        }

        LogUtils.i("WebView load URL: " + htmlStr);

        wv_content = (OnScrollCallbackWebView) findViewById(R.id.wv_content);
        WebSettings webSettings = wv_content.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        webSettings.setSupportZoom(true);//支持缩放，默认为true
        //webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //wv_content.setWebViewClient(new WebViewClient());

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        wv_content.loadUrl(htmlStr);

        //增加接口方法,让html页面调用
        // wv_content.addJavascriptInterface(this,"app");
        wv_content.addJavascriptInterface(new AppInterface(), "app");
        wv_content.setWebViewClient(new MyWebViewClient());

        //设置顶部加载进度
        final ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar);
        wv_content.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pg.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pg.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg.setProgress(newProgress);//设置进度值
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTvTitleText.setText(RegexUtils.isUrl(title) ? "" : title);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wv_content.removeAllViews();
        wv_content.destroy();
    }

    // 此方法为系统返回键的监听
    @Override
    public void onBackPressed() {
        if (wv_content.canGoBack()) {
            wv_content.goBack();
        } else if (mIsFromSplash) {
            startActivity(new Intent(WebViewPromotionActivity.this, SPUtils.isFirstRun(WebViewPromotionActivity.this) ?
                GuideActivity.class : MainActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_cancel:
                if (mIsFromSplash) {
                    startActivity(new Intent(WebViewPromotionActivity.this, SPUtils.isFirstRun(WebViewPromotionActivity.this) ?
                        GuideActivity.class : MainActivity.class));
                } else {
                    finish();
                }
                break;
            case R.id.action_back:
                onBackPressed();
                break;
        }
    }


    class AppInterface {
        @JavascriptInterface
        public void action(String json) {
            LogUtils.e(json);
            WebActionBean action = new Gson().fromJson(json, WebActionBean.class);
            LogUtils.e(action.toString());
            switch (action.getPaytype()) {
                case 7://成为会员
                    if (validateUserIdAndToken(false)) {
                        final String userId = SPUtils.getUserId(WebViewPromotionActivity.this);
                        boolean isMember = SPUtils.isMember(WebViewPromotionActivity.this);
                        if (isMember) {
                            final DialogCancelConfirm dialog = new DialogCancelConfirm(WebViewPromotionActivity.this);
                            dialog.setMessage("您已开通出行停车金卡");
                            dialog.setButtonsText("取消", "查看金卡特权");
                            dialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
                                @Override
                                public void onLeftClick() {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onRightClick() {
                                    dialog.dismiss();
                                    UIUtils.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            wv_content.loadUrl(AppConst.URL_MEMBER_DETAIL);
                                        }
                                    });
                                }
                            });
                            dialog.show();
                            return;
                        }
                        if (action.getViewType() == 0) {
                            ParkRecordPay bean = new ParkRecordPay();
                            bean.setShouldPay(action.getMoney());
                            bean.setPayType(7);
                            bean.setActualPay(action.getMoney());
                            ParkingPaymentActivity.startForResult(WebViewPromotionActivity.this, bean, ParkingPaymentActivity.LAYOUT_PROMOTION
                                , PAYMENT_REQUEST_CODE, 0);
                        } else if (action.getViewType() == 1) {
                            UIUtils.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    wv_content.loadUrl("javascript:goBuyGold()");
                                }
                            });

                        }
                    }
                    break;
            }
        }

        @JavascriptInterface
        public void showBgcolor(final String color) {
            //获取页面背景颜色

            mTitleColor = "#ffffffff";

            LogUtils.i("bgcolor : " + color);
            UIUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mTvActionCancel.setVisibility(wv_content.canGoBack() ? View.VISIBLE : View.INVISIBLE);
                }
            });

            if (StringUtils.isEmpty(color)) {
                setTitleMode(TitleMode.NoTransparent);//取不到颜色值，取透明色
                return;
            }
            String[] split = color.split(",");
            if (split.length == 0) {
                setTitleMode(TitleMode.Transparent);//颜色值出错，取透明色
                return;
            }

            if (ColorUtils.isColor(split[0])) {
                String colorString = ColorUtils.parseColor(split[0]);
                mTitleColor = colorString;
                //mTitleBar.setBackgroundColor(Color.parseColor(colorString));
            }
            if (split.length == 2 && String.valueOf(split[1]).equals("2")) {
                setTitleMode(TitleMode.NoTransparent); //不透明模式
            } else {
                setTitleMode(TitleMode.Transparent);//透明模式
            }

            /*if (StringUtils.isNotEmpty(color) && ColorUtils.isColor(color)) {
                //mTitleColor = ColorUtils.parseColor(color);
                //final float titleHight = getResources().getDimension(R.dimen.title_height) * 1.5f;

                //final TextView titleTv = titlebar.getTitleTv();
                //titleTv.setAlpha(0);

                wv_content.setOnscrollChangeCallback(new OnscrollChangeCallback() {
                    @Override
                    public void onScroll(int l, int t, int oldl, int oldt) {
                        if (t < titleHight) {
                            float precent = (float) t / titleHight;
                            LogUtils.e(precent + " -- " + t + " -- " + titleHight);
                            //titlebar.setBackgroundColor(ColorUtils.getCurrentColor(precent, 0x00000000, Color.parseColor(mTitleColor)));
                            titleTv.setAlpha(precent);
                        }
                    }
                });
            }*/
        }

        @JavascriptInterface
        public void exchangeMember(final String code) {
            LogUtils.e("receive data: " + code);
            final String userId = SPUtils.getUserId(WebViewPromotionActivity.this);
            if (StringUtils.isEmpty(userId)) {
                UIUtils.showToast("用户异常，请重新登陆");
                return;
            }

                  /*  RedeemBuyGoldCode entity = new RedeemBuyGoldCode(code, userId);
                    Observable observable = HttpHelper.getRetrofit().create(RedeemCodeApi.class).getRedeemBuyGoldCode(entity);

                  */

            RedeemBuyGoldCode entity = new RedeemBuyGoldCode(code, userId);
            HttpHelper.getRetrofit().create(RedeemCodeApi.class).getRedeemBuyGoldCode(entity)
                .flatMap(new Function<HttpResult<String>, ObservableSource<HttpResult<UserInfo>>>() {
                    @Override
                    public ObservableSource<HttpResult<UserInfo>> apply(HttpResult<String> bean) throws Exception {
                        if (bean.getStatusCode() == 200) {
                            return HttpHelper.getRetrofit().create(UserApi.class).getUserInfo(userId);
                        }
                        HttpResult<UserInfo> result = new HttpResult<>();
                        result.setStatusCode(bean.getStatusCode());
                        result.setStatusMsg(bean.getStatusMsg());
                        return Observable.just(result);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult<UserInfo>>() {
                    @Override
                    public void accept(HttpResult<UserInfo> userInfo) throws Exception {
                        if (userInfo.getStatusCode() == 200) {
                            UserInfo user = userInfo.getData();
                            LogUtils.e(user.toString());

                            GlobalApplication.membership = user.getMembership();
                            GlobalApplication.plateNum = user.getPlateNum();

                            SPUtils.putUserPlate(UIUtils.getContext(), user.getPlateNum());
                            SPUtils.putMembership(UIUtils.getContext(), user.getMembership());

                            UIUtils.showToast("兑换成功");
                            WebViewPromotionActivity.start(WebViewPromotionActivity.this, AppConst.URL_MEMBER_DETAIL, mIsFromSplash);
                            finish();
                        } else {
                            UIUtils.showToast(userInfo.getStatusMsg());
                        }
                    }
                });
        }


        @JavascriptInterface
        public void doLogin() {
            validateUserIdAndToken(false);
        }
    }

    enum TitleMode {
        Transparent, NoTransparent
    }

    private boolean flag_over = false;

    private void setTitleMode(TitleMode mode) {
        switch (mode) {
            case Transparent:
                setTitleBarColor(0);
                wv_content.setOnscrollChangeCallback(new OnscrollChangeCallback() {
                    @Override
                    public void onScroll(int l, int t, int oldl, int oldt) {
                        if (t < mScrollTitleHight) {
                            flag_over = false;
                            float percent = (float) t / mScrollTitleHight;
                            setTitleBarColor(percent);
                        }
                        if (t > mScrollTitleHight && !flag_over) {
                            setTitleBarColor(1);
                            flag_over = true;
                        }
                    }
                });
                break;
            case NoTransparent:
                setTitleBarColor(1);
                wv_content.setOnscrollChangeCallback(null);
                break;
        }
    }

    /**
     * 通过百分比设置titlebar整体颜色
     *
     * @param percent
     */
    private void setTitleBarColor(final float percent) {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mTitleBar.setBackgroundColor(ColorUtils.getCurrentColor(percent, 0x00000000, Color.parseColor(mTitleColor)));
                int textColor = ColorUtils.getCurrentColor(percent, 0xffffffff, 0xff000000);
                mTvTitleText.setTextColor(textColor);
                mTvActionCancel.setTextColor(textColor);
                mTvActionBack.setTextColor(textColor);
                mTvTitleText.setAlpha(percent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PAYMENT_REQUEST_CODE:
                //支付完成回调
                String userId = SPUtils.getUserId(this);
                if (StringUtils.isEmpty(userId)) return;
                GlobalApplication.membership = 1;
                wv_content.loadUrl(AppConst.URL_MEMBER_DETAIL);
                break;
            case LOGIN_CODE:
                //登录完成回调
                String url = wv_content.getUrl();
                wv_content.loadUrl(url+"?userId="+SPUtils.getUserId(this));
                wv_content.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wv_content.clearHistory();
                    }
                },1000);
                break;
        }
    }

    final class MyWebViewClient extends WebViewClient {
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.app.showBgcolor(document.body.getAttribute('titlecolor'))");
            super.onPageFinished(view, url);
        }
    }

    /**
     * 状态栏透明之后，标题栏会顶上去。所以标题栏的大小要加大
     */
    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 标题栏的左边按钮的点击事件
     *
     * @param view
     */
    public void titleLeftBtnClick(View view) {
        onBackPressed();
    }

    /**
     * 验证用户id和token
     *
     * @return
     */
    public boolean validateUserIdAndToken(boolean showFlag) {
        //没网络不跳转
        if (!NetworkUtils.isNetworkAvailable()) {
            UIUtils.showToast("网络连接失败，请检查网络");
            return false;
        }
        //userid或者token异常重新登录
        if (StringUtils.isEmpty(SPUtils.getUserId(this, "")) || StringUtils.isEmpty(SPUtils.getToken(this, ""))) {
            if (showFlag) UIUtils.showToast("验证失效，请重新登录");
            SPUtils.resetUser(this);
            jumpToForResult(LoginActivity.class, LOGIN_CODE);
            return false;
        }
        return true;
    }

    public void jumpToForResult(Class<? extends Activity> clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }
}
