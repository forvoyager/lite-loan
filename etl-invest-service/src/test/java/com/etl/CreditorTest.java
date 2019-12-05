package com.etl;

import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.JsonUtils;
import com.etl.base.common.util.Utils;
import com.etl.invest.InvestServiceApplication;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.common.service.ICreditorService;
import com.etl.invest.common.service.IProfitFormService;
import com.etl.invest.common.util.CreditorUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-12-04 15:04:00
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = InvestServiceApplication.class)
public class CreditorTest {

  @Resource
  private ICreditorService creditorService;

  @Resource
  private IProfitFormService profitFormService;

  @Test
  public void test_creditor() throws Exception {
    long creditor_id = 4;
    CreditorModel creditorModel = creditorService.selectById(creditor_id, Cluster.master);

    List<ProfitFormModel> profitForms = profitFormService.selectList(Utils.newHashMap(
            ProfitFormModel.CREDITOR_ID, creditor_id
    ), Cluster.master);

    System.out.println(JsonUtils.parseJson(CreditorUtils.valueDto(creditorModel, profitForms, 5)));

  }

}
