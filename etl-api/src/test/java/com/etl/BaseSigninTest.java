package com.etl;

import com.etl.base.common.constant.Constants;
import com.etl.base.common.dto.ResultDto;
import com.etl.base.common.enums.ResultCodeEnum;
import com.etl.base.common.util.JsonUtils;
import com.etl.base.common.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Before;

import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-13 17:38:00 <br>
 * <b>description</b>: 测试Test继承此类（需要登陆）<br>
 */
public class BaseSigninTest extends BaseTest {

  @Before
  public void signin() throws Exception {
    // 先登陆，获取token
    params.put("mobileNo", "15870185961"); // 手机号
    params.put("pwd", Utils.md5("123456")); // 密码
    String result = this.post(getUrl("/user/signin"), params);
    ResultDto<Map<String, String>> resultDto = JsonUtils.parseObject(result, new TypeReference<ResultDto<Map<String, String>>>(){});
    Assert.assertTrue(ResultCodeEnum.SUCCESS.getCode().equals(resultDto.getCode()));

    this.allParams.put(Constants.USER_ID, resultDto.getData().get(Constants.USER_ID));
    this.allParams.put(Constants.TOKEN, resultDto.getData().get(Constants.TOKEN));
    params.clear();
  }

}
