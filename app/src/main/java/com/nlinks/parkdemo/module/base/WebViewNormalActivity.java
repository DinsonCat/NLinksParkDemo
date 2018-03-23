package com.nlinks.parkdemo.module.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.home.GuideActivity;
import com.nlinks.parkdemo.module.home.view.MainActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;

public class WebViewNormalActivity extends BaseActivity {
    public static final String EXTRA_TITLE = "title_string";
    public static final String EXTRA_HTML = "html_string";
    private WebView wv_content;
    private CommonTitleBar mTitlebar;


    public static void start(Context context, String title, String url) {
        Intent starter = new Intent(context, WebViewNormalActivity.class);
        starter.putExtra(EXTRA_TITLE, title);
        starter.putExtra(EXTRA_HTML, url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_normal);
        initUI();
    }

    private void initUI() {
        //设置title
        String titleStr = getIntent().getExtras().getString(EXTRA_TITLE);
        mTitlebar = findViewById(R.id.titlebar);
        mTitlebar.setLeftBtnVisible(true);
        mTitlebar.setTitleText(titleStr);
        initWebView();
    }

    private void initWebView() {
        String htmlStr = getIntent().getExtras().getString(EXTRA_HTML);
        wv_content = findViewById(R.id.wv_content);
        WebSettings webSettings = wv_content.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        webSettings.setSupportZoom(true);//支持缩放，默认为true
        //        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //        wv_content.setWebViewClient(new WebViewClient());
        wv_content.loadUrl(htmlStr);

        wv_content.setWebViewClient(new WebViewClient());
        wv_content.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitlebar.setTitleText(title.startsWith("www") || title.startsWith("http") ? " " : title);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wv_content.removeAllViews();
        wv_content.destroy();
    }

    // 此方法为系统返回键的监听
    @Override
    public void onBackPressed() {
        if (wv_content.canGoBack()) {
            wv_content.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
