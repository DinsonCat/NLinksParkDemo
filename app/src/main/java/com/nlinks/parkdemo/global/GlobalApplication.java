package com.nlinks.parkdemo.global;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;


public class GlobalApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    
    public static String userId;
    public static String cid;
    public static String token;
    public static String plateNum;
    public static int membership = -1;
    public static String appKey;
    public static int appSecret;

    // public BMapManager mBMapManager = null;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();

        SDKInitializer.initialize(this);

        if (!AppConst.isDebug) {
            CrashHandler.getInstance().init(context, context.getCacheDir() + "/CrashTxt/");
        }


        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(),
                PackageManager .GET_META_DATA);
            appKey = info.metaData.getString("API_APPKEY");
            appSecret = info.metaData.getInt("API_APPSECRET");
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }

        // initEngineManager(this);
 

        if (SPUtils.getBoolean(getApplicationContext(), "nlcgv2", "cls", true)) {
            SPUtils.resetUser(getApplicationContext());
            SPUtils.putBoolean(getApplicationContext(), "nlcgv2", "cls", false);
        }
    }


    /**
     * 初始化全景地图引擎
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/20 23:33
     */
   /* public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
        LogUtils.d("initEngineManager");
    }*/
    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
