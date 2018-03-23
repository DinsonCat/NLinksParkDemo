package com.nlinks.parkdemo.module.park;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PayAPI;
import com.nlinks.parkdemo.entity._req.PayOrderReq;
import com.nlinks.parkdemo.entity._req.SubmitMemOrder;
import com.nlinks.parkdemo.entity.park.ParkRecordPay;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.entity.pay.MemberOrder;
import com.nlinks.parkdemo.entity.pay.PlatformActivity;
import com.nlinks.parkdemo.entity.pay.WalletMoney;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.modle.PaymentSuccessExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.PaymentSuccessActivity;
import com.nlinks.parkdemo.module.coupon.ParkingCouponActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.PayUtil;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

import java.util.ArrayList;

/**
 * 预定成功
 * Created by Dell on 2017/05/06.
 */
public class ParkingPaymentActivity extends BaseActivity implements View.OnClickListener {

    //布局的类型
    public static final int LAYOUT_PARKRECORD = 1;
    public static final int LAYOUT_PROMOTION = 2;//payType(188会员升级支付)

    private static final String PARKRECORD_PAY = "parkrecordpay";
    private static final String PLATFORM_ACTIVITY = "platform_activity";
    private static final String LAYOUT_TYPE = "layout_type";
    private static final String STRING_PREFERENTIAL_KEY = "STRING_PREFERENTIAL_KEY";//优惠金额

    private static final int REQUEST_CODE = 100;//优惠金额

    private RadioButton mRbWalletPay;
    private RadioGroup mRgPayway;
    private ArrayList<PlatformActivity> mPlatformActivities;

    /**
     * 会员升级调用这个方法（用于网页）
     */
    public static void startForResult(Activity activity, ParkRecordPay recordPay, int layoutType,
                                      int requestCode, double couponCount) {
        Intent intent = new Intent(activity, ParkingPaymentActivity.class);
        intent.putExtra(PARKRECORD_PAY, recordPay);
        intent.putExtra(LAYOUT_TYPE, layoutType);
        // intent.putExtra(PAY_TYPE, payType);
        intent.putExtra(STRING_PREFERENTIAL_KEY, couponCount);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 停车记录调用这个方法
     */
    public static void startForResult(Activity activity, ParkRecordPay recordPay, int layoutType,
                                      int requestCode, double couponCount, ArrayList<PlatformActivity> list) {
        Intent intent = new Intent(activity, ParkingPaymentActivity.class);
        intent.putExtra(PARKRECORD_PAY, recordPay);
        intent.putExtra(LAYOUT_TYPE, layoutType);
        intent.putParcelableArrayListExtra(PLATFORM_ACTIVITY, list);
        intent.putExtra(STRING_PREFERENTIAL_KEY, couponCount);
        activity.startActivityForResult(intent, requestCode);
    }

    // private int mPayType;
    private int mCurrentLayout;
    private ParkRecordPay mRecordPay;
    // private String mCouponId = "";

    private double mCouponCount = 0;//优惠金额
    private double mFee;//总金额


    private Button mBtnPayNow;
    private TextView mTvParkName;
    private RelativeLayout mRlParkNameInfo;

    private TextView mTvSpendTime;
    private TextView mTvCarCode;
    private TextView mTvOrderPayment;
    private TextView mTvDiscount;
    private TextView mTvActualPayment;

    private ImageView mLast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_payment);

        //mPayType = getIntent().getIntExtra(PAY_TYPE, -1);
        mCurrentLayout = getIntent().getIntExtra(LAYOUT_TYPE, 2);
        mRecordPay = getIntent().getParcelableExtra(PARKRECORD_PAY);
        mCouponCount = getIntent().getDoubleExtra(STRING_PREFERENTIAL_KEY, 0);

        initView();
        updateUI();

