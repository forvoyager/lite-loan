package com.etl;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.util.JsonUtil;
import com.etl.invest.InvestServiceApplication;
import com.etl.invest.common.model.InvestRecordModel;
import com.etl.invest.service.IInvestRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:14
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = InvestServiceApplication.class)
public class InvestTest {
  
  @Autowired
  private IInvestRecordService investRecordService;
  
  @Test
  public void test_investRecord() throws Exception{
    InvestRecordModel investRecord = new InvestRecordModel();
    investRecord.setUser_id(1L);
    investRecord.setBorrow_id(2L);
    investRecord.setPartion(5);
    investRecord.setInvest_amount(investRecord.getPartion()*(100L*100));
    investRecord.setStatus(0);
    investRecord.setChannel(AccessChannel.PC.getCode());
    investRecord = investRecordService.insert(investRecord);
    System.out.println(JsonUtil.parseJson(investRecord));
  }
  
}
