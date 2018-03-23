package com.nlinks.parkdemo.modle;

import android.os.Parcel;
import android.os.Parcelable;

import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.StringUtils;

/**
 * 优惠券验证是否可用实体
 * （最低使用限额，支持的停车场，支持的支付类型）
 */
public class CouponValidateExtra implements Parcelable {
    private int mPayType = -1;
    private double consume = 0;
    private String parkCode;

    public CouponValidateExtra() {
    }

    /**
     * 判断当前优惠券是否可用
     *
     * @param coupon 优惠券实体
     * @return true表示当前优惠券可用，false表示当前优惠券不支持
     */
    public boolean validate(ParkingCoupon coupon) {
        boolean flag = true;
        if (StringUtils.isNotEmpty(coupon.getParkCode()) && !coupon.getParkCode().equals(parkCode)) {
            return false;
        }

        try {
            if (consume == 0 && coupon.getCouponUseLimit() < consume)
                return false;
        } catch (Exception e) {
            return false;
        }
        if (mPayType < 0) return false;
        if (!NlinksParkUtils.isContainPayType(coupon.getApplication(), mPayType)) return false;
        return flag;
    }

    protected CouponValidateExtra(Parcel in) {
        mPayType = in.readInt();
        consume = in.readDouble();
        parkCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPayType);
        dest.writeDouble(consume);
        dest.writeString(parkCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CouponValidateExtra> CREATOR = new Creator<CouponValidateExtra>() {
        @Override
        public CouponValidateExtra createFromParcel(Parcel in) {
            return new CouponValidateExtra(in);
        }

        @Override
        public CouponValidateExtra[] newArray(int size) {
            return new CouponValidateExtra[size];
        }
    };

    public int getPayType() {
        return mPayType;
    }

    public void setPayType(int payType) {
        mPayType = payType;
    }

    public double getConsume() {
        return consume;
    }

    public void setConsume(double consume) {
        this.consume = consume;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }
}
