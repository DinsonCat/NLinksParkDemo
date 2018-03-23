package com.nlinks.parkdemo.module.usercenter.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.home.view.MainActivity;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;


public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    long arr[] = new long[5];//5击事件

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutUsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        initUI();

    }

    private void initUI() {
        TextView tv_company = (TextView) findViewById(R.id.tv_company);
        int year = Integer.parseInt(DateUtils.getYear());
        String yearStr = "-" + year;
        if (year <= 2015) yearStr = "";
        tv_company.setText("Copyright©2015" + yearStr + "福建网链科技有限公司");
        findViewById(R.id.tv_protocol).setOnClickListener(this);
        findViewById(R.id.easter_egg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_protocol:
                //jumpTo(RegisterProtocolActivity.class);
//                UserCenterActivity.start(this);
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
                            AppConst.SERVER_ADD="http://59.61.216.123:14101/";
                            SPUtils.resetUser(AboutUsActivity.this);
                            startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
                        }
                    });
                    dialog.show();
                }
                break;
        }
    }
}
