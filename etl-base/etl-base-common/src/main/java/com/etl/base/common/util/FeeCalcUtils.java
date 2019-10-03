package com.etl.base.common.util;

import com.etl.base.common.dto.RepaymentDetailDto;
import com.etl.base.common.dto.RepaymentPerMonthDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:10
 * @Description: 各种费用计算
 */
public class FeeCalcUtils {

  private FeeCalcUtils() {
  }

  /**
   * 等额本息还款（定期付息）
   * 即借款人每月按相等的金额偿还贷款本息，其中每月贷款利息按月初剩余贷款本金计算并逐月结清。把按揭贷款的本金总额与利息总额相加，
   * 然后平均分摊到还款期限的每个月中。作为还款人，每个月还给银行固定金额，但每月还款额中的本金比重逐月递增、利息比重逐月递减。
   * <p>
   * 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
   *
   * @param capital  本金（贷款本金）
   * @param yearRate 年利率（如10%：0.1）
   * @param period   期数（还款总月数）
   * @param startDate 起息日
   * @return 根据本金、利率和期数计算的还款资金详细信息（包含每月还款计划）
   */
  public static RepaymentDetailDto averageCapitalPlusInterest(double capital, double yearRate, int period, Long startDate) {
    if(startDate == null || startDate.longValue() == 0){
      startDate = DateUtils.currentTimeInSecond();
    }
    return new AverageCapitalPlusInterest().setCapital(capital).setYearRate(yearRate).setPeriod(period).setStartDate(startDate).calc();
  }

  public static RepaymentDetailDto averageCapitalPlusInterest(double capital, double yearRate, int period) {
    return averageCapitalPlusInterest(capital, yearRate, period, null);
  }

  /**
   * 等额本金还款
   * 在还款期内把贷款数总额等分，每月偿还同等数额的本金和剩余贷款在该月所产生的利息，这样由于每月的还款本金额固定，
   * 而利息越来越少，借款人起初还款压力较大，但是随时间的推移每月还款数也越来越少。
   * <p>
   * 公式：每月偿还本息=(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
   *
   * @param capital  本金（贷款本金）
   * @param yearRate 年利率
   * @param period   期数（还款总月数）
   * @return 根据本金、利率和期数计算的还款资金详细信息（包含每月还款计划）
   */
  public static RepaymentDetailDto averageCapital(double capital, double yearRate, int period) {
    return new AverageCapital().setCapital(capital).setYearRate(yearRate).setPeriod(period).calc();
  }

  /**
   * 每月付息到期还本
   * 前面每个月支付利息
   * 最后一个月支付最后一期的利息和本金
   *
   * @param capital  本金（贷款本金）
   * @param yearRate 年利率
   * @param period   期数（还款总月数）
   * @return 根据本金、利率和期数计算的还款资金详细信息（包含每月还款计划）
   */
  public static RepaymentDetailDto averageInterest(double capital, double yearRate, int period) {
    return new AverageInterest().setCapital(capital).setYearRate(yearRate).setPeriod(period).calc();
  }

  /**
   * 每月付息到期还本
   * 前面每个月支付利息
   * 最后一个月支付最后一期的利息和本金
   *
   * @param capital  本金（贷款本金）
   * @param yearRate 年利率
   * @param period   期数（还款总月数）
   * @return 根据本金、利率和期数计算的还款资金详细信息（包含每月还款计划）
   */
  public static RepaymentDetailDto averageInterest(double capital, double yearRate, int period, long startDate) {
    return new AverageInterest().setCapital(capital).setYearRate(yearRate).setPeriod(period).setStartDate(startDate).calc();
  }

  /**
   * 等额本金还款
   * 在还款期内把贷款数总额等分，每月偿还同等数额的本金和剩余贷款在该月所产生的利息，这样由于每月的还款本金额固定，
   * 而利息越来越少，借款人起初还款压力较大，但是随时间的推移每月还款数也越来越少。
   */
  private static class AverageCapital extends CapitalCalc {

    @Override
    public Map<Integer, Double> calcPerMonthInterest() {
      return null;
    }

    @Override
    public Map<Integer, Double> calcPerMonthCapital() {
      return null;
    }

    @Override
    public double calcCapitalInterestCount() {
      return 0;
    }
  }

  /**
   * 每月付息到期还本
   * 前面每个月支付利息
   * 最后一个月支付最后一期的利息和本金
   */
  private static class AverageInterest extends CapitalCalc {

