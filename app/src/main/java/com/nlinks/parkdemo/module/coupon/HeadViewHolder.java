package com.nlinks.parkdemo.module.coupon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeViewHolder;

/**
 * @author Dinson - 2017/7/8
 */
public class HeadViewHolder extends MultiTypeViewHolder<Head> implements View.OnClickListener {
    private OnExchangeListener listener;

    public HeadViewHolder(View itemView, OnExchangeListener listener) {
        super(itemView);
        this.listener = listener;
    }

    @Override
    public void setUpView(Head bean, int position, RecyclerView.Adapter adapter) {
        getView(R.id.btn_exchange).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText et_code = getView(R.id.et_code);
        String code = et_code.getText().toString().trim();
        if (!StringUtils.isEmpty(code))
            listener.onExchange(v, code);
    }
}
