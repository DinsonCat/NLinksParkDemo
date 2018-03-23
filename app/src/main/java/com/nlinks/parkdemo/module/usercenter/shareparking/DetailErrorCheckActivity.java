package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.parkshare.ShareParkList;
import com.nlinks.parkdemo.entity.parkshare.ShareParkStateBean;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.IntentUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;

/**
 * 共享车位状态详情
 */
public class DetailErrorCheckActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_PARK = "park";
    private ShareParkList mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepark_errorcheck);


        initUI();
    }

    private void initUI() {
        mData = getIntent().getParcelableExtra(KEY_PARK);

        if (mData.getAuditStatus() == 2) {
            //处理审核失败布局
            TextView tv_error = (TextView) findViewById(R.id.tv_error);
            tv_error.setVisibility(View.VISIBLE);
            /*Button btn_retry = (Button) findViewById(R.id.btn_retry);
            btn_retry.setVisibility(View.VISIBLE);
            btn_retry.setOnClickListener(this);*/

        }

        //设置状态详情
        ShareParkStateBean stateBean = NlinksParkUtils.formatShareParkState(mData.getAuditStatus(), mData.getSharingStatus());
        TextView tv_state_detail = (TextView) findViewById(R.id.tv_state_detail);
        tv_state_detail.setText(stateBean.getDetailState());
        tv_state_detail.setTextColor(getResources().getColor(stateBean.getColor()));

        //设置左图标
        Drawable drawable = getResources().getDrawable(stateBean.getDrawableLeft());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_state_detail.setCompoundDrawables(drawable, null, null, null);

        //设置共有信息
        TextView tv_park_name = (TextView) findViewById(R.id.tv_park_name);
        tv_park_name.setText("停车场名称 : " + mData.getParkName());
        TextView tv_stall_name = (TextView) findViewById(R.id.tv_stall_name);
        tv_stall_name.setText("\u3000\u3000车位号 : " + mData.getStallName());


    }

    public static void start(Activity context, ShareParkList sharePark) {
        Intent intent = new Intent(context, DetailErrorCheckActivity.class);
        intent.putExtra(KEY_PARK, sharePark);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.btn_retry:
                UIUtils.showToast("正在完善中...");
                break;*/
        }
    }
}
