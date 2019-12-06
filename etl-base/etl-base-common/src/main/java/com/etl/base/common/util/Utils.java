package com.etl.base.common.util;

import com.etl.base.common.enums.ResultCodeEnum;
import com.etl.base.common.exception.BaseException;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>description</b>：工具类 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
public final class Utils {

  private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  public static void throwsBizException(String message) throws Exception {
    throw new BaseException(ResultCodeEnum.ILLEGAL_STATUS, message);
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

  /**
   * Md5 加密工具
   *
   * @param content String   not null
   * @return 加密后的 MD5 值 32位大写的
   */
  public static String md5(String content) throws Exception {
    AssertUtils.notEmpty(content, "encryption content is empty.");

    byte[] btInput = content.getBytes();
    // 获得MD5摘要算法的 MessageDigest 对象
    MessageDigest mdInst = MessageDigest.getInstance("MD5");
    // 使用指定的字节更新摘要
    mdInst.update(btInput);
    // 获得密文
    byte[] md = mdInst.digest();
    // 把密文转换成十六进制的字符串形式
    int j = md.length;
    char str[] = new char[j * 2];
    int k = 0;
    for (int i = 0; i < j; i++) {
      byte byte0 = md[i];
      str[k++] = hexDigits[byte0 >>> 4 & 0xf];
      str[k++] = hexDigits[byte0 & 0xf];
    }
    return new String(str);
  }
  
  private Utils(){}
}
