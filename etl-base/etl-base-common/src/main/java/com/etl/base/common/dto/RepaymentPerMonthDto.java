package com.etl.base.common.dto;

import java.io.Serializable;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:10
 * @Description: 每月还款信息
 */
public class RepaymentPerMonthDto implements Serializable {

  private static final long serialVersionUID = 20180401180600L;

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
   * 期数
   */
  private int period;
  /**
   * 还款日期
   */
  private long repaymentDate;

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

  public long getRepaymentDate() {
    return repaymentDate;
  }

  public void setRepaymentDate(long repaymentDate) {
    this.repaymentDate = repaymentDate;
  }
}
