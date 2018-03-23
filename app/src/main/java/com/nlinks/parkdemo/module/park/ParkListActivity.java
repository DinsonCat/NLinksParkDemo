package com.nlinks.parkdemo.module.park;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.widget.PagerTab;

/**
 * Created by Dell on 2017/4/11.
 */

public class ParkListActivity extends BaseActivity implements View.OnClickListener {

	public static final String DOUBLE_LAT = "DOUBLE_LAT", DOUBLE_LNG = "DOUBLE_LNG"
			, STRING_APPOINT = "STRING_APPOINT";
	private final String[] mTags = {"按距离排序", "按空车位排序"};
	private final Fragment[] mFragments = new Fragment[2];

	private PagerTab mPtTab;
	private ViewPager mVpContainer;
	private FragmentPagerAdapter mPagerAdapter;
	private double mLat, mLng;
	private String mAppoint = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_park_list);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			mLat = bundle.getDouble(DOUBLE_LAT);
			mLng = bundle.getDouble(DOUBLE_LNG);
			mAppoint = bundle.getString(STRING_APPOINT, null);
		}
		LogUtils.d("0 ------ lat,lng = " + mLat + ',' + mLng);
		mFragments[0] = ParkListFragment.createInstance(1, mLat, mLng, mAppoint);
		mFragments[1] = ParkListFragment.createInstance(2, mLat, mLng, mAppoint);

		initViews();
	}

	private void initViews() {
		mPtTab = (PagerTab) findViewById(R.id.pt_tab);
		mVpContainer = (ViewPager) findViewById(R.id.vp_container);

		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
	}
}
