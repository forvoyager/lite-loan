package com.etl.asset.common.enums;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 13:08
 * @Description: 标的状态
 */
public enum BorrowStatus {

  REFUSE(100, "拒绝申请"),
  CHECK(200, "待审核"),
  CHECKED(300, "等待发标"),
  IN_BID(400, "投标中"),
  FULL_BID(500, "已满标"),
  IN_REPAYMENT(600, "还款中"),
  REPAYMENTED(700, "还款结束"),
  FAILURE_BID(800, "流标"),
  ;

  private int code;
  private String label;

  private BorrowStatus(int code, String label) {
    this.code = code;
    this.label = label;
  }

  public static BorrowStatus parse(int code) {
    for (BorrowStatus v : BorrowStatus.values()) {
      if (v.code == code) {
        return v;
      }
    }
    return REFUSE;
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
