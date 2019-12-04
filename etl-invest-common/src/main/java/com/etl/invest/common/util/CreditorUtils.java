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
   * @param profitForms 债权对应的收益报表信息（保证有序，按期数升序）
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
   * @param profitForms 债权对应的收益报表信息（保证有序，按期数升序）
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

    int totalPeriod = profitForms.size();   // 总期数
    Integer currentPeriod = null;           // 当期
    Integer repayPeriod = 0;             // 已还期数
    Integer lastPeriod = null;              // 最后一期

    // 期数与收益报表 Map<期数, 收益报表>
    Map<Integer, ProfitFormModel> profitFormMap = new HashMap<>();
    for (ProfitFormModel pfm : profitForms) {
      profitFormMap.put(pfm.getPeriod(), pfm);
      lastPeriod = pfm.getPeriod();

      if (currentPeriod == null && currentDate <= pfm.getPlan_repayment_time()) {
        currentPeriod = pfm.getPeriod();
      }

      // 已还
      if (pfm.getStatus().intValue() == 1) {
        repayPeriod = pfm.getPeriod();
      }

      // 未还
      else {

      }
    }

    AssertUtils.notNull(currentPeriod, "本期债权信息有误");

    ProfitFormModel currentProfitForm = profitFormMap.get(currentPeriod);
    long unpaid_capital = 0;            // 待回收本金（分）
    long unpaid_interest = 0;           // 待回收利息（分）

    // 计算待回收本息
    ProfitFormModel profitForm = null;
    for(Integer period = repayPeriod+1; period<=lastPeriod; period++ ){
      profitForm = profitFormMap.get(period);
      if(profitForm.getCapital() > 0){
        // 按份数partition折算后的本金
        unpaid_capital += discount(creditor.getPartition(), profitForm.getCapital(), partition);
      }
      if(profitForm.getInterest() > 0){
        // 按份数partition折算后的利息
        unpaid_interest += discount(creditor.getPartition(), profitForm.getInterest(), partition);
      }
    }

    valueDto.setPartition(partition);
    valueDto.setUnpaid_capital(unpaid_capital);
    valueDto.setUnpaid_interest(unpaid_interest);
    valueDto.setFairValue(unpaid_capital + unpaid_interest);
    valueDto.setSurplus_days(DateUtils.intervalDay(currentTime, profitFormMap.get(lastPeriod).getPlan_repayment_time()));
    valueDto.setDiscount_apr(-0.02); // 默认都折价2%转让

    /*
    根据 折/溢价率 算出【基准价格】
     */
    final long base_price = valueDto.getFairValue() + (long)(ArithUtils.mul(valueDto.getFairValue(), valueDto.getDiscount_apr()));

    /*
    默认：购买价格（购买人支出的钱）=基准价格
    在这个价格的基础上按下面不同场景（本期已还、逾期未还、提前还款），最终的购买价格会有浮动：
    已还：本期或后面几期已还，出让人退还[当日, 最后一期已还得最后一天]的利息给债权承接人
    未还：前面几期或本期、后面的期数都没有还，债权承接人先垫付待收本息给出让人
     */
    long buy_price = base_price;

    /*
    到账金额（出让人到账金额）
     */
    long incoming_price = base_price;

    /*
    本期已还：
    债权出让人已经收到了还款
    [当期开始, 当前日期) 这段时间的利息归债权出让人所有。
    [当前日期, 下一期还款日之前]这段时间的利息需要转给债权承接人。
     */
    if( repayPeriod >= currentPeriod.intValue() ){
      profitForm = null;
      long offset_interest = 0;
      // 提前还了多期
      if(repayPeriod-currentPeriod > 1){
        for(Integer period = currentPeriod+1; period<=repayPeriod; period++ ){
          profitForm = profitFormMap.get(period);
          if(profitForm.getInterest() > 0){
            offset_interest += profitForm.getInterest();
          }
        }
      }

      // 本期天数（本期还款时间减一个月）
      int days = DateUtils.intervalDay(
              DateUtils.addMonth(currentProfitForm.getPlan_repayment_time(), -1),
              currentProfitForm.getPlan_repayment_time());
      // 本期（债权）剩余天数
      int surplusDays = DateUtils.intervalDay(currentDate, currentProfitForm.getPlan_repayment_time());
      // 本期剩余天数对应的利息
      long surplusDaysInterest = discount(days, currentProfitForm.getInterest(), surplusDays);
      //
      offset_interest += surplusDaysInterest;
      // 因本期已还，本期利息已经在出让人手里了，本期剩余天数对应的利息需要退给承接人，这部分钱承接人不用出了，需减去
      buy_price = buy_price - offset_interest;
      // 出让人到账也相应的减去本期剩余天数对应的利息
      incoming_price = incoming_price - offset_interest;
    }

    /*
    逾期未还：
    前面几期、本期、后面的期数都没有还
     */
    if( repayPeriod < currentPeriod ){
      // 根据公允价值计算的 基准价格 已经包含的未还部分的资金，此处无需额外处理。
      buy_price = buy_price;
      incoming_price = incoming_price;
    }

    // 出让人扣除管理费
    long manage = (long)(valueDto.getFairValue()*0.005);
    incoming_price = incoming_price - manage;

    valueDto.setBase_price(base_price);
    valueDto.setManage(manage);
    valueDto.setIncoming_price(incoming_price);
    valueDto.setBuy_price(buy_price);
    valueDto.setProfit_price(unpaid_interest);

    double apr = ArithUtils.round(ArithUtils.div((12*valueDto.getProfit_price()), (buy_price*(totalPeriod-repayPeriod))), 3);
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
    return (long)(ArithUtils.div((b * a_), a, 2));
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
