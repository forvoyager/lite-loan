package com.etl;

import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.JsonUtils;
import com.etl.base.common.util.Utils;
import com.etl.invest.InvestServiceApplication;
import com.etl.invest.common.model.CreditorTransferModel;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.common.service.ICreditorTransferService;
import com.etl.invest.common.service.IProfitFormService;
import com.etl.invest.common.util.CreditorUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

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

  @Resource
  private IProfitFormService profitFormService;

  @Test
  public void test_transfer_apply() throws Exception {
    long id = creditorTransferService.apply(3, 198);
    System.out.println("transfer id:"+id);
  }

  @Test
  public void test_transfer_calc() throws Exception{
    CreditorTransferModel transferModel = creditorTransferService.selectById(1, Cluster.slave);

    List<ProfitFormModel> profitForms = profitFormService.selectList(Utils.newHashMap(
            ProfitFormModel.CREDITOR_ID, transferModel.getCreditor_id()
    ), Cluster.master);

    System.out.println(JsonUtils.parseJson(CreditorUtils.valueDto(transferModel, profitForms, 1)));
  }

}
