package com.nlinks.parkdemo.utils.avalidations.utils;

import android.content.Context;
import android.widget.EditText;

import com.nlinks.parkdemo.utils.UIUtils;

import java.util.regex.Pattern;

/**
 * @author Dinson - 2017/8/2
 */
public class CarEngineNumValidation extends ValidationExecutor {


    public CarEngineNumValidation(EditText editText) {
        super(editText);
    }

    @Override
    public boolean doValidate(Context context, String text) {

        String regex = "^[A-Za-z0-9]{4,20}$";
        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            getEditText().requestFocus();
            UIUtils.showToast("请输入正确发动机号");
            return false;
        }
        return true;
    }
}
