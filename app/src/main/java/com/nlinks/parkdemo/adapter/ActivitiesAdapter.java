package com.nlinks.parkdemo.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.advertising.AdvertisingInfo;
import com.nlinks.parkdemo.widget.GlideRoundTransform;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * Created by yanzhenyang on 2017/7/6.
 */

public class ActivitiesAdapter extends CommonAdapter<AdvertisingInfo> {

    public ActivitiesAdapter(Context context, List<AdvertisingInfo> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_activities_list;
    }

    @Override
    public void convert(CommonViewHolder holder, AdvertisingInfo bean, int position) {

        ImageView iv_activities_list_cover = holder.getView(R.id.iv_activities_list_cover);
//        bean.getCover()
//        iv_activities_list_cover.setImageResource();

        Glide.with(mContext).load(bean.getCover()).transform(new GlideRoundTransform(mContext, 3)).into(iv_activities_list_cover);

    }

}
