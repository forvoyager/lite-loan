package com.etl.base.common.page;

import java.beans.Transient;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页信息
 */
public class PageData<T> {

  private static final long serialVersionUID = 166L;

  /**
   * 总记录数
   */
  private long records;

  /**
   * 每页显示条数，默认 10
   */
  private int size = 10;

  /**
   * 总页数
   */
  private int pages;

  /**
   * 当前页（默认显示第1页）
   */
  private int page = 1;

  /**
   * 查询数据列表
   */
  private List<T> data = Collections.EMPTY_LIST;

  /**
   * 查询参数
   */
  private Map<String, Object> condition;

  public long getRecords() {
    return records;
  }

  public void setRecords(long records) {
    this.records = records;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  @Transient
  public Map<String, Object> getCondition() {
    return condition;
  }

  public void setCondition(Map<String, Object> condition) {
    this.condition = condition;
  }

  public void putCondition(String key, Object val){
    if(this.condition == null){
      this.condition = new HashMap<>();
    }
    this.condition.put(key, val);
  }
}
