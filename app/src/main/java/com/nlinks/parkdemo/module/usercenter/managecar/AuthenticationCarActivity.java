package com.nlinks.parkdemo.module.usercenter.managecar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 车辆认证（提交正反行驶证）
 */
public class AuthenticationCarActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIvOne, mIvTwo;//行驶证的正反页
    private String[] mPhotoPathArr = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_car);

        initUI();
    }

    private void initUI() {
        TextView tv_msg =  findViewById(R.id.tv_msg);
        String msg = getResources().getString(R.string.car_check_mgs);
        SpannableString span = new SpannableString("1温馨小提示：");
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_bulb);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        span.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.2f), 1, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setText(span);
        tv_msg.append("\n" + msg);


        mIvOne =  findViewById(R.id.iv_one);
        mIvTwo =  findViewById(R.id.iv_two);
        mIvOne.setOnClickListener(this);
        mIvTwo.setOnClickListener(this);

        CommonTitleBar titlebar =   findViewById(R.id.titlebar);
        titlebar.getBtnRight().setOnClickListener(this);


        findViewById(R.id.btn_authentication).setOnClickListener(this);

        findViewById(R.id.tvError).setVisibility(
            getIntent().getBooleanExtra(KEY_IS_ERROR, false) ? View.VISIBLE : View.GONE);
    }

    private static final String KEY_PLATE = "PLATE";
    private static final String KEY_IS_ERROR = "IS_ERROR";

    public static void start(Context context, String plate,boolean isError) {
        Intent starter = new Intent(context, AuthenticationCarActivity.class);
        starter.putExtra(KEY_PLATE, plate);
        starter.putExtra(KEY_IS_ERROR, isError);
        context.startActivity(starter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_one:
                break;
            case R.id.iv_two:
                break;
            case R.id.tv_title_right:
                //手动输入
                AuthenticationByHandActivity.start(this, getIntent().getStringExtra(KEY_PLATE), CODE_BY_HAND);
                break;
            case R.id.btn_authentication:
                //图片认证
                if (StringUtils.isEmpty(mPhotoPathArr[0])) {
                    UIUtils.showToast("请拍摄行驶证主页");
                    return;
                }
                if (StringUtils.isEmpty(mPhotoPathArr[1])) {
                    UIUtils.showToast("请拍摄行驶证副页");
                    return;
                }

                authenticByPic();
                break;

        }
    }

    /**
     * 车辆验证
     */
    private void authenticByPic() {
        if (!validateUserIdAndToken()) return;
        String userId = SPUtils.getUserId(this);
        String plate = getIntent().getStringExtra(KEY_PLATE);
        RequestBody front = RequestBody.create(MediaType.parse("image/*"), new File(mPhotoPathArr[0]));
        RequestBody behind = RequestBody.create(MediaType.parse("image/*"), new File(mPhotoPathArr[1]));
         HttpHelper.getRetrofit().create(PlateNumAPI.class).authentic(userId, plate, front, behind)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void aVoid) {
                CheckingActivity.start(AuthenticationCarActivity.this);
                finish();
            }
        });
    }

    private static final int CODE_TWO = 790;
    private static final int CODE_ONE = 425;
    private static final int CODE_CLIP_ONE = 477;
    private static final int CODE_CLIP_TWO = 85;
    private static final int CODE_BY_HAND = 67;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_ONE:
                    break;
                case CODE_CLIP_ONE:
                    String path1 = data.getStringExtra(ClipLicenseActivity.KEY_OUTPUT_PATH);
                    LogUtils.i("one:" + path1);
                    Glide.with(this).load(path1).error(R.mipmap.driving_license1).into(mIvOne);
                    mPhotoPathArr[0] = path1;
                    break;
                case CODE_TWO:
                    break;
                case CODE_CLIP_TWO:
                    String path2 = data.getStringExtra(ClipLicenseActivity.KEY_OUTPUT_PATH);
                    LogUtils.i("two:" + path2);
                    Glide.with(this).load(path2).error(R.mipmap.driving_license2).into(mIvTwo);
                    mPhotoPathArr[1] = path2;
                    break;
                case CODE_BY_HAND:
                    CheckingActivity.start(this);
                    finish();
                    break;
            }
        }

    }

}
