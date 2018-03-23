package com.nlinks.parkdemo.module.usercenter.parkrecord

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.nlinks.parkdemo.R
import com.nlinks.parkdemo.api.PayOrderAPI
import com.nlinks.parkdemo.entity._req.ParkRecordReq
import com.nlinks.parkdemo.entity._req.PayOrderReq
import com.nlinks.parkdemo.entity.park.ParkRecord
import com.nlinks.parkdemo.entity.park.ParkRecordOrder
import com.nlinks.parkdemo.entity.park.ParkingCoupon
import com.nlinks.parkdemo.entity.pay.PlatformActivity
import com.nlinks.parkdemo.http.BaseObserver
import com.nlinks.parkdemo.http.HttpHelper
import com.nlinks.parkdemo.http.RxSchedulers
import com.nlinks.parkdemo.listener.OnPayResultListener
import com.nlinks.parkdemo.modle.CouponValidateExtra
import com.nlinks.parkdemo.modle.PaymentSuccessExtra
import com.nlinks.parkdemo.module.base.BaseActivity
import com.nlinks.parkdemo.module.base.PaymentSuccessActivity
import com.nlinks.parkdemo.module.coupon.ParkingCouponActivity
import com.nlinks.parkdemo.module.park.ParkDetailActivity
import com.nlinks.parkdemo.utils.*
import com.nlinks.parkdemo.widget.CouponView
import kotlinx.android.synthetic.main.activity_park_record_details_for_no_pay.*
import org.jetbrains.anko.toast

class ParkRecordDetailsForNoPayAty : BaseActivity() {

    private lateinit var mData: ParkRecord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_park_record_details_for_no_pay)

        mData = intent.getParcelableExtra(ParkRecordDetailsForNoPayAty.EXTRA_DATA)

        initUI()
        initMap()
    }

    private fun initUI() {
        mData = intent.getParcelableExtra(EXTRA_DATA)

        mParkRecordHeadMsg.apply {
            setParkPrice(mData.consume.toString())
            setItemLeftMsg(mData.plateNum, "车牌号")
            setItemRightMsg(DateUtils.formatSeconds(mData.parkSeconds), "停车时长")
            setReducedPrice("停车费用", R.color.colorPrimary)
        }

        val extra = CouponValidateExtra().apply {
            this.consume = mData.consume
            this.parkCode = mData.parkCode
            this.payType = mData.payType
        }
        couponView.init(this, extra, REQUEST_COUPON, object : CouponView.OnCouponChangedListener {
            override fun onChange(coupon: ParkingCoupon?) {
                calculateMoney(coupon)
            }
        })

        //点击费用详情
        chargeDetail.setOnClickListener { ParkRecordOrderDetailsAty.start(this, mData) }

        //点击立即缴费
        btnPayNow.setOnClickListener { submitOrder() }
    }

    private fun initMap() {
        val map = BaiduMapView.map
        map.uiSettings.setAllGesturesEnabled(false)//关闭所有的手势操作
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18f))
        MapUtil.hideBaiduMapLogo(BaiduMapView, 1)
        val latLng = LatLng(mData.latitude, mData.longitude)
        //设置覆盖物添加的方式与效果
        val markerOptions = MarkerOptions()
            .position(latLng)//mark出现的位置
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.park_record_pos))       //mark图标
            .draggable(true)//mark可拖拽
            .animateType(MarkerOptions.MarkerAnimateType.grow)//从地生长的方式
