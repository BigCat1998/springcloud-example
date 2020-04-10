package com.me.cloud.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 大猫
 */
public class DateUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前日期字符串
     *
     * @return
     */
    public static synchronized String getCurrentDate() {
        return sdf.format(new Date());
    }

}
