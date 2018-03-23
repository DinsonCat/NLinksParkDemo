package com.nlinks.parkdemo.widget.recycleview;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * recyclerview的点击事件
 */
public abstract class OnRecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(final RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(childView);
                    onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(childView);
                    onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    //点击事件交给mGestureDetector处理
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    //点击回掉
    public abstract void onItemClick(View view, int position);

    //长按监听
    public abstract void onItemLongClick(View view, int position);
}