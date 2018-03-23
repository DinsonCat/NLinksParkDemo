package com.nlinks.parkdemo.module.appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.widget.PagerTab;

/**
 * Created by Dell on 2017/05/02.
 */
public class AppointmentRecordActivity extends BaseActivity {

	private final String[] mTags = {"进行中", "已完成", "全部"};

	private PagerTab mPtTab;
	private ViewPager mVpContainer;
	private FragmentPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_record);

		initUI();
	}

	private void initUI() {
		mPtTab = (PagerTab) findViewById(R.id.pt_tab);
		mVpContainer = (ViewPager) findViewById(R.id.vp_container);

		final Fragment[] fragments = {AppointmentRecordFragment.newInstance(1)
				, AppointmentRecordFragment.newInstance(2)
				, AppointmentRecordFragment.newInstance(null)};

		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return fragments[position];
			}

			@Override
			public int getCount() {
				return fragments.length;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return mTags[position];
			}
		};

		mVpContainer.setAdapter(mPagerAdapter);

		mPtTab.setViewPager(mVpContainer);
	}
}
