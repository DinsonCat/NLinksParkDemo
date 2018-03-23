package com.nlinks.parkdemo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.nlinks.parkdemo.R;

/**
 * 分享的弹窗
 * Created by Dell on 2017/04/16.
 */
public class ShareWindow implements View.OnClickListener {

	private PopupWindow mShareWindow = null;
	private View mDecorView = null;

	public static ShareWindow create(Context context){
		return new ShareWindow(context);
	}

	private ShareWindow(Context context) {
		View popView = LayoutInflater.from(context).inflate(R.layout.pop_share_window, null);
		initView(popView);
		mShareWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
		mShareWindow.setOutsideTouchable(true);
		mShareWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.alpha_background)));
		if (context instanceof Activity){
			mDecorView = ((Activity) context).getWindow().getDecorView();
		}
	}

	private void initView(View popView) {
		popView.findViewById(R.id.v_share_blank).setOnClickListener(this);
		popView.findViewById(R.id.tv_share_sina).setOnClickListener(this);
		popView.findViewById(R.id.tv_share_wx_chat).setOnClickListener(this);
		popView.findViewById(R.id.tv_share_wx_circle).setOnClickListener(this);
		popView.findViewById(R.id.tv_share_wx_favorite).setOnClickListener(this);
		popView.findViewById(R.id.tv_share_cancel).setOnClickListener(this);
	}

	/**
	 * 在activity下使用此弹窗，可以直接使用decorView
	 */
	public void showAtDefaultActivity(){
		if (mDecorView != null)
			showAt(mDecorView, Gravity.BOTTOM, 0, 0);
	}
	public void showAt(View parent, int gravity, int x, int y){
		if (mShareWindow != null && !mShareWindow.isShowing())
			mShareWindow.showAtLocation(parent, gravity, x, y);
	}
	public void dismiss(){
		if (mShareWindow != null && mShareWindow.isShowing()) mShareWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_share_sina:break;
			case R.id.tv_share_wx_chat:break;
			case R.id.tv_share_wx_circle:break;
			case R.id.tv_share_wx_favorite:break;
		}
		dismiss();
	}
}
