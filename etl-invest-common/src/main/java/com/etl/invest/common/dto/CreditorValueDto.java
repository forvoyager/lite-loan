package com.etl.invest.common.dto;

import java.io.Serializable;

/**
 * <b>author</b>: forvoyager@outlook.com <br>
 * <b>time</b>: 2019-11-19 18:09 <br>
 * <b>description</b>: 债权价值信息
 * 衡量标准：公允价值（待收本金+待收利息）
 * 在这个标准上，债权持有人可以折价/溢价卖出债权，折价/溢价比率称为折让率（可配置 或 按不同等级的会员设置不同折让率）。
 * <p>
 * 卖方：
 * 成交价格 = 公允价值+(公允价值*折让率)          # 成交价格可能大于、小于或等于公允价值
 * 管理费 = 公允价值*管理费费率                  # 管理费费率可配置，归平台
 * 到账金额 = 成交价格 - 管理费
 * <p>
 * 买方：
 * 购买价格 = 成交价格
 * 收益金额 = 购买价格 - 待收本金
 * 收益率 = [(收益金额/待收本金)/债权剩余天数]*365*100%
 */
public class CreditorValueDto implements Serializable {
  /**
   * 原始债权id
   */
  private long creditor_id;
  /**
   * 交易时间/计算下面信息的时间
   * 随着时间的推移，债权的价值是变化的，所以当前债权的所有价值信息都是以此时间未基准计算的。
   */
  private long time;
  /**
   * 转让份数
   */
  private int partition;
  /**
   * 待回收本金（分）
   */
  private long unpaid_capital;
  /**
   * 待回收利息（分）
   */
  private long unpaid_interest;
  /**
   * 债权剩余天数
   */
  private int surplus_days;
  /**
   * 折价率
   * 小于0 表示折价
   * 等于0 表示原价
   * 大于0 表示溢价
   */
  private double discount_apr;
  /**
   * 公允价值（分）= 待收本金+待收利息
   * 是指在计量日发生的有序交易中，市场参与者之间出售一项资产所能收到的价格或者转移一项负债所需支付的价格。
   */
  private long fairValue;
  /**
   * 成交价格（分）
   */
  private long trade_price;
  /**
   * 管理费（分）
   */
  private long manage;
  /**
   * 到账金额
   */
  private long incoming_price;
  /**
   * 收益金额（分）
   */
  private long profit_price;
  /**
   * 利率，如10.2% 存0.102
   */
  private double apr;

  public long getCreditor_id() {
    return creditor_id;
  }

  public CreditorValueDto setCreditor_id(long creditor_id) {
    this.creditor_id = creditor_id;
    return this;
  }

  public long getTime() {
    return time;
  }

  public CreditorValueDto setTime(long time) {
    this.time = time;
    return this;
  }

  public int getPartition() {
    return partition;
  }

  public CreditorValueDto setPartition(int partition) {
    this.partition = partition;
    return this;
  }

  public long getUnpaid_capital() {
    return unpaid_capital;
  }

  public CreditorValueDto setUnpaid_capital(long unpaid_capital) {
    this.unpaid_capital = unpaid_capital;
    return this;
  }

  public long getUnpaid_interest() {
    return unpaid_interest;
  }

  public CreditorValueDto setUnpaid_interest(long unpaid_interest) {
    this.unpaid_interest = unpaid_interest;
    return this;
  }

  public int getSurplus_days() {
    return surplus_days;
  }

  public CreditorValueDto setSurplus_days(int surplus_days) {
    this.surplus_days = surplus_days;
    return this;
  }

  public double getDiscount_apr() {
    return discount_apr;
  }

  public CreditorValueDto setDiscount_apr(double discount_apr) {
    this.discount_apr = discount_apr;
    return this;
  }

  public long getFairValue() {
    return fairValue;
  }

  public CreditorValueDto setFairValue(long fairValue) {
    this.fairValue = fairValue;
    return this;
  }

  public long getTrade_price() {
    return trade_price;
  }

  public CreditorValueDto setTrade_price(long trade_price) {
    this.trade_price = trade_price;
    return this;
  }

  public long getManage() {
    return manage;
  }

  public CreditorValueDto setManage(long manage) {
    this.manage = manage;
    return this;
  }

  public long getIncoming_price() {
    return incoming_price;
  }

  public CreditorValueDto setIncoming_price(long incoming_price) {
    this.incoming_price = incoming_price;
    return this;
  }

  public long getProfit_price() {
    return profit_price;
  }

  public CreditorValueDto setProfit_price(long profit_price) {
    this.profit_price = profit_price;
    return this;
  }

  public double getApr() {
    return apr;
  }

  public CreditorValueDto setApr(double apr) {
    this.apr = apr;
    return this;
  }
}
