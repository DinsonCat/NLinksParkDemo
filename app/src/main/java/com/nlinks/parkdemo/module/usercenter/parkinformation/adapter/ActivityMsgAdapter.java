package com.nlinks.parkdemo.module.usercenter.parkinformation.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.infomation.ActivityMsg;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * Created by PC-DINSON on 2017/4/16.
 */
public class ActivityMsgAdapter extends CommonAdapter<ActivityMsg> {
    public ActivityMsgAdapter(Context context,  List<ActivityMsg> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_park_infomation;
    }

    @Override
    public void convert(CommonViewHolder holder, ActivityMsg activityMsg, int position) {
        if (activityMsg != null) {
            ImageView ivActivityPic = holder.getView(R.id.iv_activity_pic);
            TextView tvActivityTitle = holder.getView(R.id.tv_activity_title);
            TextView tvActivityDate = holder.getView(R.id.tv_activity_date);

            Glide.with(UIUtils.getContext())
                    .load(activityMsg.getCover_pic())
                    .asBitmap()
                    .into(ivActivityPic);
            tvActivityTitle.setText(activityMsg.getTitle());
            tvActivityDate.setText(activityMsg.getSendTime());

        }
    }
}
