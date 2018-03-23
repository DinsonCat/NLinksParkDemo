package com.nlinks.parkdemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nlinks.parkdemo.utils.PayUtil;
import com.nlinks.parkdemo.wxapi.WXPayUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI mIWXAPI = PayUtil.getWXAPI();
        if (mIWXAPI != null) mIWXAPI.handleIntent(getIntent(), this);


        IWXAPI swxApi = WXPayUtil.INSTANCE.getSWXApi();
        if (swxApi != null) swxApi.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        IWXAPI mIWXAPI = PayUtil.getWXAPI();
        if (mIWXAPI != null) mIWXAPI.handleIntent(intent, this);

        IWXAPI swxApi = WXPayUtil.INSTANCE.getSWXApi();
        if (swxApi != null) swxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        int code = resp.errCode;
        String msg = "";
        switch (code) {
            case BaseResp.ErrCode.ERR_OK:
                msg = "支付成功";
                break;
            case BaseResp.ErrCode.ERR_COMM:
                msg = "一般错误";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                msg = "支付取消";
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                msg = "发送失败";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                msg = "认证被否决";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                msg = "不支持错误";
                break;
            default:
                break;
        }
        WXPayUtil.INSTANCE.sendWXCallback(resp.errCode == BaseResp.ErrCode.ERR_OK, msg);
        PayUtil.sendWXCallback(code, msg);
        finish();
    }
}