package com.nlinks.parkdemo.module.home.presenter;

import com.baidu.mapapi.model.LatLng;
import com.nlinks.parkdemo.module.home.model.HomeModelImpl;
import com.nlinks.parkdemo.module.home.model.IHomeModel;
import com.nlinks.parkdemo.module.home.model.OnLoadPackCallback;
import com.nlinks.parkdemo.module.home.view.IHomeView;

/**
 * Created by du on 2017/3/28.
 */

public class HomePresenterImpl implements IHomePresenter {

    private IHomeView mHomeView;
    private IHomeModel mHomeModel;

    public HomePresenterImpl(IHomeView homeView) {
        mHomeView = homeView;
        mHomeModel = new HomeModelImpl();
    }

    @Override
    public void loadNearParking(LatLng latLng, String radius, String phone, String isAppoint, OnLoadPackCallback callback) {
        mHomeModel.loadNearParking(latLng, radius, phone, isAppoint, callback);
    }

    @Override
    public void loadMessageCount() {
        mHomeModel.loadMessageCount();
    }

    @Override
    public void locationInMap() {
        mHomeView.locationInMap();
    }

    @Override
    public void zoomInMap(boolean in) {
        mHomeView.zoomInMap(in);
    }


}
