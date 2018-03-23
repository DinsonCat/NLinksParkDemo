package com.nlinks.parkdemo.utils;

import java.util.List;

/**
 *
 */
public class ArrayListUtils {
    /**
     * 判断是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断是否不为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }
}
