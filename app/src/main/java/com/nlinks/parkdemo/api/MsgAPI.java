package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.infomation.ActivityMsg;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PC-DINSON on 2017/4/16.
 */
public interface MsgAPI {
    /**
     * 获取停车资讯列表
     */
    @GET("api/msg/getActivityMsg")
    Observable<HttpResult<ArrayList<ActivityMsg>>> getActivityMsg(@Query("pageIndex") Integer page, @Query("pageSize") Integer size);




}
