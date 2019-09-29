package com.etl.base.common.enums;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-29 9:09
 * @Description: 访问渠道
 */
public enum AccessChannel {
  PC(0, ""),
  WAP(1, ""),
  ANDROID(2, ""),
  IOS(3, ""),
  ;
  
  private int code;
  private String description;
  private AccessChannel(int code, String des){
    this.code = code;
    this.description = des;
  }

  public int getCode() {
    return code;
  }

  public AccessChannel setCode(int code) {
    this.code = code;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public AccessChannel setDescription(String description) {
    this.description = description;
    return this;
  }
}
