package com.nlinks.parkdemo.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.widget.recycleview.EmptyRecyclerView;
import com.nlinks.parkdemo.widget.recycleview.LinearRecyclerViewOnScroller;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 基本的列表fragment
 * Created by du on 2017/3/30.
 */
public abstract class BaseListFragment<T> extends Fragment {

    public List<T> mDatas = new ArrayList<>();
    public SwipeRefreshLayout mSrlRefresher;
    private EmptyRecyclerView mRvSingleList;
    public RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Context mContext;
    private OnRecyclerItemClickListener mClickListener;
    private RecyclerView.OnScrollListener mScrollListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mSrlRefresher == null) {
            mSrlRefresher = (SwipeRefreshLayout) inflater.inflate(R.layout.single_refresh_list, container, false);
            mRvSingleList =   mSrlRefresher.findViewById(R.id.rv_single_list);

            FrameLayout view =  mSrlRefresher.findViewById(R.id.emptyViewContainer);
            View empty = inflater.inflate(setEmptyLayoutId(), view, false);
            view.addView(empty);
            mRvSingleList.setEmptyView(view);

//            mRvSingleList.setEmptyView(mSrlRefresher.findViewById(R.id.empty_layout));


            mAdapter = initAdapter();

            mLayoutManager = new LinearLayoutManager(mContext);
            mRvSingleList.setLayoutManager(mLayoutManager);

            if (mAdapter != null) mRvSingleList.setAdapter(mAdapter);

            final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData(true);
                }
            };
            mSrlRefresher.setOnRefreshListener(onRefreshListener);

            mScrollListener = new LinearRecyclerViewOnScroller(mSrlRefresher) {
                @Override
                public void loadMore() {
                    loadData(false);
                }
            };

            mClickListener = new OnRecyclerItemClickListener(mRvSingleList) {
                @Override
                public void onItemClick(View view, int position) {
                    onRvItemClick(mDatas.get(position));
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    onRvItemLongClick(mDatas.get(position));
                }
            };

            initRecyclerView(mRvSingleList);

            mSrlRefresher.post(new Runnable() {
                @Override
                public void run() {
                    onRefreshListener.onRefresh();
                    //mSrlRefresher.setRefreshing(true);
                }
            });
        }

        return mSrlRefresher;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRvSingleList.addOnItemTouchListener(mClickListener);
        mRvSingleList.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRvSingleList.removeOnItemTouchListener(mClickListener);
        mRvSingleList.removeOnScrollListener(mScrollListener);
    }

    public void loadData(final boolean reload) {
        Observable observable = initObservable(reload);
        if (observable != null) {
            observable.compose(RxSchedulers.io_main()).subscribe(new BaseObserver<List<T>>() {
                @NonNull
                @Override
                public void onHandleSuccess(List<T> ts) {
                    if (reload) {
                        mDatas.clear();
                        addDefaultData();
                    }
                    mDatas.addAll(ts);
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();
                    mSrlRefresher.setRefreshing(false);
                }

                @Override
                public void onHandleError(int code, String message) {
                    mSrlRefresher.setRefreshing(false);
                }
            });
        } else {
            mSrlRefresher.setRefreshing(false);
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

    public abstract void initRecyclerView(RecyclerView recyclerView);

    /**
     * 之所以再输入了一个datas。是因为有时候我会忘记，再次new了一个list出来，传进来这个，有助于提醒我父类中已经有list了
     */
    public abstract RecyclerView.Adapter initAdapter();

    public abstract Observable initObservable(boolean reload);

}
