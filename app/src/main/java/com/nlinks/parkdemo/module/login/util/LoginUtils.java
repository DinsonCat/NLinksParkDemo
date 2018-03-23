package com.nlinks.parkdemo.module.login.util;

import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.utils.UIUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author Dinson - 2017/8/24
 */
public class LoginUtils {
    private static final LoginUtils ourInstance = new LoginUtils();
    private IWXAPI mWxApi;

    public static LoginUtils getInstance() {
        return ourInstance;
    }

    private LoginUtils() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(UIUtils.getContext(), AppConst.WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(AppConst.WEIXIN_APP_ID);
    }

    public IWXAPI getWxApi() {
        return mWxApi;
    }
}
