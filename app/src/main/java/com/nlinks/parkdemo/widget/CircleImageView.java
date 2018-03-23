package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.nlinks.parkdemo.R;

/**
 * @author Dinson - 2017/8/18
 */
public class CircleImageView extends AppCompatImageView {

    private static final Xfermode MASK_XFERMODE;
    private Bitmap mask;
    private Paint paint;
    private int mBorderWidth = 10;
    private int mBorderColor = Color.parseColor("#f2f2f2");

    static {
        PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
        MASK_XFERMODE = new PorterDuffXfermode(localMode);
    }

    public CircleImageView(Context paramContext) {
        super(paramContext);
    }

    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        TypedArray a = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CircleImageView);
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, mBorderColor);
        final int defalut = (int) (2 * paramContext.getResources().getDisplayMetrics().density + 0.5f);
        mBorderWidth = a.getDimensionPixelOffset(R.styleable.CircleImageView_border_width, defalut);
        a.recycle();
    }

    private boolean useDefaultStyle = true;

    public void setUseDefaultStyle(boolean useDefaultStyle) {
        this.useDefaultStyle = useDefaultStyle;
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        if (useDefaultStyle) {
            super.onDraw(paramCanvas);
            return;
        }
        final Drawable localDrawable = getDrawable();
        if (localDrawable == null)
            return;
        if (localDrawable instanceof NinePatchDrawable) {
            return;
        }
        if (this.paint == null) {
            final Paint localPaint = new Paint();
            localPaint.setFilterBitmap(false);
            localPaint.setAntiAlias(true);
            localPaint.setXfermode(MASK_XFERMODE);
            this.paint = localPaint;
        }
        final int width = getWidth();
        final int height = getHeight();

        int layer = paramCanvas.saveLayer(0.0F, 0.0F, width, height, null, Canvas.MATRIX_SAVE_FLAG);

        localDrawable.setBounds(0, 0, width, height);

        localDrawable.draw(paramCanvas);
        if ((this.mask == null) || (this.mask.isRecycled())) {
            this.mask = createOvalBitmap(width, height);
        }

        paramCanvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);

        paramCanvas.restoreToCount(layer);
        drawBorder(paramCanvas, width, height);
    }


    private void drawBorder(Canvas canvas, final int width, final int height) {
        if (mBorderWidth == 0) {
            return;
        }
        final Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);
        canvas = null;
    }


    public Bitmap createOvalBitmap(final int width, final int height) {
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint();
        final int padding = (mBorderWidth - 3) > 0 ? mBorderWidth - 3 : 1;

        RectF localRectF = new RectF(padding, padding, width - padding, height - padding);
        localCanvas.drawOval(localRectF, localPaint);
        return localBitmap;
    }
}