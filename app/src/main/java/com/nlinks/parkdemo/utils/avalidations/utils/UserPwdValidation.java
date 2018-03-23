package com.nlinks.parkdemo.utils.avalidations.utils;

import android.content.Context;
import android.widget.EditText;

import com.nlinks.parkdemo.utils.UIUtils;

import java.util.regex.Pattern;

/**
 * @author Dinson - 2017/8/2
 */
public class UserPwdValidation extends ValidationExecutor {


    public UserPwdValidation(EditText editText) {
        super(editText);
    }

    @Override
    public boolean doValidate(Context context, String text) {
        String regex = "^[\\S]{6,18}$";
        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            getEditText().requestFocus();
            UIUtils.showToast("请输入6-18位密码");
            return false;
        }
        return true;
    }
}
