package com.nlinks.parkdemo.module.coupon;

import android.view.View;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.utils.NlinksParkUtils.PayType;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeViewHolder;
import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

public class CounponUnusedTypeFactory implements TypeFactory {
    public static final int TYPE_HEAD = R.layout.item_coupon_head;
    public static final int TYPE_ITEM = R.layout.item_coupon_normal;

    private String money;
    private OnExchangeListener listener;
    private CouponValidateExtra mExtra;

    public CounponUnusedTypeFactory(OnExchangeListener listener, CouponValidateExtra extra) {
        this.money = money;
        this.listener = listener;
        this.mExtra = extra;
    }

    @Override
    public int type(Visitable bean) {
        if (bean instanceof ParkingCoupon) {
            return TYPE_ITEM;
        } else {
            return TYPE_HEAD;
        }
    }

    @Override
    public MultiTypeViewHolder createViewHolder(int type, View itemView) {
        if (type == TYPE_ITEM) {
            return new UnusedViewHolder(itemView,mExtra);
        } else {
            return new HeadViewHolder(itemView, listener);
        }
    }
}
