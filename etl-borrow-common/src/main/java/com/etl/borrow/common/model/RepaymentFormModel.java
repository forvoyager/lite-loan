package com.etl.borrow.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 09:30:12 <br>
 * <b>description</b>: 借款项目还款报表 模型 <br>
 */
public class RepaymentFormModel extends BaseModel {

  public static final String ID = "id";
  public static final String BORROW_ID = "borrow_id";
  public static final String USER_ID = "user_id";
  public static final String PERIOD = "period";
  public static final String CAPITAL = "capital";
  public static final String INTEREST = "interest";
  public static final String MANAGE_FEE = "manage_fee";
  public static final String PLAN_REPAYMENT_TIME = "plan_repayment_time";
  public static final String ACTUAL_REPAYMENT_TIME = "actual_repayment_time";
  public static final String OVERDUE_DAYS = "overdue_days";
  public static final String OVERDUE_PENALTY = "overdue_penalty";
  public static final String REDUCTION_AMOUNT = "reduction_amount";
  public static final String ADVANCE = "advance";
  public static final String ADVANCE_TIME = "advance_time";
  public static final String STATUS = "status";

  /**
   * ID
   */
  private Long id;
  /**
   * 标的id
   */
  private Long borrow_id;
  /**
   * 借款人
   */
  private Long user_id;
  /**
   * 第几期
   */
  private Integer period;
  /**
   * 本金（分）
   */
  private Long capital;
  /**
   * 利息（分）
   */
  private Long interest;
  /**
   * 管理费（分）
   */
  private Long manage_fee;
  /**
   * 应还款日期
   */
  private Long plan_repayment_time;
  /**
   * 实际还款时间
   */
  private Long actual_repayment_time;
  /**
   * 逾期天数
   */
  private Integer overdue_days;
  /**
   * 逾期罚款（分）
   */
  private Long overdue_penalty;
  /**
   * 减免金额（分）
   */
  private Long reduction_amount;
  /**
   * 是否垫付 0否 1是
   */
  private Integer advance;
  /**
   * 垫付时间
   */
  private Long advance_time;
  /**
   * 状态 0未还 1已还
   */
  private Integer status;

  public Long getId() {
    return this.id;
  }
  public RepaymentFormModel setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getBorrow_id() {
    return this.borrow_id;
  }
  public RepaymentFormModel setBorrow_id(Long borrow_id) {
    this.borrow_id = borrow_id;
    return this;
  }

  public Long getUser_id() {
    return this.user_id;
  }
  public RepaymentFormModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Integer getPeriod() {
    return this.period;
  }
  public RepaymentFormModel setPeriod(Integer period) {
    this.period = period;
    return this;
  }

  public Long getCapital() {
    return this.capital;
  }
  public RepaymentFormModel setCapital(Long capital) {
    this.capital = capital;
    return this;
  }

  public Long getInterest() {
    return this.interest;
  }
  public RepaymentFormModel setInterest(Long interest) {
    this.interest = interest;
    return this;
  }

  public Long getManage_fee() {
    return this.manage_fee;
  }
  public RepaymentFormModel setManage_fee(Long manage_fee) {
    this.manage_fee = manage_fee;
    return this;
  }

  public Long getPlan_repayment_time() {
    return this.plan_repayment_time;
  }
  public RepaymentFormModel setPlan_repayment_time(Long plan_repayment_time) {
    this.plan_repayment_time = plan_repayment_time;
    return this;
  }

  public Long getActual_repayment_time() {
    return this.actual_repayment_time;
  }
  public RepaymentFormModel setActual_repayment_time(Long actual_repayment_time) {
    this.actual_repayment_time = actual_repayment_time;
    return this;
  }

  public Integer getOverdue_days() {
    return this.overdue_days;
  }
  public RepaymentFormModel setOverdue_days(Integer overdue_days) {
    this.overdue_days = overdue_days;
    return this;
  }

  public Long getOverdue_penalty() {
    return this.overdue_penalty;
  }
  public RepaymentFormModel setOverdue_penalty(Long overdue_penalty) {
    this.overdue_penalty = overdue_penalty;
    return this;
  }

  public Long getReduction_amount() {
    return this.reduction_amount;
  }
  public RepaymentFormModel setReduction_amount(Long reduction_amount) {
    this.reduction_amount = reduction_amount;
    return this;
  }

  public Integer getAdvance() {
    return this.advance;
  }
  public RepaymentFormModel setAdvance(Integer advance) {
    this.advance = advance;
    return this;
  }

  public Long getAdvance_time() {
    return this.advance_time;
  }
  public RepaymentFormModel setAdvance_time(Long advance_time) {
    this.advance_time = advance_time;
    return this;
  }

  public Integer getStatus() {
    return this.status;
  }
  public RepaymentFormModel setStatus(Integer status) {
    this.status = status;
    return this;
  }

}

