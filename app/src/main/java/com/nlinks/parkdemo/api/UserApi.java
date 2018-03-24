package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.Login;
import com.nlinks.parkdemo.entity._req.RegisterAndForgetPwd;
import com.nlinks.parkdemo.entity._req.UpdateNickName;
import com.nlinks.parkdemo.entity._req.UpdateSex;
import com.nlinks.parkdemo.entity._req.Updatepwd;
import com.nlinks.parkdemo.entity.user.LoginResultData;
import com.nlinks.parkdemo.entity.user.RegisterResult;
import com.nlinks.parkdemo.entity.user.SmsCode;
import com.nlinks.parkdemo.entity.user.UserInfo;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 用户中心API
 */
public interface UserApi {

    /**
     * 登录
     */
    @POST("api/user/login")
    Observable<HttpResult<LoginResultData>> loginByPwd(@Body Login entity);

    /**
     * 验证码登录
     */
    @POST("api/user/captchaLogin")
    Observable<HttpResult<LoginResultData>> LoginBySms(@Body Login entity);

    /**
     * 退出登录
     */
    @POST("api/user/logout")
    Observable<HttpResult<Void>> logout(@Query("token") String token, @Query("userId") String userId);

    /**
     * 获取手机验证码
     */
    @POST("api/user/smsVerifyCode")
    Observable<HttpResult<Void>> getCode(@Body SmsCode code);

    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("api/user/getUserInfo")
    Observable<HttpResult<UserInfo>> getUserInfo(@Query("userId") String userId);

    /**
     * 修改密码
     */
    @PUT("api/user/updatePassword")
    Observable<HttpResult<Void>> updatePwd(@Body Updatepwd entity);

    /**
     * 修改用户昵称
     *
     * @return
     */
    @POST("api/user/updateNickName")
    Observable<HttpResult<Void>> updateUserName(@Body UpdateNickName entity);

    /**
     * 修改用户性别
     *
     * @return
     */

    @POST("api/user/updateSex")
    Observable<HttpResult<Void>> updateSex(@Body UpdateSex entity);

    /**
     * 用户头像上传
     *
     * @return
     */
    @Multipart
    @POST("api/user/uploadPhoto")
    Observable<HttpResult<Void>> uploadPhoto(@Query("phoneNo") String phoneNo, @Part("photo\"; filename=\"user.png")
        RequestBody photo);

    /**
     * 用户是否已注册
     */
    @GET("api/user/checkUserExistByPhoneNo")
    Observable<HttpResult<Void>> checkExist(@Query("phoneNo") String phoneNo);


    /**
     * 注册
     */
    @POST("api/user/register")
    Observable<HttpResult<RegisterResult>> register(@Body RegisterAndForgetPwd entity);

    /**
     * 重置密码
     */
    @PUT("api/user/forgetPassword")
    Observable<HttpResult<Void>> forgetPwd(@Body RegisterAndForgetPwd entity);

    /**
     * 重置密码
     */
    @GET("api/user/checkSmsCodeValid")
    Observable<HttpResult<Void>> checkSmsCodeValid(@Query("phoneNo") String phoneNo, @Query("smsCode") String smsCode);

}
