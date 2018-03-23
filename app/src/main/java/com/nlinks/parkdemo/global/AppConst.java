package com.nlinks.parkdemo.global;

import android.os.Environment;

import com.nlinks.parkdemo.BuildConfig;
import com.nlinks.parkdemo.utils.UIUtils;

/**
 * 全局设置参数
 */
public class AppConst {
    //是否为开发者模式，用于日志的打印。上线版本设置为false
    public static final boolean isDebug = BuildConfig.DEBUG;
    public static final String ACCESSKEY = "yyuYT235690ct129";

    public static final String WEIXIN_APP_ID = "wxf2822985be26ccc7";
    public static final String QQ_APP_ID = "1106023273";

    //DES私钥
    public static final String DESKEY = "95880288201091325707433253118984263478572" + "987735494687588750185795377577721630844788736994473060344662006164119" + "60574122434059469100235892702736860872901247123456";

    /*网络请求地址*/

//        public static String SERVER_ADD = "http://59.61.216.123:14101/";//测试地址  PS:2017-08-01之前线上地址
    public static String SERVER_ADD = "http://www.nlinks.cn/";//新线上地址

    public static final String URL_PREFIX = SERVER_ADD + "appapi/";

    /*蜻蜓金卡会员升级页面地址*/
    public static final String URL_MEMBER = SERVER_ADD + "appapi/api/appweb/memberactivity";

    /*蜻蜓金卡会员详情页面地址（需加userid）*/
    public static final String URL_MEMBER_DETAIL = SERVER_ADD + "appapi/api/appweb/membership";

    /*关于我们页面地址（需加userid）*/
    public static final String URL_ABOUT_US = SERVER_ADD + "appapi/api/appweb/about";

    /*用户协议页面地址（需加userid）*/
    public static final String URL_USERAGREEMENT = SERVER_ADD + "appapi/api/appweb/qtUserAgreement";


    /*sdcard路径*/
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().toString();
    public static final String CACHE_IMG_DIR = UIUtils.getContext().getCacheDir() + "/img/";

    public static final int PARK_SEARCH_RADIUS = 1;
    public static final int PAGE_SIZE_DEFAULT = 20;

    /*登录模式*/
    /*public static final int MODE_REGIST = 101;
    public static final int MODE_FORGET = 102;*/

    /*地锁状态*/
    public static final int STATUS_LOCK_DOWN = 1;
    public static final int STATUS_LOCK_UP = 2;
    public static final int STATUS_LOCK_ERROR = 3;

    /*全局时间格式*/
    public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";




}
