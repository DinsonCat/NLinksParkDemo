package com.nlinks.parkdemo.module.park;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * Created by Dell on 2017/04/24.
 */

public class LocationAdapter extends CommonAdapter<PoiInfo> {

    private boolean mHistory = false;

    public LocationAdapter(Context context, List<PoiInfo> dataList) {
        super(context, dataList);
    }

//	@Override
//	public int getItemViewType(int position) {
//		return position < 0 || position >= getItemCount() || getItem(position) == null
//				|| getItem(position).isHistory() ? 0 : 1;
//	}

    public void setHistory(boolean history) {
        mHistory = history;
    }

    public boolean isHistory() {
        return mHistory;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_search_location;
    }

    @Override
    public void convert(CommonViewHolder holder, PoiInfo poiInfo, int position) {
        if (poiInfo != null) {
            ImageView ivItemIcon = holder.getView(R.id.iv_item_icon);
            //ImageView ivItemRight = holder.getView(R.id.iv_item_right);
            TextView tvItemName = holder.getView(R.id.tv_item_name);
            TextView tvItemAddress = holder.getView(R.id.tv_item_address);

            tvItemName.setText(poiInfo.name);
            if (mHistory) {
                ivItemIcon.setImageResource(R.mipmap.icon_park_bussiness_hours);
                tvItemAddress.setVisibility(View.GONE);
            } else {
                ivItemIcon.setImageResource(R.mipmap.icon_park_list_location);
                tvItemAddress.setVisibility(View.VISIBLE);
                tvItemAddress.setText(poiInfo.address);
            }


        }
    }
}
