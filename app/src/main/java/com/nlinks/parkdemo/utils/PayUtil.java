package com.nlinks.parkdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.nlinks.parkdemo.api.PayAPI;
import com.nlinks.parkdemo.entity._req.PayOrderReq;
import com.nlinks.parkdemo.entity.pay.AliPayOrder;
import com.nlinks.parkdemo.entity.pay.WXPayOrder;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.widget.CustomProgressDialog;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * 支付工具
 * Created by Dell on 2017/04/27.
 */
public class PayUtil {

    public interface AliCallback {
        void onAliResult(AliPayResult payResult);
    }

    public interface WXCallback {
        void onWXResult(int errCode, String errStr);
    }

    public interface WalletCallback {
        void onWalletResult(int code, String message);
    }

    private static WXCallback sWXCallback;
    private static IWXAPI sWXAPI = null;

    protected IWXAPI initWXAPI(Context context, String appId) {
        if (sWXAPI == null) {
            synchronized (PayUtil.class) {
                sWXAPI = WXAPIFactory.createWXAPI(context.getApplicationContext(), appId);
                sWXAPI.registerApp(appId);
            }
        }
        return sWXAPI;
    }

    public static IWXAPI getWXAPI() {
        return sWXAPI;
    }

    public static void sendWXCallback(int errCode, String errStr) {
        if (sWXCallback != null) sWXCallback.onWXResult(errCode, errStr);
    }

    public static void weixin(Activity activity, PayOrderReq orderReq, boolean showDialog,
                              WXCallback callback) {
        PayUtil payUtil = new PayUtil(activity, showDialog);
        payUtil.weixin(activity, orderReq, callback);
    }

    public static void alipay(Activity activity, PayOrderReq orderReq, boolean showDialog,
                              AliCallback callback) {
        PayUtil payUtil = new PayUtil(activity, showDialog);
        payUtil.alipay(activity, orderReq, callback);
    }

    public static void wallet(Activity activity, PayOrderReq orderReq, boolean showDialog,
                              WalletCallback callback) {
        PayUtil payUtil = new PayUtil(activity, showDialog);
        payUtil.wallet(orderReq, callback);
    }


    public static boolean alipaySuccess(AliPayResult payResult) {
        return payResult != null && "9000".equals(payResult.getResultStatus());
    }

    public static boolean weixinSuccess(int code) {
        return code == BaseResp.ErrCode.ERR_OK;
    }

    private final CustomProgressDialog mDialog;

    private PayUtil(Context context, boolean showDialog) {
        mDialog = showDialog ? new CustomProgressDialog(context, false) : null;
    }

    private void alipay(Activity activity, PayOrderReq orderReq, AliCallback callback) {
        if (orderReq == null) return;
        if (callback == null) return;
        final AliPayTask payTask = new AliPayTask(activity, callback);
        HttpHelper.getRetrofit().create(PayAPI.class).alipayOrderInfo(orderReq)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<AliPayOrder>(mDialog) {
            @NonNull
            @Override
            public void onHandleSuccess(AliPayOrder data) {
                //AliPayOrder data = httpResult.getData();
                if (data != null) payTask.execute(data.getOrderInfo(), data.getOrderCode());
            }
        });
    }

    private void wallet(PayOrderReq orderReq, final WalletCallback callback) {
        if (orderReq == null) return;
        if (callback == null) return;
        HttpHelper.getRetrofit().create(PayAPI.class).walletOrder(orderReq)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(mDialog) {
            @NonNull
            @Override
            public void onHandleSuccess(Void o) {
                dismissDialog();
            }

            @Override
            public void onNext(HttpResult value) {
                dismissDialog();
                callback.onWalletResult(value.getStatusCode(), value.getStatusMsg());
            }
        });
    }

    private void weixin(final Activity activity, PayOrderReq orderReq, WXCallback callback) {
        if (orderReq == null) return;
        if (callback == null) return;
        sWXCallback = callback;
        HttpHelper.getRetrofit().create(PayAPI.class).weixinOrderInfo(orderReq)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<WXPayOrder>(mDialog) {
            @NonNull
            @Override
            public void onHandleSuccess(WXPayOrder data) {
                if (data != null) {
                    //LogUtils.d("--------- wx " + new Gson().toJson(data));
                    String appid = data.getAppid();
                    IWXAPI iwxapi = initWXAPI(activity, appid);
                    PayReq payReq = new PayReq();
                    payReq.appId = appid;
                    payReq.partnerId = data.getPartnerid();
                    payReq.prepayId = data.getPrepayid();
                    payReq.packageValue = data.getPackageX();
                    LogUtils.d("--------- wx " + data.getPackageX());
                    payReq.nonceStr = data.getNoncestr();
                    payReq.timeStamp = data.getTimestamp();
                    payReq.sign = data.getSign();
                    iwxapi.sendReq(payReq);
                }
            }
        });
    }

    private static class AliPayTask extends AsyncTask<String, Void, AliPayResult> {

        final Activity mActivity;
        final AliCallback mCallback;

        public AliPayTask(Activity activity, AliCallback callback) {
            mActivity = activity;
            mCallback = callback;
        }

        @Override
        protected AliPayResult doInBackground(String... params) {
            String orderInfo = null, orderCode = null;
            try {
                orderInfo = params[0];
                orderCode = params[1];
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtils.d("--------- pay0 : " + orderInfo);
            LogUtils.d("--------- pay1 : " + orderCode);
            AliPayResult result = null;
            if (orderInfo != null) {
                PayTask payTask = new PayTask(mActivity);
                Map<String, String> stringMap = payTask.payV2(orderInfo, true);
                LogUtils.d("--------- pay2 : " + stringMap);
                //				String pay = payTask.pay(orderInfo, true);
                //				LogUtils.d("--------- pay2 : " + pay);
                result = new AliPayResult(stringMap);
            }
            return result;
        }

        @Override
        protected void onPostExecute(AliPayResult result) {
            super.onPostExecute(result);
            if (mCallback != null) mCallback.onAliResult(result);
        }
    }


    public static class AliPayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public AliPayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }


}
