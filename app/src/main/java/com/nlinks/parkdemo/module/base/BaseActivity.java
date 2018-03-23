package com.nlinks.parkdemo.module.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.login.view.LoginActivity;
import com.nlinks.parkdemo.utils.NetworkUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.SystemBarTintManager;
import com.nlinks.parkdemo.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 所有Activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    public static final int LOGIN_CODE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity的出现动画
        overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
    }



    @Override
    protected void onStart() {
        super.onStart();
        //去除系统自带背景，必须在setContentView后调用
        //getWindow().setBackgroundDrawable(null);
    }

    /**
     * 状态栏透明之后，标题栏会顶上去。所以标题栏的大小要加大
     */
    public void setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 状态栏透明之后，状态栏不会顶上去，标题栏多大就是多大
     * SystemBarTintManager，这时可以通过框架设置标题栏的颜色
     *
     * @param color
     */
    public void setStatusBarColor(int color) {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);// 通知栏所需颜色
        }
    }

    public void jumpTo(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    public void jumpTo(Class<? extends Activity> clazz, Bundle options) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(options);
        startActivity(intent);
    }

    /**
     * 验证用户id和token(打印“验证失效，请重新登录”)
     *
     * @return
     */
    public boolean validateUserIdAndToken() {
        return validateUserIdAndToken(true);
    }

    /**
     * 验证用户id和token
     *
     * @return
     */
    public boolean validateUserIdAndToken(boolean showFlag) {
        //没网络不跳转
        if (!NetworkUtils.isNetworkAvailable()) {
            UIUtils.showToast("网络连接失败，请检查网络");
            return false;
        }
        //userid或者token异常重新登录
        if (StringUtils.isEmpty(SPUtils.getUserId(this, "")) || StringUtils
            .isEmpty(SPUtils.getToken(this, ""))) {
            if (showFlag) UIUtils.showToast("验证失效，请重新登录");
            SPUtils.resetUser(this);
            jumpToForResult(LoginActivity.class, LOGIN_CODE);
            return false;
        }
        return true;
    }


    /**
     * 验证userid或者token才执行跳转
     *
     * @param clazz
     */
    public void validateTojump(Class<? extends Activity> clazz) {
        if (validateUserIdAndToken(false)) {
            startActivity(new Intent(this, clazz));
        }
    }

    public void jumpToForResult(Class<? extends Activity> clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }

    public void jumpToForResult(Class<? extends Activity> clazz, int requestCode, Bundle options) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(options);
        startActivityForResult(intent, requestCode);
    }


    /**
     * 标题栏的左边按钮的点击事件
     */
    public void titleLeftBtnClick(View view) {
        onBackPressed();
    }

    /**
     * 标题栏的右边按钮的点击事件
     */
    public void titleRightBtnClick(View view) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (finishWithAnim())
            overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_to_right);
    }


    public boolean finishWithAnim() {
        return true;
    }

}

