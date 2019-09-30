package com.etl;

import com.etl.base.common.util.DateUtil;
import com.etl.base.common.util.JsonUtil;
import com.etl.borrow.BorrowServiceApplication;
import com.etl.borrow.common.enums.RepaymentMode;
import com.etl.borrow.common.model.BorrowModel;
import com.etl.borrow.service.IBorrowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 11:57
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = BorrowServiceApplication.class)
public class BorrowTest {
  
  @Autowired
  private IBorrowService borrowService;
  
  @Test
  public void test_insert() throws Exception{
    long current = DateUtil.currentTimeInSecond();
    BorrowModel borrowModel = new BorrowModel();
    borrowModel.setUser_id(1L);
    borrowModel.setTitle("借款测试_"+System.currentTimeMillis());
    borrowModel.setAmount(1000000L*100); // 分
    borrowModel.setPeriod(12);
    borrowModel.setApr(10.2);
    borrowModel.setPartion_amount(100*100); // 分
    borrowModel.setRepayment_type(RepaymentMode.LAST_CAPITAL.getCode());
    borrowModel.setCreate_time(current);
    borrowModel.setUpdate_time(current);
    borrowModel = borrowService.insert(borrowModel);
    System.out.println(JsonUtil.parseJson(borrowModel));
  }
  
}
