package com.nlinks.parkdemo.module.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.adapter.ActivitiesAdapter;
import com.nlinks.parkdemo.api.ActivitiesApi;
import com.nlinks.parkdemo.entity.advertising.AdvertisingInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.utils.CacheUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by yanzhenyang on 2017/7/6.
 * 活动中心
 */

public class ActivitiesActivity extends BaseListActivity<AdvertisingInfo> {

    @Override
    public void initTitleBar(CommonTitleBar titleBar) {
        titleBar.setTitleText("活动中心");
        titleBar.setLeftBtnVisible(true);
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        int dimen = UIUtils.getDimen(R.dimen.spacing_medium);
        rv.addItemDecoration(new LinearSpaceItemDecoration(dimen, dimen / 2 * 3));
    }

    @Override
    public CommonAdapter initAdapter() {

        String cache = CacheUtils.getCache(getCacheFileName());

        if (cache != null) {
            Type founderListType = new TypeToken<ArrayList<AdvertisingInfo>>() {
            }.getType();
            mDatas = new Gson().fromJson(cache, founderListType);
            LogUtils.e("得到缓存：" + mDatas.size() + "条");
        }


        return new ActivitiesAdapter(this, mDatas);
    }

    @Override
    public Observable initObservable(boolean reload, int page) {
        LogUtils.d("------------  reload " + reload + "   " + page);
        return HttpHelper.getRetrofit().create(ActivitiesApi.class)
            .getActivityInfos(1, page, AppConst.PAGE_SIZE_DEFAULT);
    }

    @Override
    public void onRvItemClick(AdvertisingInfo item) {
        //点击事件
        //        UIUtils.showToast(item.getLinkUrl());
        if (item != null) {
            WebViewPromotionActivity.start(ActivitiesActivity.this, item.getLinkUrl());
        }
    }

    @Override
    public String getCacheFileName() {
        return "活动缓存";
    }
}
