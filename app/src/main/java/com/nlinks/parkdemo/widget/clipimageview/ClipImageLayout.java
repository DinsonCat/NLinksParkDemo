package com.nlinks.parkdemo.widget.clipimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.nlinks.parkdemo.utils.ImageFactory;


public class ClipImageLayout extends RelativeLayout {

    private ClipZoomImageView mZoomImageView;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        ClipImageBorderView clipImageView = new ClipImageBorderView(context);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        ClipImageLayoutSettings mSettings = new ClipImageLayoutSettings(context, attrs);

        ImageFactory imageFactory = new ImageFactory();
        Bitmap compressImageFromId = imageFactory.compressImageFromId(context, mSettings.getResourceId());
        mZoomImageView.setImageBitmap(compressImageFromId);


        this.addView(mZoomImageView, lp);
        this.addView(clipImageView, lp);

        int  horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSettings.getHorizontalPadding(), getResources().getDisplayMetrics());


        //int horizontalPadding = UIUtils.dip2px(mSettings.getHorizontalPadding());// 计算padding的px
        mZoomImageView.setHorizontalPadding(horizontalPadding);//设置水平padding
        clipImageView.setHorizontalPadding(horizontalPadding);//设置水平padding
        clipImageView.setAspectRatio(mSettings.getAspectRatio());//设置宽高比
        mZoomImageView.setAspectRatio(mSettings.getAspectRatio());//设置水平padding
    }


    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }

    public void setDrawable(String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        mZoomImageView.setImageBitmap(bitmap);


    }

}
