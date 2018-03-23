package com.nlinks.parkdemo.utils;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * 字符相关
 */
public class StringUtils {

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str == "null" || str == "NULL" || str.trim().length() == 0);
    }

    /**
     * 判断是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断是否不为空
     *
     * @param str
     * @return
     */
    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str == "NULL" || str.length() == 0);
    }

    /**
     * 判断是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 转化为百分数
     *
     * @param num
     * @return
     */
    public static String convertPercent(double num) {
        return convertPercent(num, 2);
    }

    /**
     * 转化为百分数
     *
     * @param num
     * @param fractionDigits 精确到小数点后几位
     * @return
     */
    public static String convertPercent(double num, int fractionDigits) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumIntegerDigits(Integer.MAX_VALUE);
        format.setMaximumFractionDigits(fractionDigits);
        return format.format(num);
    }

    /**
     * 首字母大写
     * <p/>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * 格式化金额
     *
     * @param money 金额
     * @return 返回代付string金额
     */
    public static String formatMoney(String money) {
        if (money == null) return "--.--";
        try {
            double v = Double.parseDouble(money);
            return formatMoney(v);
        } catch (Exception e) {
            return "--.--";
        }
    }

    /**
     * 格式化金额
     *
     * @param money 金额
     * @return 返回代付string金额
     */
    public static String formatMoney(double money) {
        NumberFormat nf = new DecimalFormat("#0.00");
        return nf.format(money);
    }

    /**
     * 从文本中获取纯数值。如"你好123nihao456haha哈"会返回"123456"
     */
    public static String getPureNumber(String text) {
        if (isEmpty(text)) return "0";
        return Pattern.compile("[^0-9|.]").matcher(text).replaceAll("");
    }

}
