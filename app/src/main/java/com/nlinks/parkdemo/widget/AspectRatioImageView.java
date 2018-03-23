package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.LogUtils;


/**
 * 自带宽高比属性的view
 */
public class AspectRatioImageView extends AppCompatImageView {

    private float mAspectRatio;

    public AspectRatioImageView(Context context) {
        this(context, null);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView, 0, 0);
        mAspectRatio = ta.getFloat(R.styleable.AspectRatioImageView_aspectRatio, 1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = (int) (width / mAspectRatio);
        LogUtils.i("height:" + height + " width:" + width + " ratio:" + mAspectRatio);
        // setMeasuredDimension(size, height);

        int heightMeasure = MeasureSpec.makeMeasureSpec(height, heightMode);
        super.onMeasure(widthMeasureSpec, heightMeasure);
    }
}
