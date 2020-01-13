package com.etl;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.util.HttpUtils;
import com.etl.base.common.util.Utils;

import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-13 11:24:00 <br>
 * <b>description</b>: <br>
 */
public class BaseTest {

  // 通用参数
  private Map allParams = Utils.newHashMap(
          "c", AccessChannel.ANDROID.getCode(),
          "v", "",
          "user_id", "",
          "token", ""
  );

  // 自定义参数
  protected Map params = Utils.newHashMap();

  protected String post(String url, Map param) throws Exception{
    this.allParams.putAll(param);
    return HttpUtils.post(url, this.allParams);
  }

  protected String get(String url, Map param) throws Exception{
    this.allParams.putAll(param);
    return HttpUtils.get(url, this.allParams);
  }

  protected String getUrl(String uri){
    return "http://localhost:40000"+uri;
  }
}
