package com.nlinks.parkdemo.module.park.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 导航通用类
 *
 * @author lzhixing@linewell.com
 *         Created at 2015/12/28 17:39
 */
public class NaviCommon {

    public static final String NAVI_FOLDER = "/QzPark/navi";

    private String mSDCardPath = null;
    private Activity activity;
    private BaiduNaviManager manager;
    private View view;

    public NaviCommon(Activity activity) {
        this.activity = activity;
        manager = BaiduNaviManager.getInstance();
    }

    public boolean routeplanToNavi(View startButton, double longitude0, double latitude0
        , String name0, String description0, BNRoutePlanNode.CoordinateType coType0
        , double longitude1, double latitude1, String name1, String description1
        , BNRoutePlanNode.CoordinateType coType1) {
        BNRoutePlanNode startNode = new BNRoutePlanNode(longitude0, latitude0, name0
            , description0, coType0);
        BNRoutePlanNode endNode = new BNRoutePlanNode(longitude1, latitude1, name1
            , description1, coType1);
        return routeplanToNavi(startNode, endNode, startButton);
    }

    public boolean routeplanToNavi(BNRoutePlanNode startNode, BNRoutePlanNode endNode, View view) {
        if (view == null) return false;
        this.view = view;
        if (startNode != null && endNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(startNode);
            list.add(endNode);
            return manager.launchNavigator(activity, list, 1, true, new DemoRoutePlanListener(startNode));
        }
        return false;
    }

    public boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, NAVI_FOLDER);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    public void initNavi() {
//        manager.init(activity, mSDCardPath, NAVI_FOLDER,
//            new BaiduNaviManager.NaviInitListener() {
//                @Override
//                public void onAuthResult(int status, String msg) {
//                    if (0 == status) {
//
//                    } else {
//
//                    }
//                }
//
//                public void initSuccess() {
//                }
//
//                public void initStart() {
//                }
//
//                public void initFailed() {
//                }
//            }, null);


        BaiduNaviManager.getInstance().init(activity, mSDCardPath, NAVI_FOLDER, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                LogUtils.i(authinfo);
            }

            public void initSuccess() {
                LogUtils.i("百度导航引擎初始化成功");
                initSetting();
            }

            public void initStart() {
                LogUtils.i("百度导航引擎初始化开始");
            }

            public void initFailed() {
                LogUtils.i("百度导航引擎初始化失败");
            }

        }, null, ttsHandler, ttsPlayStateListener);
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return AppConst.ROOT_DIR;
        }
        return null;
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
			 */
            Intent intent = new Intent(activity, NaviActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NaviActivity.ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            activity.startActivity(intent);
            view.setEnabled(true);
        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(activity, "算路失败", Toast.LENGTH_SHORT).show();
            view.setEnabled(true);
        }
    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };
    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
            .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9354030");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }
}
