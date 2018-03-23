package com.nlinks.parkdemo.module.usercenter.walletmoney;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nlinks.parkdemo.adapter.WalletMoneyDetailAdapter;
import com.nlinks.parkdemo.api.PayAPI;
import com.nlinks.parkdemo.entity.pay.WalletMoneyDetail;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListActivity;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import io.reactivex.Observable;

public class WalletMoneyDetailActivity extends BaseListActivity<WalletMoneyDetail> {

    @Override
    public void initTitleBar(CommonTitleBar titleBar) {
        titleBar.setTitleText("钱包明细");
        titleBar.setLeftBtnVisible(true);
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new LinearSpaceItemDecoration(0, 10));
    }

    @Override
    public CommonAdapter<WalletMoneyDetail> initAdapter() {
        return new WalletMoneyDetailAdapter(this, mDatas);
    }

    @Override
    public Observable initObservable(boolean reload, int page) {
        String userId = SPUtils.getUserId(this, "");
        if (StringUtils.isEmpty(userId)) return null;
        return HttpHelper.getRetrofit().create(PayAPI.class)
            .getWalletDetail(userId, page, AppConst.PAGE_SIZE_DEFAULT, null, null);
    }
}