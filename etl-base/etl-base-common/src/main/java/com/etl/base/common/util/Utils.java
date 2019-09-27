package com.etl.base.common.util;

import com.etl.base.common.enums.ResultCodeEnum;
import com.etl.base.common.exception.BaseException;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>description</b>：工具类 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
public final class Utils {

  /**
   * 抛出系统异常
   * @param message
   * @throws Exception
   */
  public static void throwsBaseException(String message) throws Exception {
    throw new BaseException(ResultCodeEnum.SYSTEM_ERROR, message);
  }

  public static <K, V> Map<K, V> newHashMap(Object... args) {
    HashMap map = new HashMap();
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        map.put(args[i], args[++i]);
      }
    }
    return map;
  }
  
  private Utils(){}
}
