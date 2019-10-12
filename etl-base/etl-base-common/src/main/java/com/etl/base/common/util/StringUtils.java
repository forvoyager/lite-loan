package com.etl.base.common.util;

/**
 * 字符操作工具类
 *
 * 继承自 com.ms.base.comm.util.StringUtils
 * 可以自行扩展
 *
 */
public final class StringUtils extends org.springframework.util.StringUtils {

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    private StringUtils(){}
}
