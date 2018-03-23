package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.StringUtils;


/**
 * 通用标题栏
 */
public class CommonTitleBar extends RelativeLayout {

    private TextView btnRight, tvTitle, btnLeft;
    private CommonTitleBarSettings mSettings;

    public CommonTitleBar(Context context) {
        super(context);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 获取右边按钮
     */
    public TextView getBtnRight() {
        return btnRight;
    }

    /**
     * 获取右边按钮
     */
    public TextView getBtnLeft() {
        return btnLeft;
    }

    /**
     * 获取标题控件
     */
    public TextView getTitleTv() {
        return tvTitle;
    }

    /**
     * 设置标题背景颜色
     *
     * @param color 颜色值（#ff0000）
     */
    public void setTitleBgColor(String color) {
        setBackgroundColor(Color.parseColor(color));
    }

    /**
     * 设置标题
     */
    public void setTitleText(String title) {
        tvTitle.setText(title);
    }

    /**
     * 设置标题
     */
    public void setTitleTextColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 设置右边按钮的文本
     */
    public void setRightBtnText(String msg) {
        btnRight.setText(msg);
    }

    /**
     * 设置右边按钮的图片
     */
    public void setRightBtnSrc(int resouceId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resouceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btnRight.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 设置左边按钮显示隐藏
     *
     * @param visible true：显示   false：隐藏
     */
    public void setLeftBtnVisible(boolean visible) {
        btnLeft.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * 设置右边按钮显示隐藏
     *
     * @param visible true：显示   false：隐藏
     */
    public void setRightBtnVisible(boolean visible) {
        btnRight.setVisibility(visible ? VISIBLE : GONE);
    }


    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {

        mSettings = new CommonTitleBarSettings(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);

        View view = LayoutInflater.from(context).inflate(R.layout.common_title_bar, this, true);
        btnLeft = view.findViewById(R.id.ib_title_left);
        btnRight = view.findViewById(R.id.tv_title_right);
        tvTitle = view.findViewById(R.id.tvTitle);

        //设置标题文字
        tvTitle.setText(mSettings.getTitleText());
        //设置标题颜色
        tvTitle.setTextColor(mSettings.getTitleTextColor());

        //左右按钮默认关闭
        btnLeft.setVisibility(mSettings.isLeftBtnVisible() ? VISIBLE : GONE);
        btnRight.setVisibility(mSettings.isRightBtnVisible() ? VISIBLE : GONE);

        //设置左边按钮的文字(有文字就不显示图片)
        if (StringUtils.isEmpty(mSettings.getLeftText())) {
            Drawable dLeft = ContextCompat.getDrawable(context, mSettings.getLeftBtnDrawable());
            dLeft.setBounds(0, 0, dLeft.getMinimumWidth(), dLeft.getMinimumHeight());
            btnLeft.setCompoundDrawables(dLeft, null, null, null);
        } else {
            btnLeft.setText(mSettings.getLeftText());
        }
        btnLeft.setTextColor(mSettings.getLeftBtnTextColor());

        //设置右边按钮的文字(文字图片二选一)
        if (mSettings.getRightBtnDrawable() > 0) {
            Drawable dRight = ContextCompat.getDrawable(context, mSettings.getRightBtnDrawable());
            dRight.setBounds(0, 0, dRight.getMinimumWidth(), dRight.getMinimumHeight());
            btnRight.setCompoundDrawables(dRight, null, null, null);
        } else {
            btnRight.setText(mSettings.getRightText());
        }
        btnRight.setTextColor(mSettings.getRightBtnTextColor());
    }

    /////////////////////////////以下解决标题栏问题//////////////////////////////////////////
    /*
     * 动态计算顶部padding
     */
    /*static void transparentPadding(Context context, View view, boolean ancestorFitsSystemWindow) {
        if (!ancestorFitsSystemWindow) {
            Resources resources = context.getResources();
            int barSize = getStatusBarSize(resources);
            if (barSize > 0) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height += barSize;
                    view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + barSize,
                        view.getPaddingRight(), view.getPaddingBottom());
                }
            }
        }
    }*/

    /*
     * 获取statusBar大小
     */
    /*static int getStatusBarSize(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }*/
}
