package com.nlinks.parkdemo.wxapi

import com.nlinks.parkdemo.listener.OnPayResultListener
import com.nlinks.parkdemo.modle.PayResultBean
import com.tencent.mm.opensdk.openapi.IWXAPI

/**
 * 微信支付助手
 */

object WXPayUtil {

    var sWXCallback: OnPayResultListener? = null

    var sWXApi: IWXAPI? = null

    fun sendWXCallback(isSuccess: Boolean, errStr: String) {
        sWXCallback?.onPayResult(PayResultBean(isSuccess, errStr))
    }
}
