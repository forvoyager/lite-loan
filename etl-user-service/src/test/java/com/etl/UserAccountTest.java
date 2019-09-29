package com.etl;

import com.etl.base.common.util.DateUtil;
import com.etl.base.common.util.JsonUtil;
import com.etl.user.UserServiceApplication;
import com.etl.user.common.model.UserAccountModel;
import com.etl.user.service.IUserAccountService;
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
  
  @Test
  public void test_init() throws Exception{
    long current = DateUtil.currentTimeInSecond();
    UserAccountModel userAccount = new UserAccountModel();
    userAccount.setUser_id(1L);
    userAccount.setCreate_time(current);
    userAccount.setUpdate_time(current);
    userAccount = userAccountService.insert(userAccount);
    System.out.println(JsonUtil.parseJson(userAccount));
  }
  
}
