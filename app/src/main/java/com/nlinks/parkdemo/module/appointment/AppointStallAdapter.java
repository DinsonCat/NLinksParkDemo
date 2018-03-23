package com.nlinks.parkdemo.module.appointment;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.appointment.AppointStall;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 预约车位
 * Created by Dell on 2017/05/05.
 */
public class AppointStallAdapter extends CommonAdapter<AppointStall> {

    private ParkMain mParkList;
    private final int mOrangeColor;

    public void setParkList(ParkMain parkList) {
        mParkList = parkList;
    }

    public AppointStallAdapter(Context context, List<AppointStall> dataList) {
        super(context, dataList);
        mOrangeColor = ContextCompat.getColor(context, R.color.money_orange);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public int getLayoutId(int viewType) {
        return viewType == 0 ? R.layout.item_appointment_stall_header : R.layout.item_appointment_stall;
    }

    @Override
    public void convert(CommonViewHolder holder, AppointStall appointStall, int position) {
        if (position == 0 && mParkList != null) {
            holder.setText(R.id.tv_park_name, mParkList.getName());
            holder.setText(R.id.tv_park_type, mParkList.getType() == 1 ? "路内" : "路外");
            holder.setText(R.id.tv_park_address, mParkList.getAddress() + " | " + MapUtil
                .getConvertDistance(mParkList.getDistance()));
            holder.setText(R.id.tvItemLeft, "总车位数：" + mParkList.getStallNum());
            TextView tvHeaderParkRest = holder.getView(R.id.tvItemRight);
            tvHeaderParkRest.setText(TextColorBuilder.newBuilder().addTextPart("可预约车位数：")
                                         .addTextPart(mOrangeColor, mParkList.getAppointCount())
                                         .buildSpanned());
            TextView tv_item_empty_hint = holder.getView(R.id.tv_item_empty_hint);
            tv_item_empty_hint.setVisibility(getItemCount() > 1 ? View.GONE : View.VISIBLE);
        } else if (position > 0 && appointStall != null) {
            TextView tvItemAppointmentTime = holder.getView(R.id.tv_item_appointment_time);
            TextView tvItemAppointmentDeadline = holder.getView(R.id.tv_item_appointment_deadline);
            tvItemAppointmentTime.setText(appointStall.getMoney() + "元/小时");
            tvItemAppointmentDeadline.setText("可预约至" + appointStall.getEndTime());
        }
    }
}
