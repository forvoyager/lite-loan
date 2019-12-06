package com.etl.invest.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-06 14:20:51 <br>
 * <b>description</b>: 债权转让信息 模型 <br>
 */
public class CreditorTransferModel extends BaseModel {

  public static final String ID = "id";
  public static final String USER_ID = "user_id";
  public static final String CREDITOR_ID = "creditor_id";
  public static final String BORROW_ID = "borrow_id";
  public static final String ORIGIN_PARTITION = "origin_partition";
  public static final String TRANSFER_PARTITION = "transfer_partition";
  public static final String FROZEN_PARTITION = "frozen_partition";
  public static final String DISCOUNT_APR = "discount_apr";
  public static final String UNPAID_CAPITAL = "unpaid_capital";
  public static final String UNPAID_INTEREST = "unpaid_interest";
  public static final String STATUS = "status";

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
   * 原债权份数
   */
  private Integer origin_partition;
  /**
   * 转让份数
   */
  private Integer transfer_partition;
  /**
   * 冻结份数
   */
  private Integer frozen_partition;
  /**
   * 折价率，小于0表示折价，等于0表示原价，大于0表示溢价。如-0.02表示折价2%，0.03表示溢价3%
   */
  private Double discount_apr;
  /**
   * 按转让份数计算的待回收本金（分）
   */
  private Long unpaid_capital;
  /**
   * 按转让份数计算的待回收利息（分）
   */
  private Long unpaid_interest;
  /**
   * 状态 -2已取消 -1已转让 0转让中
   */
  private Integer status;

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

  public Integer getOrigin_partition() {
    return origin_partition;
  }

  public void setOrigin_partition(Integer origin_partition) {
    this.origin_partition = origin_partition;
  }

  public Integer getTransfer_partition() {
    return transfer_partition;
  }

  public void setTransfer_partition(Integer transfer_partition) {
    this.transfer_partition = transfer_partition;
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
}

