package com.nlinks.parkdemo.module.usercenter.managecar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.adapter.BrandOfCarAdapter;
import com.nlinks.parkdemo.entity.other.BrandOfCar;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.IOUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.SideLetterBar;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BrandOfCarActivity extends BaseActivity {


    public static final String EXTRA_BRAND = "extra_brand";
    private RecyclerView mRvContent;
    private SideLetterBar mSideLetterBar;
    private BrandOfCar mBrandOfCar;
    private BrandOfCarAdapter mAdapter;

    private static final int CODE_REQUEST = 100;

    public static void start(Activity act,int requestCode) {
        Intent starter = new Intent(act, BrandOfCarActivity.class);
        act.startActivityForResult(starter,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_of_car);


        initDatas();
        initUI();

    }

    private void initDatas() {
        String dataStr = getFileFromAssets(this, "BrandOfCar.txt");
        Gson gson = new Gson();
        mBrandOfCar = gson.fromJson(dataStr, BrandOfCar.class);

    }

    private void initUI() {
        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvContent.setLayoutManager(layoutManager);

        mAdapter = new BrandOfCarAdapter(this, mBrandOfCar.getDatas());
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnRecyclerItemClickListener(mRvContent) {
            @Override
            public void onItemClick(View view, int position) {
                BrandOfCar.DatasBean data = mBrandOfCar.getDatas().get(position);
                BrandOfCarL2Activity.start(BrandOfCarActivity.this, data.getSeriesclub(),data.getBrandname(), CODE_REQUEST);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mSideLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mSideLetterBar.setOverlay(overlay);
        mSideLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mAdapter.getLetterPosition(letter);
                layoutManager.scrollToPositionWithOffset(position, 0);
            }
        });
    }

    /**
     * 获取文本
     *
     * @param context  上下文
     * @param fileName 文本
     * @return 返回获得的文本
     */
    public String getFileFromAssets(Context context, String fileName) {
        if (context == null || StringUtils.isEmpty(fileName)) {
            return null;
        }

        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader br = null;
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.close(br);
            IOUtils.close(in);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case CODE_REQUEST:
                    if (data!=null){
                        BrandOfCarActivity.this.setResult(RESULT_OK, data);
                        BrandOfCarActivity.this.onBackPressed();
                    }
                    break;
            }
        }
    }
}
