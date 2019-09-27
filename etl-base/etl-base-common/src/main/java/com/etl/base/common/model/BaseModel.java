package com.etl.base.common.model;

import java.beans.Transient;
import java.io.Serializable;

/**
 * 基础模型数据
 */
public class BaseModel implements Serializable {

  public static final String CREATE_TIME = "create_time";
  public static final String UPDATE_TIME = "update_time";
  public static final String VERSION = "version";
  public static final String WHERE_VERSION = "where_version";

  /**
   * 创建时间
   */
  protected Long create_time;
  /**
   * 最后更新时间
   */
  protected Long update_time;
  /**
   * 版本号
   */
  protected Integer version;
  /**
   * 条件 版本号 （@Transient 不序列化）
   */
  protected Integer where_version;

  public Long getCreate_time() {
    return create_time;
  }

  public void setCreate_time(Long create_time) {
    this.create_time = create_time;
  }

  public Long getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(Long update_time) {
    this.update_time = update_time;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Transient
  public Integer getWhere_version() {
    return where_version;
  }

  public void setWhere_version(Integer where_version) {
    this.where_version = where_version;
  }
}