//            .animateType(MarkerOptions.MarkerAnimateType.drop)//从天而降的方式
        //添加mark
        map.addOverlay(markerOptions) as Marker//地图上添加mark

        //弹出View(气泡，意即在地图中显示一个信息窗口)，显示当前mark位置信息
        //动态创建一个View用于显示位置信息
        val view = layoutInflater.inflate(R.layout.park_record_bubble, null)
        view.findViewById<TextView>(R.id.tvParkName).text = mData.parkName
        view.findViewById<TextView>(R.id.tvParkAddress).text = mData.address

        //在地图中显示一个信息窗口，可以设置一个View作为该窗口的内容，也可以设置一个 BitmapDescriptor 作为该窗口的内容
        val infoWindow = InfoWindow(BitmapDescriptorFactory.fromView(view), latLng, -90,
            InfoWindow.OnInfoWindowClickListener {
                //当InfoWindow被点击后隐藏
                ParkDetailActivity.start(this, mData.parkCode)
            })
        //显示信息窗口
        map.showInfoWindow(infoWindow)

        map.setOnMapLoadedCallback {
            val builder = MapStatus.Builder()
            builder.target(latLng)
            val x = (BaiduMapView.right - BaiduMapView.left) / 2
            val y = (BaiduMapView.bottom - BaiduMapView.top) / 5 * 4
            val point = Point(x, y)
            builder.targetScreen(point)
            map.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        }
    }

    /**
     * 计算余额* @param amountMoney
     */
    private fun calculateMoney(coupon: ParkingCoupon?) {
        val couponMoney = NlinksParkUtils.getCouponMoney(coupon, mData.waitPay)
        var actualMoney = mData.waitPay - couponMoney
        if (actualMoney <= 0) actualMoney = 0.0

        payView.hiddenAliAndWechat(actualMoney == 0.0)

        val spannable = SpannableString("还需支付：")
        spannable.setSpan(ForegroundColorSpan(UIUtils.getColor(R.color.text_primary)), 0,
            spannable.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spannable.setSpan(RelativeSizeSpan(0.7f), 0,
            spannable.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tvPayMoney.text = spannable

        val spannable2 = SpannableString("￥")
        spannable2.setSpan(RelativeSizeSpan(0.7f), 0,
            spannable2.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tvPayMoney.append(spannable2)

        tvPayMoney.append(StringUtils.formatMoney(actualMoney))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_COUPON && data != null) {
            val coupon = data.getParcelableExtra<ParkingCoupon>(ParkingCouponActivity.RESULT_PARCELABLE_COUPON)
            couponView.initCouponLayout(coupon)
        }
    }


    companion object {
        private const val EXTRA_DATA = "data_bean"
        private const val REQUEST_COUPON = 0x0010
        @JvmStatic
        fun start(context: Context, bean: ParkRecord) {
            val intent = Intent(context, ParkRecordDetailsForNoPayAty::class.java)
            intent.putExtra(EXTRA_DATA, bean)
            context.startActivity(intent)
        }
    }


    private fun submitOrder() {
        if (!validateUserIdAndToken()) return
        val userId = SPUtils.getUserId(this)
        val couponId = couponView.getCouponId()
        val req = ParkRecordReq(couponId, mData.parkRecordId, userId, mData.payType)
        LogUtils.d(req.toString())

        HttpHelper.getRetrofit().create(PayOrderAPI::class.java).submitOrder(req)
            .compose(RxSchedulers.io_main())
            .subscribe(object : BaseObserver<ParkRecordOrder>(this) {
                override fun onHandleSuccess(parkRecordOrder: ParkRecordOrder) {
                    val orderReq = PayOrderReq()
                    orderReq.userId = userId
                    orderReq.orderAttach = resources.getString(R.string.app_name)
                    orderReq.payType = mData.payType
                    orderReq.couponDetailIds = couponId
                    orderReq.orderCode = parkRecordOrder.orderCode
                    orderReq.totalFee = "1008"//不传微信支付时服务器会返回错误：费用为0，不能支付
                    orderReq.orderDesc = NlinksParkUtils.formatTransactDesc(mData.payType)
                    toPay(orderReq, parkRecordOrder)
                }
            })
    }

    private fun toPay(req: PayOrderReq, order: ParkRecordOrder) {
        LogUtils.d(req.toString())
        payView.pay(this, req, OnPayResultListener {
            LogUtils.e(it.toString())
            if (it.isSuccess) {
                val extra = PaymentSuccessExtra()
                extra.payMoney = StringUtils.formatMoney(order.realMoney)
                if (order.activityList != null && order.activityList.isNotEmpty())
                    extra.platformActivityList = order.activityList
                extra.attention = UIUtils.getString(R.string.park_over_attention)

                //植入优惠券
                if (couponView.getCoupon() != null) {
                    extra.platformActivityList.add(PlatformActivity(StringUtils.formatMoney(couponView.getCouponMoney()),
                        "优惠券", "平台活动"))
                }
                PaymentSuccessActivity.start(this, extra)
            } else toast(it.message)
        })
    }
}
