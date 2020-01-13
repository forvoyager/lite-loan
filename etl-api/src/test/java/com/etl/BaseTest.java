package com.etl;

import com.etl.base.common.constant.Constants;
import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.util.HttpUtils;
import com.etl.base.common.util.Utils;

import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-13 11:24:00 <br>
 * <b>description</b>: 测试Test继承此类（不需要登陆）<br>
 */
public class BaseTest {

  // 通用参数
  protected Map allParams = Utils.newHashMap(
          Constants.USER_ID, "",
          Constants.TOKEN, "",
          Constants.CHANNEL, AccessChannel.ANDROID.getCode(),
          Constants.CHANNEL_VERSION, "1.0"
  );

  // 自定义参数
  protected Map params = Utils.newHashMap();

  protected String post(String url, Map param) throws Exception{
    param.putAll(this.allParams);
    return HttpUtils.post(url, param);
  }

  protected String get(String url, Map param) throws Exception{
    param.putAll(this.allParams);
    return HttpUtils.get(url, param);
  }

  protected String getUrl(String uri){
    return "http://localhost:40000"+uri;
  }
}
