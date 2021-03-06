package com.nlinks.parkdemo.module.usercenter.walletmoney;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.monthly.MonthlyStateBean;
import com.nlinks.parkdemo.entity.monthly.UserMonthlyOrderInfo;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.NlinksParkUtils;

import org.w3c.dom.Text;

/**
 * 月卡详情
 */
public class MonthCardDetailsActivity extends BaseActivity {

    private static final String EXTRA_BEAN = "extra_bean";
    private UserMonthlyOrderInfo mData;

    public static void start(Context context, UserMonthlyOrderInfo bean) {
        Intent starter = new Intent(context, MonthCardDetailsActivity.class);
        starter.putExtra(EXTRA_BEAN, bean);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_card_details);

        initUI();
    }

    private void initUI() {
        mData = getIntent().getParcelableExtra(EXTRA_BEAN);

        //标题卡
        setTvText(R.id.tvParkName, mData.getParkName());
        setTvText(R.id.tvParkRule, mData.getRuleName());

        //包月详情
        TextView tvOrderState = findViewById(R.id.tvOrderState);
        MonthlyStateBean temp = NlinksParkUtils.formatMonthlyState(mData.getStatus());
        tvOrderState.setText(temp.getStateName());
        tvOrderState.setTextColor(getResources().getColor(temp.getColor()));

        setTvText(R.id.tvPrice, mData.getChareFee() + "元/月");
        setTvText(R.id.tvMonthlyTime, mData.getInterTime() + "个月");
        setTvText(R.id.tvStartTime, mData.getStartTime());
        setTvText(R.id.tvEndTime, mData.getEndTime());
        setTvText(R.id.tvRuleTime, NlinksParkUtils.formatRuleTime(mData.getRuleTime()));


        //包月须知
        LinearLayout container = findViewById(R.id.llContainer);
        NlinksParkUtils.createTextView(this, container, mData.getRemark());
    }


    private void setTvText(int id, String str) {
        TextView tv = findViewById(id);
        tv.setText(str);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawableResource(R.color.white);
    }
}
