package com.etl.invest.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-13 16:59:43 <br>
 * <b>description</b>: 债权转让信息 模型 <br>
 */
public class CreditorTransferModel extends BaseModel {

  public static final String ID = "id";
  public static final String USER_ID = "user_id";
  public static final String CREDITOR_ID = "creditor_id";
  public static final String BORROW_ID = "borrow_id";
  public static final String ORIGINAL_PARTITION = "original_partition";
  public static final String TRANSFER_PARTITION = "transfer_partition";
  public static final String AVAILABLE_PARTITION = "available_partition";
  public static final String DISCOUNT_APR = "discount_apr";
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
   * 原始债权份数
   */
  private Integer original_partition;
  /**
   * 转让份数
   */
  private Integer transfer_partition;
  /**
   * 可购买份数
   */
  private Integer available_partition;
  /**
   * 折价率，小于0表示折价，等于0表示原价，大于0表示溢价。如-0.02表示折价2%，0.03表示溢价3%
   */
  private Double discount_apr;
  /**
   * 状态 -2已取消 -1已转让 0转让中
   */
  private Integer status;

  public Long getId() {
    return id;
  }

  public CreditorTransferModel setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUser_id() {
    return user_id;
  }

  public CreditorTransferModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Long getCreditor_id() {
    return creditor_id;
  }

  public CreditorTransferModel setCreditor_id(Long creditor_id) {
    this.creditor_id = creditor_id;
    return this;
  }

  public Long getBorrow_id() {
    return borrow_id;
  }

  public CreditorTransferModel setBorrow_id(Long borrow_id) {
    this.borrow_id = borrow_id;
    return this;
  }

  public Integer getOriginal_partition() {
    return original_partition;
  }

  public CreditorTransferModel setOriginal_partition(Integer original_partition) {
    this.original_partition = original_partition;
    return this;
  }

  public Integer getTransfer_partition() {
    return transfer_partition;
  }

  public CreditorTransferModel setTransfer_partition(Integer transfer_partition) {
    this.transfer_partition = transfer_partition;
    return this;
  }

  public Integer getAvailable_partition() {
    return available_partition;
  }

  public CreditorTransferModel setAvailable_partition(Integer available_partition) {
    this.available_partition = available_partition;
    return this;
  }

  public Double getDiscount_apr() {
    return discount_apr;
  }

  public CreditorTransferModel setDiscount_apr(Double discount_apr) {
    this.discount_apr = discount_apr;
    return this;
  }

  public Integer getStatus() {
    return status;
  }

  public CreditorTransferModel setStatus(Integer status) {
    this.status = status;
    return this;
  }
}

