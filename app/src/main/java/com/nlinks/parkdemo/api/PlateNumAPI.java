package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.AuthenticByHand;
import com.nlinks.parkdemo.entity._req.AutoPay;
import com.nlinks.parkdemo.entity._req.ChangeBrands;
import com.nlinks.parkdemo.entity._req.PlateNum;
import com.nlinks.parkdemo.entity.plate.PlateInfo;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 车牌相关API
 */
public interface PlateNumAPI {
    /**
     * 添加车牌
     */
    @POST("api/plateNum/addCar")
    Observable<HttpResult<PlateInfo>> addPlate(@Header("Authorization") String Authorization, @Body PlateNum entity);

    /**
     * 删除车牌
     */
    @DELETE("api/plateNum/delCar")
    Observable<HttpResult<Void>> deletePlate(@Query("carIds") String carIds);

    /**
     * 获取已绑定的车牌列表
     */
    @GET("api/plateNum/carNoList")
    Observable<HttpResult<ArrayList<PlateInfo>>> getPlateList(@Query("userId") String userId);

    /**
     * 获取已绑定的车牌列表
     */
    @POST("api/plateNum/autoPay")
    Observable<HttpResult<String>> setAutoPay(@Body AutoPay entity);

    /**
     * 手动认证车牌
     */
    @POST("api/plateNum/certification/hand")
    Observable<HttpResult<Void>> authenticByHand(@Body AuthenticByHand entity);

    /**
     * 修改车牌
     */
    @POST("api/plateNum/changeBrands")
    Observable<HttpResult<Void>> changeBrands(@Body ChangeBrands entity);

    /**
     * 照片认证车牌carProspectImg"; filename="image.png
     */
    @Multipart
    @POST("api/plateNum/certification/recognition")
    Observable<HttpResult<Void>> authentic(@Query("userId") String userId, @Query("carNo") String plate
        , @Part("driveImg\"; filename=\"image.jpg") RequestBody photo, @Part("carProspectImg\"; filename=\"image.jpg") RequestBody carProspectImg);
}
