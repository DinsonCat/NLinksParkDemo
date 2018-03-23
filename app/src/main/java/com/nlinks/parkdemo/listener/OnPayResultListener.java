package com.nlinks.parkdemo.listener;

import com.nlinks.parkdemo.modle.PayResultBean;

/**
 * 钱包，微信，支付宝 --支付结果监听
 */
public interface OnPayResultListener {
    void onPayResult(PayResultBean result);
}
