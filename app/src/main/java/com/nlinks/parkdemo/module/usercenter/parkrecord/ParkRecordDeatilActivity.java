package com.nlinks.parkdemo.module.usercenter.parkrecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ParkAPI;
import com.nlinks.parkdemo.api.PayOrderAPI;
import com.nlinks.parkdemo.entity._req.ParkRecordReq;
import com.nlinks.parkdemo.entity.park.ParkRecord;
import com.nlinks.parkdemo.entity.park.ParkRecordOrder;
import com.nlinks.parkdemo.entity.park.ParkRecordPay;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.coupon.ParkingCouponActivity;
import com.nlinks.parkdemo.module.park.ParkingPaymentActivity;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

public class ParkRecordDeatilActivity extends BaseActivity {

    private static final String EXTRA_PARK = "park";
    public static final String EXTRA_PARK_RECORD_ID = "PARK_RECORD_ID";

    private ParkRecord mDatas;

    private static final int KEY_NOPAY = 100;
    private static final int KEY_NOLEAVE = 101;
    private static final int KEY_ALREADY = 102;
    private static final int KEY_ARREARAGE = 103;

    private static final int REQUEST_PAY = 108;
    private static final int REQUEST_COUPON = 109;

    private String mCouponId;//优惠券id
    private TextView mTvPayMoney;
    private ParkingCoupon mCoupon;//优惠券

    public static void start(Activity activity, ParkRecord parkRecord, int requestCode) {
        Intent intent = new Intent(activity, ParkRecordDeatilActivity.class);
        intent.putExtra(EXTRA_PARK, parkRecord);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Context context, ParkRecord parkRecord) {
        Intent intent = new Intent(context, ParkRecordDeatilActivity.class);
        intent.putExtra(EXTRA_PARK, parkRecord);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_record_deatil);


        initUI();

