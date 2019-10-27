package com.etl.invest.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-10-26 11:44:26 <br>
 * <b>description</b>: 投资记录 模型 <br>
 */
public class InvestModel extends BaseModel {

  public static final String ID = "id";
  public static final String USER_ID = "user_id";
  public static final String BORROW_ID = "borrow_id";
  public static final String INVEST_AMOUNT = "invest_amount";
  public static final String PARTION = "partion";
  public static final String INVEST_STATUS = "invest_status";
  public static final String PAY_STATUS = "pay_status";
  public static final String CHANNEL = "channel";

  /**
   * 编号
   */
  private Long id;
  /**
   * 标投人
   */
  private Long user_id;
  /**
   * 标的id
   */
  private Long borrow_id;
  /**
   * 标投金额（分）
   */
  private Long invest_amount;
  /**
   * 份数
   */
  private Integer partion;
  /**
   * 投资状态 0待处理 1成功 2失败
   */
  private Integer invest_status;
  /**
   * 放款给借款人 0否 1是
   */
  private Integer pay_status;
  /**
   * 投资渠道
   */
  private Integer channel;

  public Long getId() {
    return this.id;
  }
  public InvestModel setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUser_id() {
    return this.user_id;
  }
  public InvestModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Long getBorrow_id() {
    return this.borrow_id;
  }
  public InvestModel setBorrow_id(Long borrow_id) {
    this.borrow_id = borrow_id;
    return this;
  }

  public Long getInvest_amount() {
    return this.invest_amount;
  }
  public InvestModel setInvest_amount(Long invest_amount) {
    this.invest_amount = invest_amount;
    return this;
  }

  public Integer getPartion() {
    return this.partion;
  }
  public InvestModel setPartion(Integer partion) {
    this.partion = partion;
    return this;
  }

  public Integer getInvest_status() {
    return this.invest_status;
  }
  public InvestModel setInvest_status(Integer invest_status) {
    this.invest_status = invest_status;
    return this;
  }

  public Integer getPay_status() {
    return this.pay_status;
  }
  public InvestModel setPay_status(Integer pay_status) {
    this.pay_status = pay_status;
    return this;
  }

  public Integer getChannel() {
    return this.channel;
  }
  public InvestModel setChannel(Integer channel) {
    this.channel = channel;
    return this;
  }

}

