package com.nlinks.parkdemo.module.home.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.MsgPushAPI;
import com.nlinks.parkdemo.api.ParkAPI;
import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;
import com.nlinks.parkdemo.entity.msgpush.UnReadCount;
import com.nlinks.parkdemo.entity.park.FavPark;
import com.nlinks.parkdemo.entity.park.LastestParkRecord;
import com.nlinks.parkdemo.entity.park.ParkInfo;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.appointment.AppointmentParkActivity;
import com.nlinks.parkdemo.module.appointment.AppointmentRecordActivity;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.home.ActivitiesActivity;
import com.nlinks.parkdemo.module.home.model.OnLoadPackCallback;
import com.nlinks.parkdemo.module.home.presenter.HomePresenterImpl;
import com.nlinks.parkdemo.module.home.presenter.IHomePresenter;
import com.nlinks.parkdemo.module.messagecenter.MessageCenterActivity;
import com.nlinks.parkdemo.module.park.ParkDetailActivity;
import com.nlinks.parkdemo.module.park.ParkDetailBaiduActivity;
import com.nlinks.parkdemo.module.park.ParkListActivity;
import com.nlinks.parkdemo.module.park.navi.NaviCommon;
import com.nlinks.parkdemo.module.search.SearchLocationActivity;
import com.nlinks.parkdemo.module.update.UpdateVersion;
import com.nlinks.parkdemo.module.usercenter.UserCenterActivity;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.LocationService;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.NetworkUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.utils.TitleCompat;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.DialogOnekeyPark;
import com.nlinks.parkdemo.widget.DialogParkorder;
import com.nlinks.parkdemo.widget.marker.ParkMarker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.nlinks.parkdemo.module.home.view.MainActivity.LAYER_MODE.BAIDU;
import static com.nlinks.parkdemo.module.home.view.MainActivity.LAYER_MODE.DRAGONFLY;
import static com.nlinks.parkdemo.module.home.view.MainActivity.LAYER_MODE.DRAGONFLY_APPOINT;
import static com.nlinks.parkdemo.module.home.view.MainActivity.LAYER_MODE.NORMAL;

/**
 *
 */
public class MainActivity extends BaseActivity implements IHomeView, BDLocationListener, BaiduMap.OnMapStatusChangeListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener, SensorEventListener, BaiduMap.OnMapLoadedCallback, CompoundButton.OnCheckedChangeListener, DialogParkorder.OnNaviClickListener, View.OnClickListener {

    private static final int REQUEST_CODE_SEARCH = 1;

    private IHomePresenter mHomePresenter;

    //baidu map
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //baidu location
    private LocationService mLocationService;
    private boolean mIsRequest = false;//是否触发定位
    private boolean mIsFirstLoc = true;//是否首次定位

    //	private LatLng mLastLoc;//上次定位的地址
    private BNRoutePlanNode mStartNode;//导航开始的地方

    private float mZoomLevel = 18f;
    //	private GeoCoder mGeoCoder;
    //	private PoiInfo mStartPoiInfo;
    private UiSettings mUiSettings;
    private SensorManager mSensorManager;
    private double lastX = 0.0;
    private int mCurrentDirection = 0;
    private float mCurrentAccracy = 0;
    //当前定位位置
    private double mCurrentLocLat = 0.0;
    private double mCurrentLocLon = 0.0;
    //当前中心位置
    private double mCurrentCenLat = 0.0;
    private double mCurrentCenLon = 0.0;
    private MyLocationData locData;

    private TextView mTvParkCount;

    //private boolean mRoadStatesShowing = false, mSatelliteShowing = false;

    private Map<String, ParkMarker> mLatLngParkMarker = new HashMap<>();//存放标注点对象
    private List<Overlay> mBaiduNearMarkerLimit100 = new ArrayList<>(100);
    //    private Marker now_marker;//当前的标注点
    private Marker mSearchedMarker;//搜索出来的标注点

    private NaviCommon navi;
    // private View mRecommendView;
    //private PanoramaView mPanoramaView;
    //private InfoWindow mRecommendInfoWindow, mPanoramaInfoWindow;

