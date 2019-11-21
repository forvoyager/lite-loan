package com.etl;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.util.JsonUtils;
import com.etl.invest.InvestServiceApplication;
import com.etl.invest.common.model.InvestModel;
import com.etl.invest.common.service.IInvestService;
import com.etl.invest.common.service.IRepaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:14
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = InvestServiceApplication.class)
public class InvestTest {
  
  @Resource
  private IInvestService investService;

  @Resource
  private IRepaymentService repaymentService;
  
  @Test
  public void test_invest() throws Exception{
    InvestModel invest = new InvestModel();
    invest.setUser_id(1L);
    invest.setType(1);
    invest.setBiz_id(2L);
    invest.setPartition(5);
    invest.setInvest_amount(invest.getPartition()*(100L*100));
    invest.setInvest_status(0);
    invest.setPay_status(0);
    invest.setChannel(AccessChannel.PC.getCode());
    invest = investService.insert(invest);
    System.out.println(JsonUtils.parseJson(invest));
  }

  @Test
  public void test_verifyInitInvestorForm() throws Exception{
    investService.initInvestorForm(1L);
  }

  @Test
  public void test_apply() throws Exception{
    investService.apply(11, 1, 930000, AccessChannel.ANDROID);
  }
  
  @Test
  public void test_repayment() throws Exception{
    repaymentService.repayment(2039);
  }
}
