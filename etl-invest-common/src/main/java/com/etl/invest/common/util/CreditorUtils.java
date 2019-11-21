package com.etl.invest.common.util;

import com.etl.base.common.util.ArithUtils;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.invest.common.dto.CreditorValueDto;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.common.model.ProfitFormModel;

import java.util.List;

/**
 * <b>author</b>: forvoyager@outlook.com <br>
 * <b>time</b>: 2019-11-19 15:57 <br>
 * <b>description</b>: 债权相关工具类
 */
public final class CreditorUtils {

  /**
   * 债权价值信息
   *
   * @param creditor 债权信息
   * @param profitForms 债权对应的收益报表信息
   * @return
   * @throws Exception
   */
  public static CreditorValueDto valueDto(CreditorModel creditor, List<ProfitFormModel> profitForms) {

    AssertUtils.notNull(creditor, "债权信息不完整");
    AssertUtils.notEmpty(profitForms, "收益报表不完整");

    long current = DateUtils.currentTimeInSecond();
    CreditorValueDto valueDto = new CreditorValueDto();
    valueDto.setCreditor_id(creditor.getId());
    valueDto.setTime(current);

//    // 上一期还款日期
//    long lastMonth = DateUtils.addMonth(profitForm.getPlan_repayment_time(), -1);
//    // 上一期还款日 与 本期还款日 间隔天数（不一定是30天，也可能是29、31）
//    int days = (int)DateUtils.intervalDay(lastMonth, profitForm.getPlan_repayment_time());
//
//    // 转让人当期应收利息天数 = 上一期还款时间 与 当前时间 间隔的天数
//    int interest_days = (int)DateUtils.intervalDay(lastMonth, DateUtils.currentTimeInSecond());
//
//    // 应收利息
//    long interest = (long)(ArithUtils.mul(ArithUtils.div(interest_days, days, 2), profitForm.getInterest()/100D)*100);

    return valueDto;
  }

  public static void main(String[] args) {
    CreditorModel creditorModel = new CreditorModel();
    creditorModel.setCapital(10000L);

    ProfitFormModel profitForm = new ProfitFormModel();
    profitForm.setPlan_repayment_time(DateUtils.currentDateInSecond());
    profitForm.setInterest(83L);
    profitForm.setStatus(0);
//    System.out.println(CreditorUtils.fairValue(creditorModel, profitForm));

  }

}
