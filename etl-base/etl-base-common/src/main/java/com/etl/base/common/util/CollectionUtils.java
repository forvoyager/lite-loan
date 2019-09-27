package com.etl.base.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 */
public final class CollectionUtils {

    public static boolean isEmpty(Map collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }

        return false;
    }
    
    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }

        return false;
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    private CollectionUtils(){}
}
