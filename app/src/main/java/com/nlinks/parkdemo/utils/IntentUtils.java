package com.nlinks.parkdemo.utils;

import android.content.Intent;
import android.net.Uri;

/**
 * @author Dinson - 2017/7/26
 */
public class IntentUtils {

    /**
     * 获取跳至拨号界面意图
     *
     * @param phoneNumber 电话号码
     */
    public static Intent getDialIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
