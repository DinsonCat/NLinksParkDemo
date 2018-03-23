package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.couponactivity.CouponAd;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Dinson - 2017/9/6
 */
public interface CouponActivityApi {
    /**
     * 获取停车记录广告
     */
    @GET("api/couponActivity/activityInfo")
    Observable<HttpResult<CouponAd>> getActivityInfo(@Query("userId") String userId);
}
