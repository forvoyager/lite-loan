package com.etl.borrow.common.enums;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 13:08
 * @Description:
 */
public enum RepaymentMode {

  AVERGE_CAPITAL_PLUS_INEREST(1, "等额本息"),
  AVERGE_CAPITAL(2, "等额本金"),
  LAST_CAPITAL(3, "每月付息到期还本"),
  ;

  private int code;
  private String label;

  private RepaymentMode(int code, String label) {
    this.code = code;
    this.label = label;
  }

  public static RepaymentMode parse(int code) {
    for (RepaymentMode v : RepaymentMode.values()) {
      if (v.code == code) {
        return v;
      }
    }
    return LAST_CAPITAL;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
  
}
