package com.nlinks.parkdemo.widget.marker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

/**
 * 2017年4月19日 djc 修改，优化
 *
 * @author lzhixing@linewell.com
 *         Created at 2015/12/17 14:20
 */
public class ParkMarker extends FrameLayout {

    protected ImageView iv_marker;//标注点
    protected TextView tv_space_num;//剩余车位
    protected ParkMain mParkBean;
    protected int mColorPink, mColorCyan, mColorYellow/*, mColorBlack*/;
    private Marker mMarker = null;
    private int mNum = 0, mIsAppoint, mAppointCount;

    public ParkMarker(Context context, ParkMain parkList) {
        super(context);
        mColorPink = ContextCompat.getColor(context, R.color.map_mark_pink);
        mColorCyan = ContextCompat.getColor(context, R.color.map_mark_cyan);
        mColorYellow = ContextCompat.getColor(context, R.color.map_mark_yellow);
        //mColorBlack = ContextCompat.getColor(context, R.color.black);
        init(context);

        this.mParkBean = parkList;
    }

    protected void init(Context context) {
        LayoutParams ivLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivLp.gravity = Gravity.CENTER;
        //图标
        iv_marker = new ImageView(context);
        iv_marker.setScaleType(ImageView.ScaleType.CENTER);
        addView(iv_marker, ivLp);
        //文本
        LayoutParams tvLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
        tvLp.gravity = Gravity.CENTER;
        tvLp.setMargins(0, 0, 0, 14);
        tv_space_num = new TextView(context);
        tv_space_num.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        addView(tv_space_num, tvLp);
    }

    /**
     * 强制显示可预约
     */
    public void forceAppointment(boolean appointment) {
        mAppointment = appointment;
    }

    boolean mAppointment = false;

    /**
     * 数据是否改变
     */
    public boolean diff(int num, int isAppoint, int appointCount) {
        return num != mNum || isAppoint != mIsAppoint || appointCount != mAppointCount;
    }


    public void updatePark(ParkMain parkList) {
        if (parkList != null)
            mParkBean = parkList;
    }


    @SuppressLint("SetTextI18n")
    public BitmapDescriptor createDescriptor() {
        boolean appointment = false;
        if (mParkBean != null) {
            mNum = mAppointment ? mParkBean.getAppointCount() : mParkBean.getUnuedStallNum();
            appointment = mParkBean.getIsAppoint() != 0;
        }
        if (!StringUtils.isEmpty(mParkBean.getParkicon())) {
            tv_space_num.setText(" ");
            Glide.with(getContext()).load(mParkBean.getParkicon()).placeholder(R.mipmap.map_other_marker)
                .error(R.mipmap.map_other_marker)
                .dontAnimate()
                .into(new GlideDrawableImageViewTarget(iv_marker) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        final BitmapDescriptor view = BitmapDescriptorFactory.fromView(ParkMarker.this);
                        if (mMarker == null) {
                            UIUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mMarker==null)return;
                                    mMarker.setIcon(view);
                                }
                            },500);
                        } else {
                            mMarker.setIcon(view);
                        }
                    }
                });
        }else if(mParkBean.getParklevel()==1){
            //蓝蜻蜓显示默认图片，不显示空车位数
            tv_space_num.setText(" ");
            iv_marker.setImageResource(R.mipmap.map_other_marker);
        }
        else if (mNum <= 0) {
            tv_space_num.setText("0");
            tv_space_num.setTextColor(mColorPink);
            iv_marker.setImageResource(appointment ? R.mipmap.appointment_map_marker_red
                : R.mipmap.index_map_marker_red);
        } else if (mNum > 0 && mNum <= 10) {
            tv_space_num.setText("" + mNum);
            tv_space_num.setTextColor(mColorPink);
            iv_marker.setImageResource(appointment ? R.mipmap.appointment_map_marker_red
                : R.mipmap.index_map_marker_red);
        } else if (mNum > 10 && mNum <= 20) {
            tv_space_num.setText("" + mNum);
            tv_space_num.setTextColor(mColorYellow);
            iv_marker.setImageResource(appointment ? R.mipmap.appointment_map_marker_yellow
                : R.mipmap.index_map_marker_yellow);
        } else if (mNum > 20 && mNum <= 99) {
            tv_space_num.setText("" + mNum);
            tv_space_num.setTextColor(mColorCyan);
            iv_marker.setImageResource(appointment ? R.mipmap.appointment_map_marker_green
                : R.mipmap.index_map_marker_green);
        } else {
            tv_space_num.setText("99+");
            tv_space_num.setTextColor(mColorCyan);
            iv_marker.setImageResource(appointment ? R.mipmap.appointment_map_marker_green
                : R.mipmap.index_map_marker_green);
        }
        return BitmapDescriptorFactory.fromView(this);
    }

    /**
     * 关联marker
     *
     * @param marker
     * @return
     */
    public ParkMarker relevantTo(Marker marker) {
        mMarker = marker;
        return this;
    }

    public void updateMarker(boolean top) {
        if (mMarker != null) {
            mMarker.setIcon(createDescriptor());
            if (top)
                mMarker.setToTop();
        }
    }

    public void remove() {
        if (mMarker != null) {
            mMarker.remove();
        }
        mMarker = null;
        mParkBean = null;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public ParkMain getPark() {
        return mParkBean;
    }

}
