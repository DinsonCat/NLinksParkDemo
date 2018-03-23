package com.nlinks.parkdemo.module.usercenter.managecar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.adapter.BrandOfCarL2Adapter;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.util.ArrayList;

public class BrandOfCarL2Activity extends BaseActivity {
    private static final String EXTRA_BRAND = "brand";
    private static final String EXTRA_BRANDSTR = "brandstr";
    private String mBrand;
    private ArrayList<String> mDatas;
    private RecyclerView mRvContent;
    private BrandOfCarL2Adapter mAdapter;

    public static void start(Activity act, ArrayList<String> brand, String brandstr, int requestCode) {
        Intent starter = new Intent(act, BrandOfCarL2Activity.class);
        starter.putStringArrayListExtra(EXTRA_BRAND, brand);
        starter.putExtra(EXTRA_BRANDSTR, brandstr);

        act.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_of_car_l2);
        overridePendingTransition(R.anim.uc_activity_ifr, R.anim.uc_activity_otl);

        initExtraData();

        initUI();

    }


    private void initExtraData() {
        mBrand = getIntent().getStringExtra(EXTRA_BRANDSTR);
        mDatas = getIntent().getStringArrayListExtra(EXTRA_BRAND);
        mDatas.add("其他");
    }

    private void initUI() {
        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new BrandOfCarL2Adapter(this, mDatas);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnRecyclerItemClickListener(mRvContent) {
            @Override
            public void onItemClick(View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putString(BrandOfCarActivity.EXTRA_BRAND, mBrand + "-" + mDatas.get(position).replace(mBrand,""));

                Intent intent = new Intent(BrandOfCarL2Activity.this,AddCarPlateActivity.class);
                intent.putExtras(bundle);
                BrandOfCarL2Activity.this.setResult(RESULT_OK, intent);
                BrandOfCarL2Activity.this.finish();

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
}
