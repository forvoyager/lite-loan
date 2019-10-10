package com.etl.user.common.enums;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-10 15:45
 * @Description: 资金操作类型定义
 * 
 * 资金操作规则：先冻结（可用资金减少，冻结资金增加），后支出（可用资金不变，冻结资金减少）。
 * 以投标为例，用户投标时：
 * 1、投标冻结，可用余额减少，冻结金额增加，总金额不变。
 * 2、投标支出，可用余额不变，冻结金额减少，总金额减少。
 * 
 */
public enum FundsOperateType {

  invest_frozen(100, -1, "投标冻结"),
  invest_unfrozen(101, 1, "投标解冻"),
  invest_pay(102, 0, "投标支出"),
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
