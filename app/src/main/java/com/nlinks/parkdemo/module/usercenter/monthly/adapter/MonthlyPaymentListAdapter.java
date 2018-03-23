package com.nlinks.parkdemo.module.usercenter.monthly.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.monthly.MonthlyPark;
import com.nlinks.parkdemo.module.usercenter.monthly.MonthlyParkDetailActivity;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 错峰停车包月列表适配器
 */

public class MonthlyPaymentListAdapter extends CommonAdapter<MonthlyPark> {

    private final List<String> mClickedList;

    public MonthlyPaymentListAdapter(Context context, List<MonthlyPark> dataList) {
        super(context, dataList);
        mClickedList = SPUtils.getMonthlyPayListClicked(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_monthly_main;
    }

    @Override
    public void convert(final CommonViewHolder holder, final MonthlyPark bean, final int position) {
        holder.setText(R.id.tvParkName, bean.getName());
        holder.setText(R.id.tvParkAddress, bean.getAddress());
        formatChareFee((TextView) holder.getView(R.id.itemFront), bean.getChareFee());

        //如果已点击，则名字和地址都显示hint颜色
        if (mClickedList.contains(bean.getParkCode())) {
            holder.getView(R.id.tvParkName).setEnabled(false);
            holder.getView(R.id.tvParkAddress).setEnabled(false);
        }

        holder.getView(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.getView(R.id.tvParkName).setEnabled(false);
                holder.getView(R.id.tvParkAddress).setEnabled(false);
                SPUtils.putMonthlyPayListClicked(mContext, bean.getParkCode());
                MonthlyParkDetailActivity.start(mContext, bean);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void formatChareFee(TextView tv, String text) {
        tv.setText("￥" + text);
        SpannableString span = new SpannableString("起");
        span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_secondary)),
            0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.7f), 0, span.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(span);
    }
}
