package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.AppointmentApi;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.UIUtils;

import io.reactivex.Observable;

public class CloudTestActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIbLock;

    public static void start(Context context) {
        Intent starter = new Intent(context, CloudTestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_test);

        mIbLock = findViewById(R.id.ib_lock);
        mIbLock.setOnClickListener(this);

        mIbLock.setImageResource(R.mipmap.bg_lockup_normal);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_lock:
                onLockClick();
                break;
        }
    }

    private int cloudType = 1;

    private void onLockClick() {
         HttpHelper.getRetrofit().create(AppointmentApi.class)
            .cloudLockerControl(cloudType)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                if (cloudType == 1) {
                    cloudType = 2;
                    mIbLock.setImageResource(R.mipmap.bg_lockdown_normal);
                } else if (cloudType == 2) {
                    cloudType = 1;
                    mIbLock.setImageResource(R.mipmap.bg_lockup_normal);
                }
            }
        });
    }
}
