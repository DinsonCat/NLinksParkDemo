package com.nlinks.parkdemo.module.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.pay.PlatformActivity;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.modle.PaymentSuccessExtra;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 预定成功
 * Created by Dell on 2017/05/06.
 */
public class PaymentSuccessActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_DATA = "extra";
    private PaymentSuccessExtra mData;

    public static void start(Context context, PaymentSuccessExtra extra) {
        Intent starter = new Intent(context, PaymentSuccessActivity.class);
        starter.putExtra(EXTRA_DATA, extra);
        context.startActivity(starter);
    }

    public static void startForResult(Activity context, PaymentSuccessExtra money, int requestCode) {
        Intent starter = new Intent(context, PaymentSuccessActivity.class);
        starter.putExtra(EXTRA_DATA, money);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_success);

        mData = getIntent().getParcelableExtra(EXTRA_DATA);
        initView();
    }


    private void initView() {
        CommonTitleBar titlebar = findViewById(R.id.titlebar);
        titlebar.getBtnRight().setOnClickListener(this);

        TextView tv_money = findViewById(R.id.tv_money);
        String money = "￥" + StringUtils.formatMoney(mData.getPayMoney());
        SpannableString sp = new SpannableString(money);
        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), 0, money.length()
            , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_money.setText("支付成功");
        tv_money.append(sp);
        tv_money.append("元");

        //警示
        TextView tvAttention = findViewById(R.id.tvAttention);
        if (mData.getAttention() != null) tvAttention.setText(mData.getAttention());

        LinearLayout llContainer = findViewById(R.id.llContainer);
        createPlateFormActivity(llContainer);

        //广告点击跳转，暂时隐藏，同ios
        //findViewById(R.id.iv_ad).setOnClickListener(this);
    }

    /**
     * 创建平台活动
     *
     * @param llContainer 布局容器
     */
    private void createPlateFormActivity(LinearLayout llContainer) {
        List<PlatformActivity> list = mData.getPlatformActivityList();
        if (list == null || list.isEmpty()) return;

        findViewById(R.id.llContainerRoot).setVisibility(View.VISIBLE);

        int textColor = UIUtils.getColor(R.color.white);

        for (int i = 0; i < list.size(); i++) {
            LinearLayout llTemp = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            if (i != 0) params.setMargins(0, 10, 0, 0);
            llTemp.setLayoutParams(params);

            TextView tv1 = new TextView(this);
            tv1.setText(list.get(i).getActivityValue());
            tv1.setTextColor(textColor);
            tv1.setTextSize(16);
            tv1.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

            TextView tv2 = new TextView(this);
            tv2.setText(String.format("-%s元", StringUtils.formatMoney(list.get(i).getRandomOffMoney())));
            tv2.setTextColor(textColor);
            tv2.setTextSize(16);
            tv2.setGravity(Gravity.RIGHT);
            tv2.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1f));

            llTemp.addView(tv1);
            llTemp.addView(tv2);
            llContainer.addView(llTemp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                finish();
                break;
            case R.id.iv_ad:
                //会员
                String userId = SPUtils.getUserId(this);
                if (StringUtils.isEmpty(userId)) return;
                if (SPUtils.isMember(this)) {
                    WebViewPromotionActivity.start(this, AppConst.URL_MEMBER_DETAIL);
                } else {
                    WebViewPromotionActivity.start(this, AppConst.URL_MEMBER);
                }
                break;
        }
    }
}
