package com.nlinks.parkdemo.utils;

import android.content.Context;
import android.util.Log;

import com.nlinks.parkdemo.global.AppConst;

public class LogUtils {
    /**
     * 方便过滤
     */
    private static String FLAG = "│ -->";

    private static boolean isDebug = AppConst.isDebug;

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        return tag;
    }

    /**
     * 以级别为 v 的形式输出LOG
     */
    public static void v(String content) {
        if (isDebug) {
            Log.v(generateTag(),FLAG + content);
        }
    }

    public static void v(Context context, String msg) {
        if (isDebug) {
            Log.v(context.getClass().getSimpleName(), FLAG + msg);
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void d(String content) {
        if (isDebug) {
            Log.d(generateTag(),FLAG + content);
        }
    }
    public static void d(Context context, String msg) {
        if (isDebug) {
            Log.d(context.getClass().getSimpleName(), FLAG + msg);
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String content) {
        if (isDebug) {
            Log.i(generateTag(),FLAG + content);
        }
    }
    public static void i(Context context, String msg) {
        if (isDebug) {
            Log.i(context.getClass().getSimpleName(), FLAG + msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    public static void w(String content) {
        if (isDebug) {
            Log.w(generateTag(), FLAG +content);
        }
    }
    public static void w(Context context, String msg) {
        if (isDebug) {
            Log.w(context.getClass().getSimpleName(), FLAG + msg);
        }
    }

    /**
     * 以级别为 w 的形式输出Throwable
     */
    public static void w(Throwable tr) {
        if (isDebug) {
            Log.w(generateTag(), "", tr);
        }
    }
    public static void w(Context context, Throwable tr) {
        if (isDebug) {
            Log.w(context.getClass().getSimpleName(), "", tr);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    public static void w(Context context, String msg, Throwable tr) {
        if (isDebug && null != msg) {
            Log.w(context.getClass().getSimpleName(), FLAG + msg, tr);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    public static void e(String content) {
        if (isDebug) {
            Log.e(generateTag(),FLAG + content);
        }
    }
    public static void e(Context context, String msg) {
        if (isDebug) {
            Log.e(context.getClass().getSimpleName(), FLAG + msg);
        }
    }

    /**
     * 以级别为 e 的形式输出Throwable
     */
    public static void e(Throwable tr) {
        if (isDebug) {
            Log.e(generateTag(), "", tr);
        }
    }
    public static void e(Context context, Throwable tr) {
        if (isDebug) {
            Log.e(context.getClass().getSimpleName(), "", tr);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    public static void e(Context context, String msg, Throwable tr) {
        if (isDebug && null != msg) {
            Log.e(context.getClass().getSimpleName(), FLAG + msg, tr);
        }
    }

	/**
	 * 太长可以分段输出的log
	 * @param content
	 */
	public static void v2(String content) {
		if (isDebug) {
			String tag = generateTag();
			String[] ifStringToLong = splitIfStringToLong(FLAG + content);
			for (String log : ifStringToLong) Log.v(tag, log);
		}
	}

	/**
	 * 当内容太长时，对内容进行裁切显示
	 * 因为单条log有长度限制，超过4k?就会被截断
	 * @param content
	 * @return
	 */
	private static String[] splitIfStringToLong(String content){
		final int size = 3000;//每段大小
		int length = content.length();//内容长度
		int times = length % size > 0 ? length / size + 1 : length / size;//需要分为几段
		String[] result = new String[times];
		for (int i = 0; i < times - 1; i++) {//最后一段前的处理
			result[i] = content.substring(i * size, (i + 1) * size);
		}
		int lastFirst = times - 1 >= 0 ? times - 1 : 0;//最后一段开始的位置
		int lastEnd = times * size >= length ? length : times * size;//最后一段结束的位置
		result[times - 1] = content.substring(lastFirst * size, lastEnd);//最后一段的内容
		return result;
    }
}
