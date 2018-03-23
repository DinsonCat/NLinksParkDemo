package com.nlinks.parkdemo.module.usercenter.monthly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.MonthlyAPI;
import com.nlinks.parkdemo.entity.monthly.MonthlyPark;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.usercenter.monthly.adapter.MonthlyPaymentListAdapter;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.widget.recycleview.EmptyRecyclerView;
import com.nlinks.parkdemo.widget.recycleview.LinearRecyclerViewOnScroller;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import java.util.ArrayList;

import io.reactivex.Observable;

public class MonthlyListActivity extends BaseActivity implements TextWatcher {

    private MonthlyPaymentListAdapter mListAdapter;
    private LinearRecyclerViewOnScroller mScrollListener;
    private EmptyRecyclerView mRvContent;
    private EditText mEtInput;
    private SwipeRefreshLayout mRefresher;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;

    public static void start(Context context) {
        Intent starter = new Intent(context, MonthlyListActivity.class);
        context.startActivity(starter);
    }

    private ArrayList<MonthlyPark> mData = new ArrayList<>();
    private MonthlyAPI mApi = HttpHelper.getRetrofit().create(MonthlyAPI.class);
    private int mCurrentPage = 1;
    private static final int sPageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_payment_list);

        initUI();
        getDataFromServer(true);
        startLocate();

    }

    private void initUI() {
        mRvContent = findViewById(R.id.rv_content);
        mListAdapter = new MonthlyPaymentListAdapter(this, mData);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new LinearSpaceItemDecoration(0, 20));
        mRvContent.setAdapter(mListAdapter);
        mRvContent.setEmptyView(findViewById(R.id.iv_empty));

        final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer(true);
            }
        };

        mRefresher = findViewById(R.id.srl_refresher);
        mRefresher.setOnRefreshListener(refreshListener);
        mScrollListener = new LinearRecyclerViewOnScroller(mRefresher) {
            @Override
            public void loadMore() {
                getDataFromServer(false);
            }
        };

        mEtInput = findViewById(R.id.etInput);
        mEtInput.addTextChangedListener(this);

    }

    private void getDataFromServer(final boolean reload) {

        if (reload) {
            mCurrentPage = 1;
        }

       mApi.getRuleList(mEtInput.getText().toString(), mCurrentPage, sPageSize)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<MonthlyPark>>() {
            @NonNull
            @Override
            public void onHandleSuccess(ArrayList<MonthlyPark> monthlyParks) {
                if (reload) {
                    mData.clear();
                }
                mCurrentPage++;
                mData.addAll(monthlyParks);
                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mRefresher.setRefreshing(false);
            }
        });
    }

    @Override
    public void titleRightBtnClick(View view) {
        MonthlyOrderListActivity.start(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getDataFromServer(true);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 定位
     */
    private void startLocate() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span = 1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        //option.setScanSpan(LocationClientOption.MIN_SCAN_SPAN);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        //option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        //option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        //option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        //        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        //        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        //开启定位
        mLocationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            TextView tvLocation = findViewById(R.id.tvLocation);
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                StringBuffer sb = new StringBuffer("当前位置: ");
                sb.append(location.getCity());
                sb.append(location.getDistrict());
                sb.append(location.getStreet());
                tvLocation.setText(sb.toString());
                LogUtils.d("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                tvLocation.setText("网络不通导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                tvLocation.setText("如果当前未开启飞行模式，可以试着重启手机重试");
            } else {
                tvLocation.setText("定位失败，请返回重试");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRvContent.addOnScrollListener(mScrollListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRvContent.removeOnScrollListener(mScrollListener);
        mLocationClient.unRegisterLocationListener(mLocationListener);
    }
}
