package com.nlinks.parkdemo.module.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.AppointmentApi;
import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.entity._req.AppointmentParkReq;
import com.nlinks.parkdemo.entity.appointment.AppointPark;
import com.nlinks.parkdemo.entity.appointment.AppointStall;
import com.nlinks.parkdemo.entity.appointment.ForecastMoney;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.entity.plate.PlateInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.coupon.ParkingCouponActivity;
import com.nlinks.parkdemo.module.usercenter.managecar.ManageCarActivity;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.DialogChoosePlate;
import com.nlinks.parkdemo.widget.SingleDatetimePicker;

import java.util.ArrayList;
import java.util.Date;


/**
 * 预定车位
 * Created by Dell on 2017/05/06.
 */
public class AppointmentParkSpotActivity extends BaseActivity
    implements View.OnClickListener, SingleDatetimePicker.OnDatetimeSelectedListener {

    private View mLlContainer;
    private TextView mTvParkName;
    private TextView mTvParkPrice;
    private TextView mTvOutTime;
    private TextView mTvCarCode;
    private Button mBtnAppointmentSubmit;//立即提交
    private TextView mTvMoneyOrecast;//优惠金额

    private AppointStall mAppointStall;
    //private String mParkName;
    //private int mUnUseNum = 0;
    //	private String[] mPlates = null;
    //	private Dialog mDialog;
    //	private CustomProgressDialog mProgressDialog;
    private SingleDatetimePicker mDatetimePicker;

    private String mTime, mPlate;
    //private double mLat, mLng;
    //private String mName, mAddress;
    private double mMoney = 0;//预估支付总额
    private ParkingCoupon mCoupon;

    private final int REQUEST_CODE = 100;
    private TextView mTvParkType;


    private static final String EXTRA_PARK = "PARK";
    private static final String EXTRA_STALL = "STALL";
    private ParkMain mParkMain;

    public static void start(Context context, ParkMain park, AppointStall stall) {
        Intent starter = new Intent(context, AppointmentParkSpotActivity.class);
        starter.putExtra(EXTRA_PARK, park);
        starter.putExtra(EXTRA_STALL, stall);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_park_spot);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppointStall = extras.getParcelable(EXTRA_STALL);
            mParkMain = extras.getParcelable(EXTRA_PARK);
        }
        initView();
        updateView();

        //getPlate();
    }

    private void initView() {
        mLlContainer = findViewById(R.id.ll_container);

        mTvParkType = findViewById(R.id.tv_park_type);
        mTvParkPrice = findViewById(R.id.tv_park_price);
        mTvOutTime = findViewById(R.id.tv_out_time);
        mTvCarCode = findViewById(R.id.tv_car_code);
        mBtnAppointmentSubmit = findViewById(R.id.btn_appointment_submit);
        mTvMoneyOrecast = findViewById(R.id.tv_money_yugu);

        findViewById(R.id.ll_out_time).setOnClickListener(this);
        findViewById(R.id.ll_car_code).setOnClickListener(this);
        findViewById(R.id.ll_money).setOnClickListener(this);
        mBtnAppointmentSubmit.setOnClickListener(this);

        mDatetimePicker = new SingleDatetimePicker.Builder(this).setOnDatetimeSelectedListener(this)
            .setHideYear(true).setHasTime(true).setAlwaysStartNow(true).build();
    }

    private void updateView() {
        if (mAppointStall != null) {

            TextView parkName = findViewById(R.id.tv_park_name);
            parkName.setText(mParkMain.getName());

            TextView price = findViewById(R.id.tvItemLeft);
            price.setText(TextColorBuilder.newBuilder()
                .addTextPart(this, R.color.money_orange, mAppointStall.getMoney())
                .addTextPart("元/小时").buildSpanned());
            TextView parkRest = findViewById(R.id.tvItemRight);
            parkRest.setText("可预约至:\n" + mAppointStall.getEndTime());

            TextView address = findViewById(R.id.tv_park_address);
            address.setText(mParkMain.getAddress());
            TextView type = findViewById(R.id.tv_park_type);
            type.setText(NlinksParkUtils.formatParkType(mParkMain.getType()));

            mDatetimePicker.updateStopDate(DateUtils.convert2DateTime(mAppointStall.getEndTime()));
            //mDatetimePicker.updateStartDate(new Date());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_out_time:
                mDatetimePicker.showAtLocation(mLlContainer);
                break;
            case R.id.ll_car_code:
                getPlateNum();
                break;
            case R.id.btn_appointment_submit:
                appointNow();
                break;
            case R.id.ll_money:
                coupon();//进入选择优惠券
                break;
            default:
                break;
        }
    }

    private PlateNumAPI mPlateNumAPI;

    private void getPlateNum() {
        //选择车牌
        if (mPlateNumAPI == null) mPlateNumAPI = HttpHelper.getRetrofit().create(PlateNumAPI.class);
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
                        AppointmentParkSpotActivity.this);
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
                            ManageCarActivity.start(AppointmentParkSpotActivity.this);
                        }
                    });
                    dialog.show();
                    return;
                }
                final String[] plates = new String[plateInfos.size()];
                for (int i = 0; i < plateInfos.size(); i++) {
                    plates[i] = plateInfos.get(i).getCarNo();
                }
                /**
                 * 弹出选择车牌对话框
                 */
                final DialogChoosePlate dialog = new DialogChoosePlate(AppointmentParkSpotActivity.this,
                    plates);
                dialog.setOnChooseListener(new DialogChoosePlate.onChooseListener() {
                    @Override
                    public void onChoose(String plate) {
                        mPlate = plate;
                        mTvCarCode.setText(mPlate);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

    }

    private void coupon() {    //进入选择优惠券
        if (mMoney > 0) {
            CouponValidateExtra extra = new CouponValidateExtra();
            extra.setConsume(mMoney);
            extra.setPayType(NlinksParkUtils.PayType.APPOINTPAY.getValue());
            extra.setParkCode(mParkMain.getCode());
            ParkingCouponActivity.startForResult(this, extra, REQUEST_CODE);
        } else {
            UIUtils.showToast("请选择出场时间");
        }
    }

    @Override
    public void onDatetimeSelected(Date date, int year, int month, int day, int hour, int minute) {
        if (mAppointStall != null/* && DateUtils.convert2DateTime(mAppointStall.getEndTime()).after(date)*/) {
            mTime = DateUtils.convertToServerFormat(date);
            mTvOutTime.setText(mTime);

            //请求预估价格
            HttpHelper.getRetrofit().create(AppointmentApi.class)
                .getForecastMoney(mAppointStall.getParkSharingId(), mTime)
                .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ForecastMoney>() {
                @NonNull
                @Override
                public void onHandleSuccess(ForecastMoney forecastMoney) {
                    mMoney = forecastMoney.getAppointMoney();
                    double couponMoney = NlinksParkUtils.getCouponMoney(mCoupon, mMoney);
                    if (mCoupon != null)
                        mTvMoneyOrecast.setText("￥" + StringUtils.formatMoney(couponMoney) + "元");
                    String yuguMoney = StringUtils.formatMoney(mMoney - couponMoney);
                    mBtnAppointmentSubmit.setText("预估支付：" + yuguMoney);


                    //请求最佳优惠金额
                    CouponValidateExtra extra = new CouponValidateExtra();
                    extra.setPayType(NlinksParkUtils.PayType.APPOINTPAY.getValue());
                    extra.setConsume(mMoney);
                    extra.setParkCode(mParkMain.getCode());
                    NlinksParkUtils.chooseGreatCoupon(AppointmentParkSpotActivity.this, extra, theGreat -> {
                        mCoupon = theGreat;
                        calculateCouponMoney(theGreat);
                    });
                }
            });

        } else UIUtils.showToast("所选时间不能大于车位可预约最大时间");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mCoupon = data.getParcelableExtra(ParkingCouponActivity.RESULT_PARCELABLE_COUPON);
            if (mCoupon != null) {
                calculateCouponMoney(mCoupon);
            }
        }
    }

    /**
     * 根据优惠券计算优惠金额并显示
     *
     * @param coupon
     */
    private void calculateCouponMoney(ParkingCoupon coupon) {
        double couponMoney = NlinksParkUtils.getCouponMoney(coupon, mMoney);
        mTvMoneyOrecast.setText("￥" + StringUtils.formatMoney(couponMoney) + "元");
        double actualMoney = mMoney - couponMoney;
        if (actualMoney < 0) actualMoney = 0;
        mBtnAppointmentSubmit.setText("预估支付：" + StringUtils.formatMoney(actualMoney));
    }

    private void appointNow() {
        if (TextUtils.isEmpty(mTime)) {
            UIUtils.showToast("出场时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(mPlate)) {
            UIUtils.showToast("车牌号不能为空");
            return;
        }

        mBtnAppointmentSubmit.setEnabled(false);

        AppointmentParkReq parkReq = new AppointmentParkReq();
        parkReq.setUserId(SPUtils.getUserId(this, null));
        parkReq.setLeaveTime(mTime);
        parkReq.setParkSharingId(mAppointStall.getParkSharingId());
        parkReq.setPlateNum(mPlate);
        if (mCoupon != null) parkReq.setCouponDetailIds(mCoupon.getId());
        HttpHelper.getRetrofit().create(AppointmentApi.class).appointPark(parkReq)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<AppointPark>() {

            @NonNull
            @Override
            public void onHandleSuccess(AppointPark result) {
                if (result != null) {
                    UIUtils.showToast("车位预定成功");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppointmentPaymentActivity.PARCELABLE_APPOINT_PARK, result);

                    bundle.putString(AppointmentPaymentActivity.STRING_NAME, mParkMain.getName());
                    bundle.putString(AppointmentPaymentActivity.STRING_LEAVE_TIME, mTime);
                    bundle.putString(AppointmentPaymentActivity.STRING_CAR_CODE, mPlate);
                    //优惠金额
                    double couponMoney = NlinksParkUtils.getCouponMoney(mCoupon, mMoney);

                    bundle.putString(AppointmentPaymentActivity.STRING_PREFERENTIAL_KEY,
                        StringUtils.formatMoney(couponMoney));
                    bundle.putString(AppointmentPaymentActivity.STRING_COUPON_ID,
                        mCoupon == null ? "" : mCoupon.getId());
                    jumpTo(AppointmentPaymentActivity.class, bundle);
                    finish();
                } else {
                    UIUtils.showToast("车位预定失败");
                }
            }

            @Override
            public void onHandleError(int code, String message) {
                UIUtils.showToast(message);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mBtnAppointmentSubmit.setEnabled(true);
            }
        });
    }
}