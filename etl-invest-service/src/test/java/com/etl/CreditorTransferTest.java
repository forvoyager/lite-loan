package com.etl;

import com.etl.invest.InvestServiceApplication;
import com.etl.invest.common.service.ICreditorTransferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-06 10:42:00 <br>
 * <b>description</b>: <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = InvestServiceApplication.class)
public class CreditorTransferTest {

  @Resource
  private ICreditorTransferService creditorTransferService;

  @Test
  public void test_transfer_apply() throws Exception {
    creditorTransferService.apply(3, 198);
  }

}
