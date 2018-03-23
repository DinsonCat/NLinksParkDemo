package com.nlinks.parkdemo.module.usercenter.managecar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.entity._req.AutoPay;
import com.nlinks.parkdemo.entity._req.ChangeBrands;
import com.nlinks.parkdemo.entity.plate.PlateInfo;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.WebViewPromotionActivity;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.CommonTitleBar;
import com.nlinks.parkdemo.widget.DialogCancelConfirm;
import com.nlinks.parkdemo.widget.SwitchButton;

import java.util.ArrayList;

import static com.nlinks.parkdemo.module.usercenter.managecar.BrandOfCarActivity.EXTRA_BRAND;

public class ManageCarActivity extends BaseActivity implements View.OnClickListener {

    public static final String BOOLEAN_SELECTABLE = "BOOLEAN_SELECTABLE";
    public static final String STRING_SELECTED = "STRING_SELECTED";

    public static final int REQUEST_BRANDS = 0x1000;

    private LinearLayout ll_content;
    private ArrayList<PlateInfo> mDatas = new ArrayList<>();
    private ArrayList<ImageView> mLeftBtn = new ArrayList<>();
    private Button btn_add_car;
    private TextView tv_title_right;
    private boolean mSelectable = false;
    private DialogCancelConfirm mCustomDialog;//弹出式对话框
    private TextView tv_msg;
    private ImageView iv_bg;
    private PlateNumAPI mPlateNumAPI;

    public static void start(Context act) {
        Intent starter = new Intent(act, ManageCarActivity.class);
        act.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car);

