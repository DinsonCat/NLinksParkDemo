package com.nlinks.parkdemo.module.usercenter.walletmoney.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.monthly.UserMonthlyOrderInfo;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 月卡列表适配器
 */
public class MonthCardListAdapter extends CommonAdapter<UserMonthlyOrderInfo> {
    public MonthCardListAdapter(Context context, List<UserMonthlyOrderInfo> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_month_card;
    }

    @Override
    public void convert(CommonViewHolder holder, UserMonthlyOrderInfo bean, int position) {
        holder.setText(R.id.tvParkName, bean.getParkName());
        holder.setText(R.id.tvPlate, bean.getPlateNum() + "专属");
        holder.setText(R.id.tvDeathLine, "有效期至" + bean.getEndTime());
        //holder.setText(R.id.tvParkTime, NlinksParkUtils.formatRuleTime(bean.getRuleTime()));

        //替换背景
        ImageView iv = holder.getView(R.id.ivCardBg);
        if (bean.getStatus() != 2) {
            iv.setImageResource(R.drawable.month_card_bg_disable);
        }else{
            iv.setImageResource(R.drawable.month_card_bg);
        }
        //显示未生效
        if (bean.getStatus() == 1) {
            holder.getView(R.id.state).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.state).setVisibility(View.GONE);
        }
    }
}
