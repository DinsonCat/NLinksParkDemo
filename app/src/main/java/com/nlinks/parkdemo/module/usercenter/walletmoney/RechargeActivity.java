package com.nlinks.parkdemo.module.usercenter.walletmoney;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity._req.PayOrderReq;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.PayUtil;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.MultiLineRadioGroup;

public class RechargeActivity extends BaseActivity implements View.OnClickListener
        , MultiLineRadioGroup.OnCheckedChangedListener {

    private CheckBox cb_ali;
    private CheckBox cb_wx;
    private String mFee = null;
    private String[] mRechargeMoneyList;
    private RadioGroup mRgPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        mRechargeMoneyList = getResources().getStringArray(R.array.recharge_num);
        initUI();
    }

    private void initUI() {
        MultiLineRadioGroup multi_line_rg = (MultiLineRadioGroup) findViewById(R.id.multi_line_rg);
        multi_line_rg.setItemChecked(0);
        initFee(0);
        multi_line_rg.setOnCheckChangedListener(this);

        findViewById(R.id.btn_do_recharge).setOnClickListener(this);


        mRgPay = (RadioGroup) findViewById(R.id.rg_pay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_do_recharge:
                PayOrderReq orderReq = new PayOrderReq();
                LogUtils.d("-------- fee is " + mFee);//mFee = "0.01";
                orderReq.setPayType(1);
                orderReq.setTotalFee(mFee);
                orderReq.setOrderDesc("钱包充值");
                orderReq.setOrderDetail("钱包充值" + mFee);
                orderReq.setOrderAttach(getResources().getString(R.string.app_name));
                orderReq.setUserId(SPUtils.getUserId(this, null));


                switch (mRgPay.getCheckedRadioButtonId()) {
                    case R.id.rb_ali:
                        PayUtil.alipay(this, orderReq, true, new PayUtil.AliCallback() {
                            @Override
                            public void onAliResult(PayUtil.AliPayResult payResult) {
                                if (PayUtil.alipaySuccess(payResult)) {
                                    UIUtils.showToast("支付成功！");
                                    finish();
                                } else {
                                    UIUtils.showToast("支付失败！");
                                }
                            }
                        });
                        break;
                    case R.id.rb_wx:
                        PayUtil.weixin(this, orderReq, true, new PayUtil.WXCallback() {
                            @Override
                            public void onWXResult(int errCode, String errStr) {
                                UIUtils.showToast(errStr);
                                if (PayUtil.weixinSuccess(errCode)) finish();
                            }
                        });
                        break;

                }
                break;
        }
    }


    @Override
    public void onItemChecked(MultiLineRadioGroup group, int position, boolean checked) {
        initFee(position);
    }

    private void initFee(int position) {
        mFee = mRechargeMoneyList == null || mRechargeMoneyList.length == 0
                ? "0" : StringUtils.getPureNumber(mRechargeMoneyList[position]);
    }


}