        getExtraData();
    }


    private void initUI() {


    }

    private void getExtraData() {
        mDatas = getIntent().getParcelableExtra(EXTRA_PARK);
        if (mDatas != null) {
            //自带对象
            int state = getCurrentState();
            if (state != 0)
                setData(state);
        } else {
            //通过停车场id，请求数据
            String id = getIntent().getStringExtra(EXTRA_PARK_RECORD_ID);
            HttpHelper.getRetrofit().create(ParkAPI.class).parkRecordByID(id)
                .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ParkRecord>(this, false) {
                @NonNull
                @Override
                public void onHandleSuccess(ParkRecord parkRecord) {
                    if (parkRecord == null) {
                        onBackPressed();
                        return;
                    }
                    mDatas = parkRecord;
                    int state = getCurrentState();
                    if (state != 0) {
                        setData(state);
                    }
                }
            });
        }
    }

    /**
     * 设置页面数据
     */
    private void setData(int flag) {
        //处理公共
        TextView tvPlate = findViewById(R.id.tv_plate);
        TextView tvParkName = findViewById(R.id.tv_park_name);
        TextView tvParkAddress = findViewById(R.id.tv_park_address);
        TextView tvChargeStandard = findViewById(R.id.tv_charge_standard);
        TextView tvMsgIn = findViewById(R.id.tv_msg_in);
        TextView tvMsgTime = findViewById(R.id.tv_msg_time);
        TextView tvMsgParkMoney = findViewById(R.id.tv_msg_park_money);
        mTvPayMoney = findViewById(R.id.tv_pay_money);//未支付显示支付金额

        tvPlate.setText(mDatas.getPlateNum());//设置车牌号
        tvParkName.setText(mDatas.getParkName());//设置停车场名字

        //设置地址
        if (StringUtils.isEmpty(mDatas.getAddress()))
            tvParkAddress.setVisibility(View.GONE);
        else
            tvParkAddress.setText(mDatas.getAddress());

        tvChargeStandard.setText(StringUtils.isEmpty(mDatas.getChargeStandard()) ? "暂无价格信息" : mDatas.getChargeStandard());//设置收费标准
        tvMsgIn.setText(mDatas.getInTime());//设置入场时间
        tvMsgTime.setText(DateUtils.formatSeconds(mDatas.getParkSeconds()));//设置停车时长
        tvMsgParkMoney.setText("￥" + StringUtils.formatMoney(mDatas.getConsume()));//设置停车费用

        //
        switch (flag) {
            //欠费
            case KEY_ARREARAGE:
                //设置出场时间
                setMsg(R.id.ll_msg_out, R.id.tv_msg_out, mDatas.getOutTime());

            case KEY_NOPAY: //代缴，预缴

                //隐藏停车费用显示已支付
                if (mDatas.getHasPay() != 0)
                    setMsg(R.id.ll_msg_haspay, R.id.tv_msg_haspay, "￥" + StringUtils.formatMoney(mDatas.getHasPay()));
                findViewById(R.id.ll_msg_park_money).setVisibility(View.GONE);
//                //优惠券金额
//                if (mDatas.getCouponMoney() != 0)
//                    setMsg(R.id.ll_msg_coupon_money, R.id.tv_msg_coupon_money, "￥" + StringUtils.formatMoney(mDatas.getCouponMoney()));

                //还需支付
                findViewById(R.id.ll_footer).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_pay_now).setVisibility(View.VISIBLE);
                calculateMoney(mDatas, mTvPayMoney);//计算金额


                //顶部金额和布局显示
                findViewById(R.id.ll_msg_head_money).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_big_money)).setText(StringUtils.formatMoney(mDatas.getConsume()));

                //显示优惠券
                LinearLayout ll_layout = findViewById(R.id.ll_msg_coupon_money);
                ll_layout.setVisibility(View.VISIBLE);

                //选择最佳优惠券
                CouponValidateExtra extra = new CouponValidateExtra();
                extra.setParkCode(mDatas.getParkCode());
                extra.setPayType(mDatas.getPayType());
                extra.setConsume(mDatas.getConsume());
                NlinksParkUtils.chooseGreatCoupon(this, extra, theGreat -> {
                    //显示折扣券
                    mCoupon = theGreat;
                    mCouponId = theGreat.getId();
                    setMsg(R.id.ll_msg_coupon_money, R.id.tv_msg_coupon_money, formatCouponMoney(mCoupon));
                    calculateMoney(mDatas, theGreat, mTvPayMoney);
                });

                ll_layout.setOnClickListener(v -> turnToCoupon(mDatas.getWaitPay()));

                break;
            //等待离场
            case KEY_NOLEAVE:
                String outTime = mDatas.getWaitOutTime();

                if (outTime == null) {
                    outTime = DateUtils.int2Str(DateUtils.getCurrentTimeMillis10(), "yyyy-MM-dd HH:mm:ss");
                }
                final TextView tvCountdown = findViewById(R.id.tv_countdown);

                int end = DateUtils.str2int(outTime, "yyyy-MM-dd HH:mm:ss");
                int start = DateUtils.getCurrentTimeMillis10();
                int timerTime = end - start;
                if (timerTime < 0) {
                    timerTime = 0;
                }

                CountDownTimer timer = new CountDownTimer(timerTime * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String time = DateUtils.long2Str(millisUntilFinished, "mm:ss");
                        LogUtils.i(time);
                        tvCountdown.setText(time);
                    }

                    @Override
                    public void onFinish() {
                        setResult(RESULT_OK);
                        tvCountdown.setTextSize(UIUtils.dip2px(10));
                        tvCountdown.setText("还需缴费0元");
                    }
                };
                timer.start();
                //头部布局
                findViewById(R.id.ll_msg_head_countdown).setVisibility(View.VISIBLE);


                break;
            //已完成
            case KEY_ALREADY:

                //处理车牌
                tvPlate.setVisibility(View.GONE);
                setMsg(R.id.ll_msg_plate, R.id.tv_msg_plate, mDatas.getPlateNum());

                //设置出场时间
                setMsg(R.id.ll_msg_out, R.id.tv_msg_out, mDatas.getOutTime());

                //优惠券金额
                if (mDatas.getCouponMoney() != 0) {
                    setMsg(R.id.ll_msg_coupon_money, R.id.tv_msg_coupon_money, "￥ -" + StringUtils.formatMoney(mDatas.getCouponMoney()));
                    TextView tv = findViewById(R.id.tv_msg_coupon_money);
                    tv.setCompoundDrawables(null, null, null, null);
                }
                //隐藏头布局
                findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);
                AppBarLayout appbar = findViewById(R.id.appbar);
                ViewGroup.LayoutParams params = appbar.getLayoutParams();
                params.height = UIUtils.dip2px(80);
                appbar.setLayoutParams(params);

                //实际支付
                findViewById(R.id.tv_pay_money_already).setVisibility(View.VISIBLE);
                TextView pay_money_ = findViewById(R.id.tv_pay_money_already);
                //double couponMoney = getCouponMoney(mDatas, mCoupon);
                double money = mDatas.getConsume() - mDatas.getCouponMoney();
                pay_money_.setText("实际支付金额：￥" + StringUtils.formatMoney(money));
                break;
        }
    }

    private void turnToCoupon(double consume) {
        CouponValidateExtra extra = new CouponValidateExtra();
        extra.setConsume(consume);
        extra.setPayType(mDatas.getPayType());
        extra.setParkCode(mDatas.getParkCode());
        ParkingCouponActivity.startForResult(this, extra, REQUEST_COUPON);
    }

    private void setMsg(int layoutId, int viewId, String text) {
        if (!StringUtils.isEmpty(text)) {
            findViewById(layoutId).setVisibility(View.VISIBLE);
            TextView tv = (TextView) findViewById(viewId);
            tv.setText(text);
        } else {
            findViewById(layoutId).setVisibility(View.GONE);
        }
    }


    private int getCurrentState() {
        //判断当前状态
        int recordStatus = mDatas.getRecordStatus();
        switch (recordStatus) {
            case 1:
            case 2:
                //待缴费,预缴
                return KEY_NOPAY;
            case 3:
                //已完成
                return KEY_ALREADY;
            case 4:
                //欠费
                return KEY_ARREARAGE;
            case 5:
                //等待离场
                return KEY_NOLEAVE;
        }
        return 0;
    }

    /**
     * 跳转支付* @param view
     */
    public void doPayFor(View view) {
        if (!validateUserIdAndToken()) return;
        ParkRecordReq req = new ParkRecordReq(mCouponId, mDatas.getParkRecordId(), SPUtils.getUserId(this), mDatas.getPayType());

        LogUtils.i(req.toString());

        HttpHelper.getRetrofit().create(PayOrderAPI.class).submitOrder(req)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ParkRecordOrder>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(ParkRecordOrder parkRecordOrder) {
                turnToOrderDetail(parkRecordOrder);
            }
        });
    }

    private void turnToOrderDetail(ParkRecordOrder bean) {

        double discountMoney = 0;

        ParkRecordPay pay = new ParkRecordPay();
        pay.setPayType(mDatas.getPayType());
        pay.setParkName(mDatas.getParkName());
        pay.setSpendTime(DateUtils.formatSeconds(mDatas.getParkSeconds()));
        pay.setCarCode(mDatas.getPlateNum());
        pay.setOrderCode(bean.getOrderCode());
        pay.setParkCode(mDatas.getParkCode());

        if (mCoupon != null) {
            //discount 代金券/折扣券金额
            discountMoney = getCouponMoney(mDatas, mCoupon);
            pay.setDiscount(discountMoney);
            pay.setCouponId(mCoupon.getId());
        }
        //实际支付金额
        double shouldPay = mDatas.getConsume() - mDatas.getHasPay();
        pay.setShouldPay(shouldPay);
        pay.setActualPay(shouldPay - discountMoney);
        pay.setLastMoney(bean.getRealMoney());

        ParkingPaymentActivity.startForResult(this,
            pay, ParkingPaymentActivity.LAYOUT_PARKRECORD,
            REQUEST_PAY, discountMoney, bean.getActivityList());

        //ParkRecordPay parkRecordPay = new ParkRecordPay(name, time, plateNum, consume + "", String.valueOf(consume - money) + "", money + ""),)
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_COUPON:
                    if (data == null) return;
                    mCoupon = data.getParcelableExtra(ParkingCouponActivity.RESULT_PARCELABLE_COUPON);
                    if (mCoupon != null) {
                        mCouponId = mCoupon.getId();
                        //计算金额
                        calculateMoney(mDatas, mCoupon, mTvPayMoney);
                        //显示折扣券
                        setMsg(R.id.ll_msg_coupon_money, R.id.tv_msg_coupon_money, formatCouponMoney(mCoupon));

                    }
                    break;

                case REQUEST_PAY:
                    //缴费完成跳转等待离场界面
                   /* if (mDatas != null) {
                        mDatas.setRecordStatus(5);
                        mDatas.setPayTime(DateUtils.getCurrentDateTime(GlobalConfig.TIMEFORMAT));
                        int state = getCurrentState();
                        if (state != 0) {
                            setData(state);

                            findViewById(R.id.tv_msg_park_money).setVisibility(View.VISIBLE);
                            findViewById(R.id.ll_msg_coupon_money).setVisibility(View.GONE);
                            findViewById(R.id.ll_msg_head_money).setVisibility(View.GONE);
                            findViewById(R.id.ll_msg_haspay).setVisibility(View.GONE);
                            findViewById(R.id.ll_footer).setVisibility(View.GONE);

                            setMsg(R.id.ll_msg_park_money,R.id.tv_msg_park_money,"￥"+mDatas.getConsume());

                            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);
                            scrollView.scrollTo(0,0);

                        }
                    } else {
                        UIUtils.showToast("刷新失败请返回重试");
                    }
                    setResult(RESULT_OK);*/


                    //缴费完成跳转代缴费界面
                    if (mDatas != null) {
                        mDatas.setHasPay(mDatas.getConsume());
                        int state = getCurrentState();
                        if (state != 0) {
                            setData(state);
                        }
                    } else {
                        UIUtils.showToast("刷新失败请返回重试");
                    }
                    setResult(RESULT_OK);
                    break;
            }
        }
    }


    /**
     * 计算余额* @param amountMoney
     *
     * @param tv
     */
    private void calculateMoney(ParkRecord record, TextView tv) {
        calculateMoney(record, null, tv);
    }

    /**
     * 计算余额* @param amountMoney
     */
    private void calculateMoney(ParkRecord record, ParkingCoupon coupon, TextView tv) {
        double couponMoney = getCouponMoney(record, coupon);
        double actualMoney = record.getConsume() - record.getHasPay() - couponMoney;
        if (actualMoney <= 0) {
            actualMoney = 0;
            //findViewById(R.id.btn_pay_now).setEnabled(false);
        }
        SpannableString spannable = new SpannableString("￥" + StringUtils.formatMoney(actualMoney));
        spannable.setSpan(new ForegroundColorSpan(UIUtils.getColor(R.color.money_orange)), 0, spannable.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText("还需支付：");
        tv.append(spannable);
    }

    /**
     * 获取优惠金额
     */
    private double getCouponMoney(ParkRecord record, ParkingCoupon coupon) {
        if (coupon != null) {
            if (coupon.getCouponType() == 1) {
                //优惠券
                return coupon.getCouponAmount();
            } else if (coupon.getCouponType() == 2) {
                //折扣券
                double tempMoney = record.getConsume() - record.getHasPay();
                return tempMoney * (1 - coupon.getCouponDiscount());
            }
        }
        return 0;
    }

    private String formatCouponMoney(ParkingCoupon coupon) {
        if (coupon.getCouponType() == 1) {
            //优惠券
            return "￥-" + StringUtils.formatMoney(coupon.getCouponAmount());
        } else if (coupon.getCouponType() == 2) {
            //折扣券

            return (coupon.getCouponDiscount() * 10) + "折券";
        }
        return "";
    }
}
