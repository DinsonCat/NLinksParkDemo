package com.nlinks.parkdemo.module.usercenter.setting;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewNormalActivity;
import com.nlinks.parkdemo.module.home.view.MainActivity;
import com.nlinks.parkdemo.module.update.UpdateVersion;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.SwitchButton;

import io.reactivex.Observable;

public class SettingListActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    long arr[] = new long[5];//5击事件

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);

        initUI();
    }

    private void initUI() {
        //已登录的初始化已登录条目
        if (!StringUtils.isEmpty(SPUtils.getUserId(this, "")) && !StringUtils.isEmpty(SPUtils.getToken(this, ""))) {
            ((ViewStub) findViewById(R.id.vs_logined)).inflate();
            findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_change_pwd).setOnClickListener(this);
        }

        //当前版本
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("v" + UIUtils.getVersionName(this));

        findViewById(R.id.tv_about_us).setOnClickListener(this);
        findViewById(R.id.tv_update_vs).setOnClickListener(this);

        //常见问题
        findViewById(R.id.tv_common_problems).setOnClickListener(this);

        SwitchButton sb_ios = (SwitchButton) findViewById(R.id.sb_ios);
        sb_ios.setCheckedImmediately(SPUtils.getAutoDownMap(this));
        sb_ios.setOnCheckedChangeListener(this);

        findViewById(R.id.easter_egg).setOnClickListener(this);

    }

    public void doLogoutClick(View v) {
        if (!validateUserIdAndToken()) return;

        String token = SPUtils.getToken(this, "");
        String userId = SPUtils.getUserId(this);
        HttpHelper.getRetrofit().create(UserApi.class).logout(token, userId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this,false) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                reSetLoginState();
            }

            @Override
            public void onHandleError(int code, String message) {
                reSetLoginState();
            }
        });
    }

    /**
     * 无论成功或失败都重置登录状态
     */
    private void reSetLoginState() {
        SPUtils.resetUser(this);
        finish();

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_pwd:
                validateTojump(UpdatePwdActivity.class);
                break;
            case R.id.tv_about_us:
                WebViewNormalActivity.start(this, "关于我们", AppConst.URL_ABOUT_US);
                //jumpTo(AboutUsActivity.class);
//                ParkRecordDeatilActivity.start(this,"be718231914b11e7ad1d1c1b0dc73439");
                break;
            case R.id.tv_update_vs:
                UpdateVersion.check(this, true);
                break;
            case R.id.tv_common_problems:
                WebViewNormalActivity.start(SettingListActivity.this, "常见问题", AppConst.URL_PREFIX + "api/web/help");
//                Uri uri = Uri.parse(GlobalConfig.URL_PREFIX+"api/web/help");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                break;
            case R.id.easter_egg:
                System.arraycopy(arr, 1, arr, 0, arr.length - 1);
                arr[arr.length - 1] = SystemClock.uptimeMillis();
                if (arr[arr.length - 1] - arr[0] < 800) {
                    final DialogCancelConfirm dialog = new DialogCancelConfirm(this);
                    dialog.setMessage("ServerAdd: " + AppConst.SERVER_ADD + "\nDo you want to toggle?");
                    dialog.setButtonsText("No", "Yes");
                    dialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
                        @Override
                        public void onLeftClick() {
                            dialog.cancel();
                        }

                        @Override
                        public void onRightClick() {
                            dialog.cancel();
                            AppConst.SERVER_ADD = "http://59.61.216.123:14101/";
                            SPUtils.resetUser(SettingListActivity.this);
                            startActivity(new Intent(SettingListActivity.this, MainActivity.class));
                        }
                    });
                    dialog.show();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SPUtils.putAutoDownMap(this, isChecked);
    }
}
