package com.nlinks.parkdemo.module.messagecenter.adapter;

import android.view.View;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.msgpush.ServerMessage;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeViewHolder;
import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

public class MessageTypeFactory implements TypeFactory {
    public static final int TYPE_HEAD = R.layout.item_mc_message_head;
    public static final int TYPE_ITEM = R.layout.item_mc_message_normal;


    @Override
    public int type(Visitable bean) {
        if (bean instanceof ServerMessage) {
            return TYPE_HEAD;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public MultiTypeViewHolder createViewHolder(int type, View itemView) {
        if (type == TYPE_ITEM) {
            return new MessageItemHolder(itemView);
        } else {
            return new MessageHeadHolder(itemView);
        }
    }
}
