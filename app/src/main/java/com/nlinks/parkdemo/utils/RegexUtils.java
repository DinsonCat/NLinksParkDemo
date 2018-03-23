package com.nlinks.parkdemo.utils;

import java.util.regex.Pattern;

/**
 * 正则工具类
 */
public class RegexUtils {

    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$|([1]{11})";


    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobilePhone(CharSequence input) {
        return isMatch(REGEX_MOBILE_EXACT, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    private static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    public static boolean isUrl(CharSequence input){
        return isMatch("^[0-9h].+",input);
    }
}
