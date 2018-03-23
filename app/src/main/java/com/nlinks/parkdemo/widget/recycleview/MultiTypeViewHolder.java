package com.nlinks.parkdemo.widget.recycleview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;


/**
 * RecyclerView.ViewHolder的基类
 */
public abstract class MultiTypeViewHolder<T> extends RecyclerView.ViewHolder {

    private SparseArray<View> views;//缓存view对象
    private View mItemView;

    public MultiTypeViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        this.mItemView = itemView;
    }


    /**
     * 根据ItemView的id获取子视图View
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文字
     *
     * @param resID  必须是textview
     * @param string
     */
    public void setText(int resID, String string) {
        TextView tv =   getView(resID);
        tv.setText(string);
    }

    /**
     * 设置文字
     *
     * @param resID  必须是textview
     * @param string
     */
    public void setText(int resID, CharSequence string) {
        TextView tv =   getView(resID);
        tv.setText(string);
    }

    public abstract void setUpView(T bean, int position, RecyclerView.Adapter adapter);
}
