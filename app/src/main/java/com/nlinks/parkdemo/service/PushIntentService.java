package com.nlinks.parkdemo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.other.GeTuiPushBean;
import com.nlinks.parkdemo.global.GlobalApplication;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.home.view.MainActivity;
import com.nlinks.parkdemo.module.usercenter.parkrecord.ParkRecordDetailsForSucAndLeaveAty;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;

/**
 * 项目名称：NLinksParkForAndroid
 * 继承GTIntentService接收来⾃自个推的消息,所有消息在线程中回调,如果注册了了该服务,则务必要在AndroidManifest中声明,
 * 否则⽆无法接受消息<br>
 * 创建人：gweibin@linewell.com
 * 创建时间：2017/4/27 17:45
 */
public class PushIntentService extends GTIntentService {

    private NotificationManager mNotificationManager;

    public PushIntentService() {
        Log.e(TAG, "onReceiveClientId -> " + "PushIntentService()");
    }

    @Override
    public void onReceiveServicePid(Context context, int i) {
        Log.e(TAG, "onReceiveClientId -> " + "onReceiveServicePid= " + i);

    }

    /**
     * onReceiveClientId 接收 cid <br>
     *
     * @param context
     * @param clientid
     */
    @Override
    public void onReceiveClientId(Context context, String clientid) {

        SPUtils.putCid(context, clientid);
        GlobalApplication.cid = clientid;

        LogUtils.e("onReceiveClientId -> " + "clientid = " + clientid);
    }

    /**
     * onReceiveMessageData处理透传消息<br>
     *
     * @param context
     * @param msg
     */
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {

        LogUtils.e("onReceiveMessageData: wait...");

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        byte[] payload = msg.getPayload();
        if (payload == null || payload.length == 0) return;

        String content = new String(payload);

        LogUtils.e("onReceiveMessageData: " + content);
        try {
            GeTuiPushBean geTuiPushBean = new Gson().fromJson(content, GeTuiPushBean.class);
            LogUtils.i(geTuiPushBean.toString());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(geTuiPushBean.getTitle())
                .setContentText(geTuiPushBean.getMessage());


            String url = geTuiPushBean.getUrl();
            //如果透传的消息包含url则优先跳转网页
            if (StringUtils.isNotEmpty(url)) {
                if (!url.startsWith("http")) url = "http://" + url;
                Intent starter = new Intent(context, WebViewPromotionActivity.class);
                starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                starter.putExtra(WebViewPromotionActivity.EXTRA_HTML, url);
                starter.putExtra(WebViewPromotionActivity.EXTRA_ISFROMSPLASH, false);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, starter, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
            }else{
                switch (geTuiPushBean.getType()){
                    case 1://金蜻蜓付款成功，
                    case 3://普通用户付款成功
                        //跳转停车记录
                        String parkRecordID = geTuiPushBean.getParkRecordID();
                        if (StringUtils.isNotEmpty(parkRecordID)){
                            Intent starter = ParkRecordDetailsForSucAndLeaveAty.getIntent(this, parkRecordID);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, starter, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);
                        }
                        break;
                    default:
                        //跳转首页
                        Intent starter = new Intent(context, MainActivity.class);
                        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, starter, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
                }
            }

            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.setBigContentTitle(geTuiPushBean.getTitle())
                .bigText(geTuiPushBean.getMessage());

            builder.setStyle(style);
            builder.setAutoCancel(true);

            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
            Notification notification = builder.build();
            mNotificationManager.notify(1, notification);
        } catch (Exception e) {
            //普通文本
            Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("蜻蜓停车")
                .setContentText(content)
                .setAutoCancel(true);
            Notification notification = builder.build();
            mNotificationManager.notify(11, notification);
        }


           /*
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("消息资讯")
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            mNotificationManager.notify(11, notification);*/

/*
            Notification foregroundNote;

            new NotificationCompat().BigTextStyle;

            RemoteViews bigView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.notification_layout_big);

            // bigView.setOnClickPendingIntent() etc..
            Notification.Builder mNotifyBuilder = new Notification.Builder(this);
            foregroundNote = mNotifyBuilder.setContentTitle("消息资讯")

                .setContentText(content)

                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

            foregroundNote.bigContentView = bigView;

            // now show notification..
            //NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, foregroundNote);*/


    }

    /**
     * onReceiveOnlineState cid 离线上线通知<br>
     *
     * @param context
     * @param b
     */
    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        Log.e(TAG, "onReceiveClientId -> " + "onReceiveOnlineState = " + b);
    }

    /**
     * onReceiveCommandResult 各种事件处理理回执<br>
     *
     * @param context
     * @param gtCmdMessage
     */
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.e(TAG, "onReceiveClientId -> " + "onReceiveCommandResult= " + gtCmdMessage);
    }

}
