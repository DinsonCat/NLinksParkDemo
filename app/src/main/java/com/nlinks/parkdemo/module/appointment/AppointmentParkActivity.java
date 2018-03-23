package com.nlinks.parkdemo.module.appointment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.nlinks.parkdemo.api.AppointmentApi;
import com.nlinks.parkdemo.entity.appointment.AppointStall;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListActivity;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import io.reactivex.Observable;

/**
 * 车位预约
 * Created by Dell on 2017/05/04.
 */
public class AppointmentParkActivity extends BaseListActivity<AppointStall> {

    public static final String SERIALIZABLE_PARK_LIST = "SERIALIZABLE_PARK_LIST";
    public static final String STRING_NAME = "STRING_NAME";
    public static final String STRING_ADDRESS = "STRING_ADDRESS";

    private ParkMain mParkList;
    private String mParkName, mParkCode;
    AppointmentApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApi = HttpHelper.getRetrofit().create(AppointmentApi.class);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mParkList = extras.getParcelable(SERIALIZABLE_PARK_LIST);
            if (mParkList != null) {
                mParkName = mParkList.getName();
                mParkCode = mParkList.getCode();
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitleBar(CommonTitleBar titleBar) {
        titleBar.setTitleText(!TextUtils.isEmpty(mParkName) ? mParkName : "可预定车位列表");
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new LinearSpaceItemDecoration(0, UIUtils.dip2px(10)));
    }

    @Override
    public CommonAdapter<AppointStall> initAdapter() {
        AppointStallAdapter appointStallAdapter = new AppointStallAdapter(this, mDatas);
        appointStallAdapter.setParkList(mParkList);
        return appointStallAdapter;
    }

    @Override
    public Observable initObservable(boolean reload, int page) {
        if (!TextUtils.isEmpty(mParkCode)) {
            return mApi.getAppointmentStall(mParkCode, page, AppConst.PAGE_SIZE_DEFAULT);
        }
        return null;
    }

    @Override
    protected void addDefaultData() {
        mDatas.add(0, new AppointStall());
    }

    @Override
    public void onRvItemClick(AppointStall item) {
        if (validateUserIdAndToken(false) && item != null && !TextUtils.isEmpty(item.getDeviceCode())) {
            AppointmentParkSpotActivity.start(this, mParkList, item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true);
    }
}