        initUI();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSelectable = bundle.getBoolean(BOOLEAN_SELECTABLE);
            if (mSelectable) {
                CommonTitleBar titleBar = (CommonTitleBar) findViewById(R.id.title_temp);
                titleBar.setRightBtnVisible(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPlateFromServer();
    }

    private void initUI() {
        ll_content = findViewById(R.id.ll_content);
        btn_add_car =   findViewById(R.id.btn_add_car);
        tv_title_right =  findViewById(R.id.tv_title_right);
        tv_msg =  findViewById(R.id.tv_msg);
        iv_bg =   findViewById(R.id.iv_bg);
        tv_title_right.setOnClickListener(this);

        mPlateNumAPI = HttpHelper.getRetrofit().create(PlateNumAPI.class);
    }


    /**
     * 点击添加车辆
     */
    public void onClickAddCar(View v) {
        jumpTo(AddCarPlateActivity.class);

        // AddCarPlateActivity.startForResult(this,false,CODE_ADD);

    }


    public void getPlateFromServer() {
        String userId = SPUtils.getUserId(this, "");
        if (StringUtils.isEmpty(userId)) return;
        HttpHelper.getRetrofit().create(PlateNumAPI.class).getPlateList(userId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<PlateInfo>>() {
            @NonNull
            @Override
            public void onHandleSuccess(ArrayList<PlateInfo> plateInfos) {
                String plates = "";
                for (PlateInfo plateInfo : plateInfos) {
                    plates += plateInfo.getCarNo() + ",";
                }
                //存sp
                SPUtils.putUserPlate(ManageCarActivity.this, plates);

                mDatas = plateInfos;
                ll_content.removeAllViews();
                for (int i = 0; i < plateInfos.size(); i++) {
                    createItem(plateInfos.get(i), i);
                }

                tv_title_right.setVisibility(plateInfos.size() == 0 ? View.INVISIBLE : View.VISIBLE);
                resetButton();
            }
        });
    }


    /**
     * 创建车牌条目
     *
     * @param plateInfo
     */
    private void createItem(final PlateInfo plateInfo, int i) {
        FrameLayout view = (FrameLayout) getLayoutInflater().inflate(R.layout.item_plate_num_delete, ll_content, false);
        TextView tv_plate =  view.findViewById(R.id.tv_plate);
        ImageView iv_delete =  view.findViewById(R.id.iv_delete);
        TextView tv_brand =  view.findViewById(R.id.tv_brand);
        TextView tv_state =  view.findViewById(R.id.tv_state);
        view.findViewById(R.id.icAuthentication).setVisibility(plateInfo.getStatus() == 1 ? View.VISIBLE : View.INVISIBLE);

        final SwitchButton sb_ios =   view.findViewById(R.id.sb_ios);

        sb_ios.setCheckedImmediately(plateInfo.getIsAutoPay() != 0);
        sb_ios.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                }
                return false;
            }
        });
        sb_ios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomDialog == null)
                    mCustomDialog = new DialogCancelConfirm(ManageCarActivity.this);
                final boolean checked = sb_ios.isChecked();
                sb_ios.setCheckedImmediately(!checked);

                if (SPUtils.isMember(ManageCarActivity.this)) {
                    //蜻蜓会员，弹出是否同意自动支付功能对话框
                    mCustomDialog.setButtonsText("取消", "确定");
                    mCustomDialog.setMessage(checked ? "您确定要开通自动支付吗？" : "关闭自动支付\n车牌离场将不能使用钱包自动支付");
                    mCustomDialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
                        @Override
                        public void onLeftClick() {
                            mCustomDialog.dismiss();
                        }

                        @Override
                        public void onRightClick() {
                            mCustomDialog.dismiss();
                            AutoPay autoPay = new AutoPay(plateInfo.getId(), checked ? 1 : 0);
                            LogUtils.i(autoPay.toString());
                            postAutoPay2Server(autoPay, sb_ios, checked);
                        }
                    });
                } else {
                    //普通会员，弹出升级成会员的对话框
                    mCustomDialog.setButtonsText("取消", "成为金卡会员");
                    mCustomDialog.setMessage("您还不是金卡会员\n无法开启自动支付哦");
                    mCustomDialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
                        @Override
                        public void onLeftClick() {
                            mCustomDialog.dismiss();
                        }

                        @Override
                        public void onRightClick() {
                            WebViewPromotionActivity.start(ManageCarActivity.this, AppConst.URL_MEMBER);
                            mCustomDialog.dismiss();
                        }
                    });
                }
                mCustomDialog.show();
            }

        });

        iv_delete.setId(i);
        iv_delete.setOnClickListener(mDeleteListener);
        tv_plate.setText(NlinksParkUtils.plateAddDot(plateInfo.getCarNo()));
        String brand = plateInfo.getBrand();
        tv_brand.setText(StringUtils.isEmpty(brand) ? "请选择您的车辆品牌 >" : brand);
        if (StringUtils.isEmpty(brand)) {
            tv_brand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCurrentChangePlateId = plateInfo.getId();
                    BrandOfCarActivity.start(ManageCarActivity.this, REQUEST_BRANDS);
                }
            });
        }
        tv_state.setText(NlinksParkUtils.formatPlateState(plateInfo.getStatus()));

        //处理车牌的状态（设置点击监听，显示状态）
        dealPlateState(plateInfo, tv_state);

        ll_content.addView(view);
        mLeftBtn.add(iv_delete);
        view.setOnClickListener(this);
        view.setTag(plateInfo);
    }

    /**
     * 处理车牌的状态（设置点击监听，显示状态）
     *
     * @param plateInfo
     * @param tv_state
     */
    private void dealPlateState(final PlateInfo plateInfo, TextView tv_state) {
        if (plateInfo.getStatus() == 0 || plateInfo.getStatus() == -1 || plateInfo.getStatus() == 2) {
            boolean isError = false;
            switch (plateInfo.getStatus()) {
                //认证失败
                case -1:
                    isError = true;
                    //未认证
                case 0:
                    final boolean finalIsError = isError;
                    tv_state.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AuthenticationCarActivity.start(ManageCarActivity.this, plateInfo.getCarNo(), finalIsError);
                        }
                    });

                    break;
                //审核中
                case 2:
                    tv_state.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckingActivity.start(ManageCarActivity.this);
                        }
                    });
                    break;
            }
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.arrow_right);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_state.setCompoundDrawables(null, null, drawable, null);
        }
    }


    /**
     * 修改是否自动支付上传到服务器
     *
     * @param autoPay
     * @param sb_ios
     * @param checked
     */
    private void postAutoPay2Server(AutoPay autoPay, final SwitchButton sb_ios, final boolean checked) {
        mPlateNumAPI.setAutoPay(autoPay)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<String>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(String s) {
                sb_ios.setChecked(checked);
            }

            @Override
            public void onHandleError(int code, String message) {
                super.onHandleError(code, message);
                sb_ios.setChecked(!checked);
            }
        });
    }

    private View.OnClickListener mDeleteListener = v -> {
        final DialogCancelConfirm customDialog = new DialogCancelConfirm(ManageCarActivity.this);
        customDialog.setMessage("您确定要删除吗？");
        customDialog.setButtonsText("取消", "确定");
        customDialog.setOperationListener(new DialogCancelConfirm.OnOperationListener() {
            @Override
            public void onLeftClick() {
                customDialog.dismiss();
            }

            @Override
            public void onRightClick() {
                customDialog.dismiss();
                //向服务器请求删除
                postDelete(v);
            }

        });
        customDialog.show();
    };

    public void resetLayout(View v) {
        ll_content.getChildAt(v.getId()).setVisibility(View.GONE);
        resetButton();
    }

    public void resetButton() {
        int count = 0;
        for (int i = 0; i < ll_content.getChildCount(); i++) {
            if (ll_content.getChildAt(i).getVisibility() == View.VISIBLE) {
                count++;
            }
        }

        btn_add_car.setVisibility(count < 3 ? View.VISIBLE : View.GONE);

        iv_bg.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
        tv_msg.setVisibility(count == 0 ? View.GONE : View.VISIBLE);

        if (count == 0) {
            mIsShowDelete = !mIsShowDelete;
            showDelete(mIsShowDelete);
            tv_title_right.setText(mIsShowDelete ? "完成" : "编辑");
            tv_title_right.setVisibility(View.INVISIBLE);
        }
    }


    private boolean mIsShowDelete = false;//是否显示左侧删除

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                mIsShowDelete = !mIsShowDelete;
                showDelete(mIsShowDelete);
                tv_title_right.setText(mIsShowDelete ? "完成" : "编辑");
                break;
            case R.id.ll_swipe_delete:
                Object tag = v.getTag();
                if (mSelectable && tag instanceof PlateInfo) {
                    String carNo = ((PlateInfo) tag).getCarNo();
                    if (!TextUtils.isEmpty(carNo)) {
                        Intent intent = new Intent();
                        intent.putExtra(STRING_SELECTED, carNo);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
                break;
            case R.id.sb_ios:

                break;
        }
    }

    /**
     * 显示右上角删除键
     */
    private void showDelete(boolean show) {
        for (ImageView imageView : mLeftBtn) {
            imageView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    private void postDelete(final View v) {
        String plateId = mDatas.get(v.getId()).getId();
        mPlateNumAPI.deletePlate(plateId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void o) {
                resetLayout(v);
            }
        });
    }

    private String mCurrentChangePlateId = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_BRANDS:
                    ChangeBrands changeBrands = new ChangeBrands();
                    changeBrands.setId(mCurrentChangePlateId);
                    changeBrands.setCarBrands(data.getStringExtra(EXTRA_BRAND));
                    mPlateNumAPI.changeBrands(changeBrands)
                        .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
                        @NonNull
                        @Override
                        public void onHandleSuccess(Void aVoid) {
                            getPlateFromServer();
                        }
                    });
                    break;
            }
        }


    }


}
