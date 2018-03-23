package com.nlinks.parkdemo.widget.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 单一布局通用viewholder
 */
public class CommonViewHolder extends RecyclerView.ViewHolder {
    public CommonViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }
    public CommonViewHolder(Context context, View itemView)
    {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }
    private SparseArray<View> mViews;
    private View mConvertView;//缓存itemView内部的子View
    private Context mContext;

    public static CommonViewHolder createViewHolder(Context context, View itemView) {
        CommonViewHolder holder = new CommonViewHolder(context, itemView);
        return holder;
    }

    /**
     * 加载layoutId视图并用LGViewHolder保存
     *
     * @param parent
     * @param layoutId
     * @return
     */
    protected static CommonViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    /**
     * 根据ItemView的id获取子视图View
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 给textview设置文本
     *
     * @param viewId
     * @param str
     */
    public void setText(int viewId, String str) {
        View view = getView(viewId);
        if (!(view instanceof TextView)) return;
        view = (TextView) view;
        ((TextView) view).setText(str);
    }

}
