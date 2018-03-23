package com.nlinks.parkdemo.module.usercenter.parkrecord;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.CouponActivityApi;
import com.nlinks.parkdemo.entity.couponactivity.CouponAd;
import com.nlinks.parkdemo.entity.park.ParkRecord;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.usercenter.parkrecord.adapter.ParkRecordAdapter;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.PagerTab;
import com.nlinks.parkdemo.widget.recycleview.EmptyRecyclerView;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * 停车记录
 */
public class ParkRecordActivity extends BaseActivity implements View.OnClickListener {

    public static int REQUEST_CODE = 100;


    private final String[] mTags = {"进行中", "历史记录"};
    private ParkRecordFragment[] mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_park_record);

        initUI();

        //initAd();
    }


    private void initUI() {
        String mPlates = SPUtils.getUserPlate(this, "");

        PagerTab mPtTab = findViewById(R.id.pt_tab);
        ViewPager mVpContainer = findViewById(R.id.vp_container);


        mFragments = new ParkRecordFragment[]{ParkRecordFragment.newInstance(1, mPlates)
            , ParkRecordFragment.newInstance(2, mPlates)};


        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTags[position];
            }
        };

        mVpContainer.setAdapter(mPagerAdapter);

        mPtTab.setViewPager(mVpContainer);
    }

    private CouponAd mCouponAd;

    private void initAd() {
        //如果广告时间关闭超过24小时，则显示广告栏
        int parkRecordAdTime = SPUtils.getParkRecordAdTime(this);
        if (Math.abs(DateUtils.getCurrentTimeMillis10() - parkRecordAdTime) > 3600 * 24) {
            String userId = SPUtils.getUserId(this);
            if (StringUtils.isEmpty(userId)) return;
            HttpHelper.getRetrofit().create(CouponActivityApi.class).getActivityInfo(userId)
                .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<CouponAd>() {
                @NonNull
                @Override
                public void onHandleSuccess(CouponAd couponAd) {
                    mCouponAd = couponAd;
                    ViewStub vs_ad = findViewById(R.id.vs_ad);
                    vs_ad.inflate();

                    //设置标题
                    TextView tvAction = findViewById(R.id.tv_action);
                    tvAction.setText(couponAd.getHeadInfo());
                    findViewById(R.id.closeAd).setOnClickListener(ParkRecordActivity.this);
                    tvAction.setOnClickListener(ParkRecordActivity.this);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && mFragments.length > 0) {
                for (ParkRecordFragment fragment : mFragments) {
                    fragment.reloadData();
                }
            } else {
                UIUtils.showToast("刷新失败请返回重试");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeAd:
                final View layoutAd = findViewById(R.id.layoutAd);
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
                final int viewHeight = layoutAd.getMeasuredHeight();
                valueAnimator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = layoutAd.getLayoutParams();
                    layoutParams.height = (int) (viewHeight * value);
                    layoutAd.setLayoutParams(layoutParams);
                });
                valueAnimator.start();
                SPUtils.putParkRecordAdtTime(this);
                break;
            case R.id.tv_action:
                WebViewPromotionActivity.start(this, mCouponAd.getGoUrl());
                break;
        }
    }
}
