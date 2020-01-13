package com.etl;

import com.etl.base.common.dto.ResultDto;
import com.etl.base.common.enums.ResultCodeEnum;
import com.etl.base.common.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-13 17:49:00 <br>
 * <b>description</b>: <br>
 */
public class InvestTest extends BaseSigninTest {

  @Test
  public void bid() throws Exception{
    params.put("borrow_id", 4);
    params.put("amount", 100*100); // 投100块
    String result = this.post(getUrl("/invest/bid"), params);
    ResultDto<Map<String, Long>> resultDto = JsonUtils.parseObject(result, new TypeReference<ResultDto<Map<String, Long>>>(){});
    Assert.assertTrue(ResultCodeEnum.SUCCESS.getCode().equals(resultDto.getCode()));
  }

}
