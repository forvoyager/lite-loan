package com.etl;

import com.etl.base.common.enums.Cluster;
import com.etl.user.UserServiceApplication;
import com.etl.user.common.model.UserModel;
import com.etl.user.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-27 18:14
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE, classes = UserServiceApplication.class)
public class UserTest {
  
  @Autowired
  private IUserService userService;
  
  @Test
  public void test_query() throws Exception{
    UserModel user = userService.selectById(1, Cluster.master);
    System.out.println(user);
  }
  
}
