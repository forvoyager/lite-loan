package com.etl.user.common.enums;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-10 15:45
 * @Description: 资金操作类型定义
 * 
 * 资金操作规则：冻结-> 解冻 -> 支出
 * 以投标为例，用户投标时：
 * 1、投标冻结，可用余额减少，冻结金额增加，总金额不变。
 * 2、投标解冻，可用余额增加，冻结金额减少，总金额不变。
 * 3、投标支出，可用余额减少，冻结金额不变，总金额减少。
 * 
 */
public enum FundsOperateType {

  // 1xx 投标相关
  invest_bid_frozen(100, -1, "投标冻结"),
  invest_bid_unfrozen(101, 1, "投标解冻"),
  invest_bid_pay(102, -1, "投标支出"),

  // 2xx 债权相关
  invest_creditor_frozen(200, -1, "购买债权冻结"),
  invest_creditor_unfrozen(201, 1, "购买债权解冻"),
  invest_creditor_pay(202, -1, "购买债权支出"),

  // 3xx 借款/还款相关
  loan_entry(300, 1, "借款入账"),
  repayment_frozen(301, -1, "还款冻结"),
  repayment_unfrozen(302, 1, "还款解冻"),
  repayment_pay(303, -1, "还款支出"),
  repayment_capital_entry(304, 1, "还款收到本金"),
  repayment_interest_entry(305, 1, "还款收到利息"),

  // 4xx 充值相关
  recharge_online(400, 1, "在线充值"),

  // 5xx

  ;

  private int code; // 类型代码
  private int flag; // 资金变化 -1减少 0不变 1增加
  private String description; // 类型描述
  private FundsOperateType(int code, int flag, String des){
    this.code = code;
    this.flag = flag;
    this.description = des;
  }

  public int getCode() {
    return code;
  }

  public int getFlag() {
    return flag;
  }
  public String getDescription() {
    return description;
  }
  
  public static FundsOperateType parse(int code) {
    for(FundsOperateType fot : values()){
      if(code == fot.code){
        return fot;
      }
    }

    throw new IllegalArgumentException("非法的资金操作");
  }
}
