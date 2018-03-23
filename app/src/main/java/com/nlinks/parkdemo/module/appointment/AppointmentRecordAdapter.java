package com.nlinks.parkdemo.module.appointment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 预约记录
 * Created by Dell on 2017/05/02.
 */
public class AppointmentRecordAdapter extends CommonAdapter<AppointmentRecord> {

    public AppointmentRecordAdapter(Context context, List<AppointmentRecord> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_appointment_record;
    }

    @Override
    public void convert(CommonViewHolder holder, AppointmentRecord appointmentRecord, int position) {
        TextView tvItemParkName = holder.getView(R.id.tv_item_park_name);
        TextView tvItemParkLocation = holder.getView(R.id.tv_item_park_location);
        TextView tvItemParkPrice = holder.getView(R.id.tv_item_park_price);
        TextView tvItemTitle0 = holder.getView(R.id.tvt_appointment_time);
        TextView tvItemParkDate0 = holder.getView(R.id.tv_item_park_date0);
        TextView tvItemTitle1 = holder.getView(R.id.tv_item_title1);
        TextView tvItemParkDate1 = holder.getView(R.id.tv_item_park_date1);
        //TextView tvItemTitle2 = holder.getView(R.id.tv_item_title2);
        TextView tvItemParkCarCode = holder.getView(R.id.tv_item_park_car_code);
        View vSplitLine = holder.getView(R.id.v_split_line);
        View rlAppointmentCarStatus = holder.getView(R.id.rl_appointment_car_status);
        TextView tvItemCarStatus = holder.getView(R.id.tv_item_car_status);

        if (appointmentRecord != null) {
            String parkStatus = appointmentRecord.getParkStatus();
            if ("1".equals(parkStatus) || "2".equals(parkStatus) | "3".equals(parkStatus)) {
                vSplitLine.setVisibility(View.VISIBLE);
                rlAppointmentCarStatus.setVisibility(View.VISIBLE);
                tvItemTitle1.setVisibility(View.GONE);
                tvItemCarStatus.setText("1".equals(parkStatus) ? "未驶入" : "2".equals(parkStatus) ? "未驶出" : "已超时");
                tvItemTitle0.setText("预约出场时间");
                tvItemParkDate0.setText(appointmentRecord.getLeaveTime());
            } else {
                vSplitLine.setVisibility(View.GONE);
                rlAppointmentCarStatus.setVisibility(View.GONE);
                tvItemTitle1.setVisibility(View.VISIBLE);
                tvItemTitle0.setText("入场时间");
                tvItemParkDate0.setText(appointmentRecord.getEnterTime());
                tvItemTitle1.setText("出场时间");
                tvItemParkDate1.setText(appointmentRecord.getLeaveTime());
            }
            tvItemParkName.setText(appointmentRecord.getName());
            tvItemParkLocation.setText(appointmentRecord.getAddress());
            tvItemParkPrice.setText(String.format("￥%s", StringUtils.formatMoney(appointmentRecord.getCharge()
                    + appointmentRecord.getChargeOut())));
            tvItemParkCarCode.setText(appointmentRecord.getPlateNum());
        }
    }
}
