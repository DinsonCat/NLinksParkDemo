package com.nlinks.parkdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nlinks.parkdemo.entity.park.FavPark;
import com.nlinks.parkdemo.global.GlobalApplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SharePreference封装
 */
public class SPUtils {

    public static void resetUser(Context ctx) {
        removeAll(ctx, "nlcg");
        GlobalApplication.token = null;
        GlobalApplication.userId = null;
        GlobalApplication.membership = -1;
        GlobalApplication.plateNum = null;
    }


    /**
     * 存用户ID
     */
    public static void putUserId(Context ctx, String value) {
        putString(ctx, "nlcg", "ui", NlinksParkUtils.encrypt(value));
    }

    public static String getUserId(Context ctx) {
        return getUserId(ctx, "");
    }

    /**
     * 取用户ID
     */
    public static String getUserId(Context ctx, String defValue) {
        if (!StringUtils.isEmpty(GlobalApplication.userId))
            return GlobalApplication.userId;
        try {
            String decrypt = NlinksParkUtils.decrypt(getString(ctx, "nlcg", "ui", defValue));
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }


    /**
     * 存用户Token
     */
    public static void putToken(Context ctx, String value) {
        putString(ctx, "nlcg", "to", NlinksParkUtils.encrypt(value));
    }

    /**
     * 取用户Token
     */
    public static String getToken(Context ctx, String defValue) {
        if (!StringUtils.isEmpty(GlobalApplication.token))
            return GlobalApplication.token;
        try {
            String decrypt = NlinksParkUtils.decrypt(getString(ctx, "nlcg", "to", defValue));
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }


    /**
     * 设置用户等级
     *
     * @param ctx
     * @param membership
     */
    public static void putMembership(Context ctx, int membership) {
        if (membership == 1)
            putBoolean(ctx, "nlcg", "ms", true);
        else
            putBoolean(ctx, "nlcg", "ms", false);
    }

    /**
     * 取用户等级
     */
    public static boolean isMember(Context ctx) {
        if (GlobalApplication.membership == 1)
            return true;
        return getBoolean(ctx, "nlcg", "ms", false);
    }


    /**
     * 获取用户车牌
     */
    public static String getUserPlate(Context ctx, String defValue) {
        try {
            String decrypt = NlinksParkUtils.decrypt(getString(ctx, "nlcg", "plate", defValue));
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 设置车牌（约定格式: 闽C66666,闽C88888,闽C99999）
     */
    public static void putUserPlate(Context ctx, String plate) {
        putString(ctx, "nlcg", "plate", NlinksParkUtils.encrypt(plate));
    }

    /**
     * 存上一次登录的号码
     */
    public static void putLastPhone(Context ctx, String value) {
        putString(ctx, "nlcgv2", "nu", NlinksParkUtils.encrypt(value));
    }

    /**
     * 取上一次登录的号码
     */
    public static String getLastPhone(Context ctx, String defValue) {
        try {
            String decrypt = NlinksParkUtils.decrypt(getString(ctx, "nlcgv2", "nu", defValue));
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 存个推cid
     */
    public static void putCid(Context ctx, String value) {
        putString(ctx, "nlcgv2", "cid", value);
    }

    /**
     * 取个推cid
     */
    public static String getCid(Context ctx, String defValue) {
        if (!StringUtils.isEmpty(GlobalApplication.cid))
            return GlobalApplication.cid;
        return getString(ctx, "nlcgv2", "cid", defValue);
    }

    /**
     * 获取自动更新离线地图状态
     */
    public static boolean getAutoDownMap(Context ctx) {
        return getBoolean(ctx, "nlcgv2", "am", true);//默认自动加载
    }

    /**
     * 设置自动更新离线地图状态
     */
    public static void putAutoDownMap(Context ctx, boolean value) {
        putBoolean(ctx, "nlcgv2", "am", value);
    }

    /**
     * 取消新手引导页
     */
    public static void setFirstRunFalse(Context ctx) {
        putBoolean(ctx, "nlcgv2", "isFrist" + UIUtils.getVersionName(ctx), false);
    }

    /**
     * 是否第一次运行
     */
    public static boolean isFirstRun(Context ctx) {
        return getBoolean(ctx, "nlcgv2", "isFrist" + UIUtils.getVersionName(ctx), true);//默认第一次
    }


    /**
     * 设置我的关注列表
     */
    public static void putFavoList(Context ctx, List<FavPark> list) {
        SharedPreferences sp = ctx.getSharedPreferences("nlcgv2", Context.MODE_PRIVATE);
        String favo = "";
        for (FavPark favPark : list) {
            favo += favPark.getParkCode() + "#";
        }
        sp.edit().putString("favo", favo).apply();
    }

    public static String getFavoList(Context ctx) {
        return getString(ctx, "nlcgv2", "favo", "");
    }

    public static boolean isFavorite(Context ctx, String code) {
        return getFavoList(ctx).contains(code);
    }

    public static void appendFavorite(Context ctx, String code) {
        SharedPreferences sp = ctx.getSharedPreferences("nlcgv2", Context.MODE_PRIVATE);
        String favo = getFavoList(ctx) + "#" + code;
        sp.edit().putString("favo", favo).apply();
    }

    public static void delFavorite(Context ctx, String code) {
        SharedPreferences sp = ctx.getSharedPreferences("nlcgv2", Context.MODE_PRIVATE);
        String favo = getFavoList(ctx).replaceAll(code, "");
        sp.edit().putString("favo", favo).apply();
    }

    /**
     * 存储停车记录广告的隐藏时间
     */
    public static void putParkRecordAdtTime(Context ctx) {
        putInt(ctx, "nlcgv2", "adTime", DateUtils.getCurrentTimeMillis10());
    }

    public static int getParkRecordAdTime(Context ctx) {
        return getInt(ctx, "nlcgv2", "adTime", 0);
    }


    /**
     * 存储停车记录广告的隐藏时间
     */
    public static void putMonthlyPayListClicked(Context ctx, String parkId) {
        String string = getString(ctx, "nlcg", "monthly", "") + parkId + ",";
        putString(ctx, "nlcg", "monthly", string);
    }

    public static List<String> getMonthlyPayListClicked(Context ctx) {
        String key = getString(ctx, "nlcg", "monthly", "");
        if (StringUtils.isEmpty(key)) return new ArrayList<>();
        String[] split = key.split(",");
        return Arrays.asList(split);
    }


    /////////////////////////////////// 分割线 /////////////////////////////////////////////////////

    public static boolean getBoolean(Context ctx, String fileName, String key, boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putBoolean(Context ctx, String fileName, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void putString(Context ctx, String fileName, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx, String fileName, String key, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context ctx, String fileName, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context ctx, String fileName, String key, int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void remove(Context ctx, String fileName, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void removeAll(Context ctx, String fileName) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }


}
