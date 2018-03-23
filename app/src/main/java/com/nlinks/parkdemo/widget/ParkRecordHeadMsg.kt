package com.nlinks.parkdemo.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.nlinks.parkdemo.R
import com.nlinks.parkdemo.utils.StringUtils
import kotlinx.android.synthetic.main.parkrecord_head_message.view.*
import org.jetbrains.anko.dip

/**
 * 停车记录头部信息展示
 * （停车费用，车牌号，停车时长，优惠金额）
 */
class ParkRecordHeadMsg @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        setPadding(0, 0, 0, dip(20))
        setBackgroundResource(R.color.white)
        LayoutInflater.from(context).inflate(R.layout.parkrecord_head_message, this, true)
    }

    fun setParkPrice(money: String) {
        tvParkPrice.text = StringUtils.formatMoney(money)
        val span = SpannableString("元")
        span.setSpan(RelativeSizeSpan(0.5f), 0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvParkPrice.append(span)
    }

    fun setReducedPrice(text: String, colorId: Int) {
        tvReducedPrice.visibility = View.VISIBLE
        tvReducedPrice.setTextColor(ContextCompat.getColor(context, colorId))
        tvReducedPrice.text = text
    }

    @SuppressLint("SetTextI18n")
    fun setReducedPrice(money: Double) {
        if (money == 0.0) return
        tvReducedPrice.visibility = View.VISIBLE
        tvReducedPrice.text = "优惠金额-${StringUtils.formatMoney(money)}元"
    }


    fun setItemLeftMsg(msgTop: String, msgDesc: String) {
        setParkItemMsg(tvItemLeft, msgTop, msgDesc)
    }

    fun setItemRightMsg(msgTop: String, msgDesc: String) {
        setParkItemMsg(tvItemRight, msgTop, msgDesc)
    }

    @SuppressLint("SetTextI18n")
    private fun setParkItemMsg(textView: TextView, msgTop: String, msgDesc: String) {
        textView.text = "$msgTop\n"
        val span = SpannableString(msgDesc)
        span.setSpan(RelativeSizeSpan(0.8f), 0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        val color = ContextCompat.getColor(context, R.color.text_hint)
        span.setSpan(ForegroundColorSpan(color),
            0, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.append(span)
    }
}
