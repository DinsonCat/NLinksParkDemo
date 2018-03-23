package com.nlinks.parkdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nlinks.parkdemo.R;

import java.util.ArrayList;

/**
 * 输入法---车牌键盘
 */
public class PlateKeyboardUtils {
    private Context mContext;
    private RadioGroup mRg;//RadioButton组
    private ArrayList<RadioButton> mRbs = new ArrayList<>();//用于显示的RadioButton
    private Keyboard mKbProvince, mKbLetterNum;//省简称键盘，字母数字键盘
    private String mProvinceShort[], mLetterAndDigit[];//省简称，字母数字
    private KeyboardView mKeyboardView;
    private int mCurrentRadioButton = 0;//默认当前光标在第一个EditText
    private RadioButton mDeleTemp;

    public PlateKeyboardUtils(Activity activity, RadioGroup rg, KeyboardView keyboardView) {

        initViewData(activity, rg, keyboardView);//初始化视图数据

        initRadioButton();//初始化RadioButton组

        setNEV(false);
    }

    /**
     * 初始化视图数据
     */
    private void initViewData(Activity activity, RadioGroup rg, KeyboardView keyboardView) {
        mContext = activity;
        mRg = rg;

        mRg.setOnCheckedChangeListener(mCheckChangeListener);

        mKbProvince = new Keyboard(mContext, R.xml.keyboard_province);
        mKbLetterNum = new Keyboard(mContext, R.xml.keyboard_letter_num);
        mKeyboardView = keyboardView;
        mKeyboardView.setKeyboard(mKbProvince);//初始化省简称键盘
        mKeyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mKeyboardListener); //设置键盘按键监听器
        mProvinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
                , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
                , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
                , "青", "琼", "新", "宁"};

        mLetterAndDigit = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
                , "A", "B", "C", "D", "E", "F", "G", "H", "J", "K"
                , "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V"
                , "W", "X", "Y", "Z", "港", "澳", "学"};
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 键盘按键监听器
     */
    private OnKeyboardActionListener mKeyboardListener = new OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            switch (primaryCode) {
                case Keyboard.KEYCODE_DONE:
                    //完成
                    if (mPlateListener != null) {
                        mPlateListener.onComplete(getPlate());
                    }
                    hideKeyboard();
                    break;
                case Keyboard.KEYCODE_DELETE:
                    //回退
                    mRbs.get(mCurrentRadioButton).setText("");//将当前EditText置为""并currentEditText-1
                    mCurrentRadioButton--;
                    if (mCurrentRadioButton < 1) {
                        //切换为省份简称键盘
                        mKeyboardView.setKeyboard(mKbProvince);
                    }
                    if (mCurrentRadioButton < 0) {
                        mCurrentRadioButton = 0;
                    }
                    mRbs.get(mCurrentRadioButton).setChecked(true);//焦点转移
                    break;
                default:
                    if (mCurrentRadioButton == 0) {
                        //如果currentEditText==0代表当前为省份键盘,
                        // 按下一个按键后,设置相应的EditText的值
                        // 然后切换为字母数字键盘
                        //currentEditText+1
                        mRbs.get(0).setText(mProvinceShort[primaryCode]);
                        mCurrentRadioButton = 1;
                        //切换为字母数字键盘
                        mKeyboardView.setKeyboard(mKbLetterNum);
                    } else {
                        //第二位必须大写字母
                        if (mCurrentRadioButton == 1 && !mLetterAndDigit[primaryCode].matches("[A-Z]")) {
                            return;
                        }
                        if (mCurrentRadioButton != mRbs.size() - 1 && mLetterAndDigit[primaryCode].matches("[港,澳,学]")) {
                            return;
                        }
                        mRbs.get(mCurrentRadioButton).setText(mLetterAndDigit[primaryCode]);
                        mCurrentRadioButton++;
                        if (mCurrentRadioButton > mRbs.size() - 1) {
                            mCurrentRadioButton = mRbs.size() - 1;
                            hideKeyboard();
                        }
                    }
                    mRbs.get(mCurrentRadioButton).setChecked(true);//焦点转移
                    break;
            }
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeUp() {
        }
    };

    /**
     * 初始化RadioButton组
     */
    private void initRadioButton() {
        int childCount = mRg.getChildCount();
        for (int i = 0; i < childCount; i++) {

            RadioButton rb = (RadioButton) mRg.getChildAt(i);
            mRbs.add(rb);
            rb.setId(i + 1);
            rb.setGravity(Gravity.CENTER);
            rb.setButtonDrawable(null);
            rb.setBackground(UIUtils.getDrawable(R.drawable.selector_rb_platenum_bg));
        }
        mRbs.get(0).setChecked(true);//车牌选择第一个
    }

    /**
     * 设置是否为新能源车牌
     *
     * @param b
     */
    public void setNEV(boolean b) {
        if (b) {
            if (mDeleTemp != null) {
                mRbs.add(mDeleTemp);
                mRbs.get(mRbs.size() - 1).setVisibility(View.VISIBLE);
            }
        } else {
            mDeleTemp = mRbs.remove(mRbs.size() - 1);
            mDeleTemp.setVisibility(View.GONE);
        }
        if (mCurrentRadioButton > mRbs.size() - 1) {
            mCurrentRadioButton = mCurrentRadioButton - 1;
            mRbs.get(mCurrentRadioButton).setChecked(true);//焦点转移
        }
    }

    /**
     * 检验车牌是否完整
     *
     * @return
     */
    public boolean isComplete() {
        for (RadioButton rb : mRbs) {
            if (rb.getText().toString().isEmpty()) return false;
        }
        return true;
    }

    /**
     * 获取车牌
     *
     * @return
     */
    public String getPlate() {
        String plate = "";
        for (RadioButton rb : mRbs) {
            if (rb.getText().toString().isEmpty()) return "";
            plate += rb.getText().toString();
        }
        return plate;
    }


    private onPlateCompleteListener mPlateListener;//车牌是否完整监听器

    /**
     * 车牌是否完整监听器
     */
    public interface onPlateCompleteListener {
        void onComplete(String plate);
    }

    /**
     * 设置车牌监听器
     *
     * @param listener 监听器
     */
    public void setOnPlateCompleteListener(onPlateCompleteListener listener) {
        mPlateListener = listener;
    }


    @SuppressWarnings("ResourceType")
    private RadioGroup.OnCheckedChangeListener mCheckChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId > mRbs.size()) {
                new Exception("Under the RadioGroup radiobutton don't bring id").printStackTrace();
                return;
            }
            showKeyboard();
            mKeyboardView.setKeyboard(checkedId == 1 ? mKbProvince : mKbLetterNum);
            mRbs.get(checkedId - 1).setChecked(true);
            mCurrentRadioButton = checkedId - 1;
        }
    };


    public void clearCheck() {

        LogUtils.e(mCurrentRadioButton + "----");
        mRbs.get(mCurrentRadioButton).setChecked(false);
    }


}
