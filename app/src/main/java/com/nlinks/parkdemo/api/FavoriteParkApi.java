package com.nlinks.parkdemo.api;

import com.nlinks.parkdemo.entity.favoritepark.FavorParkInfo;
import com.nlinks.parkdemo.http.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 关注停车场
 */
public interface FavoriteParkApi {
    /**
     * 获取已关注停车场列表
     */
    @GET("api/favoritePark/list")
    Observable<HttpResult<ArrayList<FavorParkInfo>>> getFollowParks(@Query("phoneNo") String phoneNo, @Query("pageIndex") int page, @Query("pageSize") int pageSize);
}
