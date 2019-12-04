package com.etl;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.JsonUtils;
import com.etl.user.UserServiceApplication;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.model.UserAccountModel;
import com.etl.user.common.service.IRechargeService;
import com.etl.user.common.service.IUserAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-29 11:00
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE, classes = UserServiceApplication.class)
public class UserAccountTest {
  
  @Autowired
  private IUserAccountService userAccountService;
  
  @Autowired
  private IRechargeService rechargeService;
  
  @Test
  public void test_init() throws Exception{
    long current = DateUtils.currentTimeInSecond();
    UserAccountModel userAccount = new UserAccountModel();
    userAccount.setUser_id(1L);
    userAccount.setCreate_time(current);
    userAccount.setUpdate_time(current);
    userAccount = userAccountService.insert(userAccount);
    System.out.println(JsonUtils.parseJson(userAccount));
  }
  
  @Test
  public void test_frozen() throws Exception{
    userAccountService.frozen(11, 100*100, FundsOperateType.invest_frozen, RefTable.invest_record, 10);
  }

  @Test
  public void test_unfrozen() throws Exception{
    userAccountService.unfrozen(11, 100*100, FundsOperateType.invest_unfrozen, RefTable.invest_record, 10);
  }

  @Test
  public void test_pay() throws Exception{
    userAccountService.pay(11, 100*100, FundsOperateType.invest_pay, RefTable.invest_record, 10);
  }

  @Test
  public void test_recharge_initOrder() throws Exception{
    long order_id = rechargeService.initOrder(4, 30000*100, AccessChannel.ANDROID);
    rechargeService.callback(order_id, 1, "T"+System.currentTimeMillis());
  }
  
}