        getBalance();
    }

    private void initView() {
        mBtnPayNow = findViewById(R.id.btn_pay_now);
        mTvParkName = findViewById(R.id.tv_park_name);
        mRlParkNameInfo = findViewById(R.id.rl_park_name_info);

        mTvSpendTime = findViewById(R.id.tv_spend_time);
        mTvCarCode = findViewById(R.id.tv_car_code);
        mTvOrderPayment = findViewById(R.id.tv_order_payment);
        mTvDiscount = findViewById(R.id.tv_discount);
        mTvActualPayment = findViewById(R.id.tv_actual_payment);

        mBtnPayNow.setOnClickListener(this);

        mRgPayway = findViewById(R.id.rg_payway);
        mRbWalletPay = findViewById(R.id.rb_wallet_pay);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_now:
                payNow();
                break;
            case R.id.tv_discount:
                coupon();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ParkingCoupon coupon = data.getParcelableExtra(ParkingCouponActivity.RESULT_PARCELABLE_COUPON);

            if (coupon == null) return;

            //计算优惠金额
            calculateCoupon(coupon);
        }

        //跳转支付成功界面后直接finish
        if (requestCode == CODE_SUCCESS) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void updateUI() {
        switch (mCurrentLayout) {
            case LAYOUT_PARKRECORD:
                if (mRecordPay != null) {
                    mTvParkName.setText(mRecordPay.getParkName());
                    mFee = mRecordPay.getShouldPay();
                    mTvSpendTime.setText(mRecordPay.getSpendTime());
                    mTvCarCode.setText(mRecordPay.getCarCode());
                    mTvOrderPayment.setText(StringUtils.formatMoney(mRecordPay.getShouldPay()));

                    mTvDiscount.setText(mCouponCount == 0 ? "无优惠券" : StringUtils.formatMoney(mCouponCount));
                    mTvDiscount.setOnClickListener(null);
                    mTvDiscount.setCompoundDrawables(null, null, null, null);
//                    mTvDiscount.setText(mCouponCount);
                    double totle = mFee - mCouponCount;
                    if (totle < 0) totle = 0;
                    mTvActualPayment.setText(StringUtils.formatMoney(totle));
                    if (totle == 0) {
                        //实际支付为0元的话，只能用钱包支付
                        findViewById(R.id.rb_ali).setVisibility(View.GONE);
                        findViewById(R.id.rb_wechat).setVisibility(View.GONE);
                    }
                }


                mPlatformActivities = getIntent().getParcelableArrayListExtra(PLATFORM_ACTIVITY);
                if (mPlatformActivities != null && !mPlatformActivities.isEmpty()) {
                    //植入平台活动
                    LinearLayout platformContainer = findViewById(R.id.llPlatformActivity);
                    createPlatformActivity(platformContainer, mPlatformActivities);
                }
                break;

            case LAYOUT_PROMOTION:
                if (mRecordPay != null) {
                    mTvDiscount.setOnClickListener(this);
                    Drawable drawable = ActivityCompat.getDrawable(this, R.mipmap.right);
                    /// 这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mTvDiscount.setCompoundDrawables(null, null, drawable, null);
                    mTvParkName.setVisibility(View.GONE);
                    mRlParkNameInfo.setVisibility(View.GONE);

                    mFee = mRecordPay.getShouldPay();
                    mTvOrderPayment.setText(StringUtils.formatMoney(mRecordPay.getShouldPay()));

                    double total = mFee - mCouponCount;
                    if (total < 0) total = 0;
                    mTvActualPayment.setText(StringUtils.formatMoney(total));

                    //升级会员卡选择最佳优惠券
                    CouponValidateExtra extra = new CouponValidateExtra();
                    extra.setPayType(mRecordPay.getPayType());
                    extra.setConsume(mRecordPay.getShouldPay());
                    extra.setParkCode(mRecordPay.getParkCode());
                    NlinksParkUtils.chooseGreatCoupon(this, extra, this::calculateCoupon);
                }
                break;
        }
    }

    /**
     * 点击进入优惠券
     */
    private void coupon() {
        if (mRecordPay != null) {
            CouponValidateExtra extra = new CouponValidateExtra();
            extra.setConsume(mRecordPay.getShouldPay());
            extra.setPayType(mRecordPay.getPayType());
            extra.setParkCode(mRecordPay.getParkCode());
            ParkingCouponActivity.startForResult(this, extra, REQUEST_CODE);
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

    private void payNow() {
        final PayOrderReq orderReq = createPayOrderReq();//获取上传的实体

        if (orderReq == null) {
            UIUtils.showToast("订单错误，请返回重试");
            return;
        }

        if (orderReq.getPayType() == NlinksParkUtils.PayType.GOLD_MEMBER.getValue()) {
            SubmitMemOrder entity = new SubmitMemOrder();
            entity.setPayType(orderReq.getPayType());
            entity.setCouponDetailIds(orderReq.getCouponDetailIds());
            entity.setUserId(orderReq.getUserId());
            HttpHelper.getRetrofit().create(PayAPI.class).submitMemOrder(entity)
                .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<MemberOrder>(this) {
                @NonNull
                @Override
                public void onHandleSuccess(MemberOrder memberOrder) {
                    orderReq.setCouponMoney(memberOrder.getCouponMoney());
                    orderReq.setOrderCode(memberOrder.getOrderCode());
                    choosePayway(orderReq);
                }
            });
            return;
        }
        choosePayway(orderReq);
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

    private static final int CODE_SUCCESS = 462;

    /**
     * 支付成功统一处理
     */
    private void onPaySucceed() {
        PaymentSuccessExtra extra = new PaymentSuccessExtra();
        extra.setPayMoney(StringUtils.formatMoney(mRecordPay.getLastMoney()));
        if (mCurrentLayout == LAYOUT_PARKRECORD && mPlatformActivities != null) {
            extra.setPlatformActivityList(mPlatformActivities);
            extra.setAttention(UIUtils.getString(R.string.park_over_attention));

            //植入优惠券
            if (mCouponCount != 0) {
                mPlatformActivities.add(new PlatformActivity(StringUtils.formatMoney(mCouponCount),
                    "优惠券", "平台活动"));
            }
        }
        PaymentSuccessActivity.startForResult(this, extra, CODE_SUCCESS);
    }


    private PayOrderReq createPayOrderReq() {
        if (StringUtils.isEmpty(SPUtils.getUserId(this)))
            return null;
        PayOrderReq orderReq = new PayOrderReq();
        orderReq.setUserId(SPUtils.getUserId(this));
        orderReq.setOrderAttach(getResources().getString(R.string.app_name));
        orderReq.setPayType(mRecordPay.getPayType());
        orderReq.setCouponDetailIds(mRecordPay.getCouponId());
        orderReq.setOrderCode(mRecordPay.getOrderCode());
        orderReq.setTotalFee(mFee - mCouponCount);
        String str = NlinksParkUtils.formatTransactDesc(mRecordPay.getPayType());
        orderReq.setOrderDesc(str);
        orderReq.setOrderDetail(str + mFee);
        return orderReq;
    }

    private void calculateCoupon(ParkingCoupon coupon) {
        mRecordPay.setCouponId(coupon.getId());
        mCouponCount = NlinksParkUtils.getCouponMoney(coupon, mFee);
        mTvDiscount.setText(StringUtils.formatMoney(mCouponCount));
        mTvActualPayment.setText(StringUtils.formatMoney(mFee - mCouponCount));
        //升级蜻蜓会员只能选择钱包支付
        if (mRecordPay.getPayType() == NlinksParkUtils.PayType.GOLD_MEMBER.getValue() && mCouponCount == mFee) {
            findViewById(R.id.rb_ali).setVisibility(View.GONE);
            findViewById(R.id.rb_wechat).setVisibility(View.GONE);
            mRbWalletPay.setChecked(true);
        }
    }

    /**
     * 创建平台活动
     */
    private void createPlatformActivity(LinearLayout container, ArrayList<PlatformActivity> activityLists) {
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


