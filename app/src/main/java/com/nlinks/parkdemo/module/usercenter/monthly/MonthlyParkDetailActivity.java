package com.nlinks.parkdemo.module.usercenter.monthly;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.MonthlyAPI;
import com.nlinks.parkdemo.entity.monthly.FeedbackReq;
import com.nlinks.parkdemo.entity.monthly.MonthlyPark;
import com.nlinks.parkdemo.entity.monthly.MonthlyParkInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.park.ParkDetailActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.DialogAd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MonthlyParkDetailActivity extends BaseActivity {

    private static final String EXTRA_PARK_BEAN = "park_bean";
    private MonthlyPark mData;

    public static void start(Context context, MonthlyPark parkCode) {
        Intent starter = new Intent(context, MonthlyParkDetailActivity.class);
        starter.putExtra(EXTRA_PARK_BEAN, parkCode);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_park_detail);


        initUI();
        getDataFromServer();
    }


    private void initUI() {
        mData = getIntent().getParcelableExtra(EXTRA_PARK_BEAN);
        CommonTitleBar titlebar = findViewById(R.id.titlebar);
        titlebar.setTitleText(mData.getName());

        //先初始化月卡的关注
        findViewById(R.id.ivMonthlyParkBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackReq req = new FeedbackReq(MonthlyParkDetailActivity.this);
                req.setParkCode(mData.getParkCode());
                req.setMonthlyRuleName("月卡");
                postFeedback(req);
            }
        });
    }

    private void resetUI(final MonthlyParkInfo bean) {
        setTvText(R.id.tvMonthlyTitle, bean.getRuleName());
        setTvText(R.id.tvRulePrice, StringUtils.formatMoney(bean.getChareFee()));
        findViewById(R.id.noOpenUp).setVisibility(View.GONE);

        ImageView bg = findViewById(R.id.ivMonthlyParkBg);
        ImageView action = findViewById(R.id.ivAction);

        //1启用，可购买 2关注状态
        if (bean.getStatus() == 1) {
            action.setImageResource(R.drawable.monthly_q_yellow);
            Glide.with(MonthlyParkDetailActivity.this)
                .load(bean.getBackPic()).placeholder(R.drawable.monthly_card_enable)
                .error(R.drawable.monthly_card_enable).into(bg);

            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MonthlyApplyForActivity.start(MonthlyParkDetailActivity.this, bean, mData);
                }
            });
        } else {
            action.setImageResource(R.drawable.monthly_attention_white);
            findViewById(R.id.noOpenUp).setVisibility(View.VISIBLE);
            bg.setImageResource(R.drawable.monthly_card_disable);
            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FeedbackReq req = new FeedbackReq(MonthlyParkDetailActivity.this);
                    req.setMonthlyRuleId(bean.getMonthlyRuleId());
                    req.setParkCode(mData.getParkCode());
                    req.setMonthlyRuleName(bean.getRuleName());
                    postFeedback(req);
                }
            });
        }
    }

    private void setTvText(int tvId, String s) {
        TextView tv = findViewById(tvId);
        tv.setText(s);
    }

    @Override
    public void titleRightBtnClick(View view) {
        if (mData == null || StringUtils.isEmpty(mData.getParkCode())) return;
        ParkDetailActivity.start(this, mData.getParkCode());
    }

    /**
     *
     */
    public ArrayList<MonthlyParkInfo> createDefault() {
        ArrayList<MonthlyParkInfo> infos = new ArrayList<>();

        MonthlyParkInfo info1 = new MonthlyParkInfo();
        info1.setStatus(2);
        info1.setMonthlyType(1);
        info1.setRuleName("夜间包月");
        info1.setRuleTime("20:00-8:00");

        MonthlyParkInfo info2 = new MonthlyParkInfo();
        info2.setStatus(2);
        info2.setMonthlyType(1);
        info2.setRuleName("白天包月");
        info2.setRuleTime("8:00-20:00");

        MonthlyParkInfo info3 = new MonthlyParkInfo();
        info3.setStatus(2);
        info3.setMonthlyType(1);
        info3.setRuleName("假日短租");
        info3.setRuleTime("周末及法定假日");

        infos.add(info1);
        infos.add(info2);
        infos.add(info3);
        return infos;
    }

    private void getDataFromServer() {
        HttpHelper.getRetrofit().create(MonthlyAPI.class)
            .getParkRuleInfo(mData.getParkCode())
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<MonthlyParkInfo>>(this) {
            @NonNull
            @SuppressLint("SetTextI18n")
            @Override
            public void onHandleSuccess(ArrayList<MonthlyParkInfo> infos) {

                LinearLayout container = findViewById(R.id.llContainer);
                Collections.sort(infos, new SortComparator());

                boolean needFilter = false;//当后台返回的数据同时有未启用的套餐和启用的套餐时，需不需要过滤
                boolean hasRule = false;
                for (MonthlyParkInfo info : infos) {
                    //当有套餐有已启用时，就不显示未启用数据
                    if (info.getMonthlyType() == 1 && info.getStatus() == 1) {
                        needFilter = true;
                    }
                    if (info.getMonthlyType() == 1) {
                        hasRule = true;
                    }
                }
                if (!hasRule) {
                    infos.addAll(createDefault());
                }


                for (MonthlyParkInfo info : infos) {
                    if (info.getMonthlyType() == 1) {
                        //1错峰
                        if (needFilter && info.getStatus() != 1) continue;
                        createItem(info, container);
                    } else if (info.getMonthlyType() == 2) {
                        //2月卡
                        resetUI(info);
                    }
                }
            }
        });
    }

    private boolean isFirstItem = true;

    @SuppressLint("SetTextI18n")
    private void createItem(final MonthlyParkInfo monthlyParkInfo, LinearLayout container) {
        ConstraintLayout inflate = (ConstraintLayout) getLayoutInflater()
            .inflate(R.layout.item_monthly_detail, container, false);
        TextView tvSetMealsName = inflate.findViewById(R.id.tvSetMealsName);
        tvSetMealsName.setText(monthlyParkInfo.getRuleName());
        TextView tvSetMealsTime = inflate.findViewById(R.id.tvSetMealsTime);
        tvSetMealsTime.setText(NlinksParkUtils.formatRuleTime(monthlyParkInfo.getRuleTime()));
        TextView tvChareFee = inflate.findViewById(R.id.tvChareFee);
        tvChareFee.setText("￥" + monthlyParkInfo.getChareFee());
        container.addView(inflate);

        //第一个条目的图标变成红色的
        ImageView action = inflate.findViewById(R.id.ivAction);
        if (isFirstItem && monthlyParkInfo.getStatus() == 1) {
            isFirstItem = false;
            action.setImageResource(R.drawable.monthly_q_red);
        }
        if (monthlyParkInfo.getStatus() == 2) {
            //关注状态
            action.setImageResource(R.drawable.monthly_attention_blue);
            inflate.findViewById(R.id.itemFront).setEnabled(false);
            tvChareFee.setText("未开通");
        }

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (monthlyParkInfo.getStatus()) {
                    //1启用，可购买 2关注状态
                    case 1:
                        MonthlyApplyForActivity.start(MonthlyParkDetailActivity.this,
                            monthlyParkInfo, mData);
                        break;
                    case 2:
                        FeedbackReq req = new FeedbackReq(MonthlyParkDetailActivity.this);
                        req.setMonthlyRuleId(monthlyParkInfo.getMonthlyRuleId());
                        req.setParkCode(mData.getParkCode());
                        req.setMonthlyRuleName(monthlyParkInfo.getRuleName());
                        postFeedback(req);
                        break;
                }
            }
        });
    }


    public class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            MonthlyParkInfo a = (MonthlyParkInfo) lhs;
            MonthlyParkInfo b = (MonthlyParkInfo) rhs;
            if (a.getChareFee() == null || a.getChareFee() == null) return 1;
            try {
                return (int) (Double.parseDouble(a.getChareFee()) - Double.parseDouble(b.getChareFee()));
            } catch (Exception e) {
                return 1;
            }
        }
    }

    private void postFeedback(FeedbackReq req) {

        LogUtils.e(req.toString());

        HttpHelper.getRetrofit().create(MonthlyAPI.class).addFeedback(req)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                DialogAd dialogAd = new DialogAd(MonthlyParkDetailActivity.this);
                dialogAd.setTitle("关注成功");
                dialogAd.setMessage("已收到您的召唤，我们将尽快联系停车场开通~");
                dialogAd.setHeadImgResource(R.drawable.attention_bg_head);
                dialogAd.show();
            }
        });
    }
}
