package com.nlinks.parkdemo.module.messagecenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nlinks.parkdemo.entity.msgpush.ServerMessage;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeViewHolder;

/**
 * @author Dinson - 2017/7/8
 */
public class MessageHeadHolder extends MultiTypeViewHolder<ServerMessage> {

    public MessageHeadHolder(View itemView ) {
        super(itemView);
    }

    @Override
    public void setUpView(ServerMessage parkingCoupon, int position, RecyclerView.Adapter adapter) {

    }
}
