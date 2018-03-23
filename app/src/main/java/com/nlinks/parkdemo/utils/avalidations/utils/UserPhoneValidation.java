package com.nlinks.parkdemo.utils.avalidations.utils;

import android.content.Context;
import android.widget.EditText;

import com.nlinks.parkdemo.utils.RegexUtils;
import com.nlinks.parkdemo.utils.UIUtils;

import java.util.regex.Pattern;

/**
 * @author Dinson - 2017/8/2
 */
public class UserPhoneValidation extends ValidationExecutor {


    public UserPhoneValidation(EditText editText) {
        super(editText);
    }

    @Override
    public boolean doValidate(Context context, String text) {

        String regex = RegexUtils.REGEX_MOBILE_EXACT;
        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            getEditText().requestFocus();
            UIUtils.showToast("请输入正确的手机号码");
            return false;
        }
        return true;
    }
}