    @Override
    public Map<Integer, Double> calcPerMonthInterest() {

      double monthRate = this.getYearRate() / 12;

      // 应还总利息
      double sumInterest = ArithUtils.muls(this.getCapital(), monthRate, this.getPeriod());
      double interestPerMonth = sumInterest/this.getPeriod();
      
      Map<Integer, Double> map = new HashMap<Integer, Double>();
      BigDecimal monthInterest;
      for (int i = 1; i < this.getPeriod() + 1; i++) {
        monthInterest = new BigDecimal(interestPerMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
        map.put(i, monthInterest.doubleValue());
      }
      return map;
    }

    @Override
    public Map<Integer, Double> calcPerMonthCapital() {
      Map<Integer, Double> mapPrincipal = new HashMap<Integer, Double>();
      for (int i = 1; i < this.getPeriod() + 1; i++) {
        mapPrincipal.put(i, 0.0D);
      }
      mapPrincipal.put(this.getPeriod(), this.getCapital());
      return mapPrincipal;
    }

    @Override
    public double calcCapitalInterestCount() {
      double monthRate = this.getYearRate() / 12;

      // 应还总利息
      double sumInterest = ArithUtils.muls(this.getCapital(), monthRate, this.getPeriod());

      // 应还本息总和
      BigDecimal count = new BigDecimal(ArithUtils.add(this.getCapital(), sumInterest));
      count = count.setScale(2, BigDecimal.ROUND_HALF_UP);

      return count.doubleValue();
    }
  }

  /**
   * 等额本息还款（定期付息）
   * 即借款人每月按相等的金额偿还贷款本息，其中每月贷款利息按月初剩余贷款本金计算并逐月结清。把按揭贷款的本金总额与利息总额相加，
   * 然后平均分摊到还款期限的每个月中。作为还款人，每个月还给银行固定金额，但每月还款额中的本金比重逐月递增、利息比重逐月递减。
   */
  private static class AverageCapitalPlusInterest extends CapitalCalc {

    @Override
    protected RepaymentDetailDto calc() {
      RepaymentDetailDto rpmtDto = new RepaymentDetailDto();
      rpmtDto.setPeriod(this.period);
      rpmtDto.setCapital(this.capital);
      rpmtDto.setYearRate(this.yearRate);

      // 应还本息总和
      double capitalInterestCount = this.calcCapitalInterestCount();
      rpmtDto.setInterest(ArithUtils.sub(capitalInterestCount, this.capital));
      rpmtDto.setTotalAmount(capitalInterestCount);

      // 每月偿还的本金
      Map<Integer, Double> perMonthCapital = this.calcPerMonthCapital();
      // 每月偿还的利息
      Map<Integer, Double> perMonthInterest = this.calcPerMonthInterest();

      // 每月还款计划
      RepaymentPerMonthDto plan = null;
      double sumInterest = 0.0;
      this.startDate = DateUtils.dateInSecond(this.startDate);
      for (Integer i = 1; i < this.period + 1; i++) {
        plan = new RepaymentPerMonthDto();
        plan.setPeriod(i);
        plan.setCapital(perMonthCapital.get(i).doubleValue());
        plan.setInterest(perMonthInterest.get(i).doubleValue());
        plan.setTotalAmount(ArithUtils.div(capitalInterestCount, this.period));
        plan.setRepaymentDate(DateUtils.addMonth(this.startDate, i));
        rpmtDto.addRepaymentPlan(plan);

        sumInterest = ArithUtils.add(sumInterest, plan.getInterest());
      }

      // 利息金额以最终总利息为准
      rpmtDto.setInterest(sumInterest);
      // 总金额=本金+总利息
      rpmtDto.setTotalAmount(ArithUtils.add(rpmtDto.getCapital(), sumInterest));

      return rpmtDto;
    }

    /**
     * 等额本息计算每月偿还利息
     * 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     *
     * @return
     */
    @Override
    public Map<Integer, Double> calcPerMonthInterest() {
      Map<Integer, Double> map = new HashMap<Integer, Double>();
      double monthRate = this.getYearRate() / 12;
      BigDecimal monthInterest;
      for (int i = 1; i < this.getPeriod() + 1; i++) {
        BigDecimal multiply = new BigDecimal(this.getCapital()).multiply(new BigDecimal(monthRate));
        BigDecimal sub = new BigDecimal(Math.pow(1 + monthRate, this.getPeriod())).subtract(new BigDecimal(Math.pow(1 + monthRate, i - 1)));
        monthInterest = multiply.multiply(sub).divide(new BigDecimal(Math.pow(1 + monthRate, this.getPeriod()) - 1), 6, BigDecimal.ROUND_HALF_UP);
        monthInterest = monthInterest.setScale(2, BigDecimal.ROUND_HALF_UP);
        map.put(i, monthInterest.doubleValue());
      }
      return map;
    }

    /**
     * 等额本息计算每月偿还本金
     * （贷款本金 / 还款月数）+（本金 — 已归还本金累计额）×每月利率
     *
     * @return
     */
    @Override
    public Map<Integer, Double> calcPerMonthCapital() {
      double monthRate = this.getYearRate() / 12;
      BigDecimal monthIncome = new BigDecimal(this.getCapital())
              .multiply(new BigDecimal(monthRate * Math.pow(1 + monthRate, this.getPeriod())))
              .divide(new BigDecimal(Math.pow(1 + monthRate, this.getPeriod()) - 1), 2, BigDecimal.ROUND_UP);
      Map<Integer, Double> mapInterest = this.calcPerMonthInterest();
      Map<Integer, Double> mapPrincipal = new HashMap<Integer, Double>();

      for (Map.Entry<Integer, Double> entry : mapInterest.entrySet()) {
        mapPrincipal.put(entry.getKey(), monthIncome.subtract(new BigDecimal(entry.getValue())).doubleValue());
      }
      return mapPrincipal;
    }

