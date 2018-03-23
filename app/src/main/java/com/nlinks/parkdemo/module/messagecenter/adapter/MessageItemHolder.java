package com.nlinks.parkdemo.module.messagecenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.msgpush.PushMessage;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeViewHolder;

/**
 * @author Dinson - 2017/7/8
 */
public class MessageItemHolder extends MultiTypeViewHolder<PushMessage> {
    public MessageItemHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void setUpView(PushMessage bean, int position, RecyclerView.Adapter adapter) {
        setText(R.id.tv_title, bean.getTitle());
        setText(R.id.tv_time, bean.getSendtime());
        setText(R.id.tv_content, bean.getMessage());
        getView(R.id.redPoint).setVisibility(bean.getIsread() == 1 ? View.GONE : View.VISIBLE);
    }
}
