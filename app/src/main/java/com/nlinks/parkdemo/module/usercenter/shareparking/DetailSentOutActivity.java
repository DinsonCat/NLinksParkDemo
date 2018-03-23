package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.parkshare.ShareParkList;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.StringUtils;

/**
 * 共享车位发布中详情
 */
public class DetailSentOutActivity extends BaseActivity {

    private static final String KEY_PARK = "Park";

    private ShareParkList mData;

    private TextView mTvToggle;
    private boolean isDown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepark_sent_out);

        initUI();

    }

    public static void start(Activity context, ShareParkList sharePark) {
        Intent intent = new Intent(context, DetailSentOutActivity.class);
        intent.putExtra(KEY_PARK, sharePark);
        context.startActivity(intent);
    }

    private void initUI() {

        mData = getIntent().getParcelableExtra(KEY_PARK);


        TextView tv_money = (TextView) findViewById(R.id.tv_money);


        tv_money.setText("￥" + StringUtils.formatMoney(mData.getCharge()));


        TextView tv_plate = (TextView) findViewById(R.id.tv_plate);
        tv_plate.setText("----------- 车牌号码 : " + mData.getPlateNum() + " -----------");
        TextView tv_sent_time = (TextView) findViewById(R.id.tv_sent_time);
        tv_sent_time.setText(mData.getAppointTime() + " ~ " + mData.getLeaveTime());

        //设置公共信息
        TextView tv_park_name = (TextView) findViewById(R.id.tv_park_name);
        tv_park_name.setText("停车场名称 : " + mData.getParkName());
        TextView tv_stall_name = (TextView) findViewById(R.id.tv_stall_name);
        tv_stall_name.setText("\u3000\u3000车位号 : " + mData.getStallName());

        TextView tv_price = (TextView) findViewById(R.id.tv_price);
        tv_price.setText("\u3000出租价格 : " + StringUtils.formatMoney(mData.getMoney()) + "元/小时");
        TextView tv_share_time = (TextView) findViewById(R.id.tv_share_time);
        tv_share_time.setText("您的分享时间 :\n" + mData.getStartTime() + " ~ " + mData.getEndTime());

        mTvToggle = (TextView) findViewById(R.id.tv_toggle);
    }

    public void toggleMsg(View view) {
        //切换箭头
        Drawable drawable = getResources().getDrawable(isDown ? R.mipmap.blue_arrow_up : R.mipmap.blue_arrow_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvToggle.setCompoundDrawables(null, null, drawable, null);
        findViewById(R.id.ll_detail).setVisibility(isDown ? View.VISIBLE : View.GONE);//却换布局
        isDown = !isDown;
    }
}
