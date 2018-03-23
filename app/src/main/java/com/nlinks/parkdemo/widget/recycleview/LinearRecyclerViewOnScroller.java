package com.nlinks.parkdemo.widget.recycleview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 上下拉的recyclerView的OnScrollListener，用来做拉到底部加载更多
 * Created by Dell on 2017/4/13.
 */
public abstract class LinearRecyclerViewOnScroller extends RecyclerView.OnScrollListener {

	private boolean mCanLoadMore = false;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	public LinearRecyclerViewOnScroller(SwipeRefreshLayout swipeRefreshLayout) {
		mSwipeRefreshLayout = swipeRefreshLayout;
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);
		if (mSwipeRefreshLayout != null) {
			try {
				int topRowVerticalPosition = recyclerView.getChildCount() == 0 ? 0 : recyclerView.getChildAt(0).getTop();
				mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
			View childAt = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
			int childBottom = childAt.getBottom();
			int bottom = recyclerView.getBottom();
			int bottomDecorationHeight = layoutManager.getBottomDecorationHeight(childAt);
			ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
			mCanLoadMore = layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1
					&& childBottom + marginLayoutParams.bottomMargin + bottomDecorationHeight
					+ recyclerView.getPaddingBottom() >= bottom;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		super.onScrollStateChanged(recyclerView, newState);
		if (mCanLoadMore && newState == RecyclerView.SCROLL_STATE_IDLE) loadMore();
	}

	public abstract void loadMore();
}
