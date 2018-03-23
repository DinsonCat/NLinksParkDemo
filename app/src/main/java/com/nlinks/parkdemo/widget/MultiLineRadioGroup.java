package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MultiLineRadioGroup extends ViewGroup implements OnClickListener {
    private Context mContext;
    private int childMarginHorizontal = 0, childMarginVertical = 0;
    private int childResId = 0, childValuesId = 0;
    private int mLastCheckedPosition = -1;
    private OnCheckedChangedListener listener;
    private List<CheckBox> viewList = new ArrayList<CheckBox>();//存储所有的checkBox
    private int mChildcolumn, mChildRow, mChildWidth, mChildHeight;//列数，行数，每个CheckBox的宽
    private int mChildCount = 0;

    public MultiLineRadioGroup(Context context) {
        this(context, null, 0);
    }

    public MultiLineRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MultiLineRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    /**
     * 初始化和屏幕测算
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        mContext = context;

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MultiLineRadioGroup);
        mChildcolumn = arr.getInt(R.styleable.MultiLineRadioGroup_child_column, 1);
        childMarginHorizontal = arr.getDimensionPixelSize(R.styleable.MultiLineRadioGroup_child_margin_horizontal, 0);
        childMarginVertical = arr.getDimensionPixelSize(R.styleable.MultiLineRadioGroup_child_margin_vertical, 0);

        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mChildWidth = (outMetrics.widthPixels - (mChildcolumn + 1) * childMarginHorizontal) / mChildcolumn;//计算子控件宽

        childResId = arr.getResourceId(R.styleable.MultiLineRadioGroup_child_layout, 0);
        childValuesId = arr.getResourceId(R.styleable.MultiLineRadioGroup_child_values, 0);

        if (childResId == 0) {
            throw new RuntimeException("The attr 'child_layout' must be specified!");
        }
        if (childValuesId != 0) {
            String[] childValues = getResources().getStringArray(childValuesId);
            mChildCount = childValues.length;
            if (mChildCount > 0) {
                initChild(mChildCount, childValues);//创建子VIEW
                mChildRow = (mChildCount + mChildcolumn - 1) / mChildcolumn;

                LogUtils.i("mChildcolumn=" + mChildcolumn + " mChildWidth=" + mChildWidth + " mChildRow=" + mChildRow);

            } else {
                LogUtils.e("mChildCount is 0");
            }
        }
        arr.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (mChildCount > 0) {
            View child = getChildAt(0);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            mChildHeight = child.getMeasuredHeight();

            int motherHeight = (mChildHeight + childMarginVertical) * mChildRow - childMarginVertical + getPaddingBottom() + getPaddingTop();

            LogUtils.i("motherHeight=" + motherHeight);

            setMeasuredDimension(getMeasuredWidth(), motherHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            LogUtils.i("onLayout:unChanged");
            return;
        }

        int currentX = childMarginHorizontal, currentY = getPaddingTop();
        if (mChildCount > 0) {
            for (int i = 0; i < mChildCount; i++) {
                View v = getChildAt(i);
                if (i != 0 && i % mChildcolumn == 0) {
                    currentY += mChildHeight + childMarginVertical;
                    currentX = childMarginHorizontal;
                }
                v.layout(currentX, currentY, currentX + mChildWidth, currentY + mChildHeight);
                currentX += mChildWidth + childMarginHorizontal;
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {

            int i = (Integer) v.getTag();
            boolean checked = ((CheckBox) v).isChecked();
            if (mLastCheckedPosition != -1 && mLastCheckedPosition != i) {
                viewList.get(mLastCheckedPosition).setChecked(false);
            }
            if (checked) {
                mLastCheckedPosition = i;
            } else {
                mLastCheckedPosition = -1;
            }
            if (listener != null) {
                listener.onItemChecked(this, i, checked);
            }

        } catch (Exception e) {
        }
    }

    public void setOnCheckChangedListener(OnCheckedChangedListener l) {
        this.listener = l;
    }

    public boolean setItemChecked(int position) {
        if (position >= 0 && position < viewList.size()) {

            if (position == mLastCheckedPosition) {
                return true;
            }
            if (mLastCheckedPosition >= 0
                    && mLastCheckedPosition < viewList.size()) {
                viewList.get(mLastCheckedPosition).setChecked(false);
            }
            mLastCheckedPosition = position;

            viewList.get(position).setChecked(true);
            return true;
        }
        return false;
    }


    public int[] getCheckedItems() {
        if (mLastCheckedPosition >= 0
                && mLastCheckedPosition < viewList.size()) {
            return new int[]{mLastCheckedPosition};
        }
        return null;
    }

    public List<String> getCheckedValues() {
        List<String> list = new ArrayList<String>();
        if (mLastCheckedPosition >= 0
                && mLastCheckedPosition < viewList.size()) {
            list.add(viewList.get(mLastCheckedPosition).getText().toString());
            return list;
        }
        return list;
    }

    private void initChild(int count, String[] values) {

        for (int i = 0; i < count; i++) {

            View v = LayoutInflater.from(getContext()).inflate(childResId, this, false);
            if (!(v instanceof CheckBox))
                throw new RuntimeException("The attr child_layout's root must be a CheckBox!");
            CheckBox cb = (CheckBox) v;

            cb.setText(values[i]);
            cb.setTag(i);
            cb.setOnClickListener(this);
            cb.getLayoutParams().width = mChildWidth;
            viewList.add(cb);
            addView(cb);
        }


    }

    public void clearChecked() {
        if (mLastCheckedPosition >= 0
                && mLastCheckedPosition < viewList.size()) {
            viewList.get(mLastCheckedPosition).setChecked(false);
            mLastCheckedPosition = -1;
            return;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (null != viewList && viewList.size() > 0) {
            for (View v : viewList) {
                v.setEnabled(enabled);
            }
        }
    }

    public interface OnCheckedChangedListener {
        public void onItemChecked(MultiLineRadioGroup group, int position,
                                  boolean checked);
    }
}