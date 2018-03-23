package com.nlinks.parkdemo.module.usercenter.parkrecord

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.nlinks.parkdemo.R
import com.nlinks.parkdemo.api.ParkAPI
import com.nlinks.parkdemo.entity.park.ParkFee
import com.nlinks.parkdemo.entity.park.ParkRecord
import com.nlinks.parkdemo.http.BaseObserver
import com.nlinks.parkdemo.http.HttpHelper
import com.nlinks.parkdemo.http.RxSchedulers
import com.nlinks.parkdemo.module.base.BaseActivity
import com.nlinks.parkdemo.module.park.ParkDetailActivity
import com.nlinks.parkdemo.utils.DateUtils
import com.nlinks.parkdemo.utils.MapUtil
import kotlinx.android.synthetic.main.activity_park_record_details_for_suc_and_leave.*

/**
 *  停车记录详情（已完成，等待离场）
 */
class ParkRecordDetailsForSucAndLeaveAty : BaseActivity() {

    private lateinit var mData: ParkRecord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_park_record_details_for_suc_and_leave)

        mData = intent.getParcelableExtra(EXTRA_DATA)

        initUI()
        initMap()

        getReducedPrice()
    }

    private fun getReducedPrice() {
        HttpHelper.getRetrofit().create(ParkAPI::class.java).getParkFee(mData.parkRecordId)
            .compose(RxSchedulers.io_main())
            .subscribe(object : BaseObserver<ArrayList<ParkFee>>() {
                override fun onHandleSuccess(t: ArrayList<ParkFee>) {
                    if (t.isEmpty()) return
                    var reducedPrice = 0.0
                    t.forEach {
                        reducedPrice += it.couponMoney
                        reducedPrice += it.randomMoney
                    }
                    mParkRecordHeadMsg.setReducedPrice(reducedPrice)
                }
            })
    }

    private fun initUI() {
        mParkRecordHeadMsg.apply {
            setParkPrice(mData.consume.toString())
            setItemLeftMsg(mData.plateNum, "车牌号")
            setItemRightMsg(DateUtils.formatSeconds(mData.parkSeconds), "停车时长")
        }
        setParkLeaveTime()//如果为离场状态，设置离场时间提示

        //点击费用详情
        chargeDetail.setOnClickListener { ParkRecordOrderDetailsAty.start(this, mData) }
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

    @SuppressLint("SetTextI18n")
    private fun setParkLeaveTime() {
        if (mData.recordStatus != 5) return
//        mData.waitOutTime = "2017-09-15 10:00:52"
        //等待离场
        tvLeaveTime.visibility = View.VISIBLE

        tvLeaveTime.text = "请在"
        val time = DateUtils.convertStr(mData.waitOutTime, "yyyy-MM-dd HH:mm:ss", "HH:mm")
        val span = SpannableString(time)
        span.setSpan(RelativeSizeSpan(0.8f), 0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        val color = ContextCompat.getColor(this@ParkRecordDetailsForSucAndLeaveAty, R.color.money_orange)
        span.setSpan(ForegroundColorSpan(color),
            0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(RelativeSizeSpan(1.7f), 0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvLeaveTime.append(span)
        tvLeaveTime.append("前离场，避免二次缴费")
    }


    companion object {
        private const val EXTRA_DATA = "data_bean"
        @JvmStatic
        fun start(context: Context, bean: ParkRecord) {
            val intent = Intent(context, ParkRecordDetailsForSucAndLeaveAty::class.java)
            intent.putExtra(EXTRA_DATA, bean)
            context.startActivity(intent)
        }
    }
}
