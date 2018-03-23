package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.AppointmentApi;
import com.nlinks.parkdemo.api.ParkShareAPI;
import com.nlinks.parkdemo.entity._req.LockCtrl;
import com.nlinks.parkdemo.entity.parkshare.ParkLokerInfo;
import com.nlinks.parkdemo.entity.parkshare.ShareParkList;
import com.nlinks.parkdemo.entity.parkshare.ShareParkStateBean;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.CustomProgressDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailReleaseActivity extends BaseActivity {

    private static final String KEY_PARK = "park";
    private ShareParkList mData;

    private int mCurrentState = AppConst.STATUS_LOCK_DOWN;//当前状态，站立

    public static final int REQUEST_REFRESH = 102;

    private ImageButton mIbLock;
    private AppointmentApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepark_release);

        initUI();

        initLokerState();
    }

    private void initLokerState() {
        HttpHelper.getRetrofit().create(ParkShareAPI.class)
            .getParkLockerInfo(mData.getParkSharingId())
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ParkLokerInfo>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(ParkLokerInfo parkLokerInfo) {
                //重置按钮的状态和图片
                NlinksParkUtils.resetImageButton(parkLokerInfo.getStatus(), mIbLock);
                mCurrentState = parkLokerInfo.getStatus();
            }
        });
    }

    private void initUI() {
        mData = getIntent().getParcelableExtra(KEY_PARK);

        if (mData.getSharingStatus() == 2) {
            //处理已发布状态
            ((ViewStub) findViewById(R.id.vs_content)).inflate();
            TextView tv_price = findViewById(R.id.tv_price);
            String s_money = String.format("\u3000出租价格 : <font color=\"#4187ff\">%s</font>元/小时",
                StringUtils.formatMoney(mData.getMoney()));
            tv_price.setText(Html.fromHtml(s_money));
            TextView tv_share_time = findViewById(R.id.tv_share_time);
            tv_share_time.setText("您的分享时间 :\n" + mData.getStartTime() + " - " + mData.getEndTime());
        }
        //设置状态详情
        ShareParkStateBean stateBean = NlinksParkUtils
            .formatShareParkState(mData.getAuditStatus(), mData.getSharingStatus());
        TextView tv_state_detail = findViewById(R.id.tv_state_detail);
        tv_state_detail.setText(stateBean.getDetailState());
        tv_state_detail.setTextColor(getResources().getColor(stateBean.getColor()));

        //设置左图标
        Drawable drawable = getResources().getDrawable(stateBean.getDrawableLeft());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_state_detail.setCompoundDrawables(drawable, null, null, null);

        //设置公共信息
        TextView tv_park_name = findViewById(R.id.tv_park_name);
        tv_park_name.setText("停车场名称 : " + mData.getParkName());
        TextView tv_stall_name = findViewById(R.id.tv_stall_name);
        tv_stall_name.setText("\u3000\u3000车位号 : " + mData.getStallName());

        mIbLock = findViewById(R.id.ib_lock);

        CommonTitleBar titlebar = findViewById(R.id.titlebar);
        titlebar.setRightBtnVisible(true);
        titlebar.setRightBtnText(mData.getSharingStatus() == 2 ? "" : "分享车位");
        titlebar.getBtnRight().setOnClickListener(mData.getSharingStatus() == 2 ? null : shareParkListener);

        //        titlebar.setRightBtnText(mData.getSharingStatus() == 2 ? "取消分享" : "分享车位");
        //        titlebar.getBtnRight().setOnClickListener(mData.getSharingStatus() == 2 ? unShareParkListener : shareParkListener);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(new HttpHelper.LoggingInterceptor());//添加拦截器 日志
        Retrofit retrofit = HttpHelper.getRetrofit().newBuilder().client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())//对http请求结果进行统一的预处理
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxjava提供支持
            .baseUrl(AppConst.URL_PREFIX).build();
        mApi = retrofit.create(AppointmentApi.class);

    }

    /**
     * 分享车位
     */
    private View.OnClickListener shareParkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ReleaseParkActivity.start(DetailReleaseActivity.this, mData.getParkSharingId(), REQUEST_REFRESH);
        }
    };

    /**
     * 取消分享车位
     */
    private View.OnClickListener unShareParkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIUtils.showToast("程序猿正在加班做功能...");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) switch (requestCode) {
            case REQUEST_REFRESH:
                setResult(RESULT_OK);
                finish();
                break;
        }

    }

    public static void start(Activity context, ShareParkList sharePark) {
        Intent intent = new Intent(context, DetailReleaseActivity.class);
        intent.putExtra(KEY_PARK, sharePark);
        context.startActivityForResult(intent, REQUEST_REFRESH);
    }

    public void onLockClick(View view) {
        LockCtrl lockCtrl = null;
        mIbLock.setEnabled(false);
        if (mCurrentState == AppConst.STATUS_LOCK_DOWN) {
            lockCtrl = LockCtrl.up(mData.getDeviceCode());
            LogUtils.e("发送地锁上升指令");
        } else if (mCurrentState == AppConst.STATUS_LOCK_UP) {
            lockCtrl = LockCtrl.down(mData.getDeviceCode());
            LogUtils.e("发送地锁下降指令");
        }
        if (lockCtrl == null) return;
        mApi.lockerControl(lockCtrl)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this, false) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
            }

            @Override
            public void onComplete() {
                super.onComplete();
                initLokerState();
                mIbLock.setEnabled(true);
            }

        });
    }


        /*if (cloudType == 1) {
            cloudType = 2;
        } else if (cloudType == 2) {
            cloudType = 1;
        }
        String clr = cloudType == 1 ? "下降" : "上升";
        UIUtils.showToast("发送指令:" + clr + "  ctlType=" + cloudType);
        Observable observable = HttpHelper.getRetrofit().create(AppointmentApi.class)
            .cloudLockerControl(cloudType);
        observable.compose(composeFunction).subscribe(new BaseObserver<Void>(pd) {
            @Override
            public void onHandleSuccess(Void aVoid) {
                UIUtils.showToast("服务器返回200");
            }
        });*/

    private int cloudType = 2;
}