    private String mIsAppoint = null;
    private PoiSearch mPoiSearch;
    private Set<PoiInfo> mPOISet = new HashSet<>();
    private BitmapDescriptor mBaiDuParkingDescriptor;
    //private PoiInfo mPoiInfo = null;
    private int mMapCircleFill;

    //===================================================

    private ArrayList<ParkMain> mParkInfo = new ArrayList<>();//当前数据集
    //private CountDownTimer mTimer;//预约dialog的倒计时

    private FrameLayout mFlParkCount;
    private TextView mTvSearch;


    private long mClickStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitleCompat.setDefault(this);


        initLayer();//初始化底部卡片
        initUI();


        mHomePresenter = new HomePresenterImpl(this);
        mHomePresenter.loadMessageCount();

        navi = new NaviCommon(this);
        if (navi.initDirs()) {
            navi.initNavi();
        }
        initMap();

        //UpdateVersion.check(this, false);

        //showParkOrder();//请求最后一条未支付信息

        loadRedPoint();//加载首页消息小红点
    }


    /**
     * 加载首页消息小红点
     */
    private void loadRedPoint() {
        String userId = SPUtils.getUserId(this);
        if (StringUtils.isEmpty(userId)) return;
        HttpHelper.getRetrofit().create(MsgPushAPI.class).getUnReadCount(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<UnReadCount>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(UnReadCount value) {
                    if (value.getStatusCode() == 200 && value.getUnreadTotal() != 0) {
                        findViewById(R.id.ivRedPoint).setVisibility(VISIBLE);
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
    }

    /**
     * 请求最后一条未支付信息
     */
    private void showParkOrder() {
        String userPlate = SPUtils.getUserPlate(this, "");
        String userId = SPUtils.getUserId(this);
        if (StringUtils.isEmpty(userPlate) || StringUtils.isEmpty(userId)) return;
        HttpHelper.getRetrofit().create(ParkAPI.class).getLastestParkRecord(userId, userPlate)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<LastestParkRecord>() {
            @NonNull
            @Override
            public void onHandleSuccess(LastestParkRecord bean) {
                if (bean.getParkRecord().getParkName() != null) {
                    DialogParkorder dialog = new DialogParkorder(MainActivity.this);
                    dialog.setData(bean.getParkRecord());
                    dialog.show();
                }
                if (bean.getAppointRecord().getName() != null) {
                    DialogParkorder dialog = new DialogParkorder(MainActivity.this);
                    dialog.setData(bean.getAppointRecord());
                    dialog.setOnNaviClickListener(MainActivity.this);
                    dialog.show();
                }
            }
        });
    }


    //private TextView mTvParkCount;//车位统计
    private void initUI() {
        mTvParkCount = findViewById(R.id.tv_near_hint);
        mFlParkCount = (FrameLayout) findViewById(R.id.fl_near_parking);

        if (!NetworkUtils.isNetworkAvailable()) {
            mTvParkCount.setText(TextColorBuilder.newBuilder().addTextPart(this, R.color.map_mark_pink, "当前无网络，无法正常使用地图功能").buildSpanned());
        }

        //findViewById(R.id.tv_onekey_park).setOnClickListener(this);
        mTvSearch = findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(this);

    }


    private void initMap() {
        mMapView = (MapView) findViewById(R.id.mv_baidu_map_view);
        mMapView.setVisibility(INVISIBLE);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务

        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        //初始化显示最近一次的定位地点
        LatLng lastLatLng = MapUtil.getLastLocation(this);
        if (lastLatLng != null) {
            mCurrentCenLat = mCurrentLocLat = lastLatLng.latitude;
            mCurrentCenLon = mCurrentLocLon = lastLatLng.longitude;
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(lastLatLng));
            //loadData(lastLatLng);
        }
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mZoomLevel));//设置缩放级别 3-20级
        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMarkerClickListener(this);


        mBaiduMap.setOnMapClickListener(this);
        MapUtil.hideBaiduMapLogo(mMapView, 2);//隐藏百度LOGO及控件  设置控件位置 mapView.setZoomControlsPosition()

        mLocationService = LocationService.getInstance(this);
        mLocationService.registerListener(this);
        mLocationService.start();

        //		 mGeoCoder = GeoCoder.newInstance();//初始化地址解析
        //		mGeoCoder.setOnGetGeoCodeResultListener(this);
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setOverlookingGesturesEnabled(false);//设置是否允许俯视手势
        mUiSettings.setRotateGesturesEnabled(false);//设置是否允许旋转手势

        BitmapDescriptor currentMarker = BitmapDescriptorFactory.fromResource(R.drawable.map_location);
        mMapCircleFill = ContextCompat.getColor(this, R.color.map_accuracy_circle_fill);


        //BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.map_location);


        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, currentMarker//currentMarker//
            , mMapCircleFill, 0x00000000));//设置精度圈描边颜色

        //	bitmapDescriptor.recycle();

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(mPoiListener);
        mBaiDuParkingDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.map_other_marker);
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mLocationService.stop();
        mMapView.onPause();
        //if (mPanoramaView != null) mPanoramaView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mLocationService.start();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        mMapView.onResume();
        //if (mPanoramaView != null) mPanoramaView.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期 与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        mLocationService.unregisterListener(this);
        mLocationService.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView = null;
        //		mGeoCoder.destroy();
        mPoiSearch.destroy();
        //if (mTimer != null) mTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void locationInMap() {
        mIsRequest = true;
        mLocationService.requestLocation();
    }


    @Override
    public void onMapLoaded() {
        mMapView.setVisibility(VISIBLE);
        //updateCircle();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        LogUtils.d("--------------  onReceiveLocation");
        locData = new MyLocationData.Builder().latitude(mCurrentLocLat = bdLocation.getLatitude()).direction(mCurrentDirection).longitude(mCurrentLocLon = bdLocation.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);


        mCurrentAccracy = bdLocation.getRadius();

        //每次定位成功后都更新导航起始位置
        mStartNode = new BNRoutePlanNode(bdLocation.getLongitude(), bdLocation.getLatitude(), bdLocation.getAddrStr(), bdLocation.getLocationDescribe(), BNRoutePlanNode.CoordinateType.BD09LL);

        if (mIsFirstLoc || mIsRequest) {

            String city = bdLocation.getCity();

            LatLng ll = new LatLng(mCurrentLocLat, mCurrentLocLon);

            //			mLastLoc = ll;
            if (mIsFirstLoc) {
                loadData(ll);
            }

            MapStatusUpdate newLatLng = MapStatusUpdateFactory.newLatLng(ll);
            //			msu = newLatLng;
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mZoomLevel));//设置缩放级别 3-20级
            mBaiduMap.animateMapStatus(newLatLng);

            MapUtil.setLastLocation(this, ll);//设置最近一次的定位地点
            MapUtil.setNowCity(this, city);//设置当前城市
            //			mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
            mIsFirstLoc = false;
            mIsRequest = false;
        }
    }


    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        if (mapStatus != null) {
            // updateCircle();

            LatLng target = mapStatus.target;
            mCurrentCenLat = target.latitude;
            mCurrentCenLon = target.longitude;
            loadData(target);
        }
    }

    private Marker mSelectMarker = null;

    private void scaleMarker(Marker marker) {
        //点击同一个marker
        if (mSelectMarker == marker) return;
        //点击不同的marker，先重置之前选中的marker
        resetSelectMarker();
        mSelectMarker = marker;
        Bitmap bitmap = marker.getIcon().getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(1.1f, 1.1f);
        Bitmap newBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(newBm));
        newBm.recycle();
    }

    private void resetSelectMarker() {
        if (mSelectMarker == null) return;
        Bitmap bitmap = mSelectMarker.getIcon().getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(0.91f, 0.91f);
        Bitmap newBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mSelectMarker.setIcon(BitmapDescriptorFactory.fromBitmap(newBm));
        newBm.recycle();
        bitmap.recycle();
        setParkCard(LAYER_MODE.NORMAL, null);//初始化成最初布局
        mSelectMarker = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mSelectMarker == marker) {
            return true;
        }
        scaleMarker(marker);
        Bundle extraInfo = marker.getExtraInfo();
        if (extraInfo != null && extraInfo.get("poi") != null) {
            //点击百度的停车场
            PoiInfo info = extraInfo.getParcelable("poi");

            if (info != null) {
                ParkMain parkMain = new ParkMain();
                parkMain.setName(info.name);
                parkMain.setAddress(info.address);
                parkMain.setBaiduPark(true);
                parkMain.setLatitude(info.location.latitude);
                parkMain.setLongitude(info.location.longitude);
                double distance = DistanceUtil.getDistance(new LatLng(mCurrentCenLat, mCurrentCenLon), info.location);
                parkMain.setDistance(distance);

                setParkCard(LAYER_MODE.BAIDU, parkMain);//显示百度地图卡片
                this.mSelectMarker = marker;
            }
        } else {
            //点击蜻蜓停车场
            ParkMarker parkMarker = mLatLngParkMarker.get(marker.getPosition().toString());
            if (parkMarker != null) {
                //显示卡片
                setParkCard(mIsAppoint == null ? LAYER_MODE.DRAGONFLY : LAYER_MODE.DRAGONFLY_APPOINT, parkMarker.getPark());
            }
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        resetSelectMarker();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    OnGetPoiSearchResultListener mPoiListener = new OnGetPoiSearchResultListener() {
        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果
            if (result == null) return;

            List<PoiInfo> allPoi = result.getAllPoi();
            if (allPoi == null) return;
            for (Overlay marker : mBaiduNearMarkerLimit100)
                marker.remove();

            int size = allPoi.size();

            mTvParkCount.setText(TextColorBuilder.newBuilder().addTextPart("附近有").addTextPart(MainActivity.this, R.color.colorPrimary, size).addTextPart("家停车场").buildSpanned());

            mParkInfo.clear();
            for (PoiInfo info : allPoi) {
                if (info != null && info.location != null && !mPOISet.contains(info)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("poi", info);
                    MarkerOptions options = new MarkerOptions().icon(mBaiDuParkingDescriptor).position(info.location).extraInfo(bundle);
                    Marker marker = (Marker) mBaiduMap.addOverlay(options);
                    mBaiduNearMarkerLimit100.add(marker);
                    mPOISet.add(info);

                    ParkMain parkMain = new ParkMain();
                    parkMain.setName(info.name);
                    parkMain.setAddress(info.address);
                    parkMain.setLatitude(info.location.latitude);
                    parkMain.setLongitude(info.location.longitude);
                    parkMain.setBaiduPark(true);
                    LatLng latLng = new LatLng(info.location.latitude, info.location.longitude);
                    String key = latLng.toString();
                    ParkMarker parkMarker = mLatLngParkMarker.get(key);
                    if (parkMarker == null) {
                        parkMarker = new ParkMarker(MainActivity.this, parkMain);
                        mLatLngParkMarker.put(key, parkMarker.relevantTo(marker));
                    }
                    parkMain.setUnuedStallNum(-1);
                    mParkInfo.add(parkMain);
                }
            }
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };


    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder().accuracy(mCurrentAccracy)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection).latitude(mCurrentLocLat).longitude(mCurrentLocLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH:
                    if (data != null) {
                        if (mSearchedMarker != null) {
                            mSearchedMarker.remove();
                            mSearchedMarker = null;
                        }

                        resetSelectMarker();
                        PoiInfo poiInfo = data.getParcelableExtra(SearchLocationActivity.PARCELABLE_SEARCH_RESULT);
                        mTvSearch.setText(poiInfo.name);

                        MarkerOptions mo = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)).position(poiInfo.location).zIndex(30);
                        mSearchedMarker = (Marker) mBaiduMap.addOverlay(mo);

                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.target(poiInfo.location);
                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                        //mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(poiInfo.location));
                        loadData(poiInfo.location);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void markPark(List<ParkMain> parkList, List<FavPark> favParkList) {
        if (parkList == null) return;
        int parkSize = parkList.size();

        //本地保存关注列表
        if (favParkList != null && favParkList.size() > 0) {
            SPUtils.putFavoList(this, favParkList);
        }

        Set<String> keySet = mLatLngParkMarker.keySet();
        List<String> stringList = new ArrayList<>();
        stringList.addAll(keySet);

        for (int i = 0; i < parkSize; i++) {
            ParkMain park = parkList.get(i);
            LatLng latLng = new LatLng(park.getLatitude(), park.getLongitude());
            String key = latLng.toString();
            ParkMarker parkMarker = mLatLngParkMarker.get(key);
            if (parkMarker == null) {
                parkMarker = new ParkMarker(this, park);
                parkMarker.forceAppointment(mIsAppoint != null);
                MarkerOptions mo = new MarkerOptions()
                    //.animateType(MarkerOptions.MarkerAnimateType.drop)
                    .icon(parkMarker.createDescriptor()).position(latLng).zIndex(20);
                Marker marker = (Marker) mBaiduMap.addOverlay(mo);
                mLatLngParkMarker.put(key, parkMarker.relevantTo(marker));
            } else {
                stringList.remove(key);
                parkMarker.updatePark(park);
                if (parkMarker.diff(park.getUnuedStallNum(), park.getIsAppoint(), park.getAppointCount())) {
                    parkMarker.forceAppointment(mIsAppoint != null);
                    parkMarker.updateMarker(false);
                }
            }
        }

        for (String key : stringList) {
            ParkMarker remove = mLatLngParkMarker.remove(key);
            if (remove != null) remove.remove();
        }
        //
        //        if (mSearchedMarker != null || now_marker != null) scaleMarker();
    }

    private void loadData(final LatLng latLng) {

        resetSelectMarker();

        mHomePresenter.loadNearParking(latLng, AppConst.PARK_SEARCH_RADIUS + "", SPUtils.getLastPhone(this, ""), mIsAppoint, new OnLoadPackCallback() {
            @Override
            public void onSuccess(HttpResult<ParkInfo> park) {
                int size = 0;
                if (park != null) {
                    ParkInfo data = park.getData();
                    if (data != null) {
                        mParkInfo.clear();
                        mParkInfo.addAll(data.getParkInfo());
                        if (mParkInfo != null) size = mParkInfo.size();
                        markPark(mParkInfo, data.getFavPark());
                    }
                    for (Overlay marker : mBaiduNearMarkerLimit100)
                        marker.remove();
                }
                if (size == 0 && mIsAppoint == null) {
                    //UIUtils.showToast("附近" + GlobalConfig.PARK_SEARCH_RADIUS + "公里内没有停车场");
                    mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword("停车场").radius(AppConst.PARK_SEARCH_RADIUS * 1000).sortType(PoiSortType.distance_from_near_to_far).location(latLng));
                    mFlParkCount.setVisibility(INVISIBLE);
                } else {
                    mFlParkCount.setVisibility(VISIBLE);
                    mTvParkCount.setText(TextColorBuilder.newBuilder().addTextPart("附近有").addTextPart(MainActivity.this, R.color.colorPrimary, size).addTextPart("家停车场").buildSpanned());
                    mOnekeyPark.setVisibility(mIsAppoint == null ? VISIBLE : INVISIBLE);
                }
                //				mTvParkCount.setText(TextColorBuilder.newBuilder().addTextPart(MainActivity.this
                //						, R.color.global_title_bg, GlobalConfig.PARK_SEARCH_RADIUS).addTextPart("公里内有")
                //						.addTextPart(MainActivity.this, R.color.global_title_bg, size).addTextPart("家停车场")
                //						.buildSpanned());
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 设置图层模式
     */
    //layer
    private View mParkCard, mAppointmentPark, mOnekeyPark, mCardArrow;
    //卡片布局内控件
    private TextView mCardParkName, mCardParkAddress, mCardParkRest, mCardParkDistance;


    private void initLayer() {
        mParkCard = findViewById(R.id.rl_tmp_park_info);
        mOnekeyPark = findViewById(R.id.tv_onekey_park);
        mOnekeyPark.setOnClickListener(this);
        mAppointmentPark = findViewById(R.id.fl_appointment_parking);
        mCardArrow = findViewById(R.id.tv_arrow);

        //初始化右上角切换地图层
        CheckBox cb_map_road = findViewById(R.id.cb_map_road);
        cb_map_road.setOnCheckedChangeListener(this);
        CheckBox cb_map_appointment = findViewById(R.id.cb_map_appointment);
        cb_map_appointment.setOnCheckedChangeListener(this);

        //卡片信息
        mCardParkName = findViewById(R.id.tv_park_name);
        mCardParkAddress = findViewById(R.id.tv_park_address);
        mCardParkRest = findViewById(R.id.tv_park_rest);
        mCardParkDistance = findViewById(R.id.tv_park_distance);

        GifView gifView = findViewById(R.id.gif_activit_gv);
        // 设置背景gif图片资源
        gifView.setMovieResource(R.raw.gift_activity);
        gifView.setOnClickListener(v -> jumpTo(ActivitiesActivity.class));
        // 设置暂停
        // gifView.setPaused(true);
    }

    /**
     * 跳转停车场列表
     */
    public void onGetParkList(View view) {
        Bundle bundle = new Bundle();
        bundle.putDouble(ParkListActivity.DOUBLE_LAT, mCurrentCenLat);
        bundle.putDouble(ParkListActivity.DOUBLE_LNG, mCurrentCenLon);
        bundle.putString(ParkListActivity.STRING_APPOINT, mIsAppoint);
        jumpTo(ParkListActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_onekey_park:
                onClickOneKeyPark(v);
                break;
            case R.id.tv_search:
                doSearch(v);
                break;
        }
    }


    enum LAYER_MODE {
        NORMAL, BAIDU, DRAGONFLY, DRAGONFLY_APPOINT
    }

    //private LAYER_MODE mCurrentMode = LAYER_MODE.NORMAL;

    /**
     * 根据LAYER_MODE设置布局，传入park数据显示，传normal可以传null
     *
     * @param mode     NORMAL, BAIDU, DRAGONFLY, DRAGONFLY_APPOINT
     * @param parkList ParkMain
     */
    private void setParkCard(LAYER_MODE mode, ParkMain parkList) {
        //mCurrentMode = mode;
        //mOnekeyPark.setVisibility(mode == NORMAL ? VISIBLE : INVISIBLE);
        mParkCard.setVisibility(mode == LAYER_MODE.NORMAL ? GONE : VISIBLE);
        if (mode == LAYER_MODE.NORMAL) return;//普通布局不在执行，节省性能
        mCardParkRest.setVisibility(mode == LAYER_MODE.BAIDU ? GONE : VISIBLE);
        mCardParkDistance.setVisibility(mode == LAYER_MODE.BAIDU ? GONE : VISIBLE);
        mAppointmentPark.setVisibility(mode == LAYER_MODE.DRAGONFLY_APPOINT ? VISIBLE : GONE);
        mCardArrow.setVisibility(mode == LAYER_MODE.DRAGONFLY ? VISIBLE : GONE);

        if (parkList == null) return;//非空判断

        mCardParkName.setText(parkList.getName());
        if (mode != LAYER_MODE.BAIDU) {
            if (mode == LAYER_MODE.DRAGONFLY) {
                mCardParkRest.setText(TextColorBuilder.newBuilder().addTextPart("总车位：").addTextPart(this, R.color.mainactivity_yellow, parkList.getStallNum()).buildSpanned());
                mCardParkDistance.setText(TextColorBuilder.newBuilder().addTextPart("空车位：").addTextPart(this, R.color.mainactivity_yellow, parkList.getUnuedStallNum()).buildSpanned());
            } else if (mode == LAYER_MODE.DRAGONFLY_APPOINT) {
                mCardParkRest.setText(TextColorBuilder.newBuilder().addTextPart("总空车位：").addTextPart(this, R.color.mainactivity_yellow, parkList.getUnuedStallNum()).buildSpanned());
                mCardParkDistance.setText(TextColorBuilder.newBuilder().addTextPart("可预约车位：").addTextPart(this, R.color.mainactivity_yellow, parkList.getAppointCount()).buildSpanned());
            }
        }

        LatLng latLng = MapUtil.getLastLocation(this);
        if (latLng != null) {
            LatLng latLng1 = new LatLng(parkList.getLatitude(), parkList.getLongitude());
            double distance = DistanceUtil.getDistance(latLng, latLng1);
            mCardParkAddress.setText(String.format("%s | %s", parkList.getAddress(), MapUtil.getConvertDistance(distance)));
        } else {
            mCardParkAddress.setText(parkList.getAddress());
        }
    }

    /**
     * 点击用户中心
     */
    public void doJumpUsercenter(View view) {
        UserCenterActivity.start(this);
    }

    /**
     * 点击消息中心
     */
    public void doJumpMessageCenter(View view) {
        findViewById(R.id.ivRedPoint).setVisibility(GONE);
        jumpTo(MessageCenterActivity.class);
    }


    /**
     * 点击右上角的我的预约记录
     */
    public void dojumpMyAppoint(View view) {
        validateTojump(AppointmentRecordActivity.class);
    }

    /**
     * 点击预约车位
     */
    public void doJumpParkAppoint(View view) {
        //未登录，跳转登录
        if (!validateUserIdAndToken(false)) return;
        //不是会员，弹出成为会员
        /*if (!SPUtils.isMember(this)) {
            final DialogCancelConfirm dialog = new DialogCancelConfirm(this);
            dialog.setButtonsText("取消", "成为金卡会员");
            dialog.setMessage("您还不是金卡会员\n不能预约哦");
            dialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
                @Override
                public void onLeftClick() {
                    dialog.dismiss();
                }

                @Override
                public void onRightClick() {
                    WebViewPromotionActivity.start(MainActivity.this, AppConst.URL_MEMBER);
                    dialog.dismiss();
                }
            });
            dialog.show();
            return;
        }*/
        //跳转预约页面
        if (mSelectMarker != null) {
            ParkMarker parkMarker = mLatLngParkMarker.get(mSelectMarker.getPosition().toString());
            if (parkMarker != null) {
                ParkMain park = parkMarker.getPark();
                if (park != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppointmentParkActivity.SERIALIZABLE_PARK_LIST, park);
                    bundle.putString(AppointmentParkActivity.STRING_NAME, park.getName());
                    bundle.putString(AppointmentParkActivity.STRING_ADDRESS, park.getAddress());
                    jumpTo(AppointmentParkActivity.class, bundle);
                }
            }
        }
    }

    /**
     * 点击跳转停车场详情
     */
    public void doJumpParkDetail(View view) {
        if (mSelectMarker != null) {
            ParkMarker parkMarker = mLatLngParkMarker.get(mSelectMarker.getPosition().toString());
            if (parkMarker != null) {
                ParkMain park = parkMarker.getPark();
                if (park != null) {
                    if (park.isBaiduPark()) {
                        ParkDetailBaiduActivity.start(this, park);
                    } else {
                        ParkDetailActivity.start(this, park.getCode());
                    }
                }
            }
        }
    }

    /**
     * 点击一键停车
     */
    public void onClickOneKeyPark(View view) {

        LogUtils.e("点击一键停车");
        if (mParkInfo.isEmpty()) {
            UIUtils.showToast("当前无数据，请定位后重试");
            return;
        }

        ParkComparator pc = new ParkComparator();
        Collections.sort(mParkInfo, pc);

        ArrayList<ParkMain> parkMains = new ArrayList<>();
        int count = 0;
        for (ParkMain parkMain : mParkInfo) {
            if (parkMain.getUnuedStallNum() > 0 || parkMain.getUnuedStallNum() == -1) {
                parkMains.add(parkMain);
                count++;
            }
            if (count == 3) break;
        }
        if (parkMains.isEmpty()) {
            UIUtils.showToast("附近没有停车场");
            return;
        }


        DialogOnekeyPark dialog = new DialogOnekeyPark(this, parkMains, mStartNode);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
        //        OnekeyParkActivity.startForResult(this, parkMains, mStartNode);
    }

    /**
     * 点击立即导航
     */
    public void doNavigation(View view) {
        View fl_navi = findViewById(R.id.fl_navigation_now);
        fl_navi.setEnabled(false);
        if (BaiduNaviManager.isNaviInited() && mSelectMarker != null) {

            LogUtils.e("导航初始化完毕。当前点不为空");
            ParkMarker parkMarker = mLatLngParkMarker.get(mSelectMarker.getPosition().toString());
            if (parkMarker != null) {
                LogUtils.e("当前停车信息不为空");
                ParkMain park = parkMarker.getPark();
                if (park != null) {
                    LogUtils.e("当前停车场不为空");
                    double distance = MapUtil.getDistance(mStartNode.getLatitude(), mStartNode.getLongitude(), park.getLatitude(), park.getLongitude());
                    if (distance < 100) {
                        UIUtils.showToast("距离太近无法导航");
                        fl_navi.setEnabled(true);
                        return;
                    }
                    BNRoutePlanNode endNode = new BNRoutePlanNode(park.getLongitude(), park.getLatitude(), park.getName(), park.getAddress(), BNRoutePlanNode.CoordinateType.BD09LL);
                    boolean navi = this.navi.routeplanToNavi(mStartNode, endNode, fl_navi);
                    if (!navi) {
                        UIUtils.showToast("导航规划失败");
                    }
                }
            }
        }
        fl_navi.setEnabled(true);
    }

    /**
     * 点击导航
     */
    @Override
    public void onNaviClick(AppointmentRecord appointRecord, View view) {
        double distance = MapUtil.getDistance(mStartNode.getLatitude(), mStartNode.getLongitude(), appointRecord.getLatitude(), appointRecord.getLongitude());
        if (distance < 100) {
            UIUtils.showToast("距离太近无法导航");
            view.setEnabled(true);
            return;
        }


        BNRoutePlanNode endNode = new BNRoutePlanNode(appointRecord.getLongitude(), appointRecord.getLatitude(), appointRecord.getName(), appointRecord.getAddress(), BNRoutePlanNode.CoordinateType.BD09LL);
        boolean navi = this.navi.routeplanToNavi(mStartNode, endNode, view);
        if (!navi) {
            UIUtils.showToast("导航规划失败");
            view.setEnabled(true);
        }

        //  UIUtils.showToast("暂时无经纬度导航");

    }


    /**
     * 点击搜索
     */
    public void doSearch(View view) {
        jumpToForResult(SearchLocationActivity.class, REQUEST_CODE_SEARCH);
    }

    /**
     * 语音输入
     */
    public void onVoiceInput(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(SearchLocationActivity.BOOL_USE_VOICE, true);
        jumpToForResult(SearchLocationActivity.class, REQUEST_CODE_SEARCH, bundle);
    }

    /**
     * 点击回到当前定位
     */
    public void onRequestLocation(View view) {
        mHomePresenter.locationInMap();
    }


    /**
     * 点击返回
     */
    @Override
    public void onBackPressed() {
        final DialogCancelConfirm dialog = new DialogCancelConfirm(this);
        dialog.setMessage("您确定退出吗？");
        dialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
            @Override
            public void onLeftClick() {
                dialog.dismiss();
            }

            @Override
            public void onRightClick() {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }


    @Override
    public void zoomInMap(boolean in) {
        mBaiduMap.getMaxZoomLevel();
        if (in && mZoomLevel < 20) {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mZoomLevel += 0.5));
        } else if (!in && mZoomLevel > 3) {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mZoomLevel -= 0.5));
        }
    }

    /**
     * 右上角checkbox状态改变监听
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_map_road:
                mBaiduMap.setTrafficEnabled(isChecked);
                break;
            case R.id.cb_map_appointment:
                long now = DateUtils.getCurrentTimeMillis13();
                if (now - mClickStartTime > 1000) {
                    mClickStartTime = now;
                    mIsAppoint = isChecked ? "1" : null;
                    mBaiduMap.hideInfoWindow();
                    loadData(new LatLng(mCurrentCenLat, mCurrentCenLon));
                } else {
                    buttonView.setChecked(!isChecked);
                }
                break;
        }
    }

    /**
     * 过滤器，距离优先
     */
    class ParkComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            ParkMain e1 = (ParkMain) o1;
            ParkMain e2 = (ParkMain) o2;
            if (e1.getDistance() < e2.getDistance()) return -1;
            else if (e1.getDistance() == e2.getDistance()) return 0;
            else return 1;
        }
    }
}