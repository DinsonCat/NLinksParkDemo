package com.nlinks.parkdemo.module.home.model;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by du on 2017/3/28.
 */

public interface IHomeModel {

    void loadNearParking(LatLng latLng, String radius, String phone, String isAppoint, OnLoadPackCallback callback);
    void loadMessageCount();
    void loadMyLocation();

}
