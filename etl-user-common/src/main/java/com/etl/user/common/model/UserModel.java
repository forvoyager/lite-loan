package com.etl.user.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-27 14:05:16 <br>
 * <b>description</b>: 用户 模型 <br>
 */
public class UserModel extends BaseModel {

  public static final String USER_ID = "user_id";
  public static final String USER_NAME = "user_name";
  public static final String EMAIL = "email";
  public static final String MOBILE_NUMBER = "mobile_number";
  public static final String ENCRYPT_SALT = "encrypt_salt";
  public static final String PASSWORD = "password";
  public static final String PAY_PASSWORD = "pay_password";
  public static final String USER_ROLE = "user_role";
  public static final String REGISTRY_TIME = "registry_time";
  public static final String REGISTRY_CHANNEL = "registry_channel";
  public static final String PORTRAIT = "portrait";

  /**
   * 用户ID
   */
  private Integer user_id;
  /**
   * 用户名
   */
  private String user_name;
  /**
   * 邮箱
   */
  private String email;
  /**
   * 手机号
   */
  private String mobile_number;
  /**
   * 加密盐
   */
  private String encrypt_salt;
  /**
   * 密码
   */
  private String password;
  /**
   * 支付密码
   */
  private String pay_password;
  /**
   * 用户角色 1-出借人 2-借款人 3-担保人
   */
  private Integer user_role;
  /**
   * 注册时间
   */
  private Integer registry_time;
  /**
   * 注册渠道
   */
  private Integer registry_channel;
  /**
   * 头像
   */
  private String portrait;

  public Integer getUser_id() {
    return this.user_id;
  }
  public UserModel setUser_id(Integer user_id) {
    this.user_id = user_id;
    return this;
  }

  public String getUser_name() {
    return this.user_name;
  }
  public UserModel setUser_name(String user_name) {
    this.user_name = user_name;
    return this;
  }

  public String getEmail() {
    return this.email;
  }
  public UserModel setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getMobile_number() {
    return this.mobile_number;
  }
  public UserModel setMobile_number(String mobile_number) {
    this.mobile_number = mobile_number;
    return this;
  }

  public String getEncrypt_salt() {
    return this.encrypt_salt;
  }
  public UserModel setEncrypt_salt(String encrypt_salt) {
    this.encrypt_salt = encrypt_salt;
    return this;
  }

  public String getPassword() {
    return this.password;
  }
  public UserModel setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getPay_password() {
    return this.pay_password;
  }
  public UserModel setPay_password(String pay_password) {
    this.pay_password = pay_password;
    return this;
  }

  public Integer getUser_role() {
    return this.user_role;
  }
  public UserModel setUser_role(Integer user_role) {
    this.user_role = user_role;
    return this;
  }

  public Integer getRegistry_time() {
    return this.registry_time;
  }
  public UserModel setRegistry_time(Integer registry_time) {
    this.registry_time = registry_time;
    return this;
  }

  public Integer getRegistry_channel() {
    return this.registry_channel;
  }
  public UserModel setRegistry_channel(Integer registry_channel) {
    this.registry_channel = registry_channel;
    return this;
  }

  public String getPortrait() {
    return this.portrait;
  }
  public UserModel setPortrait(String portrait) {
    this.portrait = portrait;
    return this;
  }

}

