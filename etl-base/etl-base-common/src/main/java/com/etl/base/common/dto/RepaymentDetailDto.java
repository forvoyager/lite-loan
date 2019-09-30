package com.etl.base.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:10
 * @Description: 还款信息
 */
public class RepaymentDetailDto implements Serializable {

  private static final long serialVersionUID = 20180401175300L;

  /**
   * 应还本息总和
   */
  private double totalAmount;
  /**
   * 本金
   */
  private double capital;
  /**
   * 总利息
   */
  private double interest;
  /**
   * 总期数
   */
  private int period;
  /**
   * 年利率
   */
  private double yearRate;
  /**
   * 每月还款计划
   */
  private List<RepaymentPerMonthDto> repaymentPlan = new ArrayList<RepaymentPerMonthDto>();

  /**
   * 获取对应期数的还款日期
   *
   * @param period
   * @return 还款日期秒数
   */
  public long getRepaymentDate(int period) {
    if (period < repaymentPlan.size() || period > repaymentPlan.size()) {
      throw new IllegalArgumentException("can't find period:" + period);
    }

    return repaymentPlan.get(period).getRepaymentDate();
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public double getCapital() {
    return capital;
  }

  public void setCapital(double capital) {
    this.capital = capital;
  }

  public double getInterest() {
    return interest;
  }

  public void setInterest(double interest) {
    this.interest = interest;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public double getYearRate() {
    return yearRate;
  }

  public void setYearRate(double yearRate) {
    this.yearRate = yearRate;
  }

  public List<RepaymentPerMonthDto> getRepaymentPlan() {
    return repaymentPlan;
  }

  public void setRepaymentPlan(List<RepaymentPerMonthDto> repaymentPlan) {
    this.repaymentPlan = repaymentPlan;
  }

  public void addRepaymentPlan(RepaymentPerMonthDto plan) {
    if (null == plan) {
      throw new IllegalArgumentException("repayment plan can not be null");
    }
    if (null == this.repaymentPlan) {
      repaymentPlan = new ArrayList<RepaymentPerMonthDto>();
    }
    this.repaymentPlan.add(plan);
  }

}
