package com.etl.user.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-29 17:46:22 <br>
 * <b>description</b>: 用户账户 模型 <br>
 */
public class UserAccountModel extends BaseModel {

  public static final String USER_ID = "user_id";
  public static final String AVAILABLE = "available";
  public static final String FROZEN = "frozen";
  public static final String ID_NAME = "id_name";
  public static final String ID_CARD = "id_card";

  /**
   * 用户ID
   */
  private Long user_id;
  /**
   * 可用余额（分）
   */
  private Integer available;
  /**
   * 冻结金额（分）
   */
  private Integer frozen;
  /**
   * 真实姓名
   */
  private String id_name;
  /**
   * 身份证号
   */
  private String id_card;

  public Long getUser_id() {
    return this.user_id;
  }
  public UserAccountModel setUser_id(Long user_id) {
    this.user_id = user_id;
    return this;
  }

  public Integer getAvailable() {
    return this.available;
  }
  public UserAccountModel setAvailable(Integer available) {
    this.available = available;
    return this;
  }

  public Integer getFrozen() {
    return this.frozen;
  }
  public UserAccountModel setFrozen(Integer frozen) {
    this.frozen = frozen;
    return this;
  }

  public String getId_name() {
    return this.id_name;
  }
  public UserAccountModel setId_name(String id_name) {
    this.id_name = id_name;
    return this;
  }

  public String getId_card() {
    return this.id_card;
  }
  public UserAccountModel setId_card(String id_card) {
    this.id_card = id_card;
    return this;
  }

}