    /**
     * 应还本息总和
     *
     * @return
     */
    @Override
    public double calcCapitalInterestCount() {
      double monthRate = this.getYearRate() / 12;

      // 1+月利率的period次方
      double pow_1_monthRate_m = Math.pow(1 + monthRate, this.getPeriod());

      // 每个月应还本息总和
      BigDecimal perMonthCapitalInterestCount = new BigDecimal(this.getCapital())
              .multiply(new BigDecimal(monthRate * pow_1_monthRate_m))
              .divide(new BigDecimal(pow_1_monthRate_m - 1), 2, BigDecimal.ROUND_HALF_UP);

      // 应还本息总和
      BigDecimal count = perMonthCapitalInterestCount.multiply(new BigDecimal(this.getPeriod()));
      count = count.setScale(2, BigDecimal.ROUND_HALF_UP);

      return count.doubleValue();
    }
  }

}

/**
 * 还款本息计算
 */
abstract class CapitalCalc {
  protected double capital; // 本金（贷款本金）
  protected double yearRate; // 年利率
  protected int period; // 期数（还款总月数）
  protected long startDate; // 起息日（秒）

  protected RepaymentDetailDto calc() {

    RepaymentDetailDto rpmtDto = new RepaymentDetailDto();
    rpmtDto.setPeriod(this.period);
    rpmtDto.setCapital(this.capital);
    rpmtDto.setYearRate(this.yearRate);

    // 应还本息总和
    double capitalInterestCount = this.calcCapitalInterestCount();
    rpmtDto.setInterest(ArithUtils.sub(capitalInterestCount, this.capital));
    rpmtDto.setTotalAmount(capitalInterestCount);

    // 每月偿还的本金
    Map<Integer, Double> perMonthCapital = this.calcPerMonthCapital();
    // 每月偿还的利息
    Map<Integer, Double> perMonthInterest = this.calcPerMonthInterest();

    // 每月还款计划
    RepaymentPerMonthDto plan = null;
    double sumCapital = 0.0;
    double sumInterest = 0.0;
    double sumTotalAmount = 0.0;
    this.startDate = DateUtils.dateInSecond(this.startDate);
    for (Integer i = 1; i < this.period + 1; i++) {
      plan = new RepaymentPerMonthDto();
      plan.setPeriod(i);
      plan.setCapital(perMonthCapital.get(i));
      plan.setInterest(perMonthInterest.get(i));
      plan.setTotalAmount(ArithUtils.add(plan.getCapital(), plan.getInterest()));
      plan.setRepaymentDate(DateUtils.addMonth(this.startDate, i));

      sumCapital = ArithUtils.add(sumCapital, plan.getCapital());
      sumInterest = ArithUtils.add(sumInterest, plan.getInterest());
      sumTotalAmount = ArithUtils.add(sumTotalAmount, plan.getTotalAmount());

      // 最后一期修正计算误差（计算精度导致的本金/利息金额与实际的不一致）
      if (i.intValue() == this.period) {
        double capitalError = ArithUtils.sub(this.capital, sumCapital);
        plan.setCapital(ArithUtils.add(plan.getCapital(), capitalError));

        double interestError = ArithUtils.sub(rpmtDto.getInterest(), sumInterest);
        plan.setInterest(ArithUtils.add(plan.getInterest(), interestError));

        double totalAmountError = ArithUtils.sub(rpmtDto.getTotalAmount(), sumTotalAmount);
        plan.setTotalAmount(ArithUtils.add(plan.getTotalAmount(), totalAmountError));
      }

      rpmtDto.addRepaymentPlan(plan);
    }

    return rpmtDto;
  }

  /**
   * 计算每月偿还的利息
   *
   * @return 每月偿还本金和利息, 不四舍五入，直接截取小数点最后两位
   */
  protected abstract Map<Integer, Double> calcPerMonthInterest();

  /**
   * 计算每月偿还的本金
   *
   * @return 每月偿还本金和利息, 不四舍五入，直接截取小数点最后两位
   */
  protected abstract Map<Integer, Double> calcPerMonthCapital();

  /**
   * 计算应还本息总和
   *
   * @return 每月偿还本金和利息, 不四舍五入，直接截取小数点最后两位
   */
  protected abstract double calcCapitalInterestCount();

  protected CapitalCalc() {
  }

  protected double getCapital() {
    return capital;
  }

  protected CapitalCalc setCapital(double capital) {
    this.capital = capital;
    return this;
  }

  protected double getYearRate() {
    return yearRate;
  }

  protected CapitalCalc setYearRate(double yearRate) {
    this.yearRate = yearRate;
    return this;
  }

  protected int getPeriod() {
    return period;
  }

  protected CapitalCalc setPeriod(int period) {
    this.period = period;
    return this;
  }

  protected long getStartDate() {
    return startDate;
  }

  protected CapitalCalc setStartDate(long startDate) {
    this.startDate = startDate;
    return this;
  }
}