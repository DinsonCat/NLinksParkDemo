package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.AppointTimeoutPay;
import com.nlinks.parkdemo.entity._req.AppointmentParkReq;
import com.nlinks.parkdemo.entity._req.LockCtrl;
import com.nlinks.parkdemo.entity.appointment.AppointPark;
import com.nlinks.parkdemo.entity.appointment.AppointStall;
import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;
import com.nlinks.parkdemo.entity.appointment.ForecastMoney;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 预约接口
 * Created by Dell on 2017/05/04.
 */
public interface AppointmentApi {


    /**
     * 预估车位预约金额
     */
    @GET("api/parkAppoint/appointMoney")
    Observable<HttpResult<ForecastMoney>> getForecastMoney(@Query("parkSharingId") String parkSharingId,
                                                           @Query("leaveTime") String leaveTime);

    /**
     * 获取用户车位预约记录
     *
     * @param userId
     * @param appointStatus 预约车位状态： 1进行中 2已完成 全部不传参数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    //@GET("http://59.61.216.123:14101/appapi/api/parkAppoint/findParkAppointRecordByPage")
    @GET("api/parkAppoint/findParkAppointRecordByPage")
    Observable<HttpResult<List<AppointmentRecord>>> getAppointmentRecord(@Query("userId") String userId,
                                                                         @Query("appointStatus") Integer appointStatus,
                                                                         @Query("pageIndex") int pageIndex,
                                                                         @Query("pageSize") int pageSize);

    /**
     * 地锁控制
     *
     * @param lockCtrl
     * @return
     */
    //@POST("http://59.61.216.123:14101/appapi/api/parkAppoint/lockerControl")
    @POST("api/parkAppoint/lockerControl")
    Observable<HttpResult<Void>> lockerControl(@Body LockCtrl lockCtrl);

    /**
     *
     */
    //@GET("http://59.61.216.123:14101/appapi/api/parkAppoint/findParkAppointStallByPage")
    @GET("api/parkAppoint/findParkAppointStallByPage")
    Observable<HttpResult<List<AppointStall>>> getAppointmentStall(@Query("parkCode") String parkCode,
                                                                   @Query("pageIndex") int pageIndex,
                                                                   @Query("pageSize") int pageSize);

    @POST("api/parkAppoint/appointPark")
    Observable<HttpResult<AppointPark>> appointPark(@Body AppointmentParkReq parkReq);

    @POST("api/parkAppoint/overTimePay")
    Observable<HttpResult<Void>> overTimePay(@Body AppointTimeoutPay parkReq);

    //	@T("http://59.61.216.123:14101/appapi")
    //	Observable<HttpResult<?>> t();


    /**
     * 云锁控制测试接口
     * ctlType   1下降   2上升
     * <p>
     * http://59.61.216.123:14101/appapi/api/common/lockDeviceControl?ctlType=1
     */
    @POST("api/common/lockDeviceControl")
    Observable<HttpResult<Void>> cloudLockerControl(@Query("ctlType") int type);

}
