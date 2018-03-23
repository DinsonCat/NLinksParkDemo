package com.nlinks.parkdemo.adapter;

import android.content.Context;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

/**
 * @author Dinson
 * @date 2017/6/28
 */
public class BrandOfCarL2Adapter extends CommonAdapter<String> {
    public BrandOfCarL2Adapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_brand_of_car_l2;
    }

    @Override
    public void convert(CommonViewHolder holder, String bean, int position) {
        holder.setText(R.id.tv_name, bean);
    }
}
