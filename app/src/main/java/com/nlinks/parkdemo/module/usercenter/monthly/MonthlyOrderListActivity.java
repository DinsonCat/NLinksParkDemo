package com.nlinks.parkdemo.module.usercenter.monthly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nlinks.parkdemo.api.MonthlyAPI;
import com.nlinks.parkdemo.entity.monthly.UserMonthlyOrderInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListActivity;
import com.nlinks.parkdemo.module.usercenter.monthly.adapter.MonthlyOrderListAdapter;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;

import io.reactivex.Observable;

/**
 * 错峰包月记录列表
 */
public class MonthlyOrderListActivity extends BaseListActivity<UserMonthlyOrderInfo> {

    public static void start(Context context) {
        Intent starter = new Intent(context, MonthlyOrderListActivity.class);
        context.startActivity(starter);
    }

    private MonthlyAPI mAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        validateUserIdAndToken();
        mAPI = HttpHelper.getRetrofit().create(MonthlyAPI.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitleBar(CommonTitleBar titleBar) {
        titleBar.setTitleText("包月记录");
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public CommonAdapter<UserMonthlyOrderInfo> initAdapter() {
        return new MonthlyOrderListAdapter(this, mDatas);
    }

    @Override
    public Observable initObservable(boolean reload, int page) {
        String userId = SPUtils.getUserId(this);
        if (StringUtils.isEmpty(userId)) return null;
        return mAPI.getUserMonthlyRecordList(userId, page, AppConst.PAGE_SIZE_DEFAULT);
    }

    @Override
    public void onRvItemClick(UserMonthlyOrderInfo item) {
        MonthlyOrderDetailsActivity.start(this,item);
    }
}
