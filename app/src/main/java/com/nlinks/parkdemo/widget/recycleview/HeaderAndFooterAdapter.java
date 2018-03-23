package com.nlinks.parkdemo.widget.recycleview;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class HeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterAdapter(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            CommonViewHolder holder = CommonViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null) {
            CommonViewHolder holder = CommonViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    public int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }


    /**
     * 数据变化调用可以设置脚布局的显隐性
     */
    public void notifyDataSet() {
        mInnerAdapter.notifyDataSetChanged();
        setFootViewsVisibility(mInnerAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置脚布局的显隐性
     */
    private void setFootViewsVisibility(int visibility) {
        mInnerAdapter.notifyDataSetChanged();
        for (int i = 0; i < mFootViews.size(); i++) {
            int keyAt = mFootViews.keyAt(i);
            mFootViews.get(keyAt).setVisibility(visibility);
        }
    }
}