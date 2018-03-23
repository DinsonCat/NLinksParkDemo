package com.nlinks.parkdemo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.other.BrandOfCar;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * @author Dinson
 * @date 2017/6/28
 */
public class BrandOfCarAdapter extends CommonAdapter<BrandOfCar.DatasBean> {


    private HashMap<String, Integer> letterIndexes;

    public BrandOfCarAdapter(Context context, List<BrandOfCar.DatasBean> dataList) {
        super(context, dataList);

        letterIndexes = new HashMap<>();

        for (int index = 0; index < dataList.size(); index++) {
            //当前首字母
            String currentLetter = dataList.get(index).getLetter();
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? dataList.get(index - 1).getLetter() : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
//                sections[index] = currentLetter;
            }
        }

    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_brand_of_car;
    }

    @Override
    public void convert(CommonViewHolder holder, BrandOfCar.DatasBean datasBean, int position) {
        holder.setText(R.id.tv_name, datasBean.getBrandname());
        boolean equals = false;
        if (position > 0)
            equals = TextUtils.equals(datasBean.getLetter(), mDataList.get(position - 1).getLetter());
        holder.getView(R.id.tv_letter).setVisibility(equals ? View.GONE : View.VISIBLE);
        if (!equals)
            holder.setText(R.id.tv_letter, datasBean.getLetter());

        ImageView iv_img = holder.getView(R.id.iv_img);
        Glide.with(mContext).load(datasBean.getBrandimg()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_img);


    }

    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }
}
