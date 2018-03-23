package com.nlinks.parkdemo.module.appointment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PayAPI;
import com.nlinks.parkdemo.entity._req.PayOrderReq;
import com.nlinks.parkdemo.entity.appointment.AppointPark;
import com.nlinks.parkdemo.entity.pay.PlatformActivity;
import com.nlinks.parkdemo.entity.pay.WalletMoney;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.PaymentSuccessExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.PaymentSuccessActivity;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.PayUtil;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

import java.util.List;

/**
 * 预定成功
 * Created by Dell on 2017/05/06.
 */
public class AppointmentPaymentActivity extends BaseActivity {

    public static final String PARCELABLE_APPOINT_PARK = "PARCELABLE_APPOINT_PARK";
    public static final String STRING_CAR_CODE = "STRING_CAR_CODE";
    public static final String STRING_LEAVE_TIME = "STRING_LEAVE_TIME";
    public static final String STRING_NAME = "STRING_NAME";
    public static final String STRING_PREFERENTIAL_KEY = "STRING_PREFERENTIAL_KEY";//优惠金额
    public static final String STRING_COUPON_ID = "STRING_COUPON_ID";//优惠金额

    private String mName, mAddress;
    private AppointPark mAppointPark;
    private String mCouponId = "", mCouponCount, mFee = "";

