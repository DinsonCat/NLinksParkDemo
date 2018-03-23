package com.nlinks.parkdemo.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nlinks.parkdemo.R;

/**
 * Android的顶部沉浸效果 工具
 * Created by du on 2017/3/28.
 */
public class TitleCompat {

    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private int mStatusBarHeight = 0;
    private View mFakeStatusBarView;
    private Activity mActivity;

    /**
     * 设计为每个activity都有不同的title，即需要每个activity皆有不同的titleCompat
     * 为了防止使用单例的初始化，所以将构造方法设为私有方法。使每个activity都需要
     * 调用静态方法{@link TitleCompat#setStatusBar(Activity, boolean, boolean)} 来实例化本类
     * @param activity
     * @param translucent
     * @param fits
     */
    private TitleCompat(Activity activity, boolean translucent, boolean fits) {
        mActivity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getInternalDimensionSize(activity.getResources(), "status_bar_height");
            setTranslucentStatus(translucent);
            if (translucent) setContentFits(fits);
        }
    }

    public static TitleCompat setDefault(Activity activity){
	    return TitleCompat.setStatusBar(activity, true, true)
			    .setFakeStatusBarColorId(R.color.colorPrimary);
    }

    /**
     * 设置状态栏
     * @param activity activity
     * @param translucent 状态栏是否透明
     * @param fits 是否适应内容
     * @return
     */
    public static TitleCompat setStatusBar(Activity activity, boolean translucent, boolean fits){
        return new TitleCompat(activity, translucent, fits);
    }

    /**
     * 设置自己定义的statusBar是否可见
     * <br/>statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的<br/>
     * @param visibility
     */
    public TitleCompat setFakeStatusBarVisibility(boolean visibility){
        if (mFakeStatusBarView != null){
            mFakeStatusBarView.setVisibility(visibility ? View.VISIBLE :View.GONE);
        }
        return this;
    }

    /**
     * 设置自己定义的statusBar背景颜色
     * @param colorId id
     */
    public TitleCompat setFakeStatusBarColorId(int colorId) {
        int color = ContextCompat.getColor(mActivity, colorId);
        return setFakeStatusBarColor(color);
    }

    /**
     * 设置自己定义的statusBar背景颜色
     * <br/>statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的<br/>
     * @param color
     */
    public TitleCompat setFakeStatusBarColor(int color){
        setFakeStatusBarColor(new ColorDrawable(color));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//SDK大于6.0时，判断是否是浅色状态栏
//            View decorView = mActivity.getWindow().getDecorView();
//            boolean isLighter = isColorLighter(color);
//            if (isLighter){
//                Log.w(TitleCompat.class.getSimpleName(), "疑似使用浅色作为statusBar背景，请注意效果");
//                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }else decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        }
        return this;
    }

    /**
     * 设置自己定义的statusBar背景
     * <br/>statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的<br/>
     * @param drawable
     */
    public TitleCompat setFakeStatusBarColor(Drawable drawable){
        if (mFakeStatusBarView != null){
            setFakeStatusBarVisibility(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
                mFakeStatusBarView.setBackground(drawable);
            else mFakeStatusBarView.setBackgroundDrawable(drawable);
        }
        return this;
    }

    /**
     * 设置内部内容适应
     * @param fits 要使内容在statusBar下则true
     */
    public TitleCompat setContentFits(boolean fits){
        ViewGroup contentFrameLayout = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null){
            parentView.setPadding(parentView.getPaddingLeft()
                    , fits ? mStatusBarHeight : 0
                    , parentView.getPaddingRight(), parentView.getPaddingBottom());
        }
        return this;
    }

    /**
     * 设置状态栏透明
     * @param on 透明则true
     */
    public TitleCompat setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = mActivity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);

            ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();
            mFakeStatusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                    , mStatusBarHeight);
            params.gravity = Gravity.TOP;
            mFakeStatusBarView.setLayoutParams(params);
            mFakeStatusBarView.setBackgroundColor(DEFAULT_TINT_COLOR);
            mFakeStatusBarView.setVisibility(View.GONE);
            decorViewGroup.addView(mFakeStatusBarView);
        }
        return this;
    }

    /**
     * 通过id名称，获取dimen资源
     * @param res
     * @param key
     * @return
     */
    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    /**
//     * 判断颜色深浅。我自己的算法，不科学的
//     * 这是我自己的算法，根据"RGB值越接近，颜色就越接近灰色或黑白，数值越大就越白，反之越黑"
//     * 我认为，当128，128，128这个值就很浅
//     * 那么，就可以简单的通过判断argb大于这个值为更浅
//     * 算法如下：
//     * 1.如果三个数之间的绝对值差之和在48(16*3)之下，且每个数值接近128之上，认为浅色
//     * 2.透明度小于128(256/2)，认为浅色
//     * 3.其余情况认为深色
//     * @author dujc
//     * @return 颜色深浅，深FALSE，浅TRUE
//     */
//    private static boolean isColorLighter(int color){
//        int alpha = color >>> 24;
//        int red = (color >> 16) & 0xFF;
//        int green = (color >> 8) & 0xFF;
//        int blue = color & 0xFF;
//        System.out.println("------ red is "+red+", green is "+green+", blue is "
//                +blue+", alpha is "+alpha);
//        if (Math.abs(red - green) + Math.abs(green - blue) + Math.abs(blue - red) <= 48){
//            return (red > 128 || green > 128 || blue > 128) || alpha < 128;
//        }else return alpha <= 128;
//    }
}
