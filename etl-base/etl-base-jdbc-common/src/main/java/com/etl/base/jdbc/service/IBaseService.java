package com.etl.base.jdbc.service;

import com.etl.base.common.enums.Cluster;
import com.etl.base.common.page.PageData;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据基础操作定义
 */
public interface IBaseService<T> {

  /**
   * <p>
   * 插入一条记录
   * </p>
   *
   * @param entity 实体对象
   * @return T 插入成功的对象
   */
  T insert(T entity) throws Exception;

  /**
   * <p>
   * 插入（批量）
   * </p>
   *
   * @param entityList 实体对象列表
   * @return Integer 插入成功的记录数
   */
  int insertBatch(List<T> entityList) throws Exception;

  /**
   * <p>
   * 存在则更新，否则插入
   * </p>
   *
   * @param entity 实体对象
   * @return T 插入/更新成功的对象
   */
  T insertOrUpdate(T entity) throws Exception;

  /**
   * <p>
   * 根据 ID 删除
   * </p>
   *
   * @param id 主键ID
   * @return Integer 删除的行数
   */
  int deleteById(Serializable id) throws Exception;

  /**
   * <p>
   * 根据ID 批量删除
   * </p>
   *
   * @param idList 主键ID列表
   * @return Integer 删除的行数
   */
  long deleteByIds(Collection<? extends Serializable> idList) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，删除记录
   * </p>
   *
   * @param condition 表字段 map 对象
   * @return Integer 删除的行数
   */
  long deleteByMap(Map<String, Object> condition) throws Exception;

  /**
   * <p>
   * 修改
   * </p>
   *
   * @param entity 实体对象
   * @return T 更新的行数
   */
  long update(T entity) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，修改记录
   * </p>
   *
   * @param columnMap 表字段 map 对象
   * @return Integer 删除的行数
   */
  long updateByMap(Map<String, Object> columnMap) throws Exception;

  /**
   * <p>
   * 根据 ID 查询
   * </p>
   *
   * @param id 主键ID
   * @param cluster 主节点 or 从节点
   * @return T
   */
  T selectById(Serializable id, Cluster cluster) throws Exception;

  /**
   * <p>
   * 根据 ID 批量查询
   * </p>
   *
   * @param idList
   * @param cluster
   * @return
   * @throws Exception
   */
  List<T> selectByIds(Collection<? extends Serializable> idList, Cluster cluster) throws Exception;

  /**
   * <p>
   * 根据 conditiont条件，查询一条记录
   * </p>
   *
   * @param condition 表字段 map 对象
   * @param cluster 主节点 or 从节点
   * @return T
   */
  T selectOne(Map<String, Object> condition, Cluster cluster) throws Exception;

  /**
   * <p>
   * 查询（根据 condition 条件）
   * </p>
   *
   * @param condition 表字段 map 对象
   * @param cluster 主节点 or 从节点
   * @return List<T>
   */
  List<T> selectList(Map<String, Object> condition, Cluster cluster) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，查询记录，并转换成map，key是主键字段的值
   * </p>
   *
   * @param condition 表字段 map 对象
   * @param cluster 主节点 or 从节点
   * @return Map<String,Object>
   */
  Map<String, T> selectMap(Map<String, Object> condition, Cluster cluster) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，查询总记录数
   * </p>
   *
   * @param condition 表字段 map 对象
   * @param cluster 主节点 or 从节点
   * @return long
   */
  long selectCount(Map<String, Object> condition, Cluster cluster) throws Exception;

  /**
   * <p>
   * 翻页查询
   * </p>
   *
   * @param page 第几页
   * @param size 每页记录数
   * @param condition 表字段 map 对象
   * @param cluster 主节点 or 从节点
   * @return
   */
  PageData<T> selectPage(int page, int size, Map<String, Object> condition, Cluster cluster) throws Exception;

}
