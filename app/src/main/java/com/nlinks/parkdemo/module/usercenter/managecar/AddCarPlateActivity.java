package com.nlinks.parkdemo.module.usercenter.managecar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.entity._req.PlateNum;
import com.nlinks.parkdemo.entity.plate.PlateInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.PlateKeyboardUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.DialogAd;

import io.reactivex.Observable;

public class AddCarPlateActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnTouchListener {

    private PlateKeyboardUtils mKeyboardUtils;
    private TextView mTvBrand;

    private boolean isFromLogin = false;

    private static final String EXTRA_FROM_LOGIN = "login";

    public static void startForResult(Activity context, boolean isFromLogin, int requestCode) {
        Intent starter = new Intent(context, AddCarPlateActivity.class);
        starter.putExtra(EXTRA_FROM_LOGIN, isFromLogin);
        context.startActivityForResult(starter, requestCode);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_plate);
        initUI();

        jumpToForResult(BrandOfCarActivity.class, CODE_BRAND);
    }

    private void initUI() {

        //先判断是否从登陆界面进入
        isFromLogin = getIntent().getBooleanExtra(EXTRA_FROM_LOGIN, false);

        mTvBrand = (TextView) findViewById(R.id.tv_brand);
        findViewById(R.id.fl_brand).setOnClickListener(this);
        ScrollView sv_container = (ScrollView) findViewById(R.id.sv_container);
        sv_container.setOnTouchListener(this);

        RadioGroup rg_plate = (RadioGroup) findViewById(R.id.rg_plate);
        KeyboardView kb = (KeyboardView) findViewById(R.id.kbView);

        mKeyboardUtils = new PlateKeyboardUtils(this, rg_plate, kb);
        mKeyboardUtils.showKeyboard();

        CheckBox cb_nev = (CheckBox) findViewById(R.id.cb_nev);
        cb_nev.setOnCheckedChangeListener(this);

        mKeyboardUtils.setOnPlateCompleteListener(new PlateKeyboardUtils.onPlateCompleteListener() {
            @Override
            public void onComplete(String plate) {
                validateUserIdAndToken();
                onClickSave(null);
            }
        });
    }

    public void onClickSave(View v) {
        if (!mKeyboardUtils.isComplete() || StringUtils.isEmpty(mTvBrand.getText())) {
            UIUtils.showToast("请完善资料后添加");
            return;
        }
        if (validateUserIdAndToken())
            postToServer(mKeyboardUtils.getPlate());
    }

    public void postToServer(String plate) {

        PlateNum plateNum = new PlateNum(SPUtils.getUserId(this, ""), plate);
        String brand = mTvBrand.getText().toString();
        if (StringUtils.isNotEmpty(brand)) plateNum.setBrand(brand);//车型

        LogUtils.i(plateNum.toString() + " token:" + SPUtils.getToken(this, ""));

        HttpHelper.getRetrofit().create(PlateNumAPI.class).addPlate("Bearer " + SPUtils.getToken(this, ""), plateNum)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<PlateInfo>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(final PlateInfo httpResult) {
                if (isFromLogin) {
                    UIUtils.showToast("添加成功");
                    onBackPressed();
                } else {
                    DialogAd dialogAd = new DialogAd(AddCarPlateActivity.this);
                    dialogAd.setTitle("添加成功");
                    dialogAd.setMessage("车牌添加成功，是否去认证车辆？");
                    dialogAd.setHeadImgResource(R.drawable.car_bg_head);
                    dialogAd.setOperationListener("马上去认证", new DialogAd.OnOperationListener() {
                        @Override
                        public void onActionClick() {
                            AuthenticationCarActivity.start(AddCarPlateActivity.this, httpResult.getCarNo(), false);
                            onBackPressed();
                        }
                    });
                    dialogAd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onBackPressed();
                        }
                    });
                    dialogAd.show();
                }
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mKeyboardUtils.setNEV(isChecked);
    }

    private static final int CODE_BRAND = 100;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_BRAND:
                    if (data != null) {
                        String brand = data.getStringExtra(BrandOfCarActivity.EXTRA_BRAND);
                        mTvBrand.setText(brand);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_brand:
                jumpToForResult(BrandOfCarActivity.class, CODE_BRAND);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mKeyboardUtils.hideKeyboard();
        }

        return false;
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void titleLeftBtnClick(View view) {
        onBackPressed();
    }
}
