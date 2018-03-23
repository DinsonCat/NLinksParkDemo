package com.nlinks.parkdemo.api;


import com.nlinks.parkdemo.entity.common.LatestVersion;
import com.nlinks.parkdemo.http.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dell on 2017/05/24.
 */
public interface CommonApi {

	/**
	 * 获取Android最新版本
	 * @param versionCode
	 * @return
	 */
	@GET("api/common/androidLatestVersion")
	Observable<HttpResult<LatestVersion>> checkLatestVersion(@Query("versionCode") int versionCode);
}
