package com.nlinks.parkdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关
 */
public class NetworkUtils {

    /**
     * 获取活动网络信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) UIUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isNetworkAvailable() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

}
