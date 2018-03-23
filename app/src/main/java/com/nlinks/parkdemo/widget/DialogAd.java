package com.nlinks.parkdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nlinks.parkdemo.R;


/**
 * 提示框
 */
public class DialogAd extends Dialog implements View.OnClickListener {

    OnOperationListener operationListener;

    public DialogAd(Context context) {
        super(context, R.style.custom_dialog);
        init();
    }

    public DialogAd(Context context, View.OnClickListener listener) {
        super(context, R.style.custom_dialog);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_ad);
        findViewById(R.id.iv_cancel).setOnClickListener(this);
    }

    public void setOperationListener(String actionStr, OnOperationListener operationListener) {
        TextView action = (TextView) findViewById(R.id.action);
        action.setText(actionStr);
        action.setOnClickListener(this);
        action.setVisibility(View.VISIBLE);
        this.operationListener = operationListener;
    }


    public void setMessage(CharSequence message) {
        TextView tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvMsg.setVisibility(View.VISIBLE);
        tvMsg.setText(message);
    }

    public void setTitle(CharSequence message) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(message);
    }

    public void setHeadImgResource(int id) {
        ImageView iv_ad_head = (ImageView) findViewById(R.id.iv_ad_head);
        iv_ad_head.setImageResource(id);
    }

    public interface OnOperationListener {
        public void onActionClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                this.cancel();
                break;
            case R.id.action:
                if (operationListener != null)
                    operationListener.onActionClick();
                break;
        }
    }

    @Override
    public void show() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (attributes != null) {
                int intrinsicHeight = ContextCompat.getDrawable(getContext(), R.mipmap.update_close)
                    .getIntrinsicHeight();
                attributes.y = attributes.y - intrinsicHeight / 2;
                window.setAttributes(attributes);
            }
        }
        super.show();
    }
}