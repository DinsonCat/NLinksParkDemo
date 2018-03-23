package com.nlinks.parkdemo.module.usercenter.parkrecord

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.nlinks.parkdemo.R
import com.nlinks.parkdemo.api.ParkAPI
import com.nlinks.parkdemo.entity.park.ParkFee
import com.nlinks.parkdemo.entity.park.ParkRecord
import com.nlinks.parkdemo.http.BaseObserver
import com.nlinks.parkdemo.http.HttpHelper
import com.nlinks.parkdemo.http.RxSchedulers
import com.nlinks.parkdemo.module.base.BaseActivity
import com.nlinks.parkdemo.utils.DateUtils
import com.nlinks.parkdemo.utils.NlinksParkUtils
import com.nlinks.parkdemo.utils.StringUtils
import com.nlinks.parkdemo.widget.DialogSingleMsg
import kotlinx.android.synthetic.main.activity_park_record_order_details.*

class ParkRecordOrderDetailsAty : BaseActivity() {
    private lateinit var mData: ParkRecord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_park_record_order_details)

        mData = intent.getParcelableExtra(EXTRA_DATA)
        initUI()

        getDateFromService()
    }

    private fun getDateFromService() {
        HttpHelper.getRetrofit().create(ParkAPI::class.java).getParkFee(mData.parkRecordId)
            .compose(RxSchedulers.io_main())
            .subscribe(object : BaseObserver<ArrayList<ParkFee>>() {
                override fun onHandleSuccess(t: ArrayList<ParkFee>) {
                    if (t.isEmpty()) return
                    emptyView.visibility = View.GONE
                    freeContainer.visibility = View.VISIBLE
                    var reducedPrice = 0.0
                    t.forEach {
                        freeContainer.addView(createParkFeeItem(it))
                        reducedPrice+=it.couponMoney
                        reducedPrice+=it.randomMoney
                    }
                    mParkRecordHeadMsg.setReducedPrice(reducedPrice)
                }
            })
    }

    @SuppressLint("SetTextI18n")
    fun createParkFeeItem(entity: ParkFee): View {
        val view = layoutInflater.inflate(R.layout.item_parkrecord_order, null)

        view.findViewById<TextView>(R.id.tvTime).text = DateUtils.convertStr(entity.createTime,
            "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日 HH:mm")


        view.findViewById<TextView>(R.id.tvPayChannel).text = NlinksParkUtils.formatPayChannel(entity.payChannel)
        view.findViewById<TextView>(R.id.tvPaidMoney).text = "${StringUtils.formatMoney(entity.realMoney)}元"

        if (entity.couponMoney == 0.0) view.findViewById<View>(R.id.layoutCoupon).visibility = View.GONE
        else {
            view.findViewById<TextView>(R.id.tvCouponMoney).text = "${StringUtils.formatMoney(entity.couponMoney)}元"
        }
        if (entity.randomMoney == 0.0) view.findViewById<View>(R.id.LayoutRandom).visibility = View.GONE
        else {
            view.findViewById<TextView>(R.id.tvRandomMoney).text = "${StringUtils.formatMoney(entity.randomMoney)}元"
        }
        return view
    }


    private fun initUI() {
        tvParkName.text = mData.parkName
        //收费标准
        orderStandard.setOnClickListener {
            val dialog = DialogSingleMsg(this)
            dialog.setTitle("收费标准")
            dialog.setMessage(if (StringUtils.isEmpty(mData.chargeStandard)) "暂无价格信息" else mData.chargeStandard)
            dialog.show()
        }
        mParkRecordHeadMsg.apply {
            setParkPrice(mData.consume.toString())
            setItemLeftMsg(mData.plateNum, "车牌号")
            setItemRightMsg(DateUtils.formatSeconds(mData.parkSeconds), "停车时长")
        }

        //创建添加出入场时间
        createTimeContainerView()?.forEach { llTimeContainer.addView(it) }
    }

    /**
     * 创建添加出入场时间
     */
    @SuppressLint("SetTextI18n")
    private fun createTimeContainerView(): ArrayList<View>? {
        val list = arrayListOf<View>()
        if (StringUtils.isNotEmpty(mData.inTime)) {
            list.add(TextView(this).apply {
                setTextColor(ContextCompat.getColor(this@ParkRecordOrderDetailsAty, R.color.text_hint))
                text = "入场时间: ${mData.inTime}"
                gravity = Gravity.CENTER_HORIZONTAL
            })
        }
        if (StringUtils.isNotEmpty(mData.outTime)) {
            list.add(TextView(this).apply {
                setTextColor(ContextCompat.getColor(this@ParkRecordOrderDetailsAty, R.color.text_hint))
                text = "出场时间: ${mData.outTime}"
                gravity = Gravity.CENTER_HORIZONTAL
            })
        }
        return list
    }


    companion object {
        private const val EXTRA_DATA = "data_bean"
        @JvmStatic
        fun start(context: Context, bean: ParkRecord) {
            val intent = Intent(context, ParkRecordOrderDetailsAty::class.java)
            intent.putExtra(EXTRA_DATA, bean)
            context.startActivity(intent)
        }
    }
}
