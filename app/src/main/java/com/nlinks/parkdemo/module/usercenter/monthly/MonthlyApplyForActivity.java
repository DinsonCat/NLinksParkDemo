package com.nlinks.parkdemo.module.usercenter.monthly;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.MonthlyAPI;
import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.entity._req.MonthlyOrderReq;
import com.nlinks.parkdemo.entity._req.MonthlyParkReq;
import com.nlinks.parkdemo.entity._req.PayOrderReq;
import com.nlinks.parkdemo.entity.monthly.MonthlyOrderResponse;
import com.nlinks.parkdemo.entity.monthly.MonthlyPark;
import com.nlinks.parkdemo.entity.monthly.MonthlyParkInfo;
import com.nlinks.parkdemo.entity.monthly.MonthlyParkResponse;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.entity.plate.PlateInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.coupon.ParkingCouponActivity;
import com.nlinks.parkdemo.module.usercenter.managecar.ManageCarActivity;
import com.nlinks.parkdemo.module.usercenter.monthly.model.MonthlyPay;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CouponView;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.DialogChoosePlate;
import com.nlinks.parkdemo.widget.datepicker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MonthlyApplyForActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_RULE = "extra_rule";
    private static final String EXTRA_PARK = "extra_park";
    private static final String FORMAT_TIME = "yyyy-MM-dd";
    private static final int REQUEST_CODE = 0x0010;

    private MonthlyParkInfo mData;//包月套餐数据
    private PlateNumAPI mPlateNumAPI;//API
    private TextView mTvPayCount;
    private TextView mTvStartTime;
    private TextView mTvPlate;
    private TextView mTvOrderStartTime;
    private TextView mTvResult;
    private TextView mTvOrderEndTime;
    private MonthlyPark mMonthlyPark;
    private View mSubBtn, mPlusBtn;//加减按钮
    private String mFormatStartTime;//一个格式化的开始时间
    private CouponView mCouponView;

    public static void start(Context context, MonthlyParkInfo bean, MonthlyPark monthlyPark) {
        Intent starter = new Intent(context, MonthlyApplyForActivity.class);
        starter.putExtra(EXTRA_RULE, bean);
        starter.putExtra(EXTRA_PARK, monthlyPark);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_apply_for);

        initUI();

        if (mData.getPeriodType() == 2) {
            //每月
            Calendar ca = Calendar.getInstance();
            ca.set(Calendar.HOUR_OF_DAY, 0);
            ca.clear(Calendar.MINUTE);
            ca.clear(Calendar.SECOND);
            ca.clear(Calendar.MILLISECOND);
            ca.set(Calendar.DAY_OF_MONTH, 1);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_TIME);
            mFormatStartTime = dateFormat.format(ca.getTime());
            mTvStartTime.setText(mFormatStartTime);
            mTvOrderStartTime.setText(mFormatStartTime);
        } else {
            //每日
            mFormatStartTime = DateUtils.getCurrentDateTime(FORMAT_TIME);
            //String s = DateUtils.long2Str(time, FORMAT_TIME);
            mTvStartTime.setText(mFormatStartTime);
            mTvOrderStartTime.setText(mFormatStartTime);
        }

        calculateOrderEndTime();

    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        mData = getIntent().getParcelableExtra(EXTRA_RULE);
        mMonthlyPark = getIntent().getParcelableExtra(EXTRA_PARK);
        if (mData == null) {
            finish();
            UIUtils.showToast("数据异常");
        }

        mCouponView = findViewById(R.id.couponView);
        CouponValidateExtra extra = new CouponValidateExtra();
        extra.setParkCode(mMonthlyPark.getParkCode());
        extra.setPayType(NlinksParkUtils.PayType.MONTHLY_PAY.getValue());
        extra.setConsume(Double.parseDouble(mData.getChareFee()));
        mCouponView.init(this, extra, REQUEST_CODE, null);

        formatTv(R.id.tvParkName, mData.getRuleName());
        //服务时间
        formatTv(R.id.tvSetMealsTime, NlinksParkUtils.formatRuleTime(mData.getRuleTime()));

        //包月价格
        TextView tvPrice = findViewById(R.id.tvPrice);
        tvPrice.setText("￥" + mData.getChareFee());
        SpannableString span = new SpannableString("\n收费标准");
        span.setSpan(new RelativeSizeSpan(0.5f), 0, span.length(), SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPrice.append(span);

        //生效日期
        mTvOrderStartTime = findViewById(R.id.tvOrderStartTime);
        //结束日期
        mTvOrderEndTime = findViewById(R.id.tvOrderEndTime);

        //包月须知
        LinearLayout container = findViewById(R.id.llContainer);
        NlinksParkUtils.createTextView(this, container, mData.getRemark());

        //点击事件
        mTvStartTime = findViewById(R.id.tvStartTime);
        mTvStartTime.setOnClickListener(this);
        mTvPlate = findViewById(R.id.tvPlate);
        mTvPlate.setOnClickListener(this);

        mSubBtn = findViewById(R.id.sub);
        mSubBtn.setOnClickListener(this);
        mPlusBtn = findViewById(R.id.plus);
        mPlusBtn.setOnClickListener(this);
        mTvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnApplyFor).setOnClickListener(this);

        setPayCount(1);//默认购买1个月
        resetPlusSubBtn(mData.getMaxMonth(), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE) {
            ParkingCoupon coupon = data.getParcelableExtra(ParkingCouponActivity.RESULT_PARCELABLE_COUPON);
            mCouponView.initCouponLayout(coupon);
        }
    }

    private void formatTv(int id, String text) {
        TextView tv = findViewById(id);
        tv.setText(text);
    }

    private void setPayCount(int monthCount) {
        String price = StringUtils.formatMoney(Double.parseDouble(mData.getChareFee()) * monthCount);
        if (mTvPayCount == null) mTvPayCount = findViewById(R.id.tvPayCount);
        mTvPayCount.setText("总价: ");
        SpannableString span = new SpannableString(price);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.money_orange)), 0,
            span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPayCount.append(span);

        SpannableString span2 = new SpannableString("  (共" + monthCount + "个月)");
        span2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.money_orange)), 0,
            span2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span2.setSpan(new RelativeSizeSpan(0.7f), 0,
            span2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPayCount.append(span2);
    }

    /**
     * 改变优惠券
     *
     * @param monthCount 月份
     */
    private void changeCoupon(int monthCount) {
        CouponValidateExtra extra = new CouponValidateExtra();
        extra.setParkCode(mMonthlyPark.getParkCode());
        extra.setConsume(Double.parseDouble(mData.getChareFee()) * monthCount);
        extra.setPayType(NlinksParkUtils.PayType.MONTHLY_PAY.getValue());
        mCouponView.initCouponLayout(extra);
    }


    private void calculateOrderEndTime() {
        String startDate = mTvStartTime.getText().toString();
        if (StringUtils.isEmpty(startDate)) return;
        int monthCount = Integer.parseInt(mTvResult.getText().toString());
        Date date = DateUtils.convert2DateTime(startDate, FORMAT_TIME);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthCount);//把日期往后增加一个月.整数往后推,负数往前移动
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String str = DateUtils.long2Str(calendar.getTime().getTime(), FORMAT_TIME);
        mTvOrderEndTime.setText(str);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sub://减
                int i = Integer.parseInt(mTvResult.getText().toString());
                if (i == 1) return;
                i -= 1;

                mTvResult.setText(String.valueOf(i));
                setPayCount(i);
                changeCoupon(i);
                resetPlusSubBtn(mData.getMaxMonth(), i);
                if (StringUtils.isNotEmpty(mTvOrderStartTime.getText().toString())) {
                    //有生效日期
                    calculateOrderEndTime();
                }
                break;
            case R.id.plus://加
                int i2 = Integer.parseInt(mTvResult.getText().toString());
                i2 += 1;
                if (i2 > mData.getMaxMonth()) {
                    UIUtils.showToast("已达到最大购买月数");
                    return;
                }
                mTvResult.setText(String.valueOf(i2));
                setPayCount(i2);
                changeCoupon(i2);
                resetPlusSubBtn(mData.getMaxMonth(), i2);
                if (StringUtils.isNotEmpty(mTvOrderStartTime.getText().toString())) {
                    //有生效日期
                    calculateOrderEndTime();
                }
                break;

            case R.id.btnApplyFor:
                MonthlyPay validate = validate();
                if (validate == null) return;
                //生成支付订单req
                MonthlyParkReq payOrderReq = createPayOrderReq(validate);
                //申请包月，生成订单
                requestOrder(payOrderReq, validate);
                break;
            case R.id.tvStartTime:
                //选择日期
               /* new SingleDatetimePicker.Builder(this).withTextView(mTvStartTime)
                    .setStartDate(DateUtils.getCurrentTimeMillis13()).setHideYear(false).setHasTime(false)
                    .setOnDatetimeSelectedListener(new SingleDatetimePicker.OnDatetimeSelectedListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDatetimeSelected(Date date, int year, int month, int day, int hour,
                                                       int minute) {
                            long time = date.getTime();
                            String s = DateUtils.long2Str(time, FORMAT_TIME);
                            mTvStartTime.setText(s);
                            mTvOrderStartTime.setText(s);
                            calculateOrderEndTime();
                        }
                    }).showWithTextView(FORMAT_TIME);*/

                showPicker(mData.getPeriodType());
                break;
            case R.id.tvPlate:
                //选择车牌
                if (mPlateNumAPI == null)
                    mPlateNumAPI = HttpHelper.getRetrofit().create(PlateNumAPI.class);
                String userId = SPUtils.getUserId(this);
                if (StringUtils.isEmpty(userId)) return;
                mPlateNumAPI.getPlateList(SPUtils.getUserId(this))
                    .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<PlateInfo>>(this) {
                    @NonNull
                    @Override
                    public void onHandleSuccess(final ArrayList<PlateInfo> plateInfos) {
                        if (plateInfos == null || plateInfos.size() == 0) {
                            //对话框
                            final DialogCancelConfirm dialog = new DialogCancelConfirm(
                                MonthlyApplyForActivity.this);
                            dialog.setMessage("您还没有添加车牌，是否去添加车牌");
                            dialog.setButtonsText("取消", "添加车牌");
                            dialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
                                @Override
                                public void onLeftClick() {
                                    dialog.cancel();
                                }

                                @Override
                                public void onRightClick() {
                                    dialog.cancel();
                                    ManageCarActivity.start(MonthlyApplyForActivity.this);
                                }
                            });
                            dialog.show();
                            return;
                        }
                        final String[] plates = new String[plateInfos.size()];
                        for (int i = 0; i < plateInfos.size(); i++) {
                            plates[i] = plateInfos.get(i).getCarNo();
                        }
                        final DialogChoosePlate dialog = new DialogChoosePlate(MonthlyApplyForActivity.this,
                            plates);
                        dialog.setOnChooseListener(new DialogChoosePlate.onChooseListener() {
                            @Override
                            public void onChoose(String plate) {
                                mTvPlate.setText(plate);
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                });
                break;
        }
    }

    /**
     * 验证订单
     *
     * @return 订单对象，跳转订单支付
     */
    private MonthlyPay validate() {
        String plate = mTvPlate.getText().toString();
        if (StringUtils.isEmpty(plate)) {
            UIUtils.showToast("请选择车牌");
            return null;
        }
        String startTime = mTvOrderStartTime.getText().toString();
        if (StringUtils.isEmpty(startTime)) {
            UIUtils.showToast("请选择开始日期");
            return null;
        }
        String endTime = mTvOrderEndTime.getText().toString();
        int timeCount = Integer.parseInt(mTvResult.getText().toString());
        String ruleName = mData.getRuleName();
        return new MonthlyPay(mMonthlyPark.getName(), ruleName, plate, startTime + " 00:00:00",
            endTime + " 23:59:59", mData.getChareFee(), timeCount,
            mData.getMonthlyRuleId());
    }

    /**
     * 申请包月，生成订单
     *
     * @param monthlyPark
     */
    private void requestOrder(final MonthlyParkReq monthlyPark, final MonthlyPay validate) {
        if (monthlyPark == null) return;

        LogUtils.e(monthlyPark.toString());

        HttpHelper.getRetrofit().create(MonthlyAPI.class)
            .submitMonthlyPark(monthlyPark)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<MonthlyParkResponse>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(MonthlyParkResponse bean) {
                String couponId = "";
                ParkingCoupon coupon = mCouponView.getCoupon();
                if (coupon != null) {
                    couponId = coupon.getId();
                    validate.setCouponMoney(mCouponView.getCouponMoney());
                }

                submitOrder(new MonthlyOrderReq(bean.getMonthlyRecordId(), monthlyPark.getUserId(), couponId),
                    validate);
            }
        });
    }

    private void submitOrder(MonthlyOrderReq entity, final MonthlyPay validate) {
        LogUtils.e(entity.toString());
        HttpHelper.getRetrofit().create(MonthlyAPI.class).submitMonthlyOrder(entity)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<MonthlyOrderResponse>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(MonthlyOrderResponse bean) {

                PayOrderReq orderReq = new PayOrderReq();
                if (bean == null) return;

                orderReq.setOrderCode(bean.getOrderCode());
                orderReq.setPayType(8);
                String money = StringUtils.formatMoney(bean.getOrderMoney());
                orderReq.setTotalFee(money);
                orderReq.setOrderDesc("错峰包月缴费");
                orderReq.setOrderDetail("错峰包月缴费" + money);
                orderReq.setOrderAttach(getResources().getString(R.string.app_name));
                if (!validateUserIdAndToken()) {
                    return;
                }
                orderReq.setUserId(SPUtils.getUserId(MonthlyApplyForActivity.this, null));

                //植入平台活动
                validate.setActivityLists(bean.getPlatformActivityList());
                validate.setLastMoney(bean.getRealMoney());

                LogUtils.e(validate.toString());
                MonthlyPaymentActivity.start(MonthlyApplyForActivity.this, validate, orderReq);
            }

        });
    }

    /**
     * 生成支付订单req
     *
     * @return
     */
    private MonthlyParkReq createPayOrderReq(MonthlyPay data) {
        String userId = SPUtils.getUserId(this);
        if (StringUtils.isEmpty(userId)) return null;
        return new MonthlyParkReq(data.getMonthlyRuleId(), String.valueOf(data.getTimeCount()), data.getPlateNum(),
            data.getStartTime(), userId);
    }


    /**
     * 根据当前购买的月分数，重新设置加减的enable状态
     *
     * @param max     支持购买的最大月份数
     * @param current 当前购买的月份数
     */
    private void resetPlusSubBtn(int max, int current) {
        //mSubBtn.setEnabled(current > 1);
        //mPlusBtn.setEnabled(current <= max - 1);
    }

    /**
     * 显示日期选择控件
     *
     * @param type 周期类型       1每日 2每月
     */
    private void showPicker(int type) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = DateUtils.convertStr(mFormatStartTime, FORMAT_TIME, "yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 50);
        String endTime = sdf.format(calendar.getTime());
        CustomDatePicker picker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                time = DateUtils.convertStr(time, "yyyy-MM-dd HH:mm", "yyyy-MM-dd");
                mTvStartTime.setText(time);
                mTvOrderStartTime.setText(time);
                calculateOrderEndTime();
            }
        }, now, endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        //周期类型       1每日 2每月
        if (type == 2) {
            //支持按月选择日期
            picker.hideDayAndSpecificTime(); // 显示时和分
        } else {
            //支持按日选择日期
            picker.showSpecificTime(false);
        }
        picker.setIsLoop(false); // 允许循环滚动
        picker.show(now);
    }
}
