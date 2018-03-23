package com.nlinks.parkdemo.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.UIUtils;

/**
 * 加载提醒对话框
 */
public class CustomProgressDialog extends ProgressDialog {

    private boolean mCancelable = true;
    private Context mContext;

    public CustomProgressDialog(Context context) {
        this(context, R.style.CustomDialog);
    }

    public CustomProgressDialog(Context context, boolean cancelable) {
        this(context, R.style.CustomDialog);
        mCancelable = cancelable;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context) {
        //设置是否可取消
        setCancelable(mCancelable);
        setCanceledOnTouchOutside(mCancelable);

        View inflate = UIUtils.inflate(R.layout.progress_loading);
        setContentView(inflate);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }
}