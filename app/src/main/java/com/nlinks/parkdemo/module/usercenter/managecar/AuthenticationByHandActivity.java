package com.nlinks.parkdemo.module.usercenter.managecar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.entity._req.AuthenticByHand;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.avalidations.EditTextValidator;
import com.nlinks.parkdemo.utils.avalidations.ValidationModel;
import com.nlinks.parkdemo.utils.avalidations.utils.CarEngineNumValidation;
import com.nlinks.parkdemo.utils.avalidations.utils.CarFrameNumValidation;
import com.nlinks.parkdemo.utils.avalidations.utils.NameValidation;
import com.nlinks.parkdemo.widget.CustomProgressDialog;

import io.reactivex.Observable;

public class AuthenticationByHandActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_PLATE = "plate";
    private EditTextValidator mValidator;
    private Button mBtnAuthenticNow;
    private EditText mEtPlate, mEtName, mEtFrame, mEtEngine;

    public static void start(Activity context, String plate, int requestCode) {
        Intent starter = new Intent(context, AuthenticationByHandActivity.class);
        starter.putExtra(KEY_PLATE, plate);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_by_hand);

        initUI();

    }

    private void initUI() {


        findViewById(R.id.btn_back).setOnClickListener(this);
        mEtPlate = (EditText) findViewById(R.id.et_plate);
        mEtPlate.setText(getIntent().getStringExtra(KEY_PLATE));

        mEtName = (EditText) findViewById(R.id.et_name);
        mEtFrame = (EditText) findViewById(R.id.et_frame);
        mEtEngine = (EditText) findViewById(R.id.et_engine);

        mBtnAuthenticNow = (Button) findViewById(R.id.btn_authentic_now);
        mBtnAuthenticNow.setOnClickListener(this);

        mValidator = new EditTextValidator(this)
            .setButton(mBtnAuthenticNow)
            .add(new ValidationModel(new NameValidation(mEtName)))
            .add(new ValidationModel(new CarFrameNumValidation(mEtFrame)))
            .add(new ValidationModel(new CarEngineNumValidation(mEtEngine)))
            .execute();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_authentic_now:
                if (mValidator.validate()) {
                    //提交验证车牌
                    doAuthentic();
                }
                break;
        }
    }

    /**
     * 提交验证车牌
     */
    private void doAuthentic() {
        if (!validateUserIdAndToken()) return;
        String userId = SPUtils.getUserId(this);
        AuthenticByHand entity = new AuthenticByHand();
        entity.setCarNo(mEtPlate.getText().toString());
        entity.setUserId(userId);
        entity.setCarUserName(mEtName.getText().toString());
        entity.setCarUserPhone(SPUtils.getLastPhone(this, ""));
        entity.setEngineNo(mEtEngine.getText().toString());
        entity.setVinNo(mEtFrame.getText().toString());

        LogUtils.d(entity.toString());

         HttpHelper.getRetrofit().create(PlateNumAPI.class).authenticByHand(entity)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void bean) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
