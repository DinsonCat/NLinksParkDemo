package com.nlinks.parkdemo.module.usercenter.walletmoney;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nlinks.parkdemo.api.MonthlyAPI;
import com.nlinks.parkdemo.entity.monthly.UserMonthlyOrderInfo;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListActivity;
import com.nlinks.parkdemo.module.usercenter.walletmoney.adapter.MonthCardListAdapter;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import io.reactivex.Observable;

public class MonthCardActivity extends BaseListActivity<UserMonthlyOrderInfo> {

    private MonthlyAPI mAPI;

    public static void start(Context context) {
        Intent starter = new Intent(context, MonthCardActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validateUserIdAndToken();
        mAPI = HttpHelper.getRetrofit().create(MonthlyAPI.class);
    }

    @Override
    public void initTitleBar(CommonTitleBar titleBar) {
        titleBar.setTitleText("月卡");
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new LinearSpaceItemDecoration(20, 20));
    }

    @Override
    public CommonAdapter<UserMonthlyOrderInfo> initAdapter() {
        return new MonthCardListAdapter(this, mDatas);
    }

    @Override
    public Observable initObservable(boolean reload, int page) {
        if (page>1)return null;
        String userId = SPUtils.getUserId(this);
        if (StringUtils.isEmpty(userId)) return null;
        return mAPI.getUserMonthlyCardList(userId);
    }

    @Override
    public void onRvItemClick(UserMonthlyOrderInfo item) {
        MonthCardDetailsActivity.start(this, item);
    }
}
