package com.nlinks.parkdemo.module.usercenter;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity.user.UserInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.appointment.AppointmentRecordActivity;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.login.view.LoginActivity;
import com.nlinks.parkdemo.module.usercenter.account.ManageAccountActivity;
import com.nlinks.parkdemo.module.usercenter.managecar.ManageCarActivity;
import com.nlinks.parkdemo.module.usercenter.monthly.MonthlyListActivity;
import com.nlinks.parkdemo.module.usercenter.myfollow.MyFollowActivity;
import com.nlinks.parkdemo.module.usercenter.parkrecord.ParkRecordActivity;
import com.nlinks.parkdemo.module.usercenter.setting.SettingListActivity;
import com.nlinks.parkdemo.module.usercenter.shareparking.ShareParkingMainActivity;
import com.nlinks.parkdemo.module.usercenter.suggestion.SuggestionActivity;
import com.nlinks.parkdemo.module.usercenter.walletmoney.WalletMoneyActivity;
import com.nlinks.parkdemo.utils.BitmapUtils;
import com.nlinks.parkdemo.utils.CacheUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.GlideCircleTransform;

import io.reactivex.Observable;

/**
 * 用户中心
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvUserHead;
    private TextView mTvUsername;
    // private TextView tv_walletMoney;
    private View mHasLoginLayout, mNoLoginLayout;
    //private TextView tv_do_login;

    private UserInfo mData;
    private View mMemberView;
    private ObjectAnimator mAnimator;

    public static void start(Context context) {
        Intent starter = new Intent(context, UserCenterActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.uc_activity_ifl, R.anim.uc_activity_otr);
        setContentView(R.layout.activity_usercenter);

        initUI();//初始化UI
    }

    @Override
    protected void onResume() {
        super.onResume();


        resetUserBar();

        initData();

        getDataFromServer();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            mAnimator.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    private void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.back).setOnClickListener(this);
        toolbar.findViewById(R.id.setting).setOnClickListener(this);

        mMemberView = findViewById(R.id.member);
        mMemberView.setOnClickListener(this);
        mAnimator = ObjectAnimator.ofFloat(mMemberView, "translationY", 0f, 20, 0f, 20f, 0f, 20f, 0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(5000);

        findViewById(R.id.myPurse).setOnClickListener(this);
        findViewById(R.id.manageCar).setOnClickListener(this);
        findViewById(R.id.tv_share_parking).setOnClickListener(this);
        findViewById(R.id.tv_suggestion).setOnClickListener(this);
        findViewById(R.id.parkRecord).setOnClickListener(this);
        findViewById(R.id.tv_myfollow).setOnClickListener(this);
        findViewById(R.id.tv_appointment_record).setOnClickListener(this);
        findViewById(R.id.tv_monthly_payment).setOnClickListener(this);

        mTvUsername = (TextView) findViewById(R.id.tv_username);
        mIvUserHead = (ImageView) findViewById(R.id.iv_userhead);
        mIvUserHead.setOnClickListener(this);
        mNoLoginLayout = findViewById(R.id.noLoginLayout);
        mNoLoginLayout.setOnClickListener(this);
        mHasLoginLayout = findViewById(R.id.hasLoginLayout);

    }


    /**
     * 初始化数据
     */
    private void initData() {

        boolean hasUserId = StringUtils.notEmpty(SPUtils.getUserId(this));

        if (!hasUserId) return;
        String cache = CacheUtils.getCache(SPUtils.getUserId(this));
        if (cache != null) {
            setData(new Gson().fromJson(cache, UserInfo.class));
        }

    }

    /**
     * 重置头像栏
     */
    private void resetUserBar() {
        boolean hasUserId = StringUtils.notEmpty(SPUtils.getUserId(this, ""));
        mNoLoginLayout.setVisibility(hasUserId ? View.GONE : View.VISIBLE);
        mHasLoginLayout.setVisibility(hasUserId ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.setting:
                SettingListActivity.start(this);
                break;
            case R.id.noLoginLayout:
                jumpToForResult(LoginActivity.class, LOGIN_CODE);
                break;
            case R.id.member://会员
                String userId = SPUtils.getUserId(this);
                WebViewPromotionActivity
                    .start(this, SPUtils.isMember(this) ? AppConst.URL_MEMBER_DETAIL : AppConst.URL_MEMBER);
                break;
            case R.id.myPurse://我的钱包
                validateTojump(WalletMoneyActivity.class);
                break;
            case R.id.manageCar://车辆管理
                validateTojump(ManageCarActivity.class);
                break;
            case R.id.tv_share_parking://共享车位
                validateTojump(ShareParkingMainActivity.class);
                break;
            case R.id.tv_suggestion://我的反馈
                jumpTo(SuggestionActivity.class);
                break;
            case R.id.iv_userhead://用户头像
                if (mData == null) return;
                turnToNext(mIvUserHead);
                break;
            case R.id.tv_monthly_payment://错峰包月
                validateTojump(MonthlyListActivity.class);
                break;
            case R.id.tv_myfollow://我的关注
                validateTojump(MyFollowActivity.class);
                break;
            case R.id.parkRecord://停车记录
                validateTojump(ParkRecordActivity.class);
                break;
            case R.id.tv_appointment_record://预约记录
                validateTojump(AppointmentRecordActivity.class);
                break;
        }
    }

    private void turnToNext(ImageView view) {
        Intent intent = new Intent(this, ManageAccountActivity.class);
        intent.putExtra(ManageAccountActivity.IMG_BITMAP, BitmapUtils.drawableToBitamp(view.getDrawable()));
        intent.putExtra(ManageAccountActivity.DATA_BEAN, mData);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(UserCenterActivity.this, view, "transition_img");
            Bundle bundle = options.toBundle();
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
    }

    private void getDataFromServer() {
        final String userId = SPUtils.getUserId(this);
        if (StringUtils.isEmpty(userId)) return;

        HttpHelper.getRetrofit().create(UserApi.class).getUserInfo(userId)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<UserInfo>() {
            @NonNull
            @Override
            public void onHandleSuccess(UserInfo userInfo) {
                //设置缓存
                CacheUtils.setCache(userId, new Gson().toJson(userInfo));
                SPUtils.putUserPlate(UserCenterActivity.this, userInfo.getPlateNum());
                GlobalApplication.membership = userInfo.getMembership();
                SPUtils.putMembership(UserCenterActivity.this, userInfo.getMembership());

                resetUserBar();
                mData = userInfo;
                setData(userInfo);
            }
        });
    }

    /**
     * 设置数据
     *
     * @param user
     */
    private void setData(UserInfo user) {
        //设置页面数据
        mTvUsername
            .setText(StringUtils.notEmpty(user.getDisplayName()) ? user.getDisplayName() : user.getPhoneNo());
        findViewById(R.id.goldHat).setVisibility(user.isMemberShip() ? View.VISIBLE : View.GONE);
        Glide.with(UIUtils.getContext()).load(user.getPhotoUrl()).placeholder(R.mipmap.wd_tx)
            .transform(new GlideCircleTransform(this)).dontAnimate().error(R.mipmap.wd_tx).into(mIvUserHead);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.uc_activity_ifr, R.anim.uc_activity_otl);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        LogUtils.e("UserCenterActivity onNewIntent call-----------------------");
    }

    /**
     * 关闭默认动画
     */
    @Override
    public boolean finishWithAnim() {
        return false;
    }
}
