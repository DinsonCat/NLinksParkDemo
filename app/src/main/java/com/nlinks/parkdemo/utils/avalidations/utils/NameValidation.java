package com.nlinks.parkdemo.utils.avalidations.utils;

import android.content.Context;
import android.widget.EditText;

import com.nlinks.parkdemo.utils.UIUtils;

import java.util.regex.Pattern;

/**
 * @author Dinson - 2017/8/2
 */
public class NameValidation extends ValidationExecutor {


    public NameValidation(EditText editText) {
        super(editText);
    }

    @Override
    public boolean doValidate(Context context, String text) {

        String regex = "^[\\u4e00-\\u9fa5]{2,}$";
        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            getEditText().requestFocus();
            UIUtils.showToast("请输入正确名字");
            return false;
        }
        return true;
    }
}
