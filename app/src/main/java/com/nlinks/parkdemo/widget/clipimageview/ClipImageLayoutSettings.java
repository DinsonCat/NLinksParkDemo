package com.nlinks.parkdemo.widget.clipimageview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.nlinks.parkdemo.R;


public class ClipImageLayoutSettings {

    private final float mHorizontalPadding;
    private final float mAspectRatio;
    private final int mResourceId;


    ClipImageLayoutSettings(Context context, AttributeSet attrs) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ClipImageLayout, 0, 0);
        mHorizontalPadding = styledAttributes.getDimension(R.styleable.ClipImageLayout_horizontalPadding,dpToPx(context,20));
        mAspectRatio = styledAttributes.getFloat(R.styleable.ClipImageLayout_drawableAspectRatio, 1f);
        mResourceId = styledAttributes.getResourceId(R.styleable.ClipImageLayout_drawable, R.mipmap.ic_launcher);
        styledAttributes.recycle();
    }
    private static float dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
    public float getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }

    public int getResourceId() {
        return mResourceId;
    }
}
