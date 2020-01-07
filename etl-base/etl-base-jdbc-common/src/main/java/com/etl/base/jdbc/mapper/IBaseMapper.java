package com.etl.base.jdbc.mapper;

import java.util.List;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>description</b>: 业务基础操作定义 <br>
 */
public interface IBaseMapper<T> {

  /**
   * <p>
   * 插入一条记录
   * </p>
   *
   * @param entity 实体对象
   */
  void insert(T entity);

  /**
   * <p>
   * 插入（批量），该方法不适合 Oracle
   * </p>
   *
   * @param entityList 实体对象列表
   */
  void insertBatch(List<T> entityList);

  /**
   * <p>
   * 根据 condition 条件，删除记录
   * </p>
   *
   * @param condition 删除条件
   * @return Integer 删除的行数
   */
  int delete(Map<String, Object> condition);

  /**
   * 根据传入的map参数进行更新
   *
   * @param columnMap 更新参数/查询条件
   * @return
   */
  int update(Map<String, Object> columnMap);

  /**
   * <p>
   * 查询（根据 condition 条件）
   * </p>
   *
   * @param condition 查询条件
   * @return List<T>
   */
  List<T> selectList(Map<String, Object> condition);

  /**
   * <p>
   * 根据 Wrapper 条件，查询总记录数
   * </p>
   *
   * @param condition 查询条件
   * @return long
   */
  long selectCount(Map<String, Object> condition);

}
