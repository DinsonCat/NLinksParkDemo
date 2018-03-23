package com.nlinks.parkdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.UIUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * 提示框
 */
public class DialogChoosePlate extends Dialog implements View.OnClickListener {

    private String[] mData;
    private final ViewPager mVpContainer;

    public DialogChoosePlate(Context context, String[] data ) {
        super(context, R.style.custom_dialog);
        setContentView(R.layout.dialog_plate);
        findViewById(R.id.dialogLeftBtn).setOnClickListener(this);
        findViewById(R.id.dialogRightBtn).setOnClickListener(this);

        mData = data;
        mVpContainer = findViewById(R.id.vpContainer);
        mVpContainer.setOffscreenPageLimit(3);
        mVpContainer.setPageMargin(20);// setPageMargin表示设置page之间的间距


        ArrayList<View> views = new ArrayList<>();
        for (String plate : mData) {
            TextView textView = new TextView(context);
            textView.setBackgroundResource(R.drawable.plate_bg);
            textView.setText(plate);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 0, 30, 0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setTextColor(context.getResources().getColor(R.color.white));
            views.add(textView);
        }
        mVpContainer.setAdapter(new VpAdater(context, views));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogLeftBtn:
                this.cancel();
                break;
            case R.id.dialogRightBtn:
                if (mListener != null) mListener.onChoose(mData[mVpContainer.getCurrentItem()]);
                break;
        }
    }


    class VpAdater extends PagerAdapter {

        private List<View> content;

        public VpAdater(Context context, List<View> content) {
            this.content = content;
        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(content.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(content.get(position));
            return content.get(position);
        }
    }


    private onChooseListener mListener;

    public void setOnChooseListener(onChooseListener listener) {
        mListener = listener;
    }

    public interface onChooseListener {
        void onChoose(String plate);
    }

}