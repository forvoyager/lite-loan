package com.etl.invest.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 11:51:35 <br>
 * <b>description</b>: 投资人收益报表 模型 <br>
 */
public class ProfitFormModel extends BaseModel {

  public static final String ID = "id";
  public static final String CREDITOR_ID = "creditor_id";
  public static final String USER_ID = "user_id";
  public static final String BORROW_ID = "borrow_id";
  public static final String STATUS = "status";
  public static final String CAPITAL = "capital";
  public static final String INTEREST = "interest";
  public static final String PERIOD = "period";

  /**
   * 收益ID
   */
  private Long id;
  /**
   * 债权ID
   */
  private Long creditor_id;
  /**
   * 债权人
   */
  private Long user_id;
  /**
   * 标的id
   */
  private Long borrow_id;
  /**
   * 状态 -1无效 0未还 1已还
   */
  private Integer status;
  /**
   * 本金（分）
   */
  private Long capital;
  /**
   * 利息（分）
   */
  private Long interest;
  /**
   * 期数
   */
  private Integer period;
  /**
   * 应还款日期
   */
  private Long plan_repayment_time;

  public Long getId() {
    return this.id;
  }
  public ProfitFormModel setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getCreditor_id() {
    return this.creditor_id;
  }
  public ProfitFormModel setCreditor_id(Long creditor_id) {
    this.creditor_id = creditor_id;
    return this;
  }

  public Long getUser_id() {
    return this.user_id;
  }
  public ProfitFormModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Long getBorrow_id() {
    return this.borrow_id;
  }
  public ProfitFormModel setBorrow_id(Long borrow_id) {
    this.borrow_id = borrow_id;
    return this;
  }

  public Integer getStatus() {
    return this.status;
  }
  public ProfitFormModel setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public Long getCapital() {
    return this.capital;
  }
  public ProfitFormModel setCapital(Long capital) {
    this.capital = capital;
    return this;
  }

  public Long getInterest() {
    return this.interest;
  }
  public ProfitFormModel setInterest(Long interest) {
    this.interest = interest;
    return this;
  }

  public Integer getPeriod() {
    return this.period;
  }
  public ProfitFormModel setPeriod(Integer period) {
    this.period = period;
    return this;
  }

  public Long getPlan_repayment_time() {
    return plan_repayment_time;
  }

  public ProfitFormModel setPlan_repayment_time(Long plan_repayment_time) {
    this.plan_repayment_time = plan_repayment_time;
    return this;
  }
}

