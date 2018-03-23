package com.nlinks.parkdemo.module.appointment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PayAPI;
import com.nlinks.parkdemo.entity._req.PayOrderReq;
import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;
import com.nlinks.parkdemo.entity.pay.WalletMoney;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.PaymentSuccessExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.PaymentSuccessActivity;
import com.nlinks.parkdemo.utils.PayUtil;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

/**
 * 预定成功
 * Created by Dell on 2017/05/06.
 */
public class AppointTimeoutPaymentActivity extends BaseActivity implements View.OnClickListener {

    public static final String SERIALIZABLE_APPOINT_RECORD = "SERIALIZABLE_APPOINT_RECORD";

    private AppointmentRecord mAppointmentRecord;

    private TextView mTvParkName;
    private TextView mTvAppointmentTime;
    private TextView mTvOutTime;
    private TextView mTvSpendTime;
    private TextView mTvCarCode;
    //	private TextView mTvOrderPayment;
//	private TextView mTvDiscount;
    private TextView mTvActualPayment;
    private ImageView mIvCheckWalletStatus;
    private TextView mTvPayWallet;
    private ImageView mIvCheckWechatStatus;
    //	private TextView mTvPayWechat;
    private ImageView mIvCheckAlipayStatus;
//	private TextView mTvPayAlipay;

