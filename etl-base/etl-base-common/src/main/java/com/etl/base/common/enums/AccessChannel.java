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

  public String getDescription() {
    return description;
  }

  public static AccessChannel parse(String code) throws Exception{
    for(AccessChannel ac:values()){
      if(String.valueOf(ac.getCode()).equals(code)){
        return ac;
      }
    }

    throw new IllegalArgumentException("非法的访问渠道");
  }

  public static AccessChannel parse(int code) throws Exception{
    return parse(String.valueOf(code));
  }

  /**
   * 是否是移动端
   *
   * @param channel
   * @return
   * @throws Exception
   */
  public static boolean isApp(AccessChannel channel) throws Exception{
    if(channel == AccessChannel.ANDROID || channel == AccessChannel.IOS){
      return true;
    }

    return false;
  }
}
