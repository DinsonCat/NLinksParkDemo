package com.nlinks.parkdemo.widget.recycleview;


import android.view.View;

public interface TypeFactory {
    int type(Visitable bean);
    MultiTypeViewHolder createViewHolder(int type, View itemView);
}

