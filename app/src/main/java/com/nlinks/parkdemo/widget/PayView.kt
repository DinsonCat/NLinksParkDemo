package com.nlinks.parkdemo.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.alipay.sdk.app.PayTask
import com.nlinks.parkdemo.R
import com.nlinks.parkdemo.api.PayAPI
import com.nlinks.parkdemo.entity._req.PayOrderReq
import com.nlinks.parkdemo.entity.pay.AliPayOrder
import com.nlinks.parkdemo.entity.pay.WXPayOrder
import com.nlinks.parkdemo.entity.pay.WalletMoney
import com.nlinks.parkdemo.http.BaseObserver
import com.nlinks.parkdemo.http.HttpHelper
import com.nlinks.parkdemo.http.RxSchedulers
import com.nlinks.parkdemo.listener.OnPayResultListener
import com.nlinks.parkdemo.modle.PayResultBean
import com.nlinks.parkdemo.utils.LogUtils
import com.nlinks.parkdemo.utils.SPUtils
import com.nlinks.parkdemo.utils.StringUtils
import com.nlinks.parkdemo.utils.UIUtils
import com.nlinks.parkdemo.wxapi.WXPayUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Observable

/**
 *优惠券的选择（包含了请求优惠券，自动匹配优惠券）
 */
class PayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : RadioGroup(context, attrs) {

    private val mWalletPay: RadioButton
    private val mWechatPay: RadioButton
    private val mAliPay: RadioButton

    init {
        orientation = VERTICAL
        setBackgroundResource(R.color.white)

        mWalletPay = createRadioBtn(R.drawable.payment_wallet, "钱包支付")
        mWechatPay = createRadioBtn(R.drawable.payment_wetchat, "微信支付")
        mAliPay = createRadioBtn(R.drawable.payment_alipay, "支付宝支付")

        addView(mWalletPay)
        //addView(mWechatPay)
        addView(mAliPay)

        check(mWalletPay.id)
        getBalance()
    }

    private fun getBalance() {
        val userId = SPUtils.getUserId(context, "")
        if (StringUtils.isEmpty(userId)) return
        HttpHelper.getRetrofit().create(PayAPI::class.java).getBalance(userId)
            .compose(RxSchedulers.io_main())
            .subscribe(object : BaseObserver<WalletMoney>(context) {
                @SuppressLint("SetTextI18n")
                override fun onHandleSuccess(walletMoney: WalletMoney) {
                    val money = StringUtils.formatMoney(walletMoney.usableMoney)
                    mWalletPay.text = "钱包支付\t\t余额：(" + money + "元)"
                }
            })
    }

    private fun createRadioBtn(drawableLeftId: Int, text: String): RadioButton {
        return RadioButton(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            background = null
            buttonDrawable = null

            val left = ContextCompat.getDrawable(context, drawableLeftId)
            left.setBounds(0, 0, left.minimumWidth, left.minimumHeight)
            val right = ContextCompat.getDrawable(context, R.drawable.selector_cb_blue)
            right.setBounds(0, 0, right.minimumWidth, right.minimumHeight)
            setCompoundDrawables(left, null, right, null)
            compoundDrawablePadding = UIUtils.dip2px(10f)

            val leftAndRight = context.resources.getDimensionPixelSize(R.dimen.spacing_medium)
            val topAndBottom = UIUtils.dip2px(12f)
            setPadding(leftAndRight, topAndBottom, leftAndRight, topAndBottom)
            setText(text)
            setTextColor(ContextCompat.getColor(context, R.color.text_primary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            val attribute = intArrayOf(android.R.attr.selectableItemBackground)
            val typedArray = context.theme.obtainStyledAttributes(typedValue.resourceId, attribute)
            background = typedArray.getDrawable(0)
        }
    }

    fun pay(activity: Activity, orderReq: PayOrderReq, listener: OnPayResultListener) {
        when (checkedRadioButtonId) {
            mWalletPay.id -> {
                HttpHelper.getRetrofit().create(PayAPI::class.java).walletOrder(orderReq)
                    .compose(RxSchedulers.io_main())
                    .subscribe(object : BaseObserver<Void>(context) {
                        override fun onHandleSuccess(o: Void) {
                            listener.onPayResult(PayResultBean(true, "支付成功"))
                        }

                        override fun onHandleError(code: Int, message: String) {
                            listener.onPayResult(PayResultBean(false, message))
                        }
                    })
            }
            mWechatPay.id -> {
                HttpHelper.getRetrofit().create(PayAPI::class.java).weixinOrderInfo(orderReq)
                    .compose(RxSchedulers.io_main())
                    .subscribe(object : BaseObserver<WXPayOrder>(context, false) {
                        override fun onHandleSuccess(data: WXPayOrder) {
                            wechatPay(data, listener)
                        }
                    })
            }
            mAliPay.id -> {
                //支付宝下单
                HttpHelper.getRetrofit().create(PayAPI::class.java).alipayOrderInfo(orderReq)
                    .compose(RxSchedulers.io_main())
                    .subscribe(object : BaseObserver<AliPayOrder>(context, false) {
                        override fun onHandleSuccess(t: AliPayOrder) {
                            executeAliTask(activity, t, listener)
                        }
                    })
            }
        }
    }


    private fun wechatPay(data: WXPayOrder, listener: OnPayResultListener) {
        WXPayUtil.sWXCallback = listener
        WXPayUtil.sWXApi = WXAPIFactory.createWXAPI(context.applicationContext, data.appid).apply {
            registerApp(data.appid)
        }

        val payReq = PayReq()
        payReq.appId = data.appid
        payReq.partnerId = data.partnerid
        payReq.prepayId = data.prepayid
        payReq.packageValue = data.packageX
        payReq.nonceStr = data.noncestr
        payReq.timeStamp = data.timestamp
        payReq.sign = data.sign
        WXPayUtil.sWXApi?.sendReq(payReq)
    }


    private fun executeAliTask(activity: Activity, t: AliPayOrder, listener: OnPayResultListener) {
        LogUtils.d(t.toString())
        Observable.just(t.orderInfo)
            .flatMap({
                Observable.just(PayTask(activity).payV2(it, true))
            })
            .compose(RxSchedulers.io_main())
            .subscribe {
                val aliPayResult = AliPayResult(it)
                LogUtils.d(aliPayResult.toString())
                listener.onPayResult(PayResultBean(aliPayResult.isSuccess(), aliPayResult.memo))
            }
    }

    class AliPayResult(rawResult: Map<String, String>) {
        var resultStatus = ""
        var result = ""
        var memo = ""

        init {
            rawResult.forEach {
                when (it.key) {
                    "resultStatus" -> resultStatus = it.value
                    "result" -> result = it.value
                    "memo" -> memo = it.value
                }
            }
        }

        override fun toString(): String {
            return "resultStatus=$resultStatus memo=$memo result=$result"
        }

        fun isSuccess() = "9000" == result
    }

    fun hiddenAliAndWechat(hidden: Boolean) {
        mAliPay.visibility = if (hidden) View.GONE else View.VISIBLE
        mWechatPay.visibility = if (hidden) View.GONE else View.VISIBLE
    }
}