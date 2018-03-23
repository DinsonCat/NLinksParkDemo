package com.nlinks.parkdemo.module.usercenter.myfollow;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nlinks.parkdemo.adapter.MyFollowAdapter;
import com.nlinks.parkdemo.api.FavoriteParkApi;
import com.nlinks.parkdemo.entity.favoritepark.FavorParkInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListActivity;
import com.nlinks.parkdemo.module.park.ParkDetailActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import io.reactivex.Observable;

/**
 * 我的关注
 */
public class MyFollowActivity extends BaseListActivity<FavorParkInfo> {

    //private static final int REQUEST_CODE_FOLLOW = 2;

    private FavorParkInfo mMaybeChangedPark = null;

    @Override
    public void initTitleBar(CommonTitleBar titleBar) {
        titleBar.setTitleText("我的关注");
        titleBar.setLeftBtnVisible(true);
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new LinearSpaceItemDecoration(0, 10));
    }

    @Override
    public CommonAdapter initAdapter() {
        return new MyFollowAdapter(this, mDatas);
    }

    @Override
    public Observable initObservable(boolean reload, int page) {
        LogUtils.d("------------  reload " + reload + "   " + page);
        return HttpHelper.getRetrofit().create(FavoriteParkApi.class)
            .getFollowParks(SPUtils.getLastPhone(this, "")
                , page, AppConst.PAGE_SIZE_DEFAULT);
    }

    @Override
    public void onRvItemClick(FavorParkInfo item) {
        //UIUtils.showToast(item.getName());
        if (item != null) {
            ParkDetailActivity.start(this, item.getParkCode());
        }
    }
}
