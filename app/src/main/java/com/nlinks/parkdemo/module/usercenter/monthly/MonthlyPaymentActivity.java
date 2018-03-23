package com.nlinks.parkdemo.module.usercenter.monthly;

import android.content.Context;
import android.content.Intent;
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
import com.nlinks.parkdemo.entity.pay.PlatformActivity;
import com.nlinks.parkdemo.entity.pay.WalletMoney;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.PaymentSuccessExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.PaymentSuccessActivity;
import com.nlinks.parkdemo.module.usercenter.monthly.model.MonthlyPay;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.PayUtil;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

import java.util.List;

public class MonthlyPaymentActivity extends BaseActivity {
    private static final String EXTRA_BEAN = "extra_bean";
    private static final String EXTRA_REQ = "extra_req";
    private MonthlyPay mData;
    private RadioGroup mRgPayway;
    private RadioButton mRbWalletPay;
    private PayOrderReq mOrderReq;

    public static void start(Context context, MonthlyPay bean, PayOrderReq req) {
        Intent starter = new Intent(context, MonthlyPaymentActivity.class);
        starter.putExtra(EXTRA_BEAN, bean);
        starter.putExtra(EXTRA_REQ, req);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_payment);

        initUI();
        getBalance();
    }

    private void initUI() {
        mData = getIntent().getParcelableExtra(EXTRA_BEAN);

        setTvText(R.id.tvParkName, mData.getParkName() + " - " + mData.getParkRuleName());
        setTvText(R.id.tvPlate, mData.getPlateNum());
        setTvText(R.id.tvStartTime, mData.getStartTime());
        setTvText(R.id.tvEndTime, mData.getEndTime());
        setTvText(R.id.tvPrice, mData.getPrice() + "元/月");
        setTvText(R.id.tvMonthCount, mData.getTimeCount() + "个月");


        double orderMoney = Double.parseDouble(mData.getPrice()) * mData.getTimeCount();
        String payMoney = StringUtils.formatMoney(orderMoney - mData.getCouponMoney());
        setTvText(R.id.tvPayMoney, payMoney + "元");

        if (mData.getCouponMoney() != 0.0) {
            findViewById(R.id.rlCoupon).setVisibility(View.VISIBLE);
            setTvText(R.id.tvOrderMoney, StringUtils.formatMoney(orderMoney) + "元");
            setTvText(R.id.tvCouponMoney, StringUtils.formatMoney(mData.getCouponMoney()) + "元");
        }

        LinearLayout platformContainer = findViewById(R.id.llPlatformActivity);
        createPlatformActivity(platformContainer, mData.getActivityLists());

        findViewById(R.id.btnAction).setOnClickListener(mListener);
        mRgPayway = findViewById(R.id.rgPayway);
        mRbWalletPay = findViewById(R.id.rb_wallet_pay);
    }

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

    private void getBalance() {
        String userId = SPUtils.getUserId(this, "");
        if (StringUtils.isEmpty(userId)) return;

        HttpHelper.getRetrofit().create(PayAPI.class).getBalance(userId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<WalletMoney>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(WalletMoney walletMoney) {
                String money = StringUtils.formatMoney(walletMoney.getUsableMoney());
                mRbWalletPay.setText("钱包支付\t\t余额：(" + money + "元)");
            }
        });
    }


    private void setTvText(int id, String str) {
        TextView tv = findViewById(id);
        tv.setText(str);
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            payNow();
        }
    };

    private void payNow() {
        mOrderReq = getIntent().getParcelableExtra(EXTRA_REQ);
        if (mOrderReq == null) {
            UIUtils.showToast("订单错误，请返回重试");
            return;
        }
        choosePayway(mOrderReq);
    }


    private void choosePayway(PayOrderReq orderReq) {
        LogUtils.e(orderReq.toString());
        switch (mRgPayway.getCheckedRadioButtonId()) {
            case R.id.rb_ali:
                PayUtil.alipay(this, orderReq, true, new PayUtil.AliCallback() {
                    @Override
                    public void onAliResult(PayUtil.AliPayResult payResult) {
                        if (PayUtil.alipaySuccess(payResult)) {
                            //支付成功统一处理
                            onPaySucceed();
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
                        if (PayUtil.weixinSuccess(errCode)) {
                            //支付成功统一处理
                            onPaySucceed();
                        } else {
                            UIUtils.showToast(errStr);
                        }
                    }
                });
                break;
            case R.id.rb_wallet_pay:
                PayUtil.wallet(this, orderReq, true, new PayUtil.WalletCallback() {
                    @Override
                    public void onWalletResult(int code, String message) {
                        if (code == 200) {
                            //支付成功统一处理
                            onPaySucceed();
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
    private void onPaySucceed() {
        PaymentSuccessExtra extra = new PaymentSuccessExtra();
        extra.setPayMoney(StringUtils.formatMoney(mData.getLastMoney()));
        if (mData.getActivityLists() != null) {
            extra.setPlatformActivityList(mData.getActivityLists());

            //如果包月支持优惠券的话平台活动要植入优惠券
            if (mData.getCouponMoney() != 0)
                extra.getPlatformActivityList().add(new PlatformActivity(StringUtils.formatMoney(mData.getCouponMoney()),
                    "优惠券", "平台活动"));
        }

        PaymentSuccessActivity.startForResult(this, extra, REQUEST_CODE);
    }

    private static final int REQUEST_CODE = 0x1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            MonthlyListActivity.start(this);
        }
    }
}
