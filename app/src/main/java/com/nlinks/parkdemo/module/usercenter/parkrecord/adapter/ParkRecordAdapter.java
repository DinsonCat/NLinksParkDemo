package com.nlinks.parkdemo.module.usercenter.parkrecord.adapter;

import android.content.Context;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkRecord;
import com.nlinks.parkdemo.entity.park.RecordStateBean;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 停车记录适配器
 */
public class ParkRecordAdapter extends CommonAdapter<ParkRecord> {

    public ParkRecordAdapter(Context context, List<ParkRecord> dataList) {
        super(context, dataList);

    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_park_record;
    }

    @Override
    public void convert(CommonViewHolder holder, final ParkRecord bean, int position) {
        holder.setText(R.id.tv_park_name, bean.getParkName());//停车场名称
        RecordStateBean recordStateBean = NlinksParkUtils.formatParkRecordState(bean);
        TextView tv_state = holder.getView(R.id.tv_state);

        tv_state.setText(recordStateBean.getStateName());
        tv_state.setTextColor(UIUtils.getColor(recordStateBean.getColor()));//设置颜色
        holder.setText(R.id.tv_money, "￥" + StringUtils.formatMoney(bean.getConsume()));//设置消费金钱
        holder.setText(R.id.tv_plate, bean.getPlateNum());//设置车牌
        holder.setText(R.id.tv_park_time, DateUtils.formatSeconds(bean.getParkSeconds()));//设置停车时长
    }
}
