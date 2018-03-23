package com.nlinks.parkdemo.listener;

import com.nlinks.parkdemo.entity.park.ParkingCoupon;

/**
 * 选择最佳优惠券的监听回调
 */

public interface OnChooseGreatListener {
    void onGreat(ParkingCoupon theGreat);
}
