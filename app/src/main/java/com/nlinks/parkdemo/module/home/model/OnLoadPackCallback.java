package com.nlinks.parkdemo.module.home.model;

import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.entity.park.ParkInfo;

/**
 * Created by du on 2017/3/29.
 */

public interface OnLoadPackCallback {

    void onSuccess(HttpResult<ParkInfo> park);
    void onFailure();
}
