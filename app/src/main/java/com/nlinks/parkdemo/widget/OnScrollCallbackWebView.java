package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 当页面滑动时会回调的webview
 */
public class OnScrollCallbackWebView extends WebView {
    public OnScrollCallbackWebView(Context context) {
        super(context);
    }

    public OnScrollCallbackWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnScrollCallbackWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null) {
            callback.onScroll(l, t, oldl, oldt);
        }
    }

    private OnscrollChangeCallback callback;

    public interface OnscrollChangeCallback {
        void onScroll(int l, int t, int oldl, int oldt);
    }

    public void setOnscrollChangeCallback(OnscrollChangeCallback callback) {
        this.callback=callback;
    }

}
