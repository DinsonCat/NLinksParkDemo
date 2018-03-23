package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.advertising.AdvertisingInfo;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yanzhenyang on 2017/7/6.
 */

public interface ActivitiesApi {

    /**
     * 获取活动列表
     */
    @GET("api/couponActivity/getCouponActivityList")
    Observable<HttpResult<ArrayList<AdvertisingInfo>>> getActivityInfos(@Query("status") int status, @Query("pageIndex") int page, @Query("pageSize") int pageSize);
}
