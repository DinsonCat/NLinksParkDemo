package com.nlinks.parkdemo.module.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.utils.CacheUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.EmptyRecyclerView;
import com.nlinks.parkdemo.widget.recycleview.LinearRecyclerViewOnScroller;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.util.ArrayList;

import io.reactivex.Observable;

public abstract class BaseListActivity<T> extends BaseActivity {

    protected ArrayList<T> mDatas = new ArrayList<>();
    private CommonAdapter<T> mAdapter;
    private SwipeRefreshLayout mRefresher;
    private RecyclerView.OnScrollListener mScrollListener;

    private LinearLayoutManager mLayoutManager;
    private EmptyRecyclerView mRvContent;
    private OnRecyclerItemClickListener mItemClickListener;

    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);

        initUI();

    }

    private void initUI() {
        CommonTitleBar titleBar = findViewById(R.id.titlebar);
        initTitleBar(titleBar);

        final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        };

        mRefresher = findViewById(R.id.srl_refresher);
        mRefresher.setOnRefreshListener(refreshListener);

        mScrollListener = new LinearRecyclerViewOnScroller(mRefresher) {
            @Override
            public void loadMore() {
                loadData(false);
            }
        };

        mRvContent = findViewById(R.id.rv_content);
        FrameLayout view = findViewById(R.id.emptyViewContainer);
        View empty = LayoutInflater.from(this).inflate(setEmptyLayoutId(), view, false);
        view.addView(empty);
        mRvContent.setEmptyView(view);

        mLayoutManager = new LinearLayoutManager(this);
        mRvContent.setLayoutManager(mLayoutManager);

        mItemClickListener = new OnRecyclerItemClickListener(mRvContent) {
            @Override
            public void onItemClick(View view, int position) {
                onRvItemClick(mDatas.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                onRvItemLongClick(mDatas.get(position));
            }
        };

        mAdapter = initAdapter();
        if (mAdapter != null) {
            mRvContent.setAdapter(mAdapter);
        }

        initRecyclerView(mRvContent);

        mRefresher.post(new Runnable() {
            @Override
            public void run() {
                refreshListener.onRefresh();
                mRefresher.setRefreshing(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRvContent.addOnItemTouchListener(mItemClickListener);
        mRvContent.addOnScrollListener(mScrollListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRvContent.removeOnItemTouchListener(mItemClickListener);
        mRvContent.removeOnScrollListener(mScrollListener);
    }

    protected void loadData(final boolean reload) {
        if (reload) mPage = 1;
        Observable observable = initObservable(reload, mPage);
        if (observable != null) {
            observable.compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<T>>() {
                @NonNull
                @Override
                public void onHandleSuccess(ArrayList<T> ts) {
                    if (reload) {
                        mDatas.clear();
                        addDefaultData();
                    }
                    if (ts.size() > 0) mPage++;
                    mDatas.addAll(ts);
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();
                    mRefresher.setRefreshing(false);


                    if (getCacheFileName() != null)
                        CacheUtils.setCache(getCacheFileName(), new Gson().toJson(ts));
                }

                @Override
                public void onHandleError(int code, String message) {
                    mRefresher.setRefreshing(false);
                }
            });
        } else {
            mRefresher.setRefreshing(false);
        }
    }

    /**
     * 设置空布局的id
     *
     * @return 空布局的id
     */
    private int setEmptyLayoutId() {
        return R.layout.empty_recycleview;
    }

    protected void addDefaultData() {
    }

    public void onRvItemClick(T item) {
    }

    public void onRvItemLongClick(T item) {
    }

    public abstract void initTitleBar(CommonTitleBar titleBar);

    public abstract void initRecyclerView(RecyclerView rv);

    /**
     * 之所以再输入了一个datas。是因为有时候我会忘记，再次new了一个list出来，传进来这个，有助于提醒我父类中已经有list了
     */
    public abstract CommonAdapter<T> initAdapter();

    public abstract Observable initObservable(boolean reload, int page);


    public String getCacheFileName() {
        return null;
    }
}
