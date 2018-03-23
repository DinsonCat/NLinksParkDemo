package com.nlinks.parkdemo.module.usercenter.parkinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nlinks.parkdemo.api.MsgAPI;
import com.nlinks.parkdemo.entity.infomation.ActivityMsg;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListActivity;
import com.nlinks.parkdemo.module.base.WebViewNormalActivity;
import com.nlinks.parkdemo.module.usercenter.parkinformation.adapter.ActivityMsgAdapter;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;

import io.reactivex.Observable;

public class ParkInfoActivity extends BaseListActivity<ActivityMsg> {

    private MsgAPI mMsgAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMsgAPI = HttpHelper.getRetrofit().create(MsgAPI.class);
    }


    @Override
    public void initTitleBar(CommonTitleBar titleBar) {
        titleBar.setTitleText("停车资讯");
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public CommonAdapter initAdapter() {
        return new ActivityMsgAdapter(this, mDatas);
    }

    @Override
    public Observable initObservable(boolean reload, int page) {
        return mMsgAPI.getActivityMsg(page, AppConst.PAGE_SIZE_DEFAULT);
    }

    @Override
    public void onRvItemClick(ActivityMsg item) {
        super.onRvItemClick(item);
        Intent intent = new Intent(ParkInfoActivity.this, WebViewNormalActivity.class);
        intent.putExtra(WebViewNormalActivity.EXTRA_TITLE, item.getTitle());
        intent.putExtra(WebViewNormalActivity.EXTRA_HTML, item.getContent());
        startActivity(intent);
    }
}
