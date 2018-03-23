package com.nlinks.parkdemo.module.usercenter.walletmoney;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PayAPI;
import com.nlinks.parkdemo.entity.pay.WalletMoney;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.module.coupon.ParkingCouponActivity;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

import io.reactivex.Observable;

/**
 * 我的钱包
 */
public class WalletMoneyActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_money);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromServer();
    }

    private void initUI() {
        findViewById(R.id.tv_member).setOnClickListener(this);
        findViewById(R.id.ll_coupon).setOnClickListener(this);
        findViewById(R.id.tv_title_right).setOnClickListener(this);
        findViewById(R.id.tv_turn_recharge).setOnClickListener(this);
        findViewById(R.id.tv_monthly).setOnClickListener(this);
    }

    private void getDataFromServer() {
        String userId = SPUtils.getUserId(this, "");
        if (StringUtils.isEmpty(userId)) return;

        HttpHelper.getRetrofit().create(PayAPI.class).getBalance(userId)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<WalletMoney>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(WalletMoney walletMoney) {
                setData(walletMoney);
            }
        });
    }

    private void setData(WalletMoney walletMoney) {
        TextView tv_balance = findViewById(R.id.tv_balance);//账户余额
        TextView tv_money_share = findViewById(R.id.tv_money_share);//共享金额
        TextView tv_money_count = findViewById(R.id.tv_money_count);//总资产

        tv_balance.setText(StringUtils.formatMoney(walletMoney.getUsableMoney()) + "\n");
        SpannableString span = new SpannableString("账户余额(元)");
        span.setSpan(new RelativeSizeSpan(0.7f), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(UIUtils.getColor(R.color.text_primary)), 0, span.length(),
                     Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_balance.append(span);

        double shareMoney = walletMoney.getSharedBenefit();

        tv_money_share.setText(StringUtils.formatMoney(shareMoney) + "\n");
        SpannableString shareSpan = new SpannableString("共享收益(元)");
        shareSpan.setSpan(new RelativeSizeSpan(0.7f), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        shareSpan.setSpan(new ForegroundColorSpan(UIUtils.getColor(R.color.text_primary)), 0, span.length(),
                          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_money_share.append(shareSpan);

        tv_money_count.setText(StringUtils.formatMoney((walletMoney.getUsableMoney() + shareMoney)));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_turn_recharge:
                validateTojump(RechargeActivity.class);
                break;
            case R.id.ll_coupon:
                validateTojump(ParkingCouponActivity.class);
                break;
            case R.id.tv_title_right://钱包明细
                //                validateTojump(AsMemberActivity.class);//会员卡详情
                validateTojump(WalletMoneyDetailActivity.class);
                break;
            case R.id.tv_member:
                //会员
                String userId = SPUtils.getUserId(this);
                if (StringUtils.isEmpty(userId)) return;
                if (SPUtils.isMember(this)) {
                    WebViewPromotionActivity.start(this, AppConst.URL_MEMBER_DETAIL);
                } else {
                    WebViewPromotionActivity.start(this, AppConst.URL_MEMBER);
                }
                break;
            case R.id.tv_monthly:
                MonthCardActivity.start(this);
                break;
            default:
                break;
        }
    }
}
