package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.RedeemBuyGoldCode;
import com.nlinks.parkdemo.entity._req.RedeemCode;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Dinson - 2017/7/8
 */
public interface RedeemCodeApi {

    /**
     * 获取手机验证码
     */
    @POST("api/redeemCode/getRedeemCode")
    Observable<HttpResult<String>> getRedeemCode(@Body RedeemCode entity);

    /**
     * 获取手机验证码
     */
    @POST("api/redeemCode/getRedeemBuyGoldCode")
    Observable<HttpResult<String>> getRedeemBuyGoldCode(@Body RedeemBuyGoldCode entity);

}
