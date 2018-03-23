package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.nlinks.parkdemo.utils.TypefaceUtils;


/**
 * IconFont支持
 */
public class IconFontTextView extends AppCompatTextView {

    public IconFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IconFontTextView(Context context) {
        super(context);
        init();
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }


    public void init() {
        Typeface typeface = TypefaceUtils.get(getContext(), "iconfont/iconfont.ttf");
        setTypeface(typeface);
    }
}
