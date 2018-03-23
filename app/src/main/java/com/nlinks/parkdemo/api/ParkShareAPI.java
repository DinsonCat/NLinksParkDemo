package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.AddSharePark;
import com.nlinks.parkdemo.entity._req.PublishPark;
import com.nlinks.parkdemo.entity.parkshare.ParkLokerInfo;
import com.nlinks.parkdemo.entity.parkshare.ShareParkList;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 车位共享API
 */

public interface ParkShareAPI {
    /**
     * 获取共享车位列表
     */
//    @GET("http://59.61.216.123:14101/appapi/api/parkSharing/findParkSharingStallList")
    @GET("api/parkSharing/findParkSharingStallList")
    Observable<HttpResult<ArrayList<ShareParkList>>> getParkSharingList(@Query("userId") String userId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);


    /**
     * 添加共享车位
     */
//    @POST("http://59.61.216.123:14101/appapi/api/parkSharing/addParkingSharing")
    @POST("api/parkSharing/addParkingSharing")
    Observable<HttpResult<Void>> addSharePark(@Body AddSharePark entity);

    /**
     * 获取地锁状态
     */
//    @GET("http://59.61.216.123:14101/appapi/api/parkSharing/getParkLockerInfo")
    @GET("api/parkSharing/getParkLockerInfo")
    Observable<HttpResult<ParkLokerInfo>> getParkLockerInfo(@Query("parkSharingId") String parkSharingId);

    /**
     * 发布车位
     */
//    @POST("http://59.61.216.123:14101/appapi/api/parkSharing/publishParking")
    @POST("api/parkSharing/publishParking")
    Observable<HttpResult<ParkLokerInfo>> publishParking(@Body PublishPark entity);
}
