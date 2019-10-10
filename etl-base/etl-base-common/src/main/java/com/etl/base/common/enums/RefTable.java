package com.etl.base.common.enums;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-10 16:18
 * @Description: 关联表定义
 */
public enum RefTable {

  invest_record(1, "投资记录"),
  ;
  
  private int code; // 类型代码
  private String description; // 类型描述
  private RefTable(int code, String des){
    this.code = code;
    this.description = des;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public static RefTable parse(int code) {
    for(RefTable fot : values()){
      if(code == fot.code){
        return fot;
      }
    }

    throw new IllegalArgumentException("非法的关联类型");
  }
}
