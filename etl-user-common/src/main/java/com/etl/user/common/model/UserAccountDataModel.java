package com.etl.user.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-29 17:46:22 <br>
 * <b>description</b>: 账户流水 模型 <br>
 */
public class UserAccountDataModel extends BaseModel {

  public static final String ID = "id";
  public static final String USER_ID = "user_id";
  public static final String AMOUNT = "amount";
  public static final String BALANCE = "balance";
  public static final String INCOME = "income";
  public static final String TYPE = "type";
  public static final String REF_ID = "ref_id";
  public static final String REF_TABLE = "ref_table";

  /**
   * 流水ID
   */
  private Long id;
  /**
   * 用户号
   */
  private Long user_id;
  /**
   * 金额（分）
   */
  private Long amount;
  /**
   * 余额（分）
   */
  private Long balance;
  /**
   * 标志位
   */
  private Integer income;
  /**
   * 交易类型
   */
  private Integer type;
  /**
   * 关联ID
   */
  private Long ref_id;
  /**
   * 关联表
   */
  private Integer ref_table;

  public Long getId() {
    return this.id;
  }
  public UserAccountDataModel setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUser_id() {
    return this.user_id;
  }
  public UserAccountDataModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Long getAmount() {
    return this.amount;
  }
  public UserAccountDataModel setAmount(Long amount) {
    this.amount = amount;
    return this;
  }

  public Long getBalance() {
    return this.balance;
  }
  public UserAccountDataModel setBalance(Long balance) {
    this.balance = balance;
    return this;
  }

  public Integer getIncome() {
    return this.income;
  }
  public UserAccountDataModel setIncome(Integer income) {
    this.income = income;
    return this;
  }

  public Integer getType() {
    return this.type;
  }
  public UserAccountDataModel setType(Integer type) {
    this.type = type;
    return this;
  }

  public Long getRef_id() {
    return this.ref_id;
  }
  public UserAccountDataModel setRef_id(Long ref_id) {
    this.ref_id = ref_id;
    return this;
  }

  public Integer getRef_table() {
    return this.ref_table;
  }
  public UserAccountDataModel setRef_table(Integer ref_table) {
    this.ref_table = ref_table;
    return this;
  }

}

