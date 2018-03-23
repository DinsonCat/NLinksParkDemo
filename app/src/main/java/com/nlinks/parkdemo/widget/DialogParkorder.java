package com.nlinks.parkdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;
import com.nlinks.parkdemo.entity.park.ParkRecord;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.module.usercenter.parkrecord.ParkRecordDetailsForNoPayAty;
import com.nlinks.parkdemo.module.usercenter.parkrecord.ParkRecordDetailsForSucAndLeaveAty;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.NlinksParkUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;


/**
 * 提示框
 */
public class DialogParkorder extends Dialog implements View.OnClickListener {

    private OnNaviClickListener operationListener;
    private Context mContext;
    private CountDownTimer mTimer;

    public DialogParkorder(Context context) {
        super(context, R.style.custom_dialog);
        init(context);
    }

    public DialogParkorder(Context context, View.OnClickListener listener) {
        super(context, R.style.custom_dialog);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setContentView(R.layout.dialog_parkorder);
        findViewById(R.id.iv_cancel).setOnClickListener(this);
    }

    public void setOnNaviClickListener(OnNaviClickListener operationListener) {
        this.operationListener = operationListener;
    }


    public void setMessage(CharSequence message) {
        ((TextView) findViewById(R.id.tv_msg)).setText(message);
    }

    public void setTitle(CharSequence message) {
        ((TextView) findViewById(R.id.tv_title)).setText(message);
    }

    /**
     * 预约记录
     * 供外部调用，设置数据
     */
    public void setData(final AppointmentRecord appointRecord) {
        setMsg(R.id.tv_plate, appointRecord.getPlateNum());
        setMsg(R.id.tv_park_name, appointRecord.getName());
        setMsg(R.id.tv_park_address, appointRecord.getAddress());

        TextView tvLeaveDesc = (TextView) findViewById(R.id.tv_leave_desc);
        tvLeaveDesc.setText("离场倒计时");
        Drawable drawable = UIUtils.getDrawable(R.mipmap.ic_sand_clock);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeaveDesc.setCompoundDrawables(drawable, null, null, null);

        int end = DateUtils.str2int(appointRecord.getLeaveTime(), AppConst.TIMEFORMAT);
        int start = DateUtils.getCurrentTimeMillis10();
        int distance = end - start;
        if (distance < 0) distance = 0;

        mTimer = new CountDownTimer(distance, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                LogUtils.e(millisUntilFinished + "   >" + DateUtils.long2Str(millisUntilFinished, "mm:ss"));
                String time = DateUtils.long2Str(millisUntilFinished, "mm:ss");
                setMsg(R.id.tv_leave_time, time);
            }

            @Override
            public void onFinish() {
                setMsg(R.id.tv_leave_time, " 00:00");
                Button btnAction = (Button) findViewById(R.id.btn_action);
                btnAction.setText("已超时");
                btnAction.setEnabled(false);
            }
        };

        mTimer.start();

        setMsg(R.id.tv_money_desc, "预约停车费");
        setMsg(R.id.tv_money, "￥" + StringUtils.formatMoney(appointRecord.getCharge()));


        findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final View tvNavi = findViewById(R.id.iv_navi);
        tvNavi.setVisibility(View.VISIBLE);
        tvNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operationListener != null) {
                    cancel();
                    operationListener.onNaviClick(appointRecord, tvNavi);
                }
            }
        });
    }


    /**
     * 停车记录
     * 供外部调用，设置数据
     */
    public void setData(final ParkRecord parkRecord) {
        setMsg(R.id.tv_plate, parkRecord.getPlateNum());
        setMsg(R.id.tv_park_name, parkRecord.getParkName());
        setMsg(R.id.tv_park_address, parkRecord.getAddress());

        setMsg(R.id.tv_leave_desc, "停车时长");
        setMsg(R.id.tv_leave_time, DateUtils.formatSeconds(parkRecord.getParkSeconds()));

        TextView tvMoney = (TextView) findViewById(R.id.tv_money);
        tvMoney.setText("￥" + StringUtils.formatMoney(parkRecord.getConsume()));
        tvMoney.setTextColor(UIUtils.getColor(R.color.money_orange));

        findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();

                //传递数据
                switch (parkRecord.getRecordStatus()) {
                    case 1:
                    case 2:
                        //待缴费,预缴
                    case 4://欠费
                        ParkRecordDetailsForNoPayAty.start(getContext(), parkRecord);
                        break;
                    case 3://已完成
                    case 5://等待离场
                        ParkRecordDetailsForSucAndLeaveAty.start(getContext(), parkRecord);
                        break;
                }
                //ParkRecordDeatilActivity.start(mContext, parkRecord);
            }
        });

        setMsg(R.id.tv_money_desc, "停车费用");

        ImageView iv_park_state = (ImageView) findViewById(R.id.iv_park_state);
        iv_park_state.setVisibility(View.VISIBLE);
        iv_park_state.setImageResource(NlinksParkUtils.formatParkRecordState(parkRecord).getImgRes());
    }


    private void setMsg(int id, String msg) {
        ((TextView) findViewById(id)).setText(msg);
    }


    public interface OnNaviClickListener {
        void onNaviClick(AppointmentRecord appointRecord, View view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                this.cancel();
                break;
        }
    }

    @Override
    public void cancel() {
        LogUtils.i("cancel() called");
        super.cancel();
    }

    @Override
    public void dismiss() {
        LogUtils.i("dismiss() called");
        if (mTimer != null) mTimer.cancel();
        super.dismiss();
    }


    @Override
    public void show() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (attributes != null) {
                int intrinsicHeight = ContextCompat.getDrawable(getContext(), R.mipmap.update_close).getIntrinsicHeight();
                attributes.y = attributes.y - intrinsicHeight / 2;
                window.setAttributes(attributes);
            }
        }
        super.show();
    }

}