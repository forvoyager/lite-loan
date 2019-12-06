package com.etl.invest.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-06 11:12:38 <br>
 * <b>description</b>: 债权转让信息 模型 <br>
 */
public class CreditorTransferModel extends BaseModel {

  public static final String ID = "id";
  public static final String USER_ID = "user_id";
  public static final String CREDITOR_ID = "creditor_id";
  public static final String BORROW_ID = "borrow_id";
  public static final String PARTITION = "partition";
  public static final String FROZEN_PARTITION = "frozen_partition";
  public static final String DISCOUNT_APR = "discount_apr";
  public static final String UNPAID_CAPITAL = "unpaid_capital";
  public static final String UNPAID_INTEREST = "unpaid_interest";
  public static final String STATUS = "status";
  public static final String START_TIME = "start_time";
  public static final String END_TIME = "end_time";

  /**
   * 债转ID
   */
  private Long id;
  /**
   * 债权人ID
   */
  private Long user_id;
  /**
   * 债权ID
   */
  private Long creditor_id;
  /**
   * 标的id
   */
  private Long borrow_id;
  /**
   * 转让份数
   */
  private Integer partition;
  /**
   * 冻结份数
   */
  private Integer frozen_partition;
  /**
   * 折价率，小于0表示折价，等于0表示原价，大于0表示溢价。如-0.02表示折价2%，0.03表示溢价3%
   */
  private Double discount_apr;
  /**
   * 待回收本金（分）
   */
  private Long unpaid_capital;
  /**
   * 待回收利息（分）
   */
  private Long unpaid_interest;
  /**
   * 状态 -2已取消 -1已转让 0转让中
   */
  private Integer status;
  /**
   * 债权开始日期
   */
  private Long start_time;
  /**
   * 债权结束日期
   */
  private Long end_time;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  public Long getCreditor_id() {
    return creditor_id;
  }

  public void setCreditor_id(Long creditor_id) {
    this.creditor_id = creditor_id;
  }

  public Long getBorrow_id() {
    return borrow_id;
  }

  public void setBorrow_id(Long borrow_id) {
    this.borrow_id = borrow_id;
  }

  public Integer getPartition() {
    return partition;
  }

  public void setPartition(Integer partition) {
    this.partition = partition;
  }

  public Integer getFrozen_partition() {
    return frozen_partition;
  }

  public void setFrozen_partition(Integer frozen_partition) {
    this.frozen_partition = frozen_partition;
  }

  public Double getDiscount_apr() {
    return discount_apr;
  }

  public void setDiscount_apr(Double discount_apr) {
    this.discount_apr = discount_apr;
  }

  public Long getUnpaid_capital() {
    return unpaid_capital;
  }

  public void setUnpaid_capital(Long unpaid_capital) {
    this.unpaid_capital = unpaid_capital;
  }

  public Long getUnpaid_interest() {
    return unpaid_interest;
  }

  public void setUnpaid_interest(Long unpaid_interest) {
    this.unpaid_interest = unpaid_interest;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getStart_time() {
    return start_time;
  }

  public void setStart_time(Long start_time) {
    this.start_time = start_time;
  }

  public Long getEnd_time() {
    return end_time;
  }

  public void setEnd_time(Long end_time) {
    this.end_time = end_time;
  }
}

