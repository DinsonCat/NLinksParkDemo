package com.nlinks.parkdemo.widget

import android.app.Activity
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.nlinks.parkdemo.R
import com.nlinks.parkdemo.api.PayCouponAPI
import com.nlinks.parkdemo.entity.park.ParkingCoupon
import com.nlinks.parkdemo.http.BaseObserver
import com.nlinks.parkdemo.http.HttpHelper
import com.nlinks.parkdemo.http.RxSchedulers
import com.nlinks.parkdemo.modle.CouponValidateExtra
import com.nlinks.parkdemo.module.coupon.ParkingCouponActivity
import com.nlinks.parkdemo.utils.*
import kotlinx.android.synthetic.main.layout_coupon_view.view.*
import org.jetbrains.anko.dip

/**
 *优惠券的选择（包含了请求优惠券，自动匹配优惠券）
 */
class CouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    init {
        setPadding(0, 0, 0, dip(20))
        setBackgroundResource(R.color.white)
        LayoutInflater.from(context).inflate(R.layout.layout_coupon_view, this, true)
        visibility = View.GONE
    }

    private var mCoupon: ParkingCoupon? = null
    private var mCouponMoney: Double = 0.0
    private lateinit var mCouponValidateExtra: CouponValidateExtra
    private var mCouponList: List<ParkingCoupon>? = null
    private var mOnCouponChangedListener: OnCouponChangedListener? = null

    /**
     * 获取当前的优惠券
     */
    fun getCoupon() = mCoupon

    fun getCouponId() = if (mCoupon == null) "" else mCoupon!!.id

    fun getCouponMoney(): Double {
        if (mCoupon == null) return 0.0
        return mCouponMoney
    }

    fun init(context: Context, extra: CouponValidateExtra, requestCode: Int, listener: OnCouponChangedListener? = null) {
        mCouponValidateExtra = extra
        mOnCouponChangedListener = listener
        //点击选择优惠券
        chooseCoupon.setOnClickListener {
            ParkingCouponActivity.startForResult(context as Activity, extra, requestCode)
        }
        //点击去卡包
        jumpToCoupon.setOnClickListener {
            ParkingCouponActivity.startForResult(context as Activity, extra, requestCode)
        }

        //点击取消优惠券
        cancelCoupon.setOnClickListener {
            initCouponLayout(null)
        }
        //自动选择最佳优惠券
        val userId = SPUtils.getUserId(context)
        if (!StringUtils.isEmpty(userId)) {
            HttpHelper.getRetrofit().create(PayCouponAPI::class.java).getAllUsablePayCoupon(userId)
                .compose(RxSchedulers.io_main())
                .subscribe(object : BaseObserver<List<ParkingCoupon>>() {
                    override fun onHandleSuccess(listHttpResult: List<ParkingCoupon>) {
                        mCouponList = listHttpResult
                        visibility = View.VISIBLE
                        initCouponLayout(NlinksParkUtils.filterCoupon(mCouponList, extra))
                    }

                    override fun onHandleError(code: Int, message: String) {
                        super.onHandleError(code, message)
                        visibility = View.VISIBLE
                    }
                })
        }
    }

    fun initCouponLayout(coupon: ParkingCoupon?) {
        mOnCouponChangedListener?.onChange(coupon)

        mCoupon = coupon
        chooseCoupon.visibility = if (coupon == null) View.VISIBLE else View.INVISIBLE
        couponLayout.visibility = if (coupon == null) View.INVISIBLE else View.VISIBLE

        if (coupon == null) return
        mCouponMoney = NlinksParkUtils.getCouponMoney(coupon, mCouponValidateExtra.consume)

        val money = if (mCouponMoney.toInt().compareTo(mCouponMoney) == 0) mCouponMoney.toInt().toString()
        else StringUtils.formatMoney(mCouponMoney.toString())

        tvPrice.typeface = TypefaceUtils.get(UIUtils.getContext(), "fonts/PingFangNum.ttf")
        tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, if (money.contains(".")) 25f else 45f)

        val span = SpannableString("￥")
        span.setSpan(RelativeSizeSpan(0.5f), 0, span.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvPrice.text = span
        tvPrice.append(money)
        tvCompany.text = coupon.couponRemark
    }

    fun initCouponLayout(extra: CouponValidateExtra) {
        if (mCouponList == null) return
        mCouponValidateExtra = extra
        initCouponLayout(NlinksParkUtils.filterCoupon(mCouponList, extra))
    }


    interface OnCouponChangedListener {
        fun onChange(coupon: ParkingCoupon?)
    }
}