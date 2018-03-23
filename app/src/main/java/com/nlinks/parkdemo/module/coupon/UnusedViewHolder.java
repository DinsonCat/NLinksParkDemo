package com.nlinks.parkdemo.module.coupon;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.TypefaceUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeViewHolder;

import java.math.BigDecimal;

/**
 * @author Dinson - 2017/7/8
 */
public class UnusedViewHolder extends MultiTypeViewHolder<ParkingCoupon> {

    private CouponValidateExtra mExtra;

    public UnusedViewHolder(View itemView, CouponValidateExtra extra) {
        super(itemView);
        mExtra = extra;
    }

    @Override
    public void setUpView(ParkingCoupon parkingCoupon, int position, RecyclerView.Adapter adapter) {
        if (parkingCoupon == null) return;

        TextView tvCouponType = getView(R.id.tv_item_coupon_type);
        TextView tvCouponCount = getView(R.id.tv_item_coupon_count);
        tvCouponCount.setTypeface(TypefaceUtils.get(UIUtils.getContext(), "fonts/PingFangNum.ttf"));
        //setText(R.id.tv_item_coupon_name, "(" + parkingCoupon.getCouponName() + ")");

        String endTime = parkingCoupon.getCouponEndTime().split(" ")[0];
        setText(R.id.tv_item_coupon_deadline, "有效期至" + endTime);
        setText(R.id.tv_item_coupon_condition, parkingCoupon.getCouponRemark());


        View root = getView(R.id.rl_root);
        root.setEnabled(true);

        if (parkingCoupon.getCouponType() == 1) {
            tvCouponType.setText("抵用券");
            double money = parkingCoupon.getCouponAmount();
            String moneyStr = money == (int) money ? String.valueOf((int) money) : StringUtils.formatMoney(money);
            Spannable span = new SpannableString("￥" + moneyStr);
            span.setSpan(new RelativeSizeSpan(0.7f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCouponCount.setText(span);

            root.setBackgroundResource(R.mipmap.bg_coupon_qctt);
        } else {
            tvCouponType.setText("折扣券");
            BigDecimal bigDecimal = new BigDecimal(parkingCoupon.getCouponDiscount() * 10).setScale(1, BigDecimal.ROUND_HALF_UP);
            Spannable span2 = new SpannableString(bigDecimal + "折");
            span2.setSpan(new RelativeSizeSpan(0.5f), 1, span2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCouponCount.setText(span2);

            root.setBackgroundResource(R.mipmap.bg_coupon_zbhx);
        }

        switch (parkingCoupon.getStatus()) {
            case 1:
                root.setAlpha(1f);
                break;
            case 2:
            case 3:
            default:
                root.setAlpha(0.5f);
                break;
        }

        if (mExtra != null) {
            //没达到最低限制金额或者不是支持的券种或者不是未使用，不让点击
            if (parkingCoupon.getStatus() != 1 || !mExtra.validate(parkingCoupon)) {
                root.setAlpha(0.5f);
                root.setEnabled(false);
            }
        }
    }
}
