package com.nlinks.parkdemo.module.appointment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.AppointmentApi;
import com.nlinks.parkdemo.api.ParkShareAPI;
import com.nlinks.parkdemo.entity._req.AppointTimeoutPay;
import com.nlinks.parkdemo.entity._req.LockCtrl;
import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;
import com.nlinks.parkdemo.entity.parkshare.ParkLokerInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 预约详情
 * Created by Dell on 2017/05/03.
 */
public class AppointmentRecordDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String SERIALIZABLE_APPOINTMENT_RECORD = "SERIALIZABLE_APPOINTMENT_RECORD";

    private TextView mTvItemParkName;
    private TextView mTvItemParkLocation;
    private TextView mTvItemParkStatus;
    //private TextView mTvtAppointmentTime;
    private TextView mTvAppointmentTime;
    private TextView mTvtParkingTime;
    private TextView mTvParkingTime;
    //private TextView mTvtLatestOutTime;
    private TextView mTvLatestOutTime;
    private TextView mTvtActualOutTime;
    private TextView mTvActualOutTime;
    //private TextView mTvtCarCode;
    private TextView mTvCarCode;
    //private TextView mTvtPrePayment;
    private TextView mTvPrePayment;
    private RelativeLayout mRlTimeout;
    //private TextView mTvtTimeout;
    private TextView mTvTimeout;
    private TextView mTvTimeoutPayment;
    private Button mBtnAppointmentSubmit;
    private DialogCancelConfirm mConfirmDialog;

    private AppointmentRecord mAppointmentRecord;
    private int mFlag = -1;
    private String mDeviceCode;

    private AppointmentApi mApi;
    private int mCurrentState = AppConst.STATUS_LOCK_DOWN;//当前状态，站立
    private int mCompare = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_record_detail);

        if (getIntent().getExtras() != null) {
            mAppointmentRecord = (AppointmentRecord) getIntent().getExtras().getSerializable(SERIALIZABLE_APPOINTMENT_RECORD);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(new HttpHelper.LoggingInterceptor());//添加拦截器 日志
        Retrofit retrofit = HttpHelper.getRetrofit().newBuilder().client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())//对http请求结果进行统一的预处理
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxjava提供支持
            .baseUrl(AppConst.URL_PREFIX)
            .build();
        mApi = retrofit.create(AppointmentApi.class);

        initView();
        updateView();

        initLokerState();
    }

    private void initView() {
        mTvItemParkName = (TextView) findViewById(R.id.tv_item_park_name);
        mTvItemParkLocation = (TextView) findViewById(R.id.tv_item_park_location);
        mTvItemParkStatus = (TextView) findViewById(R.id.tv_item_park_status);
        //mTvtAppointmentTime = (TextView) findViewById(R.id.tvt_appointment_time);
        mTvAppointmentTime = (TextView) findViewById(R.id.tv_appointment_time);
        mTvtParkingTime = (TextView) findViewById(R.id.tvt_parking_time);
        mTvParkingTime = (TextView) findViewById(R.id.tv_parking_time);
        //mTvtLatestOutTime = (TextView) findViewById(R.id.tvt_latest_out_time);
        mTvLatestOutTime = (TextView) findViewById(R.id.tv_latest_out_time);
        mTvtActualOutTime = (TextView) findViewById(R.id.tvt_actual_out_time);
        mTvActualOutTime = (TextView) findViewById(R.id.tv_actual_out_time);
        //mTvtCarCode = (TextView) findViewById(R.id.tvt_car_code);
        mTvCarCode = (TextView) findViewById(R.id.tv_car_code);
        //mTvtPrePayment = (TextView) findViewById(R.id.tvt_pre_payment);
        mTvPrePayment = (TextView) findViewById(R.id.tv_pre_payment);
        mRlTimeout = (RelativeLayout) findViewById(R.id.rl_timeout);
        //mTvtTimeout = (TextView) findViewById(R.id.tvt_timeout);
        mTvTimeout = (TextView) findViewById(R.id.tv_timeout);
        //mTvtTimeoutPayment = (TextView) findViewById(R.id.tvt_timeout_payment);
        mTvTimeoutPayment = (TextView) findViewById(R.id.tv_timeout_payment);
        mBtnAppointmentSubmit = (Button) findViewById(R.id.btn_appointment_submit);

        mBtnAppointmentSubmit.setOnClickListener(this);
    }

    private void updateView() {
        if (mAppointmentRecord != null) {
            mDeviceCode = mAppointmentRecord.getDeviceCode();

            mTvItemParkName.setText(mAppointmentRecord.getName());
            mTvItemParkLocation.setText(mAppointmentRecord.getAddress());
            mTvAppointmentTime.setText(mAppointmentRecord.getCreateTime());

            mRlTimeout.setVisibility(View.GONE);
            mBtnAppointmentSubmit.setVisibility(View.VISIBLE);

            String parkStatus = mAppointmentRecord.getParkStatus();
            if ("1".equals(parkStatus)) {
                mTvItemParkStatus.setText("未驶入");
                mTvParkingTime.setVisibility(View.GONE);
                mTvtParkingTime.setVisibility(View.GONE);
                mTvActualOutTime.setVisibility(View.GONE);
                mTvtActualOutTime.setVisibility(View.GONE);
                mBtnAppointmentSubmit.setText("确认入场");
                mFlag = 1;
            } else {
                mTvParkingTime.setVisibility(View.VISIBLE);
                mTvtParkingTime.setVisibility(View.VISIBLE);
                mTvActualOutTime.setVisibility(View.VISIBLE);
                mTvtActualOutTime.setVisibility(View.VISIBLE);


                //入场时间
                if (StringUtils.isEmpty(mAppointmentRecord.getEnterTime())) {
                    mTvtParkingTime.setVisibility(View.GONE);
                } else {
                    mTvParkingTime.setText(mAppointmentRecord.getEnterTime());
                }

                //实际出场时间
                if (StringUtils.isEmpty(mAppointmentRecord.getActualTime())) {
                    mTvtActualOutTime.setVisibility(View.GONE);
                } else {
                    mTvActualOutTime.setText(mAppointmentRecord.getActualTime());
                }


                if ("3".equals(parkStatus)) {
                    mFlag = 3;
                    mBtnAppointmentSubmit.setText("超时支付");
                    mTvItemParkStatus.setText("已超时");
                    mCompare = 0;
//					try {
//						mCompare = DateUtils.compareTime(mAppointmentRecord.getActualTime()
//								, mAppointmentRecord.getLeaveTime(), "yyyy-MM-dd HH:mm:ss");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
                    mCompare = mAppointmentRecord.getTimeOut();
                    if (mCompare != 0) {
                        mBtnAppointmentSubmit.setEnabled(true);
                        mRlTimeout.setVisibility(View.VISIBLE);
                        mTvTimeout.setText(mCompare + "分钟");
                        mTvTimeoutPayment.setText(StringUtils.formatMoney(mAppointmentRecord.getChargeOut()));
                    } else {
                        mBtnAppointmentSubmit.setEnabled(false);
                    }
                } else {
                    mRlTimeout.setVisibility(View.GONE);
                    if ("2".equals(parkStatus)) {
                        mTvActualOutTime.setVisibility(View.GONE);
                        mTvtActualOutTime.setVisibility(View.GONE);
                        mFlag = 2;
                        mTvItemParkStatus.setText("未驶出");
                        mBtnAppointmentSubmit.setText("确认出场");
                    } else {
                        mFlag = 4;
                        mTvItemParkStatus.setText("4".equals(parkStatus) ? "已完成" : "");
                        mBtnAppointmentSubmit.setVisibility(View.GONE);
                    }
                }
            }
            mTvLatestOutTime.setText(mAppointmentRecord.getLeaveTime());
            mTvCarCode.setText(mAppointmentRecord.getPlateNum());
            mTvPrePayment.setText(StringUtils.formatMoney(mAppointmentRecord.getCharge()));
        }
    }

    @Override
    public void onClick(View v) {
        if (mFlag == 1 || mFlag == 2) {
            String message = mCurrentState == AppConst.STATUS_LOCK_UP ? "点击后地锁将降下" : "点击后地锁将上升";
            showConfirm(message + (mFlag == 2 ? "\n请确认车辆已开出" : "\n请确认已到达现场附近"));
        } else if (mFlag == 3) {
            timeOverPay();
        }
    }

    private void showConfirm(String message) {
        if (mConfirmDialog == null) {
            mConfirmDialog = new DialogCancelConfirm(this);
            mConfirmDialog.setButtonsText("取消", "确定");
            mConfirmDialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
                @Override
                public void onLeftClick() {
                    mConfirmDialog.dismiss();
                }

                @Override
                public void onRightClick() {
                    if (mCurrentState == AppConst.STATUS_LOCK_DOWN) postLockUpDown("up");
                    else if (mCurrentState == AppConst.STATUS_LOCK_UP) postLockUpDown("down");
                    mConfirmDialog.dismiss();
                }
            });
        }
        mConfirmDialog.setMessage(message);
        if (!mConfirmDialog.isShowing()) mConfirmDialog.show();
    }

    private void timeOverPay() {
        AppointTimeoutPay parkReq = new AppointTimeoutPay(mAppointmentRecord.getId(), SPUtils.getUserId(this));
        LogUtils.e(parkReq.toString());
        mApi.overTimePay(parkReq)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>() {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppointTimeoutPaymentActivity.SERIALIZABLE_APPOINT_RECORD
                    , mAppointmentRecord);
                jumpTo(AppointTimeoutPaymentActivity.class, bundle);
                finish();
            }
        });
    }

    private void postLockUpDown(final String up_down) {
        LogUtils.d("-----------------  " + mDeviceCode);
        mBtnAppointmentSubmit.setEnabled(false);
        if (!TextUtils.isEmpty(mDeviceCode)) {
            mApi.lockerControl(new LockCtrl(up_down, mDeviceCode))
                .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>() {
                @Override
                public void onNext(HttpResult<Void> value) {
                    initLokerState();
                    mBtnAppointmentSubmit.setEnabled(true);
                    super.onNext(value);
                }

                @NonNull
                @Override
                public void onHandleSuccess(Void aVoid) {
                    UIUtils.showToast("up".equals(up_down) ? "地锁上升成功" : "地锁下降成功");
                    if (mFlag == 1) mAppointmentRecord.setParkStatus("2");
                    else if (mFlag == 2) mAppointmentRecord.setParkStatus("4");
                    updateView();
                }
            });
        }
    }

    private void initLokerState() {
        HttpHelper.getRetrofit().create(ParkShareAPI.class)
            .getParkLockerInfo(mAppointmentRecord.getParkSharingId())
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ParkLokerInfo>() {
            @NonNull
            @Override
            public void onHandleSuccess(ParkLokerInfo parkLokerInfo) {
                if (parkLokerInfo != null) mCurrentState = parkLokerInfo.getStatus();
            }
        });
    }
}
