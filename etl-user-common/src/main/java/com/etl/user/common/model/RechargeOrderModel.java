package com.etl.user.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-10-12 10:33:09 <br>
 * <b>description</b>: 用户充值订单 模型 <br>
 */
public class RechargeOrderModel extends BaseModel {

  public static final String ID = "id";
  public static final String USER_ID = "user_id";
  public static final String AMOUNT = "amount";
  public static final String STATUS = "status";
  public static final String CHANNEL = "channel";
  public static final String TRACE_ID = "trace_id";

  /**
   * 主键ID
   */
  private Long id;
  /**
   * 用户ID
   */
  private Long user_id;
  /**
   * 充值余额（分）
   */
  private Long amount;
  /**
   * 状态 0待处理 1成功 2失败
   */
  private Integer status;
  /**
   * 充值渠道
   */
  private Integer channel;
  /**
   * 第三方交易流水ID
   */
  private String trace_id;

  public Long getId() {
    return this.id;
  }
  public RechargeOrderModel setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUser_id() {
    return this.user_id;
  }
  public RechargeOrderModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Long getAmount() {
    return this.amount;
  }
  public RechargeOrderModel setAmount(Long amount) {
    this.amount = amount;
    return this;
  }

  public Integer getStatus() {
    return this.status;
  }
  public RechargeOrderModel setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public Integer getChannel() {
    return this.channel;
  }
  public RechargeOrderModel setChannel(Integer channel) {
    this.channel = channel;
    return this;
  }

  public String getTrace_id() {
    return this.trace_id;
  }
  public RechargeOrderModel setTrace_id(String trace_id) {
    this.trace_id = trace_id;
    return this;
  }

}

