package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity._req.FeedBack;
import com.nlinks.parkdemo.entity.feedback.FbResult;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by PC-DINSON on 2017/4/12.
 */
public interface FeedBackAPI {
    /**
     * 提交工单
     */
    @POST("api/workOrder/submit")
    Observable<HttpResult<Void>> submit(@Body FeedBack entity);


    /**
     * 上传单张照片
     */
    @Multipart
    @POST("api/common/uploadPhoto")
    Observable<HttpResult<FbResult>> uploadPhoto(@Part("photo\"; filename=\"image.png") RequestBody photo);


}
