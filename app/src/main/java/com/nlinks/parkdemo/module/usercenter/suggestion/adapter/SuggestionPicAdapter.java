package com.nlinks.parkdemo.module.usercenter.suggestion.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.CommonViewHolder;

import java.util.List;

import static com.nlinks.parkdemo.R.id.fl_root;

/**
 * Created by PC-DINSON on 2017/4/21.
 */
public class SuggestionPicAdapter extends CommonAdapter<String> {

    public SuggestionPicAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_suggest_pic;
    }

    @Override
    public void convert(CommonViewHolder holder, String item, final int position) {
        ImageView view = holder.getView(R.id.squareImageView);
        Glide.with(UIUtils.getContext()).load(item).dontAnimate().into(view);

        holder.getView(fl_root).setVisibility(position == 3 ? View.GONE : View.VISIBLE);

        ImageView iv_delete = holder.getView(R.id.iv_delete);
        iv_delete.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        iv_delete.setOnClickListener(position == getItemCount() - 1 ? null : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}
