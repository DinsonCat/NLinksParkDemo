package com.nlinks.parkdemo.module.messagecenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.MsgPushAPI;
import com.nlinks.parkdemo.entity.msgpush.PushMessage;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.widget.CommonTitleBar;

public class PushMessageDetailActivity extends BaseActivity {

    private static final String EXTRA_PUSHMESSAGE = "PUSHMESSAGE";

    public static void start(Context context, PushMessage bean) {
        Intent starter = new Intent(context, PushMessageDetailActivity.class);
        starter.putExtra(EXTRA_PUSHMESSAGE, bean);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message_detail);

        initUI();
    }


    private void initUI() {
        PushMessage data = getIntent().getParcelableExtra(EXTRA_PUSHMESSAGE);

        CommonTitleBar titlebar =  findViewById(R.id.titlebar);
        titlebar.setTitleText(data.getTitle());
        setTvText(R.id.tv_title_msg, data.getTitle());
        setTvText(R.id.tv_time, data.getSendtime());
        setTvText(R.id.tv_content, data.getMessage());

        post2server(data);
    }
    private void post2server( PushMessage data) {

       HttpHelper.getRetrofit().create(MsgPushAPI.class).updateIsRead(data.getId())
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>() {
           @NonNull
           @Override
           public void onHandleSuccess(Void aVoid) {

           }
       });
    }

    private void setTvText(int id, String msg) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(msg);
    }
}
