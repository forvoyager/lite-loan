package com.etl.base.common.exception;

import com.etl.base.common.enums.ResultCodeEnum;

import java.util.Map;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2020-01-11 20:50:00
 * @Description: 自定义异常
 * 调整为继承自Exception（checked受检查异常），
 * 原来继承自RuntimeException（Unchecked异常），dubbo会对此类异常重新封装，不会把原始异常信息抛出去。
 */
public class BaseException extends Exception {
  /**
   * 代码
   * @see ResultCodeEnum
   */
  private String code;
  /**
   * 信息提示
   */
  private String message;
  /**
   * 扩展数据
   */
  private Map extData;

  public BaseException(){
//    this.code = ResultCodeEnum.SYSTEM_ERROR.getCode();
//    this.message = ResultCodeEnum.SYSTEM_ERROR.getLabel();
  }

  public BaseException(ResultCodeEnum code, String message){
    this.code = code.getCode();
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map getExtData() {
    return extData;
  }

  public void setExtData(Map extData) {
    this.extData = extData;
  }
}
