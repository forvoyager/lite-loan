package com.etl.invest.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 11:51:35 <br>
 * <b>description</b>: 投资记录 模型 <br>
 */
public class InvestRecordModel extends BaseModel {

  public static final String INVEST_ID = "invest_id";
  public static final String USER_ID = "user_id";
  public static final String BORROW_ID = "borrow_id";
  public static final String INVEST_AMOUNT = "invest_amount";
  public static final String PARTION = "partion";
  public static final String STATUS = "status";
  public static final String CHANNEL = "channel";

  /**
   * 编号
   */
  private Long invest_id;
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
   * 状态 0待处理 1成功 2失败
   */
  private Integer status;
  /**
   * 投资渠道
   */
  private Integer channel;

  public Long getInvest_id() {
    return this.invest_id;
  }
  public InvestRecordModel setInvest_id(Long invest_id) {
    this.invest_id = invest_id;
    return this;
  }

  public Long getUser_id() {
    return this.user_id;
  }
  public InvestRecordModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Long getBorrow_id() {
    return this.borrow_id;
  }
  public InvestRecordModel setBorrow_id(Long borrow_id) {
    this.borrow_id = borrow_id;
    return this;
  }

  public Long getInvest_amount() {
    return this.invest_amount;
  }
  public InvestRecordModel setInvest_amount(Long invest_amount) {
    this.invest_amount = invest_amount;
    return this;
  }

  public Integer getPartion() {
    return this.partion;
  }
  public InvestRecordModel setPartion(Integer partion) {
    this.partion = partion;
    return this;
  }

  public Integer getStatus() {
    return this.status;
  }
  public InvestRecordModel setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public Integer getChannel() {
    return this.channel;
  }
  public InvestRecordModel setChannel(Integer channel) {
    this.channel = channel;
    return this;
  }

}

