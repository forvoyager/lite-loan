package com.etl.invest.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-11-06 13:27:01 <br>
 * <b>description</b>: 投资记录（投标、买债权） 模型 <br>
 */
public class InvestModel extends BaseModel {

  public static final String ID = "id";
  public static final String USER_ID = "user_id";
  public static final String TYPE = "type";
  public static final String BIZ_ID = "biz_id";
  public static final String INVEST_AMOUNT = "invest_amount";
  public static final String PARTITION = "partition";
  public static final String INVEST_STATUS = "invest_status";
  public static final String PAY_STATUS = "pay_status";
  public static final String CHANNEL = "channel";

  /**
   * 编号
   */
  private Long id;
  /**
   * 标资人id
   */
  private Long user_id;
  /**
   * 投资类型 1投标 2买债权
   */
  private Integer type;
  /**
   * 由type确定：标的id/债转id
   */
  private Long biz_id;
  /**
   * 标投金额（分）
   */
  private Long invest_amount;
  /**
   * 份数
   */
  private Integer partition;
  /**
   * 投资状态 0待处理 1成功 2失败
   */
  private Integer invest_status;
  /**
   * 放款 0否 1是
   */
  private Integer pay_status;
  /**
   * 投资渠道
   */
  private Integer channel;

  public Long getId() {
    return id;
  }

  public InvestModel setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUser_id() {
    return user_id;
  }

  public InvestModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Integer getType() {
    return type;
  }

  public InvestModel setType(Integer type) {
    this.type = type;
    return this;
  }

  public Long getBiz_id() {
    return biz_id;
  }

  public InvestModel setBiz_id(Long biz_id) {
    this.biz_id = biz_id;
    return this;
  }

  public Long getInvest_amount() {
    return invest_amount;
  }

  public InvestModel setInvest_amount(Long invest_amount) {
    this.invest_amount = invest_amount;
    return this;
  }

  public Integer getPartition() {
    return partition;
  }

  public InvestModel setPartition(Integer partition) {
    this.partition = partition;
    return this;
  }

  public Integer getInvest_status() {
    return invest_status;
  }

  public InvestModel setInvest_status(Integer invest_status) {
    this.invest_status = invest_status;
    return this;
  }

  public Integer getPay_status() {
    return pay_status;
  }

  public InvestModel setPay_status(Integer pay_status) {
    this.pay_status = pay_status;
    return this;
  }

  public Integer getChannel() {
    return channel;
  }

  public InvestModel setChannel(Integer channel) {
    this.channel = channel;
    return this;
  }
}

