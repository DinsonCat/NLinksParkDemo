package com.nlinks.parkdemo.widget.marker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.nlinks.parkdemo.R;

/**
 * 圆形控件，用于地图的中心点展示
 * Created by Dell on 2017/05/10.
 */
public class CircleView extends View {

	private int mColor;
	private Paint mPaint;
	private float mRadius = 0;

	public CircleView(Context context) {
		this(context, null, 0);
	}

	public CircleView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		mColor = ContextCompat.getColor(context, R.color.map_accuracy_circle_fill);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mRadius != 0) {
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mRadius == 0) super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		else {
			int measureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * 2 + 0.99f), MeasureSpec.EXACTLY);
			super.onMeasure(measureSpec, measureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(true, left, top, right, bottom);
	}

	public void setRadius(float radius) {
		mRadius = radius;
	}

	public void update() {
		invalidate();
	}

	public void updateRadius(float radius) {
		if (mRadius != radius) {
			setRadius(radius);
			update();
		}
	}
}