    private RadioButton mRbWalletPay;//余额支付
    private RadioGroup mRgPayWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_payment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initView(extras);
            getBalance();
        } else {
            finish();
            UIUtils.showToast("参数错误");
        }
    }

    /**
     * 设置textview的文字
     *
     */
    private void setTvText(int id, String text) {
        TextView tv =   findViewById(id);
        tv.setText(text);
    }

    private void initView(Bundle extras) {

        mAppointPark = extras.getParcelable(PARCELABLE_APPOINT_PARK);
        mName = extras.getString(STRING_NAME);
        String leaveTime = extras.getString(STRING_LEAVE_TIME);
        String carCode = extras.getString(STRING_CAR_CODE);
        mCouponCount = extras.getString(STRING_PREFERENTIAL_KEY);
        mCouponId = extras.getString(STRING_COUPON_ID);


        if (mAppointPark != null) {

            mFee = mAppointPark.getAppointMoney();

            setTvText(R.id.tv_park_name, mName);
            setTvText(R.id.tv_out_time, leaveTime);
            setTvText(R.id.tv_appointment_time, mAppointPark.getAppointTime());
            //相差分钟数
            int compare = DateUtils.compareTimeUseDefaultFormat(leaveTime, mAppointPark.getAppointTime()) / 60;
            //超过不足一小时记一小时
            setTvText(R.id.tv_spend_time, Math.abs(compare % 60 == 0 ? compare / 60 : compare / 60 + 1) + "小时");

            setTvText(R.id.tv_car_code, carCode);
            setTvText(R.id.tv_order_payment, mAppointPark.getAppointMoney());

            setTvText(R.id.tv_discount, StringUtils.isEmpty(mCouponCount) ? "无优惠券" : mCouponCount);

            double totle = Double.valueOf(mAppointPark.getAppointMoney()) - Double.valueOf(mCouponCount);
            if (totle < 0) totle = 0;
            mFee = StringUtils.formatMoney(totle);
            setTvText(R.id.tv_actual_payment, mFee);

            if (totle == 0) {
                //实际支付为0元的话隐藏支付方式
                findViewById(R.id.rb_wechat).setVisibility(View.GONE);
                findViewById(R.id.rb_ali).setVisibility(View.GONE);
            }

            mRgPayWay = findViewById(R.id.rg_payway);


            //创建平台活动
            LinearLayout llContainer = findViewById(R.id.llPlatformActivity);
            createPlatformActivity(llContainer, mAppointPark.getActivityList());
        }
    }

    private void getBalance() {
        String userId = SPUtils.getUserId(this, "");
        if (StringUtils.isEmpty(userId)) return;

        HttpHelper.getRetrofit().create(PayAPI.class).getBalance(userId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<WalletMoney>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(WalletMoney walletMoney) {
                String money = StringUtils.formatMoney(walletMoney.getUsableMoney());
                if (mRbWalletPay == null)
                    mRbWalletPay =  findViewById(R.id.rb_wallet_pay);
                mRbWalletPay.setText("钱包支付\t\t余额：(" + money + "元)");
            }
        });
    }

    /**
     * 点击支付
     *
     * @param view button
     */
    public void doPayFor(View view) {
        payNow();
    }

    private void payNow() {
        PayOrderReq orderReq = new PayOrderReq();
        if (mAppointPark != null) orderReq.setOrderCode(mAppointPark.getOrderCode());
        orderReq.setCouponDetailIds(mCouponId);
        orderReq.setCouponMoney(mCouponCount);
        orderReq.setPayType(3);
        orderReq.setTotalFee(mFee);
        orderReq.setOrderDesc("预约支付");
        orderReq.setOrderDetail("预约支付" + mFee);
        orderReq.setOrderAttach(getResources().getString(R.string.app_name));
        orderReq.setUserId(SPUtils.getUserId(this, null));

        LogUtils.e(orderReq.toString());

        switch (mRgPayWay.getCheckedRadioButtonId()) {
            case R.id.rb_ali:
                PayUtil.alipay(this, orderReq, true, new PayUtil.AliCallback() {
                    @Override
                    public void onAliResult(PayUtil.AliPayResult payResult) {
                        if (PayUtil.alipaySuccess(payResult)) {
                            onPaySuccess();
                        } else {
                            UIUtils.showToast("支付失败！");
                        }
                    }
                });
                break;
            case R.id.rb_wechat:
                PayUtil.weixin(this, orderReq, true, new PayUtil.WXCallback() {
                    @Override
                    public void onWXResult(int errCode, String errStr) {
                        UIUtils.showToast(errStr);
                        if (PayUtil.weixinSuccess(errCode)) onPaySuccess();
                    }
                });
                break;
            case R.id.rb_wallet_pay:
                PayUtil.wallet(this, orderReq, true, new PayUtil.WalletCallback() {
                    @Override
                    public void onWalletResult(int code, String message) {
                        if (code == 200) {
                            onPaySuccess();
                        } else {
                            UIUtils.showToast(message);
                        }
                    }
                });
                break;
        }
    }

    /**
     * 支付成功统一处理
     */
    private void onPaySuccess() {
        PaymentSuccessExtra extra = new PaymentSuccessExtra();
        extra.setAttention(String.format("定位停放车位: %S", mAppointPark.getStallCode()));
        extra.setPayMoney(StringUtils.formatMoney(mAppointPark.getRealMoney()));
        if (mAppointPark.getActivityList() != null) {
            extra.setPlatformActivityList(mAppointPark.getActivityList());
            if (StringUtils.isNotEmpty(mCouponCount))
                extra.getPlatformActivityList().add(new PlatformActivity(StringUtils.formatMoney(mCouponCount),
                    "优惠券", "平台活动"));
        }
        PaymentSuccessActivity.start(this, extra);
        finish();
    }


    /**
     * 平台活动添加条目，待抽取
     */
    private void createPlatformActivity(LinearLayout container, List<PlatformActivity> activityLists) {
        if (activityLists == null || activityLists.isEmpty()) return;
        container.setVisibility(View.VISIBLE);
        for (PlatformActivity activityList : activityLists) {
            RelativeLayout rl = new RelativeLayout(this);
            int padding = UIUtils.getDimen(R.dimen.spacing_medium);
            rl.setPadding(padding, 0, padding, 0);
            rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(50)));
            rl.setBackgroundColor(UIUtils.getColor(R.color.white));

            TextView child1 = new TextView(this);
            Drawable drawable = UIUtils.getDrawable(R.drawable.ic_discounts);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            child1.setCompoundDrawables(null, null, drawable, null);

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.CENTER_VERTICAL);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            child1.setLayoutParams(params1);
            int color = UIUtils.getColor(R.color.text_secondary);
            child1.setTextColor(color);
            child1.setText(activityList.getActivityName());
            child1.setTextSize(16);

            TextView child2 = new TextView(this);
            child2.setText(activityList.getActivityValue());
            child2.setTextColor(color);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            child2.setLayoutParams(params);

            rl.addView(child1);
            rl.addView(child2);
            container.addView(rl);
        }
    }
}
