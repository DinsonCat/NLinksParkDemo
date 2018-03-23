package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.park.Coordinate;
import com.nlinks.parkdemo.entity.park.FavParkReqEntity;
import com.nlinks.parkdemo.entity.park.LastestParkRecord;
import com.nlinks.parkdemo.entity.park.ParkDetail;
import com.nlinks.parkdemo.entity.park.ParkFee;
import com.nlinks.parkdemo.entity.park.ParkInfo;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.entity.park.ParkPlan;
import com.nlinks.parkdemo.entity.park.ParkRecord;
import com.nlinks.parkdemo.http.HttpResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 停车数据相关接口
 * <p/>
 * Created by lzhixing@linewell.com on 16/7/28 17:42.
 */
public interface ParkAPI {
    /**
     * 获取用户的停车记录(17-6-2弃用)
     */
//    @GET("api/park/parkingRecord")
//    Observable<HttpResult<ArrayList<ParkRecord>>> getParkRecord(
//            @Query("phoneNo") String phoneNo,
//            @Query("status") Integer status,
//            @Query("pageIndex") Integer page,
//            @Query("pageSize") Integer rows);


//================================

    /**
     * 获取所有停车场
     */
    @GET("api/park/parkSearchByLocation")
    Call<HttpResult<ParkInfo>> getParks(
        @Query("latitude") String latitude,
        @Query("longitude") String longitude,
        @Query("radius") String radius,
        @Query("phoneNo") String phoneNo,
        @Query("isAppoint") String isAppoint);

    /**
     * 获取停车场列表 带分页
     */
    @GET("api/park/parkSearchByLocationList")
    Observable<HttpResult<List<ParkMain>>> getParkList(
        @Query("latitude") double latitude,
        @Query("longitude") double longitude,
        @Query("radius") double radiusKM,
        @Query("pageIndex") int index,
        @Query("pageSize") int size,
        @Query("field") String field,
        @Query("direction") String direction,
        @Query("isAppoint") String isAppoint);

    /**
     * 获取停车场详情
     */
    @GET("api/park/parkInfo")
    Observable<HttpResult<ParkDetail>> getParkDetail(@Query("parkCode") String parkCode);

    /**
     * 获取停车场平面图
     */
    @GET("api/park/parkMap")
    Call<HttpResult<ParkPlan>> getParkPlan(@Query("parkCode") String parkCode);

    /**
     * 获取用户车辆在平面图上的位置
     */
    @GET("api/park/stallPosition")
    Call<HttpResult<Coordinate>> getCarPosition(@Query("phoneNo") String phoneNo);

    /**
     * 添加关注、取消关注
     */
    @POST("api/favoritePark/concern")
    Observable<HttpResult<Void>> followPark(@Body FavParkReqEntity entity);

    /**
     * 获取用户的停车记录
     */
    @GET("api/park/parkRecordByCarNo")
    Observable<HttpResult<ArrayList<ParkRecord>>> getParkRecord(@Query("plateNum") String plateNum, @Query("status") Integer status, @Query("pageIndex") Integer pageIndex, @Query("pageSize") Integer pageSize);

    /**
     * 获取用户的停车记录
     */
    @GET("api/park/lastestParkRecord")
    Observable<HttpResult<LastestParkRecord>> getLastestParkRecord(@Query("userId") String userId, @Query("plateNum") String plateNum);

    /**
     * 获取用户的停车记录
     */
    @GET("api/park/parkRecordByID")
    Observable<HttpResult<ParkRecord>> parkRecordByID(@Query("id") String id);

    /**
     * 获取用户的停车记录
     * GET /api/park/getParkFee
     */
    @GET("api/park/getParkFee")
    Observable<HttpResult<ArrayList<ParkFee>>> getParkFee(@Query("parkRecordId") String parkRecordId);




}
