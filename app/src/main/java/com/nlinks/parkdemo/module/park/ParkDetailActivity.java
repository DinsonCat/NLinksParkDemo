package com.nlinks.parkdemo.module.park;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.appointment.AppointmentParkActivity;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.park.navi.NaviCommon;
import com.nlinks.parkdemo.module.usercenter.monthly.MonthlyParkDetailActivity;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.mzbanner.MZBannerView;
import com.nlinks.parkdemo.widget.mzbanner.holder.MZHolderCreator;

import java.text.DecimalFormat;

/**
 * 停车场详情
 */
public class ParkDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String STRING_PARK_CODE = "STRING_PARK_CODE";

    private String mParkCode = "false";
    private boolean mFavorite = false;
    private ParkDetail mParkDetail;
    private View mParkNavigation;
    //private ShareWindow mShareWindow;
    private NaviCommon navi;
    private ParkAPI mParkAPI;
    private CommonTitleBar mTitleBar;

    public static void start(Context context, String parkCode) {
        Intent starter = new Intent(context, ParkDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ParkDetailActivity.STRING_PARK_CODE, parkCode);
        starter.putExtras(bundle);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail);

        //mShareWindow = ShareWindow.create(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //			mParkBean = (ParkList) bundle.getSerializable(SERIALIZABLE_PARK_LIST);
            mParkCode = bundle.getString(STRING_PARK_CODE);
            mFavorite = SPUtils.isFavorite(this, mParkCode);
        } else {
            onBackPressed();
            UIUtils.showToast("停车场数据异常");
        }

        initView();

        mParkAPI = HttpHelper.getRetrofit().create(ParkAPI.class);
        getParkDetailFromServer();
    }

    private void initView() {


        //mIvParkFavorite = (ImageView) findViewById(R.id.iv_park_favorite);
        mParkNavigation = findViewById(R.id.navigation);

        //mIvParkFavorite.setOnClickListener(this);
        mParkNavigation.setOnClickListener(this);
        //findViewById(R.id.ib_title_left).setOnClickListener(this);
        findViewById(R.id.jumpToMonthlyParkDetails).setOnClickListener(this);


        mTitleBar = findViewById(R.id.titlebar);
        mTitleBar.getBtnRight().setOnClickListener(this);
    }

    /**
     * 获取停车场详情
     */
    private void getParkDetailFromServer() {
        mParkAPI.getParkDetail(mParkCode)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ParkDetail>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(ParkDetail parkDetail) {
                mParkDetail = parkDetail;
                initUIData(parkDetail);
            }
        });
    }

    private void initUIData(ParkDetail bean) {
        if (bean.getIsAppoint() != 0) {
            View navi = findViewById(R.id.parkAppoint);
            navi.setOnClickListener(this);
            navi.setVisibility(View.VISIBLE);
        }

        MZBannerView mMZBannerView = findViewById(R.id.MZbanner);
        mMZBannerView.setIndicatorRes(R.drawable.park_detail_indicator_normal,
            R.drawable.park_detail_indicator_selected);
        mMZBannerView.setCanLoop(bean.getParkImageList().size() > 1);

        mMZBannerView.setPages(bean.getParkImageList(), new MZHolderCreator<ParkDetailBannerViewHolder>() {
            @Override
            public ParkDetailBannerViewHolder createViewHolder() {
                return new ParkDetailBannerViewHolder();
            }
        });
        mMZBannerView.start();

        setTvText(R.id.tv_park_name, bean.getName());
        setTvText(R.id.tv_park_type, bean.getType() == 1 ? "路内" : "路外");
        TextView tv_park_rest = findViewById(R.id.tv_park_rest);
        tv_park_rest.setText(TextColorBuilder.newBuilder().addTextPart("空车位数：")
            .addTextPart(this, R.color.payment_yellow, bean.getUnuedStallNum())
            .buildSpanned());
        setTvText(R.id.tv_park_count, "总车位数：" + bean.getStallNumber());

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
        setTvText(R.id.tv_park_price,
            StringUtils.isEmpty(bean.getInstruction()) ? "暂无价格信息" : bean.getInstruction());

        // TODO: 2017/8/18 是否收藏，闲着没事的时候改
        mTitleBar
            .setRightBtnSrc(mFavorite ? R.drawable.icon_favorite_already : R.drawable.icon_favorite_not_yet);
    }

    /**
     * 给textview设置数据
     */
    private void setTvText(int tvId, String str) {
        TextView tv = findViewById(tvId);
        tv.setText(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.iv_park_favorite:
                followPark();
                break;*/
            case R.id.navigation:
                navigationTo();//启动定位
                break;
            case R.id.tv_title_right:
                //分享
                //mShareWindow.showAt(mSvContainer, Gravity.CENTER, 0, 0);

                //收藏
                followPark();
                break;
            case R.id.parkAppoint:
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
                            WebViewPromotionActivity.start(ParkDetailActivity.this, AppConst.URL_MEMBER);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return;
                }*/
                if (mParkDetail != null) {
                    ParkMain parkList = new ParkMain();
                    parkList.setLongitude(mParkDetail.getLongitude());
                    parkList.setLatitude(mParkDetail.getLatitude());
                    parkList.setName(mParkDetail.getName());
                    parkList.setCode(mParkDetail.getCode());
                    parkList.setAddress(mParkDetail.getAddress());
                    parkList.setStallNum(mParkDetail.getStallNumber());
                    parkList.setAppointCount(mParkDetail.getAppointCount());
                    {
                        LatLng latLng = MapUtil.getLastLocation(this);
                        LatLng latLng1 = new LatLng(mParkDetail.getLatitude(), mParkDetail.getLongitude());
                        double distance = DistanceUtil.getDistance(latLng, latLng1);
                        parkList.setDistance(distance);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppointmentParkActivity.SERIALIZABLE_PARK_LIST, parkList);
                    bundle.putString(AppointmentParkActivity.STRING_NAME, mParkDetail.getName());
                    bundle.putString(AppointmentParkActivity.STRING_ADDRESS, mParkDetail.getAddress());
                    jumpTo(AppointmentParkActivity.class, bundle);
                }
                break;
            case R.id.jumpToMonthlyParkDetails:
                if (validateUserIdAndToken(false)) {
                    MonthlyParkDetailActivity.start(this, mParkDetail.conversion());
                }
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
        if (mParkDetail == null) {
            UIUtils.showToast("加载数据失败，请返回重试");
            return;
        }
        LatLng latLng = MapUtil.getLastLocation(this);
        if (latLng == null) {
            UIUtils.showToast("无当前定位，请返回定位");
            return;
        }
        //距离太近无法导航
        double distance = MapUtil.getDistance(latLng.latitude, latLng.longitude, mParkDetail.getLatitude(),
            mParkDetail.getLongitude());
        if (distance < 100) {
            UIUtils.showToast("距离太近无法导航");
            return;
        }

        if (!BaiduNaviManager.isNaviInited()) {
            UIUtils.showToast("导航未初始化");
            return;
        }

        //导航初始化
        if (navi == null) navi = new NaviCommon(this);
        if (navi.initDirs()) navi.initNavi();

        BNRoutePlanNode startNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, "当前位置", "当前位置",
            BNRoutePlanNode.CoordinateType.BD09LL);
        LatLng latLng1 = new LatLng(mParkDetail.getLatitude(), mParkDetail.getLongitude());
        BNRoutePlanNode endNode = new BNRoutePlanNode(latLng1.longitude, latLng1.latitude,
            mParkDetail.getName(), mParkDetail.getAddress(),
            BNRoutePlanNode.CoordinateType.BD09LL);
        boolean navi = this.navi.routeplanToNavi(startNode, endNode, mParkNavigation);
        if (!navi) UIUtils.showToast("导航规划失败");

        mParkNavigation.setEnabled(true);
    }

    private void followPark() {
        if (!validateUserIdAndToken(false)) {
            UIUtils.showToast("还未登录无法收藏");
            //			jumpToForResult(LoginActivity.class, LOGIN_CODE);
            return;
        }
        String phone = SPUtils.getLastPhone(this, null);

        mParkAPI.followPark(new FavParkReqEntity(phone, mParkCode))
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void s) {
                mFavorite = true;
                mTitleBar.setRightBtnSrc(R.drawable.icon_favorite_already);
                UIUtils.showToast("关注成功");
                SPUtils.appendFavorite(ParkDetailActivity.this, mParkCode);
            }

            @Override
            public void onHandleError(int code, String message) {
                if (code == 201) {
                    mFavorite = false;
                    mTitleBar.setRightBtnSrc(R.drawable.icon_favorite_not_yet);
                    UIUtils.showToast("取消关注成功");
                    SPUtils.delFavorite(ParkDetailActivity.this, mParkCode);
                }
            }
        });
    }
}
