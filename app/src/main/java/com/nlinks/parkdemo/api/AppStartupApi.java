package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.appstartup.StartupInfo;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Dinson - POST /api/appStartup/
 */
public interface AppStartupApi {
    /**
     * 登录
     */
    @GET("api/appStartup/startupInfo")
    Observable<HttpResult<StartupInfo>> getStartUpPic();
}
