package com.nlinks.parkdemo.module.home.presenter;

import com.baidu.mapapi.model.LatLng;
import com.nlinks.parkdemo.module.home.model.OnLoadPackCallback;

/**
 * Created by du on 2017/3/28.
 */

public interface IHomePresenter {

    void loadNearParking(LatLng latLng, String radius, String phone, String isAppoint, OnLoadPackCallback callback);
    void loadMessageCount();
    void locationInMap();//定位
    void zoomInMap(boolean in);//in ? 放大 : 缩小


}
