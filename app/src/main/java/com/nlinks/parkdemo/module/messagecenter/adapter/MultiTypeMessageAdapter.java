package com.nlinks.parkdemo.module.messagecenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nlinks.parkdemo.widget.recycleview.MultiTypeViewHolder;
import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

import java.util.List;


/**
 * 多重布局的适配器
 */
public class MultiTypeMessageAdapter extends RecyclerView.Adapter<MultiTypeViewHolder> {

    private List<Visitable> modelList;
    private final TypeFactory typeFactory;

    public MultiTypeMessageAdapter(List<Visitable> modelList, TypeFactory typeFactory) {
        this.modelList = modelList;
        this.typeFactory = typeFactory;
    }



    @Override
    public MultiTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(viewType, itemView);
    }

    @Override
    public void onBindViewHolder(MultiTypeViewHolder holder, int position) {
        holder.setUpView(modelList.get(position), position, this);
    }

    @Override
    public int getItemCount() {
        return modelList == null ? 0 : modelList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return modelList.get(position).type(typeFactory);
    }
}
