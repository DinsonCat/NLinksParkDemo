package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ParkShareAPI;
import com.nlinks.parkdemo.entity._req.AddSharePark;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.DialogAd;
import com.nlinks.parkdemo.widget.EditTextClear;

/**
 * 添加共享车位
 */
public class AddShareParkActivity extends BaseActivity {

    private EditTextClear mEtParkNum;
    private EditTextClear mEtParkName;
    private EditTextClear mEtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_share_parking);

        initUI();
    }

    private void initUI() {
        mEtPhone = findViewById(R.id.et_phone);
        mEtParkNum = findViewById(R.id.et_park_num);
        mEtParkName = findViewById(R.id.et_park_name);
        mEtPhone.setText(SPUtils.getLastPhone(this, ""));
    }

    public static void start(Activity context, int requestCode) {
        Intent intent = new Intent(context, AddShareParkActivity.class);
        context.startActivityForResult(intent, requestCode);
    }


    /**
     * 点击确认提交
     */
    public void doSubmit(View view) {
        String sParkName = formatEt(mEtParkName);
        String sPhone = formatEt(mEtPhone);
        String sParkNum = formatEt(mEtParkNum);
        String userId = SPUtils.getUserId(this);

        if (StringUtils.isEmpty(sParkName)) {
            UIUtils.showToast("请完善资料");
            return;
        }
        if (StringUtils.isEmpty(sPhone)) {
            UIUtils.showToast("请完善资料");
            return;
        }
        if (StringUtils.isEmpty(sParkNum)) {
            UIUtils.showToast("请完善资料");
            return;
        }
        if (StringUtils.isEmpty(userId)) {
            UIUtils.showToast("请完善资料");
            return;
        }

        AddSharePark bean = new AddSharePark(sParkName, sPhone, sParkNum, userId);
        HttpHelper.getRetrofit().create(ParkShareAPI.class).addSharePark(bean)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                DialogAd dialogAd = new DialogAd(AddShareParkActivity.this);
                dialogAd.setTitle("提交成功");
                dialogAd.setMessage("客服会尽快联系您~");
                dialogAd.setOnCancelListener(dialog -> {
                    setResult(RESULT_OK);
                    finish();
                });
                dialogAd.show();
            }
        });
    }

    private String formatEt(EditText et) {
        return et.getText().toString().trim().replaceAll(" ", "");
    }
}
