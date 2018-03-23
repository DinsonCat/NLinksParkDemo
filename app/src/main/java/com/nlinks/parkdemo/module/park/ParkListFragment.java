package com.nlinks.parkdemo.module.park;

import android.support.v7.widget.RecyclerView;

import com.nlinks.parkdemo.api.ParkAPI;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListFragment;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import io.reactivex.Observable;

/**
 * 停车场列表
 * Created by du on 2017/3/30.
 */
public class ParkListFragment extends BaseListFragment<ParkMain> {

    private static final int REQUEST_CODE_FOLLOW = 2;

    private int mFlag = 0;
    private double mLat, mLng;
    private ParkAPI mParkAPI;
    private int mPage = 1;
    private ParkMain mMaybeChangedPark = null;
    private String mAppoint = null;

    public static ParkListFragment createInstance(int flag, double lat, double lng, String appoint) {
        ParkListFragment fragment = new ParkListFragment();
        /*Bundle bundle = new Bundle();
		bundle.putInt("flag", flag);
		fragment.setArguments(bundle);*/
        fragment.mFlag = flag;
        fragment.mLat = lat;
        fragment.mLng = lng;
        fragment.mAppoint = appoint;
        fragment.mParkAPI = HttpHelper.getRetrofit().create(ParkAPI.class);
        return fragment;
    }

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new LinearSpaceItemDecoration(0, UIUtils.dip2px(10)));
    }

    @Override
    public CommonAdapter<ParkMain> initAdapter() {
        return new ParkListAdapter(mContext, mDatas);
    }

    @Override
    public Observable initObservable(boolean reload) {
        String field, order;
        if (mFlag == 1) {
            field = "distance";
            order = "ASC";
        } else {
            field = "unuedStallNum";
            order = "DESC";
        }
        LogUtils.d("------ lat,lng = " + mLat + ',' + mLng);
        if (reload) mPage = 1;

        return mParkAPI.getParkList(mLat, mLng, AppConst.PARK_SEARCH_RADIUS
            , mPage++, AppConst.PAGE_SIZE_DEFAULT, field, order, mAppoint);
    }

    @Override
    public void onRvItemClick(ParkMain item) {
        mMaybeChangedPark = item;
        ParkDetailActivity.start(getContext(), item.getCode());
    }


}