    private int mCurrent = 0;
    private ImageView mLast = null;
    private double mFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_timeout_payment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppointmentRecord = (AppointmentRecord) extras.getSerializable(SERIALIZABLE_APPOINT_RECORD);
        }

        initView();
        updateUI();

        getBalance();
    }

    private void initView() {
        Button mBtnPayNow = findViewById(R.id.btn_pay_now);
        mTvParkName =  findViewById(R.id.tv_park_name);
        mTvAppointmentTime =  findViewById(R.id.tv_appointment_time);
        mTvOutTime =  findViewById(R.id.tv_out_time);
        mTvSpendTime =  findViewById(R.id.tv_spend_time);
        mTvCarCode =  findViewById(R.id.tv_car_code);
//		mTvOrderPayment =  findViewById(R.id.tv_order_payment);
//		mTvDiscount =  findViewById(R.id.tv_discount);
        mTvActualPayment =  findViewById(R.id.tv_actual_payment);
        mIvCheckWalletStatus =  findViewById(R.id.iv_check_wallet_status);
        mTvPayWallet =  findViewById(R.id.tv_pay_wallet);
        mIvCheckWechatStatus =  findViewById(R.id.iv_check_wechat_status);
        TextView tvPayWechat =  findViewById(R.id.tv_pay_wechat);
        mIvCheckAlipayStatus =  findViewById(R.id.iv_check_alipay_status);
        TextView tvPayAlipay =  findViewById(R.id.tv_pay_alipay);

        mLast = mIvCheckWalletStatus;
        mBtnPayNow.setOnClickListener(this);
        mIvCheckAlipayStatus.setOnClickListener(this);
        mIvCheckWalletStatus.setOnClickListener(this);
        mIvCheckWechatStatus.setOnClickListener(this);
//		mTvDiscount.setOnClickListener(this);
        mTvPayWallet.setOnClickListener(this);
        tvPayAlipay.setOnClickListener(this);
        tvPayWechat.setOnClickListener(this);
        findViewById(R.id.tvt_pay_wallet).setOnClickListener(this);
        findViewById(R.id.tvt_pay_wechat).setOnClickListener(this);
        findViewById(R.id.tvt_pay_alipay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_now:
                payNow();
                break;
            case R.id.tv_pay_alipay:
            case R.id.tvt_pay_alipay:
            case R.id.iv_check_alipay_status:
                check(mIvCheckAlipayStatus, 2);
                break;
            case R.id.tv_pay_wechat:
            case R.id.tvt_pay_wechat:
            case R.id.iv_check_wechat_status:
                check(mIvCheckWechatStatus, 1);
                break;
            case R.id.tv_pay_wallet:
            case R.id.tvt_pay_wallet:
            case R.id.iv_check_wallet_status:
                check(mIvCheckWalletStatus, 0);
                break;
//			case R.id.tv_discount:
//				coupon();
//				break;
            default:
                break;
        }
    }

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 1 && resultCode == RESULT_OK && data != null){
//			mCoupon = data.getStringExtra(ParkingCouponActivity.RESULT_STRING_COUPON_ID);
//			if (!TextUtils.isEmpty(mCoupon)){
//				//mFee = ;
//			}
//		}
//	}

    private void updateUI() {
        if (mAppointmentRecord != null) {
            mTvParkName.setText(mAppointmentRecord.getName());
            mFee = mAppointmentRecord.getChargeOut();
            mTvAppointmentTime.setText(mAppointmentRecord.getLeaveTime());
            mTvOutTime.setText(mAppointmentRecord.getActualTime());
            //相差分钟数
            int compare = mAppointmentRecord.getTimeOut();
            //超过不足一小时记一小时
            mTvSpendTime.setText("" + Math.abs(compare % 60 == 0 ? compare / 60 : compare / 60 + 1));
            mTvCarCode.setText(mAppointmentRecord.getPlateNum());
//			mTvOrderPayment.setText(mAppointPark.getAppointMoney());
//			mTvDiscount.setText(mCoupon);
            mTvActualPayment.setText(StringUtils.formatMoney(mFee));
        }
    }

    private void check(ImageView last, int checked) {
        if (mLast != null) mLast.setImageResource(R.mipmap.check_circle_normal);
        mCurrent = checked;
        (mLast = last).setImageResource(R.mipmap.check_circle_selected);
    }

//	private void coupon() {
//		Bundle bundle = new Bundle();
//		bundle.putBoolean(ParkingCouponActivity.BOOLEAN_SELECTABLE, true);
//		jumpToForResult(ParkingCouponActivity.class, 1, bundle);
//	}

    private void getBalance() {
        String userId = SPUtils.getUserId(this, "");
        if (StringUtils.isEmpty(userId)) return;

         HttpHelper.getRetrofit().create(PayAPI.class).getBalance(userId)
             .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<WalletMoney>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(WalletMoney walletMoney) {
                String money = StringUtils.formatMoney(walletMoney.getUsableMoney());
                mTvPayWallet.setText("余额：(" + money + "元)");
            }
        });
    }

    private void payNow() {
        PayOrderReq orderReq = new PayOrderReq();
        if (mAppointmentRecord != null) orderReq.setOrderCode(mAppointmentRecord.getId());
        //orderReq.setCouponDetailIds(mCoupon);
        orderReq.setPayType(2);
        orderReq.setTotalFee(mFee);
        orderReq.setOrderDesc("预约支付");
        orderReq.setOrderDetail("预约支付" + mFee);
        orderReq.setOrderAttach(getResources().getString(R.string.app_name));
        orderReq.setUserId(SPUtils.getUserId(this, null));

        switch (mCurrent) {
            case 2:
                PayUtil.alipay(this, orderReq, true, payResult -> {
                    if (PayUtil.alipaySuccess(payResult)) {
                        onPaySucceed();
                    } else {
                        UIUtils.showToast("支付失败！");
                    }
                });
                break;
            case 1:
                PayUtil.weixin(this, orderReq, true, (errCode, errStr) -> {
                    UIUtils.showToast(errStr);
                    if (PayUtil.weixinSuccess(errCode)) onPaySucceed();
                });
                break;
            case 0:
                PayUtil.wallet(this, orderReq, true, (code, message) -> {
                    if (code == 200) {
                        onPaySucceed();
                    } else {
                        UIUtils.showToast(message);
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
        extra.setPayMoney(StringUtils.formatMoney(mFee));
        PaymentSuccessActivity.start(this, extra);
        finish();
    }
}
