package com.etl.invest.common.util;

import com.etl.base.common.util.ArithUtils;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.invest.common.dto.CreditorValueDto;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.common.model.ProfitFormModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com <br>
 * <b>time</b>: 2019-11-19 15:57 <br>
 * <b>description</b>: 债权相关工具类
 */
public final class CreditorUtils {

  /**
   * 计算债权当前时间的价值信息
   *
   * @param creditor    债权信息
   * @param profitForms 债权对应的收益报表信息
   * @return
   * @throws Exception
   */
  public static CreditorValueDto valueDto(CreditorModel creditor, List<ProfitFormModel> profitForms) throws Exception {
    // 默认全部转让
    return valueDto(creditor, profitForms, creditor.getPartition());
  }

  /**
   * 计算债权当前时间的价值信息
   *
   * @param creditor    债权信息
   * @param profitForms 债权对应的收益报表信息
   * @param partition   转让份数
   * @return
   * @throws Exception
   */
  public static CreditorValueDto valueDto(CreditorModel creditor, List<ProfitFormModel> profitForms, int partition) throws Exception {
    AssertUtils.notNull(creditor, "债权信息不完整");
    AssertUtils.notEmpty(profitForms, "收益报表不完整");
    AssertUtils.isTrue(creditor.getPeriod().intValue() == profitForms.size(), "收益报表不完整");
    if (partition < 1 || partition > creditor.getPartition()) {
      Utils.throwsBizException("转让份数不正确");
    }

    // 当前时间
    long currentTime = DateUtils.currentTimeInSecond();
    long currentDate = DateUtils.currentDateInSecond();

    CreditorValueDto valueDto = new CreditorValueDto();
    valueDto.setCreditor_id(creditor.getId());
    valueDto.setTime(currentTime);

    int creditor_status = creditor.getStatus().intValue();
    if (creditor_status == -2 || creditor_status == -1) {
      // 已结束的债权，各种值都为0
      return valueDto;
    }

    Integer currentPeriod = null;       // 当期
    int repayPeriod = 0;                // 已还期数
    int surplusPeriod = 0;              // 剩余期数
    long last_repayment_time = 0;       // 最后一期还款时间

    // 期数与收益报表 Map<期数, 收益报表>
    Map<Integer, ProfitFormModel> profitFormPeriodMap = new HashMap<>();
    for (ProfitFormModel pfm : profitForms) {
      profitFormPeriodMap.put(pfm.getPeriod(), pfm);
      last_repayment_time = pfm.getPlan_repayment_time();

      if (currentPeriod == null && currentDate <= pfm.getPlan_repayment_time()) {
        currentPeriod = pfm.getPeriod();
      }

      // 已还
      if (pfm.getStatus().intValue() == 1) {
        repayPeriod += 1;
      }

      // 未还
      else {
        surplusPeriod += 1;
      }
    }

    AssertUtils.notNull(currentPeriod, "本期债权信息有误");

//    if(pfm.getCapital() > 0){
//      // 按份数partition折算后的本金
//      long partition_capital = discount(pfm.getCapital(), creditor.getPartition(), partition);
//      offset_capital += partition_capital;
//      unpaid_capital += partition_capital;
//    }
//    if(pfm.getInterest() > 0){
//      // 按份数partition折算后的利息
//      long partition_interest = discount(pfm.getInterest(), creditor.getPartition(), partition);
//      offset_interest += partition_interest;
//      unpaid_interest += partition_interest;
//    }

    ProfitFormModel profitForm = null;
    long unpaid_capital = 0;            // 待回收本金（分）
    long unpaid_interest = 0;           // 待回收利息（分）
    long offset_capital = 0;            // 由买方代偿（逾期还款）/由卖方退回（提前还款） 的本金（分）
    long offset_interest = 0;           // 由买方代偿（逾期还款）/由卖方退回（提前还款） 的利息（分）

    /*
    本期已还：
    债权出让人已经收到了还款
    [当期开始, 当前日期) 这段时间的收益归债权出让人所有。
    [当前日期, 下一期还款日之前]这段时间的收益需要转给债权承接人。
     */
    if( repayPeriod == currentPeriod.intValue() ){
      profitForm = profitFormPeriodMap.get(currentPeriod);
      // 本期天数（如果是第一期，按30天算）
      int days = profitForm.getPeriod().intValue() == 1
              ? 30
              : DateUtils.intervalDay(profitFormPeriodMap.get(currentPeriod-1).getPlan_repayment_time(), profitForm.getPlan_repayment_time());
      // 本期债权剩余天数。
      int surplusDays = DateUtils.intervalDay(currentDate, profitForm.getPlan_repayment_time());
      // 剩余天数对应的利息（转给债权承接人）
      long surplusDaysInterest = discount(days, profitForm.getInterest(), surplusDays);

    }

    /*
    逾期未还：
    前面几期、本期、后面的期数都没有还
    债权承接人垫付 当前日期 之前应还的本金和利息给债权出让人
    借款人对逾期债权还款时，收益和本金都归债权承接人
     */
    if( repayPeriod < currentPeriod ){
    }

    /*
    提前还款：

     */
    if( repayPeriod > currentPeriod ){
    }

    valueDto.setPartition(partition);
    valueDto.setUnpaid_capital(unpaid_capital);
    valueDto.setUnpaid_interest(unpaid_interest);
    valueDto.setSurplus_days(DateUtils.intervalDay(currentTime, last_repayment_time));
    valueDto.setDiscount_apr(-0.02); // 默认都折价2%转让
    valueDto.setFairValue(unpaid_capital + unpaid_interest);

    long manage = (long)(ArithUtils.round(ArithUtils.mul(valueDto.getFairValue(), 0.005), 2)*100);
    long trade_price = valueDto.getFairValue() + (long)(ArithUtils.round(ArithUtils.mul(valueDto.getFairValue(), valueDto.getDiscount_apr()), 2)*100);
    long incoming_price = trade_price - manage;
    valueDto.setTrade_price(trade_price);
    valueDto.setManage(manage);
    valueDto.setIncoming_price(incoming_price);
    valueDto.setBuy_price(trade_price);
    valueDto.setProfit_price(valueDto.getBuy_price() - unpaid_capital);

    double apr = ArithUtils.round(ArithUtils.div((12*valueDto.getProfit_price()), (unpaid_capital*surplusPeriod)), 3);
    valueDto.setApr(apr);

    return valueDto;
  }

  /**
   * 折算
   * a/a_ = b/b_
   * 已知 a, a_, b 求b_ = (b*a_)/a
   *
   * @param a
   * @param a_
   * @param b
   * @return b_
   */
  private static long discount(long a, long a_, long b){
    return (long)(ArithUtils.div((b * a_), a, 2)*100);
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
