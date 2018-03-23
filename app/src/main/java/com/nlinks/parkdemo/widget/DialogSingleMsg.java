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
 * 简单的带标题和文字的提示框
 */
public class DialogSingleMsg extends Dialog implements View.OnClickListener {


    public DialogSingleMsg(Context context) {
        super(context, R.style.custom_dialog);
        init();
    }

    public DialogSingleMsg(Context context, View.OnClickListener listener) {
        super(context, R.style.custom_dialog);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_single_msg);
        findViewById(R.id.iv_cancel).setOnClickListener(this);
    }


    public void setMessage(CharSequence message) {
        TextView tvMsg = findViewById(R.id.tv_msg);
        tvMsg.setVisibility(View.VISIBLE);
        tvMsg.setText(message);
    }

    public void setTitle(CharSequence message) {
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                this.cancel();
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