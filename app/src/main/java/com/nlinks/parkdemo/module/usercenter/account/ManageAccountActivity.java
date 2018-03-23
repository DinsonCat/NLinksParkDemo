package com.nlinks.parkdemo.module.usercenter.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity._req.UpdateNickName;
import com.nlinks.parkdemo.entity._req.UpdateSex;
import com.nlinks.parkdemo.entity.user.UserInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.EditTextClear;
import com.nlinks.parkdemo.widget.GlideCircleTransform;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 账号管理
 */
public class ManageAccountActivity extends BaseActivity implements View.OnClickListener {
    public static final String IMG_BITMAP = "img_bitmap";
    public static final String DATA_BEAN = "data_bean";
    private TextView tv_username;
    private UserInfo mUserInfo;
    private TextView tv_sex;
    private ImageView mIv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        initUI();
    }

    private void initUI() {
        //设置图片
        Bitmap bitmap = getIntent().getParcelableExtra(IMG_BITMAP);
        mIv_img =  findViewById(R.id.iv_img);
        mIv_img.setImageBitmap(bitmap);
        mIv_img.setOnClickListener(this);

        //设置文字
        mUserInfo = getIntent().getParcelableExtra(DATA_BEAN);
        tv_username =  findViewById(R.id.tv_username);
        tv_username.setText(StringUtils.notEmpty(mUserInfo.getDisplayName()) ? mUserInfo.getDisplayName() : mUserInfo.getPhoneNo());
        tv_sex =  findViewById(R.id.tv_sex);
        tv_sex.setText(NlinksParkUtils.formatSex(mUserInfo.getSex()));
        TextView tv_phone =  findViewById(R.id.tv_phone);
        tv_phone.setText(mUserInfo.getPhoneNo());


        findViewById(R.id.ll_nickname).setOnClickListener(this);
        findViewById(R.id.ll_sex).setOnClickListener(this);
    }

    @Override
    public void titleLeftBtnClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nickname:
                changeNickName();
                break;
            case R.id.ll_sex:
                changeSex();
                break;
            case R.id.iv_img:
                break;
        }
    }


    /**
     * 弹出修改昵称对话框
     */
    private void changeNickName() {
        final EditTextClear editTextClear = new EditTextClear(this);
        editTextClear.setPadding(48, 48, 48, 0);
        editTextClear.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        editTextClear.setBackground(UIUtils.getDrawable(R.drawable.item_white_divider_normal));

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改昵称").setView(editTextClear)
            .setPositiveButton("确定", null);
        final AlertDialog alertDialog = builder.create();

        /** 3.自动弹出软键盘 **/
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextClear, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        TextView tv = new TextView(this);
        tv.setText("修改昵称");
        tv.setTextColor(UIUtils.getColor(R.color.text_primary));
        tv.setTextSize(18);
        tv.setPadding(48, 56, 48, 0);
        alertDialog.setCustomTitle(tv);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = editTextClear.getText().toString().trim().replaceAll(" ", "");
                if (StringUtils.isEmpty(nick)) {
                    UIUtils.showToast("昵称不能为空");
                    return;
                }
                postNickName(alertDialog, nick);
            }
        });
    }

    /**
     * 请求发送修改密码
     *
     * @param alertDialog
     * @param nick
     */
    private void postNickName(final AlertDialog alertDialog, final String nick) {
        validateUserIdAndToken();
        HttpHelper.getRetrofit().create(UserApi.class).updateUserName(new UpdateNickName(SPUtils.getUserId(this, ""), nick))
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void o) {
                alertDialog.dismiss();
                tv_username.setText(nick);
            }
        });
    }

    /**
     * 弹出修改性别对话框
     */
    private void changeSex() {
        final String items[] = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int current = formatSex(mUserInfo.getSex());

        builder.setSingleChoiceItems(items, current, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (current != which) {
                    postSex(which + 1);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 请求发送修改性别
     *
     * @param
     */
    private void postSex(final int sex) {
        HttpHelper.getRetrofit().create(UserApi.class).updateSex(new UpdateSex(SPUtils.getUserId(this, ""), sex))
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>() {
            @NonNull
            @Override
            public void onHandleSuccess(Void o) {
                mUserInfo.setSex(sex);
                tv_sex.setText(NlinksParkUtils.formatSex(sex));
            }
        });
    }

    private int formatSex(int state) {
        return state - 1;
    }

    private static final int CODE_IMG = 974;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CODE_IMG) {
        }
    }

    /**
     * 上传图片
     *
     * @param filePath
     */
    private void uploadPhoto(String filePath) {
        final File photoFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png*"), photoFile);
        //调用上传文件接口
        HttpHelper.getRetrofit().create(UserApi.class).uploadPhoto(mUserInfo.getPhoneNo(), requestBody)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void httpResult) {
                Glide.with(UIUtils.getContext()).load(photoFile)
                    .transform(new GlideCircleTransform(ManageAccountActivity.this))
                    .dontAnimate().into(mIv_img);
            }
        });
    }
}
