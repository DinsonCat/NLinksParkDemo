package com.nlinks.parkdemo.http;

import android.app.ProgressDialog;
import android.content.Context;

import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CustomProgressDialog;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<HttpResult<T>> {
    private static final int SUCCESS_CODE = 200;
    private static final int TOKEN_ERROR = 10086;
    private ProgressDialog mPd;

    public BaseObserver() {
    }

    public BaseObserver(ProgressDialog pd) {
        this.mPd = pd;
    }

    public BaseObserver(Context context) {
        this.mPd = new CustomProgressDialog(context);
    }

    public BaseObserver(Context context, boolean dialogCancelable) {
        this.mPd = new CustomProgressDialog(context, dialogCancelable);
    }


    @Override
    public void onSubscribe(Disposable d) {
        if (mPd != null && !mPd.isShowing()) {
            mPd.show();
        }
    }

    @Override
    public void onNext(HttpResult<T> value) {

        //成功
        if (value.getStatusCode() == SUCCESS_CODE) {
            T t = value.getData();
            onHandleSuccess(t);
            return;
        }

        switch (value.getStatusCode()) {
            case TOKEN_ERROR:
                SPUtils.resetUser(UIUtils.getContext());
                onHandleError(value.getStatusCode(), "用户已过期，请重新登陆");
                break;
            default:
                onHandleError(value.getStatusCode(), value.getStatusMsg());
                break;
        }
    }

    @Override
    public void onError(Throwable e) {
        dismissDialog();
        LogUtils.d("Request error: " + e.toString() + "\n" + e.getLocalizedMessage());
        onHandleError(511, "网络异常，请稍后再试");
    }

    @Override
    public void onComplete() {
        dismissDialog();
    }

    protected final void dismissDialog() {
        if (mPd != null && mPd.isShowing()) {
            mPd.dismiss();
        }
    }

    public abstract void onHandleSuccess(@NotNull T t);

    public void onHandleError(@NotNull int code, @NotNull String message) {
        UIUtils.showToast(message);
    }
}
