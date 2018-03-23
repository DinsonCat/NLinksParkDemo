package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.msgpush.PushMessage;
import com.nlinks.parkdemo.entity.msgpush.UnReadCount;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by PC-DINSON on 2017/4/16.
 */
public interface MsgPushAPI {
    /**
     * 获取推送消息列表
     */
    @GET("api/msgpush/list")
    Observable<HttpResult<ArrayList<PushMessage>>> getList(@Query("userID") String userID, @Query("pageIndex") Integer page, @Query("pageSize") Integer size);

    /**
     * 更新推送消息状态
     */
    @POST("api/msgpush/updateIsRead")
    Observable<HttpResult<Void>> updateIsRead(@Query("id") String id);


    /**
     * 更新推送消息状态
     * GET /api/msgpush/unReadCount
     */
    @GET("api/msgpush/unReadCount")
    Observable<UnReadCount> getUnReadCount(@Query("userID") String userId);
}
