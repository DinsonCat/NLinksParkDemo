package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.MonthlyOrderReq;
import com.nlinks.parkdemo.entity._req.MonthlyParkReq;
import com.nlinks.parkdemo.entity.monthly.FeedbackReq;
import com.nlinks.parkdemo.entity.monthly.MonthlyOrderResponse;
import com.nlinks.parkdemo.entity.monthly.MonthlyPark;
import com.nlinks.parkdemo.entity.monthly.MonthlyParkInfo;
import com.nlinks.parkdemo.entity.monthly.MonthlyParkResponse;
import com.nlinks.parkdemo.entity.monthly.UserMonthlyOrderInfo;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * cu
 */
public interface MonthlyAPI {

    /**
     * 错峰包月可申请列表
     */
    @GET("api/monthly/rulelist")
    Observable<HttpResult<ArrayList<MonthlyPark>>> getRuleList(@Query("search") String search,
                                                               @Query("pageIndex") int pageIndex,
                                                               @Query("pageSize") int pageSize);

    /**
     * 错峰包月可申请列表
     */
    @GET("api/monthly/rulelist")
    Observable<HttpResult<ArrayList<MonthlyPark>>> getRuleList(@Query("pageIndex") int pageIndex,
                                                               @Query("pageSize") int pageSize);

    /**
     * 停车场错峰包月信息
     */
    @GET("api/monthly/parkRuleInfo")
    Observable<HttpResult<ArrayList<MonthlyParkInfo>>> getParkRuleInfo(@Query("parkCode") String parkCode);

    /**
     * 用户错峰包月记录列表,记录状态 0未支付 1未生效 2生效中 3已过期
     */
    @GET("api/monthly/userMonthlyRecordList")
    Observable<HttpResult<ArrayList<UserMonthlyOrderInfo>>> getUserMonthlyRecordList(
        @Query("userId") String userId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 用户错峰包月月卡列表
     */
    @GET("api/monthly/userMonthlyCardList")
    Observable<HttpResult<ArrayList<UserMonthlyOrderInfo>>> getUserMonthlyCardList(
        @Query("userId") String userId);

    /**
     * 提交错峰停车包月申请,startTime 格式：2017-11-11 00:00:00
     * POST /api/monthly/submitMonthlyPark
     */
    @POST("api/monthly/submitMonthlyPark")
    Observable<HttpResult<MonthlyParkResponse>> submitMonthlyPark(@Body MonthlyParkReq entity);

    /**
     * 提交错峰停车缴费订单, payType: 8错峰包月缴费
     * POST /api/monthly/submitMonthlyOrder
     */
    @POST("api/monthly/submitMonthlyOrder")
    Observable<HttpResult<MonthlyOrderResponse>> submitMonthlyOrder(@Body MonthlyOrderReq entity);

    /**
     * 错峰包月-关注
     * POST /api/monthly/addFeedback
     */
    @POST("api/monthly/addFeedback")
    Observable<HttpResult<Void>> addFeedback(@Body FeedbackReq entity);

}
