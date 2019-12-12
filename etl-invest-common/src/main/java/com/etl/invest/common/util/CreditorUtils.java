package com.etl.invest.common.util;

import com.etl.base.common.util.ArithUtils;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.invest.common.dto.CreditorValueDto;
import com.etl.invest.common.model.CreditorTransferModel;
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
   * @param transfer    债转信息
   * @param profitForms 债权对应的收益报表信息（保证有序，按期数升序）
   * @return
   * @throws Exception
   */
  public static CreditorValueDto valueDto(CreditorTransferModel transfer, List<ProfitFormModel> profitForms) throws Exception {
    // 默认全部转让
    return valueDto(transfer, profitForms, transfer.getTransfer_partition());
  }

  /**
   * 计算债权当前时间的价值信息
   *
   * @param transfer    债转信息
   * @param profitForms 债权对应的收益报表信息（保证有序，按期数升序）
   * @param partition   转让份数
   * @return
   * @throws Exception
   */
  public static CreditorValueDto valueDto(CreditorTransferModel transfer, List<ProfitFormModel> profitForms, int partition) throws Exception {
    AssertUtils.notNull(transfer, "债转信息不完整");
    AssertUtils.notEmpty(profitForms, "收益报表不完整");
    if (partition < 1 || partition > transfer.getTransfer_partition()) {
      Utils.throwsBizException("转让份数不正确");
    }

    // 当前时间
    long currentTime = DateUtils.currentTimeInSecond();
    long currentDate = DateUtils.currentDateInSecond();

    CreditorValueDto valueDto = new CreditorValueDto();
    valueDto.setCreditor_id(transfer.getCreditor_id());
    valueDto.setTime(currentTime);

    if (transfer.getStatus().intValue() != 0) {
      // 已结束的债转，各种值都为0
      return valueDto;
    }

    Integer firstPeriod = null;             // 第一期
    Integer currentPeriod = null;           // 当期
    Integer repayPeriod = 0;                // 已还期数
    Integer lastPeriod = null;              // 最后一期

    // 期数与收益报表 Map<期数, 收益报表>
    Map<Integer, ProfitFormModel> profitFormMap = new HashMap<>();
    for (ProfitFormModel pfm : profitForms) {
      profitFormMap.put(pfm.getPeriod(), pfm);
      if(firstPeriod == null){
        firstPeriod = pfm.getPeriod();
      }
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

    // 按份数partition折算后的待回收本金、利息
    long part_unpaid_capital = (long)ArithUtils.discount(transfer.getTransfer_partition(), transfer.getUnpaid_capital(), partition);
    long part_unpaid_interest = (long)ArithUtils.discount(transfer.getTransfer_partition(), transfer.getUnpaid_interest(), partition);

    valueDto.setPartition(partition);
    valueDto.setUnpaid_capital(part_unpaid_capital);
    valueDto.setUnpaid_interest(part_unpaid_interest);
    valueDto.setFairValue(valueDto.getUnpaid_capital());
    valueDto.setSurplus_days(DateUtils.intervalDay(currentTime, profitFormMap.get(lastPeriod).getPlan_repayment_time()));
    valueDto.setDiscount_apr(transfer.getDiscount_apr());

    /*
    根据 折/溢价率 算出【基准价格】
     */
    final long base_price = valueDto.getFairValue() + (long)(ArithUtils.mul(valueDto.getFairValue(), valueDto.getDiscount_apr()));

    /*
    购买价格（购买人支出的钱） = 基准价格
    到账金额（出让人到账金额） = 基准价格
    按不同场景，最终的价格会有浮动，详见：com.etl.invest.common.dto.CreditorValueDto
     */
    long buy_price = base_price;
    long incoming_price = base_price;
    ProfitFormModel currentProfitForm = profitFormMap.get(currentPeriod);

    /*
    已还：
    债权出让人已经收到了还款。
    [当期开始, 当日) 这段时间的利息归债权出让人所有。
    [当日, 下一期还款日之前]这段时间的利息归承接人所有。
     */
    if( repayPeriod >= currentPeriod.intValue() ){
      // 提前还款，需要退回给承接人的利息
      long offset_interest = 0;
      // 提前还了多期
      if(repayPeriod - currentPeriod > 1){
        for(Integer period = currentPeriod+1; period<=repayPeriod; period++ ){
          offset_interest += profitFormMap.get(period).getInterest();
        }
      }

      // 按份数partition折算后的利息
      offset_interest = (long)ArithUtils.discount(transfer.getOrigin_partition(), offset_interest, partition);

      // 本期天数（本期还款时间减一个月）
      int days = DateUtils.intervalDay(
              DateUtils.addMonth(currentProfitForm.getPlan_repayment_time(), -1),
              currentProfitForm.getPlan_repayment_time());
      // 本期（债权）剩余天数
      int surplusDays = DateUtils.intervalDay(currentDate, currentProfitForm.getPlan_repayment_time());
      // 本期剩余天数对应的利息
      long surplusDaysInterest = (long)ArithUtils.discount(days, currentProfitForm.getInterest(), surplusDays);
      // 按份数折算
      surplusDaysInterest = (long)ArithUtils.discount(transfer.getOrigin_partition(), surplusDaysInterest, partition);
      offset_interest += surplusDaysInterest;

      // 退回利息，承接人少出一部分钱
      buy_price -= offset_interest;
      incoming_price -= offset_interest;
    }

    /*
    逾期未还：
    前面几期、本期、后面的期数没有还。
    此时发起债转，相当于把未还的债权进行折价后卖出。
    未还的金额已经折算在基准价格里，无需额外计算。
     */
    else {
      // 未还，需要承接人垫付的利息
      long offset_interest = 0;
      // 多期逾期
      if(currentPeriod - repayPeriod > 1){
        for(Integer period = repayPeriod+1; period<currentPeriod; period++ ){
          offset_interest += profitFormMap.get(period).getInterest();
        }
      }
      // 按份数partition折算后的利息
      offset_interest = (long)ArithUtils.discount(transfer.getOrigin_partition(), offset_interest, partition);

      // 本期天数（本期还款时间减一个月）
      long currentProfitFormStartTime = DateUtils.addMonth(currentProfitForm.getPlan_repayment_time(), -1);
      int days = DateUtils.intervalDay(currentProfitFormStartTime, currentProfitForm.getPlan_repayment_time());
      // 本期（债权）持有天数
      int holdDays = DateUtils.intervalDay(currentProfitFormStartTime, currentDate);
      // 本期持有天数对应的利息
      long holdDaysInterest = (long)ArithUtils.discount(days, currentProfitForm.getInterest(), holdDays);
      // 按份数折算
      holdDaysInterest = (long)ArithUtils.discount(transfer.getOrigin_partition(), holdDaysInterest, partition);
      // 垫付的利息 = 未还期数的利息 + 本期出让人持有天数对应的利息
      offset_interest += holdDaysInterest;

      // 退回利息，承接人少出一部分钱
      buy_price += offset_interest;
      incoming_price += offset_interest;
    }

    // 出让人扣除管理费
    long manage = (long)(valueDto.getFairValue()*0.005);
    incoming_price = incoming_price - manage;

    valueDto.setBase_price(base_price);
    valueDto.setManage(manage);
    valueDto.setIncoming_price(incoming_price);
    valueDto.setBuy_price(buy_price);
    valueDto.setProfit_price(valueDto.getUnpaid_interest());

    /*
    根据承接人投入的本金和收益，计算收益率
    本金=购买价格
    收益=待收收益
     */
    double apr = ArithUtils.round(ArithUtils.div((365*valueDto.getProfit_price()), (buy_price*valueDto.getSurplus_days())), 3);
    valueDto.setApr(apr);

    return valueDto;
  }

}
