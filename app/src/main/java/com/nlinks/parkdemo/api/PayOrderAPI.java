package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.ParkRecordReq;
import com.nlinks.parkdemo.entity.park.ParkRecordOrder;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 支付订单API
 * POST /api/payOrder/submitOrder
 */
public interface PayOrderAPI {
    @POST("api/payOrder/submitOrder")
    Observable<HttpResult<ParkRecordOrder>> submitOrder(@Body ParkRecordReq entity);
}
