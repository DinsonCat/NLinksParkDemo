package com.nlinks.parkdemo.module.park;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 停车场
 * Created by Dell on 2017/3/31.
 */
public class ParkListAdapter extends CommonAdapter<ParkMain> {

    private int mColor;

    public ParkListAdapter(Context context, List<ParkMain> dataList) {
        super(context, dataList);
        mColor = ContextCompat.getColor(context, R.color.money_orange);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_park_list;
    }

    @Override
    public void convert(CommonViewHolder holder, ParkMain bean, int position) {
        if (bean != null) {
            holder.setText(R.id.tv_park_name, bean.getName());
            holder.setText(R.id.tv_park_type, NlinksParkUtils.formatParkType(bean.getType()));
            holder.setText(R.id.tv_park_address, bean.getAddress() + " | " + MapUtil.getConvertDistance(bean.getDistance()));
            holder.setText(R.id.tv_park_count, "总车位数：" + bean.getStallNum());
            TextView tvParkRest = holder.getView(R.id.tv_park_rest);
            tvParkRest.setText(TextColorBuilder.newBuilder()
                .addTextPart("空车位数：").addTextPart(mColor, bean.getUnuedStallNum())
                .buildSpanned());
        }
    }
}
