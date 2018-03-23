package com.nlinks.parkdemo.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.pay.WalletMoneyDetail;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 我的钱包详情适配器
 */
public class WalletMoneyDetailAdapter extends CommonAdapter<WalletMoneyDetail> {


    public WalletMoneyDetailAdapter(Context context, List<WalletMoneyDetail> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_walletmoney_detail;
    }

    @Override
    public void convert(CommonViewHolder holder, WalletMoneyDetail bean, int position) {
        holder.setText(R.id.tv_content,
                       NlinksParkUtils.formatTransactDesc(bean.getTransactDesc()) + " ( " + NlinksParkUtils
                           .formatPayChannel(bean.getPayChannel()) + " )");//内容
        holder.setText(R.id.tv_date, bean.getCreateTime());//时间

        //条目头部图片
        ImageView iv_item_head = (ImageView) holder.getView(R.id.iv_item_head);
        iv_item_head.setImageDrawable(
            bean.getTransactDesc() == 1 ? UIUtils.getDrawable(R.mipmap.mx_cz) : UIUtils
                .getDrawable(R.mipmap.mx_zc));

        TextView tvMoney = holder.getView(R.id.tv_money);
        String money = StringUtils.formatMoney(bean.getTransactMoney());

        //1钱包充值、2停车缴费、3预约缴费、4预约超时缴费、5停车预缴、6路外停车缴费、7会员升级
        switch (bean.getTransactDesc()) {
            case 1:
                tvMoney.setText("+" + money);
                tvMoney.setTextColor(UIUtils.getColor(R.color.money_red));
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                if (bean.getTransactMoney() >= 0) {
                    tvMoney.setText("-" + money);
                } else {
                    tvMoney.setText(" " + money);
                }

                tvMoney.setTextColor(UIUtils.getColor(R.color.map_mark_cyan));
                break;
            default://未知
                holder.setText(R.id.tv_money, money);
                tvMoney.setTextColor(UIUtils.getColor(R.color.text_primary));
                break;
        }
    }
}
