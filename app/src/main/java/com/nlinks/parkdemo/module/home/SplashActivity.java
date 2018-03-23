package com.nlinks.parkdemo.module.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.appstartup.StartupInfo;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.home.view.MainActivity;
import com.nlinks.parkdemo.service.SplashDownLoadService;
import com.nlinks.parkdemo.utils.CacheUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 欢迎页
 */
public class SplashActivity extends Activity implements View.OnClickListener {

    /*private static final int SPLASH_TIME = 2000;
    private static final int ALL_READY = 101;*/
    //private long mStartTime;// 开始时间

    /*private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ALL_READY:// 如果城市列表加载完毕，就发送此消息
                    long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
                    if (loadingTime < SPLASH_TIME) {// 如果比最小显示时间还短，就延时进入MainActivity，否则直接进入
                        mHandler.postDelayed(goToMainActivity, SPLASH_TIME - loadingTime);
                    } else {
                        mHandler.post(goToMainActivity);
                    }
                    break;
                default:
                    break;
            }
        }
    };*/
    //进入下一个Activity
    /*Runnable goToMainActivity = new Runnable() {
        @Override
        public void run() {

        }
    };*/
    private TextView mSpJumpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gotoMainActivity();

        //init();
    }

    private void init() {

        mSpJumpBtn =  findViewById(R.id.sp_jump_btn);
        mSpJumpBtn.setOnClickListener(this);

        setLocalImage();

        //SplashDownLoadService.startDownLoadSplashImage(this);

        //Message message = new Message();
        //message.what = ALL_READY;
        //mHandler.handleMessage(message);
    }

    //////////////////////////////////////2017-6-26修改/////////////////////////////////////////////////////
    private void setLocalImage() {
        final StartupInfo splash = CacheUtils.getStartupInfo();
        //如果取出本地序列化的对象成功 则进行图片加载和倒计时
        if (splash != null && splash.getLocalPath() != null) {
            LogUtils.i("SplashActivity 获取本地序列化成功");
            final ImageView sp_bg = (ImageView) findViewById(R.id.sp_bg);

            Glide.with(this).load(splash.getLocalPath()).dontAnimate().into(sp_bg);
            findViewById(R.id.iv_bottom).setVisibility(View.VISIBLE);
            sp_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countDownTimer.cancel();
                    finish();
                    LogUtils.e("最新活动：" + splash.getLinkUrl());
                    WebViewPromotionActivity.start(SplashActivity.this, splash.getLinkUrl(), true);
                }
            });
            countDownTimer.start();
        } else {
            // 如果本地没有 直接跳转
            mSpJumpBtn.setVisibility(View.INVISIBLE);
            mSpJumpBtn.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoMainActivity();
                }
            }, 400);
        }

    }

    private CountDownTimer countDownTimer = new CountDownTimer(3200, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mSpJumpBtn.setText("跳过(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            mSpJumpBtn.setText("跳过(" + 0 + "s)");
            gotoMainActivity();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sp_jump_btn:
                gotoMainActivity();
                break;
        }
    }

    private void gotoMainActivity() {
        startActivity(new Intent(SplashActivity.this,   MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
