package com.nlinks.parkdemo.module.coupon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.PagerTab;

/**
 * 优惠券界面
 */
public class ParkingCouponActivity extends BaseActivity {

    public static String RESULT_PARCELABLE_COUPON = "RESULT_PARCELABLE_COUPON";

    private final String[] mTags = {"未使用", "已使用", "已过期"};
    private Fragment[] mFragments;

    private static final String EXTRA_DATA = "extra_data";

    /**
     * 启动优惠券界面
     *
     * @param context context
     * @param extra   优惠券验证是否可用实体
     */
    public static void start(Context context, CouponValidateExtra extra) {
        Intent starter = new Intent(context, ParkingCouponActivity.class);
        starter.putExtra(EXTRA_DATA, extra);
        context.startActivity(starter);
    }

    /**
     * 启动优惠券界面
     *
     * @param context context
     * @param extra   优惠券验证是否可用实体
     */
    public static void startForResult(Activity context, CouponValidateExtra extra, int code) {
        Intent starter = new Intent(context, ParkingCouponActivity.class);
        starter.putExtra(EXTRA_DATA, extra);
        context.startActivityForResult(starter, code);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_coupon);

        CouponValidateExtra data = getIntent().getParcelableExtra(EXTRA_DATA);
        if (data != null) {
            //没传付款类型。说明是选择优惠券模式，显示标题栏右边的“不用券”
            CommonTitleBar title = findViewById(R.id.titlebar);
            title.getBtnRight().setVisibility(View.VISIBLE);
        }

        mFragments = new Fragment[]{ParkingCouponFragment.createInstance(1, data),
            ParkingCouponFragment.createInstance(2, data),
            ParkingCouponFragment.createInstance(3, data)};

        initViews();
    }

    private void initViews() {
        PagerTab mPtTab = findViewById(R.id.pt_tab);
        ViewPager vpContainer = findViewById(R.id.vp_container);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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

        vpContainer.setAdapter(pagerAdapter);
        mPtTab.setViewPager(vpContainer);
    }


    @Override
    public void titleRightBtnClick(View view) {
        super.titleRightBtnClick(view);
        Intent intent = new Intent();
        intent.putExtra(ParkingCouponActivity.RESULT_PARCELABLE_COUPON, new ParkingCoupon());
        setResult(Activity.RESULT_OK, intent);
        onBackPressed();
    }
}
