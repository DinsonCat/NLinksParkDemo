package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;


public class MZbug extends View     {

    /**
     * 是否允许点击Indicator切换ViewPager
     */
    private boolean mIsEnableClickSwitch = false;

    public MZbug(Context context) {
        super(context);

    }

    public MZbug(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public MZbug(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MZbug(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {



        setMeasuredDimension(2000, 2);

    }




}
