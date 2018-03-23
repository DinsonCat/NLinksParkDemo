package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ParkShareAPI;
import com.nlinks.parkdemo.entity.parkshare.ShareParkList;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.ArrayListUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.widget.recycleview.EmptyRecyclerView;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * 共享车位列表
 */
public class ShareParkingMainActivity extends BaseActivity {

    public static final int REQUEST_ADD = 101;

    private EmptyRecyclerView mRvContent;
    private ArrayList<ShareParkList> mData = new ArrayList<>();
    private int mPage = 1, mPageSize = 20;
    private ShareParkMainAdapter mAdapter;
    private LinearLayout mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepark_main);

        initUI();

        getDataFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initUI() {
        FrameLayout mEmptyLayout = (FrameLayout) findViewById(R.id.fl_empty);
        mRvContent = (EmptyRecyclerView) findViewById(R.id.rv_content);
        mRvContent.setEmptyView(mEmptyLayout);
        mRvContent.addOnItemTouchListener(new OnRecyclerItemClickListener(mRvContent) {
            @Override
            public void onItemClick(View view, int position) {
                turn2Next(mData.get(position));//根据状态值跳转activity
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });

        mContentLayout = (LinearLayout) findViewById(R.id.ll_content);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShareParkMainAdapter(this, mData);
        mRvContent.setAdapter(mAdapter);
    }

    public void doAddParking(View view) {
        AddShareParkActivity.start(this, REQUEST_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD:
                    getDataFromServer();

                    LogUtils.e("重新请求服务器");

                    break;
                case DetailReleaseActivity.REQUEST_REFRESH:

                    getDataFromServer();

                    LogUtils.e("重新请求服务器");
                    break;
            }
        }
    }

    public void getDataFromServer() {
          HttpHelper.getRetrofit().create(ParkShareAPI.class)
            .getParkSharingList(SPUtils.getUserId(this, ""), mPage, mPageSize)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<ShareParkList>>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(ArrayList<ShareParkList> lists) {
                if (ArrayListUtils.isNotEmpty(lists)) {
                    mData.clear();
                    mData.addAll(lists);
                    mAdapter.notifyDataSetChanged();
                    mRvContent.scrollToPosition(0);
                }
            }
        });
    }

    private void turn2Next(ShareParkList bean) {
        switch (bean.getAuditStatus()) {
            case 1:
                DetailErrorCheckActivity.start(this, bean);//审核中
                break;
            case 2:
                DetailErrorCheckActivity.start(this, bean);//审核失败
                break;
            case 3:
                int status = bean.getSharingStatus();
                if (status == 1) DetailReleaseActivity.start(this, bean);//待发布
                else if (status == 2) DetailReleaseActivity.start(this, bean);//发布中
                else if (status == 3) DetailSentOutActivity.start(this, bean);//已出租
                else DetailErrorCheckActivity.start(this, bean);//审核失败
                break;
            default:
                break;
        }
    }

    @Override
    public void titleRightBtnClick(View view) {
        CloudTestActivity.start(this);
    }
}
