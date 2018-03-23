package com.nlinks.parkdemo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.favoritepark.FavorParkInfo;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * 我的关注详情适配器
 */
public class MyFollowAdapter extends CommonAdapter<FavorParkInfo> {


    private final int mColor;

    public MyFollowAdapter(Context context, List<FavorParkInfo> dataList) {
        super(context, dataList);

        mColor = ContextCompat.getColor(context, R.color.colorPrimary);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_park_list;
    }

    @Override
    public void convert(CommonViewHolder holder, FavorParkInfo bean, int position) {
        holder.setText(R.id.tv_park_name, bean.getName());
        holder.setText(R.id.tv_park_type, NlinksParkUtils.formatParkType(bean.getType()));
        holder.setText(R.id.tv_park_address, bean.getAddress() );
        holder.setText(R.id.tv_park_count, "总车位数：" + bean.getStallNum());
        TextView tvParkRest = holder.getView(R.id.tv_park_rest);
        tvParkRest.setText(TextColorBuilder.newBuilder()
            .addTextPart("空车位数：").addTextPart(mColor, bean.getUnUsedStallNum())
            .buildSpanned());

    }
}
