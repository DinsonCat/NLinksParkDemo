package com.nlinks.parkdemo.api;

import android.support.annotation.Nullable;

import com.nlinks.parkdemo.entity._req.PayOrderReq;
import com.nlinks.parkdemo.entity._req.SubmitMemOrder;
import com.nlinks.parkdemo.entity.pay.AliPayOrder;
import com.nlinks.parkdemo.entity.pay.MemberOrder;
import com.nlinks.parkdemo.entity.pay.OrderList;
import com.nlinks.parkdemo.entity.pay.WXPayOrder;
import com.nlinks.parkdemo.entity.pay.WalletMoney;
import com.nlinks.parkdemo.entity.pay.WalletMoneyDetail;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 */
public interface PayAPI {

    /**
     * 获取账户余额信息
     */
    @GET("api/payCash/cashInfo")
    Observable<HttpResult<WalletMoney>> getBalance(@Query("userId") String userId);

    /**
     * 获取账户充值明细
     */
    @GET("api/payCash/cashFlow")
    Observable<HttpResult<List<WalletMoneyDetail>>> getWalletDetail(
        @Query("userId") String userId,
        @Nullable @Query("pageIndex") int pageIndex,
        @Nullable @Query("pageSize") int pageSize,
        @Nullable @Query("field") String field,
        @Nullable @Query("direction") String direction);

    /**
     * 获取订单列表
     */
    @GET("api/payOrder/list")
    Observable<HttpResult<ArrayList<OrderList>>> getOrderList(
        @Query("plateNum") String plateNum,
        @Query("userId") String userId,
        @Nullable @Query("orderStatus") Integer orderStatus,
        @Nullable @Query("parkCode") String parkCode,
        @Nullable @Query("pageIndex") Integer pageIndex,
        @Nullable @Query("pageSize") Integer pageSize,
        @Nullable @Query("field") String field,
        @Nullable @Query("direction") String direction);

    /**
     * 支付宝下单
     *
     * @param orderReq
     * @return
     */
//	@POST("http://59.61.216.123:14101/appapi/api/alipayOpen/orderInfo")
    @POST("api/alipayOpen/orderInfo")
    Observable<HttpResult<AliPayOrder>> alipayOrderInfo(@Body PayOrderReq orderReq);

    /**
     * 微信下单
     *
     * @param orderReq
     * @return
     */
//	@POST("http://59.61.216.123:14101/appapi/api/weixinPay/unifiedorder")
    @POST("api/weixinPay/unifiedorder")
    Observable<HttpResult<WXPayOrder>> weixinOrderInfo(@Body PayOrderReq orderReq);

    /**
     * 钱包支付
     *
     * @param orderReq
     * @return
     */
//	@POST("http://59.61.216.123:14101/appapi/api/weixinPay/unifiedorder")
    @POST("api/payOrder/walletPay")
    Observable<HttpResult<Void>> walletOrder(@Body PayOrderReq orderReq);

    /**
     * 提交会员订单
     *
     * @param entity 实体类
     * @return
     */
    @POST("api/payOrder/submitMemOrder")
    Observable<HttpResult<MemberOrder>> submitMemOrder(@Body SubmitMemOrder entity);


}
