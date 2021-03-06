package com.nlinks.parkdemo.module.park.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航页面
 *
 * @author lzhixing@linewell.com
 *         Created at 2015/12/28 11:18
 */
public class NaviActivity extends Activity {

	public static final String ROUTE_PLAN_NODE = "ROUTE_PLAN_NODE";

	private BNRoutePlanNode mBNRoutePlanNode = null;
	private static final int MSG_SHOW = 1;
	private static final int MSG_HIDE = 2;
	private static final int MSG_RESET_NODE = 3;
	private Handler hd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createHandler();

		//TitleCompat.setDefault(this);

		View view = BNRouteGuideManager.getInstance().onCreate(this
				, new BNRouteGuideManager.OnNavigationListener() {

			@Override
			public void onNaviGuideEnd() {
				finish();
			}

			@Override
			public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
				LogUtils.i("actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:"
						+ obj.toString());
			}

		});

		if (view != null) {
			setContentView(view);
		}

		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(ROUTE_PLAN_NODE);
			}
		}
	}

	@Override
	protected void onResume() {
		BNRouteGuideManager.getInstance().onResume();
		super.onResume();
		if (hd != null) {
			hd.sendEmptyMessageAtTime(MSG_SHOW, 2000);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		BNRouteGuideManager.getInstance().onPause();
	}

	@Override
	protected void onDestroy() {
		BNRouteGuideManager.getInstance().onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		BNRouteGuideManager.getInstance().onStop();
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		BNRouteGuideManager.getInstance().onBackPressed(false);
		overridePendingTransition(R.anim.uc_activity_ifl, R.anim.uc_activity_otr);
	}

	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	private void addCustomizedLayerItems() {
		List<BNRouteGuideManager.CustomizedLayerItem> items = new ArrayList<BNRouteGuideManager.CustomizedLayerItem>();
		BNRouteGuideManager.CustomizedLayerItem item1 = null;
		if (mBNRoutePlanNode != null) {
			item1 = new BNRouteGuideManager.CustomizedLayerItem(mBNRoutePlanNode.getLongitude()
					, mBNRoutePlanNode.getLatitude(),
					mBNRoutePlanNode.getCoordinateType(), getResources()
					.getDrawable(R.mipmap.ic_launcher)
					, BNRouteGuideManager.CustomizedLayerItem.ALIGN_CENTER);
			items.add(item1);

			BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
		}
		BNRouteGuideManager.getInstance().showCustomizedLayer(true);
	}

	private void createHandler() {
		if (hd == null) {
			hd = new Handler(getMainLooper()) {
				public void handleMessage(android.os.Message msg) {
					if (msg.what == MSG_SHOW) {
						addCustomizedLayerItems();
					} else if (msg.what == MSG_HIDE) {
						BNRouteGuideManager.getInstance().showCustomizedLayer(false);
					} else if (msg.what == MSG_RESET_NODE) {
//                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(
//                                new BNRoutePlanNode(116.21142, 40.85087, "百度大厦11", null, BNRoutePlanNode.CoordinateType.BD09LL));
					}
				}
			};
		}
	}



}
