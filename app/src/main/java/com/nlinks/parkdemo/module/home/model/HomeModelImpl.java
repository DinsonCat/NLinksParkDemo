package com.nlinks.parkdemo.module.home.model;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.nlinks.parkdemo.api.ParkAPI;
import com.nlinks.parkdemo.entity.park.ParkInfo;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by du on 2017/3/28.
 */
public class HomeModelImpl implements IHomeModel {

    private ParkAPI mAPI;

    public HomeModelImpl() {
        mAPI = HttpHelper.getRetrofit().create(ParkAPI.class);
    }

    @Override
    public void loadNearParking(LatLng latLng, String radius, String phone, String isAppoint, final OnLoadPackCallback callback) {
        mAPI.getParks(latLng.latitude + "", latLng.longitude + "", radius, phone, isAppoint).enqueue(
                new Callback<HttpResult<ParkInfo>>() {
                    @Override
                    public void onResponse(Call<HttpResult<ParkInfo>> call, Response<HttpResult<ParkInfo>> response) {
                        HttpResult<ParkInfo> park = response.body();
                        callback.onSuccess(park);
                    }

                    @Override
                    public void onFailure(Call<HttpResult<ParkInfo>> call, Throwable t) {
                        Log.i("Tag", "Throwable=" + t);
                        callback.onFailure();
                    }
                });
    }

    @Override
    public void loadMessageCount() {

    }

    @Override
    public void loadMyLocation() {

    }

}
