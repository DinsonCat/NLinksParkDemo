package com.nlinks.parkdemo.module.usercenter.managecar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.IntentUtils;

public class CheckingActivity extends BaseActivity implements View.OnClickListener {


    private TextView mTvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);

        initUI();
    }

    private void initUI() {
        TextView tv_msg = (TextView) findViewById(R.id.tv_msg);
        String msg = getResources().getString(R.string.car_checking_mgs);
        SpannableString span = new SpannableString("1温馨小提示：");
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_bulb);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        span.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.2f), 1, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setText(span);
        tv_msg.append("\n" + msg);

        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvPhone.setOnClickListener(this);


    }


    public static void start(Context context) {
        Intent starter = new Intent(context, CheckingActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:
                startActivity(IntentUtils.getDialIntent(mTvPhone.getText().toString()));
                break;
        }
    }
}
