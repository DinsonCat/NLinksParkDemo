package com.nlinks.parkdemo.module.usercenter.monthly.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.monthly.MonthlyStateBean;
import com.nlinks.parkdemo.entity.monthly.UserMonthlyOrderInfo;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 错峰包月记录适配器
 */
public class MonthlyOrderListAdapter extends CommonAdapter<UserMonthlyOrderInfo> {
    public MonthlyOrderListAdapter(Context context, List<UserMonthlyOrderInfo> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_monthly_order;
    }

    @Override
    public void convert(CommonViewHolder holder, UserMonthlyOrderInfo bean, int position) {

        holder.setText(R.id.tvTitle, bean.getParkName()+" － "+bean.getRuleName());
        holder.setText(R.id.tvStartTime, bean.getStartTime());
        holder.setText(R.id.tvEndTime, bean.getEndTime());
        holder.setText(R.id.tvPlate, bean.getPlateNum());

        TextView tvOrderState = holder.getView(R.id.tvOrderState);
        tvOrderState.setText("订单状态: ");

        MonthlyStateBean stateBean = NlinksParkUtils.formatMonthlyState(bean.getStatus());
        SpannableString span = new SpannableString(stateBean.getStateName());
        span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(stateBean.getColor())), 0,
                     stateBean.getStateName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvOrderState.append(span);
    }
}
