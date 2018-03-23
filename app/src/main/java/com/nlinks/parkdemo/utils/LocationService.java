package com.nlinks.parkdemo.utils;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * @author baidu
 */
public class LocationService {

    private static LocationService sInstance = null;

    private LocationClient client = null;
    private LocationClientOption DIYoption;

    /***
     * @param locationContext
     */
    public static LocationService getInstance(Context locationContext) {
        if (sInstance == null) {
            synchronized (LocationService.class) {
                sInstance = new LocationService(locationContext.getApplicationContext());
            }
        }
        return sInstance;
    }

    /***
     *
     * @return DefaultLocationClientOption
     */
    public static LocationClientOption getDefaultLocationClientOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        //option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        option.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        return option;
    }

    /***
     * @param appContext
     */
    private LocationService(Context appContext) {
        synchronized (LocationService.class) {
            if (client == null) {
                client = new LocationClient(appContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    /***
     *
     * @param listener
     * @return
     */

    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public LocationClientOption getOption() {
        return DIYoption;
    }

    public void start() {
        synchronized (LocationService.class) {
            if (client != null && !client.isStarted()) {
                client.start();
            }
        }
    }

    public void requestLocation() {
        synchronized (LocationService.class) {
            if (client != null && client.isStarted()) {
                client.requestLocation();
            }
        }
    }

    public void stop() {
        synchronized (LocationService.class) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    public boolean requestHotSpotState() {
        return client.requestHotSpotState();
    }

}
