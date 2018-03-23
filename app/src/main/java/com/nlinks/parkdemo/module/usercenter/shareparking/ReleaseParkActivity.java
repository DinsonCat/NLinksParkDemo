package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ParkShareAPI;
import com.nlinks.parkdemo.entity._req.PublishPark;
import com.nlinks.parkdemo.entity.parkshare.ParkLokerInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.SingleDatetimePicker;

import io.reactivex.Observable;

/**
 * 发布共享车位
 */
public class ReleaseParkActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final String KEY_PARKID = "parkid";
    private static final String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";


    private EditText mEtPrice;
    private TextView mTvEndTime;
    private TextView mTvStartTime;
    private String mParkSharingId;

    public static void start(Activity context, String parkId, int requestCode) {
        Intent intent = new Intent(context, ReleaseParkActivity.class);
        intent.putExtra(KEY_PARKID, parkId);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_park);

        initUI();
    }

    private void initUI() {
        mParkSharingId = getIntent().getStringExtra(KEY_PARKID);

        mEtPrice = (EditText) findViewById(R.id.et_price);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);

        mTvEndTime.setOnClickListener(this);
        mTvStartTime.setOnClickListener(this);

        mEtPrice.addTextChangedListener(this);

    }

    public void onReleasePark(View view) {
        PublishPark bean = validate();
        if (bean == null) return;

        LogUtils.e(bean.toString());


        HttpHelper.getRetrofit().create(ParkShareAPI.class).publishParking(bean)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ParkLokerInfo>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(ParkLokerInfo httpResult) {
                UIUtils.showToast("添加成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private PublishPark validate() {
        String startTime = mTvStartTime.getText().toString();
        String endTime = mTvEndTime.getText().toString();
        String price = mEtPrice.getText().toString().trim();

        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) || StringUtils.isEmpty(price)) {
            return null;
        }

        try {
            if (DateUtils.compareTime(startTime, endTime, FORMAT_TIME) >= 0) {
                UIUtils.showToast("开始时间不能大于结束时间");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showToast("时间有误");
            return null;
        }

        return new PublishPark(startTime, endTime, price, mParkSharingId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_end_time:
                new SingleDatetimePicker.Builder(this)
                    .withTextView(mTvEndTime)
                    .setStartDate(DateUtils.getCurrentTimeMillis13())
                    .setHideYear(true)
                    .setHasTime(true)
                    .showWithTextView(FORMAT_TIME);
                break;
            case R.id.tv_start_time:
                new SingleDatetimePicker.Builder(this)
                    .withTextView(mTvStartTime)
                    .setStartDate(DateUtils.getCurrentTimeMillis13())
                    .setHideYear(true)
                    .setHasTime(true)
                    .showWithTextView(FORMAT_TIME);
                break;
        }
    }

    //-----------------------------------------------------------------
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                    s.toString().indexOf(".") + 3);
                mEtPrice.setText(s);
                mEtPrice.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            mEtPrice.setText(s);
            mEtPrice.setSelection(2);
        }

        if (s.toString().startsWith("0")
            && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                mEtPrice.setText(s.subSequence(0, 1));
                mEtPrice.setSelection(1);
                return;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
