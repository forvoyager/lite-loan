package com.etl.user.common.model;

import com.etl.base.common.model.BaseModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-27 14:05:16 <br>
 * <b>description</b>: 用户信息 模型 <br>
 */
public class UserDataModel extends BaseModel {

  public static final String USER_ID = "user_id";
  public static final String GENDER = "gender";
  public static final String ID_NAME = "id_name";
  public static final String ID_NUMBER = "id_number";
  public static final String CONTACTS = "contacts";
  public static final String JOBS = "jobs";
  public static final String RESIDENCE = "residence";

  /**
   * 用户ID
   */
  private Integer user_id;
  /**
   * 性别(5-女，7-男)
   */
  private Integer gender;
  /**
   * 真实姓名
   */
  private String id_name;
  /**
   * 身份证号
   */
  private String id_number;
  /**
   * 联系人信息，JSON格式
   */
  private String contacts;
  /**
   * 工作信息，JSON格式
   */
  private String jobs;
  /**
   * 居住信息，JSON格式
   */
  private String residence;

  public Integer getUser_id() {
    return this.user_id;
  }
  public UserDataModel setUser_id(Integer user_id) {
    this.user_id = user_id;
    return this;
  }

  public Integer getGender() {
    return this.gender;
  }
  public UserDataModel setGender(Integer gender) {
    this.gender = gender;
    return this;
  }

  public String getId_name() {
    return this.id_name;
  }
  public UserDataModel setId_name(String id_name) {
    this.id_name = id_name;
    return this;
  }

  public String getId_number() {
    return this.id_number;
  }
  public UserDataModel setId_number(String id_number) {
    this.id_number = id_number;
    return this;
  }

  public String getContacts() {
    return this.contacts;
  }
  public UserDataModel setContacts(String contacts) {
    this.contacts = contacts;
    return this;
  }

  public String getJobs() {
    return this.jobs;
  }
  public UserDataModel setJobs(String jobs) {
    this.jobs = jobs;
    return this;
  }

  public String getResidence() {
    return this.residence;
  }
  public UserDataModel setResidence(String residence) {
    this.residence = residence;
    return this;
  }

}

