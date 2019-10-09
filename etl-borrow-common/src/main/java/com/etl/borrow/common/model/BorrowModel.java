package com.etl.borrow.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 09:30:12 <br>
 * <b>description</b>: 借款项目 模型 <br>
 */
public class BorrowModel extends BaseModel {

  public static final String BORROW_ID = "borrow_id";
  public static final String USER_ID = "user_id";
  public static final String TITLE = "title";
  public static final String STATUS = "status";
  public static final String AMOUNT = "amount";
  public static final String PERIOD = "period";
  public static final String APR = "apr";
  public static final String PARTION_AMOUNT = "partion_amount";
  public static final String REPAYMENT_MODE = "repayment_mode";
  public static final String INVEST_START_TIME = "invest_start_time";
  public static final String INVEST_END_TIME = "invest_end_time";

  /**
   * 标的id
   */
  private Long borrow_id;
  /**
   * 借款人id
   */
  private Long user_id;
  /**
   * 标题
   */
  private String title;
  /**
   * 状态
   */
  private Integer status;
  /**
   * 借款金额（分）
   */
  private Long amount;
  /**
   * 借款期数
   */
  private Integer period;
  /**
   * 利率
   */
  private Double apr;
  /**
   * 每份金额（分）
   */
  private Integer partion_amount;
  /**
   * 还款方式
   */
  private Integer repayment_mode;
  /**
   * 投标开始时间（秒）
   */
  private Long invest_start_time;
  /**
   * 投标结束时间（秒）
   */
  private Long invest_end_time;

  public Long getBorrow_id() {
    return this.borrow_id;
  }
  public BorrowModel setBorrow_id(Long borrow_id) {
    this.borrow_id = borrow_id;
    return this;
  }

  public Long getUser_id() {
    return this.user_id;
  }
  public BorrowModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public String getTitle() {
    return this.title;
  }
  public BorrowModel setTitle(String title) {
    this.title = title;
    return this;
  }

  public Integer getStatus() {
    return this.status;
  }
  public BorrowModel setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public Long getAmount() {
    return this.amount;
  }
  public BorrowModel setAmount(Long amount) {
    this.amount = amount;
    return this;
  }

  public Integer getPeriod() {
    return this.period;
  }
  public BorrowModel setPeriod(Integer period) {
    this.period = period;
    return this;
  }

  public Double getApr() {
    return this.apr;
  }
  public BorrowModel setApr(Double apr) {
    this.apr = apr;
    return this;
  }

  public Integer getPartion_amount() {
    return this.partion_amount;
  }
  public BorrowModel setPartion_amount(Integer partion_amount) {
    this.partion_amount = partion_amount;
    return this;
  }

  public Integer getRepayment_mode() {
    return this.repayment_mode;
  }
  public BorrowModel setRepayment_mode(Integer repayment_mode) {
    this.repayment_mode = repayment_mode;
    return this;
  }

  public Long getInvest_start_time() {
    return this.invest_start_time;
  }
  public BorrowModel setInvest_start_time(Long invest_start_time) {
    this.invest_start_time = invest_start_time;
    return this;
  }

  public Long getInvest_end_time() {
    return this.invest_end_time;
  }
  public BorrowModel setInvest_end_time(Long invest_end_time) {
    this.invest_end_time = invest_end_time;
    return this;
  }

}

