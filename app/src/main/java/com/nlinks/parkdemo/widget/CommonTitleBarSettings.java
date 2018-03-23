package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.nlinks.parkdemo.R;


/**
 * 标题栏设置
 */
public class CommonTitleBarSettings {

    private String titleText = "", leftText = "", rightText = "";
    private final int titleTextColor;
    private final boolean leftBtnVisible, rightBtnVisible;
    private final int rightBtnTextColor, leftBtnTextColor;
    private final int leftBtnDrawable, rightBtnDrawable;

    CommonTitleBarSettings(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);

        titleText = attributes.getString(R.styleable.CommonTitleBar_title_text);
        titleTextColor = attributes.getColor(R.styleable.CommonTitleBar_title_text_color,
            context.getResources().getColor(R.color.white));

        leftBtnVisible = attributes.getBoolean(R.styleable.CommonTitleBar_left_button_visible,
            false);
        rightBtnVisible = attributes.getBoolean(R.styleable.CommonTitleBar_right_button_visible,
            false);

        leftText = attributes.getString(R.styleable.CommonTitleBar_left_button_text);
        rightText = attributes.getString(R.styleable.CommonTitleBar_right_button_text);

        leftBtnTextColor = attributes.getColor(R.styleable.CommonTitleBar_left_button_text_color,
            context.getResources().getColor(R.color.white));
        rightBtnTextColor = attributes.getColor(R.styleable.CommonTitleBar_right_button_text_color,
            context.getResources().getColor(R.color.white));

        leftBtnDrawable = attributes.getResourceId(R.styleable.CommonTitleBar_left_button_drawable,
            R.mipmap.fh);
        rightBtnDrawable = attributes.getResourceId(R.styleable.CommonTitleBar_right_button_drawable,
            -1);

        attributes.recycle();
    }

    String getTitleText() {
        return titleText;
    }

    String getLeftText() {
        return leftText;
    }

    String getRightText() {
        return rightText;
    }

    int getTitleTextColor() {
        return titleTextColor;
    }

    boolean isLeftBtnVisible() {
        return leftBtnVisible;
    }

    boolean isRightBtnVisible() {
        return rightBtnVisible;
    }

    int getRightBtnTextColor() {
        return rightBtnTextColor;
    }

    int getLeftBtnTextColor() {
        return leftBtnTextColor;
    }

    int getLeftBtnDrawable() {
        return leftBtnDrawable;
    }

    int getRightBtnDrawable() {
        return rightBtnDrawable;
    }
}
