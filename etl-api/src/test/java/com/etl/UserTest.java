package com.etl;

import com.etl.base.common.dto.ResultDto;
import com.etl.base.common.enums.ResultCodeEnum;
import com.etl.base.common.util.JsonUtils;
import com.etl.base.common.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-13 11:48:00 <br>
 * <b>description</b>: <br>
 */
public class UserTest extends BaseTest {

  @Test
  public void signup() throws Exception{
    params.put("user_role", "0");
    params.put("mobileNo", "15870185961");
    params.put("pwd", Utils.md5("123456"));
    String result = this.post(getUrl("/user/signup"), params);
    ResultDto<Map<String, Long>> resultDto = JsonUtils.parseObject(result, new TypeReference<ResultDto<Map<String, Long>>>(){});
    Assert.assertTrue(ResultCodeEnum.SUCCESS.getCode().equals(resultDto.getCode()));
    System.out.println("注册成功，用户ID：" + resultDto.getData().get("user_id"));;
  }

  @Test
  public void signin() throws Exception{
    params.put("mobileNo", "15870185961");
    params.put("pwd", Utils.md5("123456"));
    String result = this.post(getUrl("/user/signin"), params);
    ResultDto<Map<String, String>> resultDto = JsonUtils.parseObject(result, new TypeReference<ResultDto<Map<String, String>>>(){});
    Assert.assertTrue(ResultCodeEnum.SUCCESS.getCode().equals(resultDto.getCode()));

  }
}
