package com.nlinks.parkdemo.module.usercenter.shareparking;

import android.content.Context;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.parkshare.ShareParkList;
import com.nlinks.parkdemo.entity.parkshare.ShareParkStateBean;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

public class ShareParkMainAdapter extends CommonAdapter<ShareParkList> {
    public ShareParkMainAdapter(Context context, List<ShareParkList> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_share_main;
    }

    @Override
    public void convert(CommonViewHolder holder, ShareParkList bean, int position) {
        holder.setText(R.id.tv_park_name,bean.getParkName());
        holder.setText(R.id.tv_park_num,bean.getStallName());
        ShareParkStateBean stateBean = NlinksParkUtils.formatShareParkState(bean.getAuditStatus(), bean.getSharingStatus());
        TextView tv = holder.getView(R.id.tv_park_state);
        tv.setText(stateBean.getStateName());
        tv.setTextColor(mContext.getResources().getColor(stateBean.getColor()));
        holder.getView(R.id.view_line).setBackgroundColor(mContext.getResources().getColor(stateBean.getColor()));
    }
}