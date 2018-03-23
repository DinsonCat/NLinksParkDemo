package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.ThirdAuthLogin;
import com.nlinks.parkdemo.entity._req.ThirdAuthLoginByQQ;
import com.nlinks.parkdemo.entity.thirdlogin.LoginResult;
import com.nlinks.parkdemo.entity.thirdlogin.SignInfo;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Dinson - 2017/8/28
 */
public interface ThirdLoginApi {
    /**
     * 微信注册
     */
    @POST("api/thirdLogin/weixinAuthLogin")
    Observable<HttpResult<LoginResult>> weixinAuthLogin(@Body ThirdAuthLogin entity);

    /**
     * 验证是否已经使用微信注册过
     */
    @GET("api/thirdLogin/checkWeixinLogin")
    Observable<HttpResult<LoginResult>> checkWeixinLogin(@Query("authCode") String authCode, @Query("cid") String cid);

    /**
     * 微信注册
     */
    @POST("api/thirdLogin/qqAuthLogin")
    Observable<HttpResult<LoginResult>> QQAuthLogin(@Body ThirdAuthLoginByQQ entity);

    /**
     * 验证是否已经使用QQ注册过
     */
    @GET("api/thirdLogin/checkQQLogin")
    Observable<HttpResult<LoginResult>> checkQQLogin(@Query("openid") String openid, @Query("accessToken") String accessToken, @Query("qqappid") String qqappid, @Query("cid") String cid);

    /**
     * 支付宝注册
     */
    @POST("api/thirdLogin/alipayAuthLogin")
    Observable<HttpResult<LoginResult>> aliAuthLogin(@Body ThirdAuthLogin entity);

    /**
     * 验证是否已经使用支付宝注册过
     */
    @GET("api/thirdLogin/checkAlipayLogin")
    Observable<HttpResult<LoginResult>> checkAlipayLogin(@Query("authCode") String authCode, @Query("cid") String cid);

    /**
     * 获取支付宝签名
     */
    @GET("api/thirdLogin/authLoginInfo")
    Observable<HttpResult<SignInfo>> authLoginInfo();

}
