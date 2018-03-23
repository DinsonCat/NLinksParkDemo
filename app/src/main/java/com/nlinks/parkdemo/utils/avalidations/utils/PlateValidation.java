package com.nlinks.parkdemo.utils.avalidations.utils;

import android.content.Context;
import android.widget.EditText;

import com.nlinks.parkdemo.utils.UIUtils;

import java.util.regex.Pattern;

/**
 * @author Dinson - 2017/8/2
 */
public class PlateValidation extends ValidationExecutor {


    public PlateValidation(EditText editText) {
        super(editText);
    }

    @Override
    public boolean doValidate(Context context, String text) {

        String regex = "^[a-zA-Z](?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9_]{7,11}$";
        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            getEditText().requestFocus();
            UIUtils.showToast("请输入正确车牌");
            return false;
        }
        return true;
    }
}
