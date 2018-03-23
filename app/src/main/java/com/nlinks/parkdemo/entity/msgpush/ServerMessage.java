package com.nlinks.parkdemo.entity.msgpush;

import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

/**
 * 消息中心系统消息的实体类
 * @author Dinson - 2017/9/6
 */
public class ServerMessage implements Visitable{
    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
