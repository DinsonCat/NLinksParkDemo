package com.nlinks.parkdemo.module.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.home.view.MainActivity;
import com.nlinks.parkdemo.utils.SPUtils;

import java.util.ArrayList;


/**
 * 新手引导页
 */
public class GuideActivity extends Activity implements View.OnClickListener {
    private ViewPager mViewPager;
    private int[] mImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private ArrayList<View> mImageViewList;
    private GuideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        initData();
        mAdapter = new GuideAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    private void initData() {
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_guide, null, false);
            ImageView img = (ImageView) view.findViewById(R.id.iv_img);
            img.setBackgroundResource(mImageIds[i]);
            if (i == mImageIds.length - 1) {
                View start_btn = view.findViewById(R.id.start_btn);
                start_btn.setVisibility(View.VISIBLE);
                start_btn.setOnClickListener(this);
            }
            //添加到ImageView的集合中
            mImageViewList.add(view);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                SPUtils.setFirstRunFalse(this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    class GuideAdapter extends PagerAdapter {
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }
    }
}
