package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 停车券接口
 * Created by Dell on 2017/04/17.
 */
public interface PayCouponAPI {

    @GET("api/payCoupon/couponByPage")
    Observable<HttpResult<List<ParkingCoupon>>> getPayCouponList(
        @Query("couponDetailStatus") int couponDetailStatus
        , @Query("pageIndex") int pageIndex
        , @Query("pageSize") int pageSize
        , @Query("field") String field
        , @Query("direction") String direction
        , @Query("userId") String userId
    );

    /**
     * 获取所有可用的优惠券
     */
    @GET("api/payCoupon/couponByPage?couponDetailStatus=1&pageIndex=0&pageSize=0")
    Observable<HttpResult<List<ParkingCoupon>>> getAllUsablePayCoupon(@Query("userId") String userId);

    @GET("api/payCoupon/getCouponNameById")
    Observable<HttpResult<ParkingCoupon>> getPayCouponNameById(@Query("couponId") String couponId);

//	@POST("api/payCoupon/newCoupon")
//	Observable<HttpResult<ParkingCoupon>> createNewCoupon(@Query("couponDetailEntity") String couponDetailEntity);
}
