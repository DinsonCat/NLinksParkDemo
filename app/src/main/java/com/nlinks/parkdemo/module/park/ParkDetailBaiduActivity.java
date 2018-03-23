package com.nlinks.parkdemo.module.park;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ParkAPI;
import com.nlinks.parkdemo.entity.park.FavParkReqEntity;
import com.nlinks.parkdemo.entity.park.ParkDetail;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.appointment.AppointmentParkActivity;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.park.navi.NaviCommon;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.marker.ParkMarker;
import com.nlinks.parkdemo.widget.mzbanner.MZBannerView;
import com.nlinks.parkdemo.widget.mzbanner.holder.MZHolderCreator;

import java.text.DecimalFormat;

import io.reactivex.Observable;

/**
 * 停车场详情
 */
public class ParkDetailBaiduActivity extends BaseActivity implements View.OnClickListener {

    public static final String STRING_PARK = "STRING_PARK";

    private ParkMain mData=null;
     private View mParkNavigation;
    //private ShareWindow mShareWindow;
    private NaviCommon navi;
    //private ParkAPI mParkAPI;
    private CommonTitleBar mTitleBar;

    public static void start(Context context, ParkMain parkMain) {
        Intent starter = new Intent(context, ParkDetailBaiduActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ParkDetailBaiduActivity.STRING_PARK, parkMain);
        starter.putExtras(bundle);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail_baidu);

        //mShareWindow = ShareWindow.create(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//			mParkBean = (ParkList) bundle.getSerializable(SERIALIZABLE_PARK_LIST);
            mData = bundle.getParcelable(STRING_PARK);
        } else {
            finish();
            UIUtils.showToast("停车场数据异常");
        }

        initView();
    }

    private void initView() {
        mParkNavigation = findViewById(R.id.navigation);
        mParkNavigation.setOnClickListener(this);
        initUIData(mData);
    }



    private void initUIData(ParkMain bean) {
        setTvText(R.id.tv_park_name, bean.getName());
        //设置地址
        String address = bean.getAddress();
        LatLng latLng = MapUtil.getLastLocation(this);
        if (latLng != null) {
            LatLng latLng1 = new LatLng(bean.getLatitude(), bean.getLongitude());
            double distance = DistanceUtil.getDistance(latLng, latLng1);
            DecimalFormat df = new DecimalFormat("#.0");
            address += " | " + (distance >= 1000 ? df.format(distance / 1000) + "km" : (int) distance + "m");
        }
        setTvText(R.id.tv_park_address, address);
    }

    /**
     * 给textview设置数据
     */
    private void setTvText(int tvId, String str) {
        TextView tv = (TextView) findViewById(tvId);
        tv.setText(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation:
                navigationTo();//启动定位
                break;
            case R.id.ib_title_left:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    /**
     * 立即导航
     */
    private void navigationTo() {
        mParkNavigation.setEnabled(false);
        if (mData == null) {
            UIUtils.showToast("加载数据失败，请返回重试");
            return;
        }
        LatLng latLng = MapUtil.getLastLocation(this);
        if (latLng == null) {
            UIUtils.showToast("无当前定位，请返回定位");
            return;
        }
        //距离太近无法导航
        double distance = MapUtil.getDistance(latLng.latitude, latLng.longitude
            , mData.getLatitude(), mData.getLongitude());
        if (distance < 100) {
            UIUtils.showToast("距离太近无法导航");
            return;
        }

        if (!BaiduNaviManager.isNaviInited()) {
            UIUtils.showToast("导航未初始化");
            return;
        }

        //导航初始化
        if (navi == null)
            navi = new NaviCommon(this);
        if (navi.initDirs())
            navi.initNavi();

        BNRoutePlanNode startNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude,
            "当前位置", "当前位置", BNRoutePlanNode.CoordinateType.BD09LL);
        LatLng latLng1 = new LatLng(mData.getLatitude(), mData.getLongitude());
        BNRoutePlanNode endNode = new BNRoutePlanNode(latLng1.longitude, latLng1.latitude,
            mData.getName(), mData.getAddress(), BNRoutePlanNode.CoordinateType.BD09LL);
        boolean navi = this.navi.routeplanToNavi(startNode, endNode, mParkNavigation);
        if (!navi)
            UIUtils.showToast("导航规划失败");

        mParkNavigation.setEnabled(true);
    }

}
