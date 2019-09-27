package com.etl.user.common.controller;

import com.etl.base.common.dto.ResultDto;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.page.PageData;
import com.etl.user.common.model.UserDataModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-27 14:05:16 <br>
 * <b>description</b>: 用户信息 HTTP接口 <br>
 */
public interface IUserDataController {

  /**
   * <p>
   * 插入一条记录
   * </p>
   *
   * @param entity 实体对象
   * @return UserDataModel 插入成功的对象
   */
  @RequestMapping("/userData/insert")
  ResultDto<UserDataModel> insert(@RequestBody UserDataModel entity) throws Exception;

  /**
   * <p>
   * 插入（批量）
   * </p>
   *
   * @param entityList 对象列表
   * @return Integer 插入成功的记录数
   */
  @RequestMapping("/userData/insert/batch")
  ResultDto<Integer> insertBatch(@RequestBody List<UserDataModel> entityList) throws Exception;

  /**
   * <p>
   * 存在则更新，否则插入
   * </p>
   *
   * @param entity 实体对象
   * @return UserDataModel 插入/更新成功的对象
   */
  @RequestMapping("/userData/insert/update")
  ResultDto<UserDataModel> insertOrUpdate(@RequestBody UserDataModel entity) throws Exception;

  /**
   * <p>
   * 根据 主键ID 删除
   * </p>
   *
   * @param user_id 主键ID
   * @return Integer 删除的行数
   */
  @RequestMapping("/userData/delete/{user_id}")
  ResultDto<Integer> deleteById(@PathVariable("user_id") long user_id) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，删除记录
   * </p>
   *
   * @param condition 删除条件
   * @return Integer 删除的行数
   */
  @RequestMapping("/userData/delete")
  ResultDto<Long> deleteByMap(@RequestBody Map<String, Object> condition) throws Exception;

  /**
   * <p>
   * 根据 model 修改数据
   * </p>
   *
   * @param entity 实体对象
   * @return UserDataModel 更新的行数
   */
  @RequestMapping("/userData/update/model")
  ResultDto<Long> update(@RequestBody UserDataModel entity) throws Exception;

  /**
   * <p>
   * 根据map条件 修改
   * </p>
   *
   * @param columnMap 更新数据/更新条件
   * @return Integer 更新的行数
   */
  @RequestMapping("/userData/update/map")
  ResultDto<Long> updateByMap(@RequestBody Map<String, Object> columnMap) throws Exception;

  /**
   * <p>
   * 根据 主键ID 查询
   * </p>
   *
   * @param user_id 主键ID
   * @param master 主节点 or 从节点
   * @return UserDataModel
   */
  @RequestMapping("/userData/select/{master}/{user_id}")
  ResultDto<UserDataModel> selectById(@PathVariable("user_id") long user_id, @PathVariable("master") Cluster master) throws Exception;

  /**
   * <p>
   * 根据 ID 批量查询
   * </p>
   *
   * @param idList 主键ID列表
   * @param master 主节点 or 从节点
   * @return List<UserDataModel> 列表
   */
  @RequestMapping("/userData/select/{master}/batch")
  ResultDto<List<UserDataModel>> selectByIds(@RequestBody Collection<? extends Serializable> idList, @PathVariable("master") Cluster master) throws Exception;

  /**
   * <p>
   * 查询（根据 condition 条件）
   * </p>
   *
   * @param condition 查询条件
   * @param master 主节点 or 从节点
   * @return List<UserDataModel> 列表
   */
  @RequestMapping("/userData/select/{master}/list")
  ResultDto<List<UserDataModel>> selectList(@RequestBody Map<String, Object> condition, @PathVariable("master") Cluster master) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，查询一条记录
   * </p>
   *
   * @param condition 查询条件
   * @param master 主节点 or 从节点
   * @return UserDataModel
   */
  @RequestMapping("/userData/select/{master}/one")
  ResultDto<UserDataModel> selectOne(@RequestBody Map<String, Object> condition, @PathVariable("master") Cluster master) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，查询并转换成map
   * </p>
   *
   * @param condition 查询条件
   * @param master 主节点 or 从节点
   * @return Map<主键, UserDataModel>
   */
  @RequestMapping("/userData/select/{master}/map")
  ResultDto<Map<Integer, UserDataModel>> selectMap(@RequestBody Map<String, Object> condition, @PathVariable("master") Cluster master) throws Exception;

  /**
   * <p>
   * 根据 condition 条件，查询总记录数
   * </p>
   *
   * @param condition 查询条件
   * @param master 主节点 or 从节点
   * @return long 记录数
   */
  @RequestMapping("/userData/select/{master}/count")
  ResultDto<Long> selectCount(@RequestBody Map<String, Object> condition, @PathVariable("master") Cluster master) throws Exception;

  /**
   * <p>
   * 翻页查询
   * </p>
   *
   * @param page 第几页
   * @param size 每页记录数
   * @param condition 查询条件
   * @param master 主节点 or 从节点
   * @return
   */
  @RequestMapping("/userData/select/{master}/page/{page}/{size}")
  ResultDto<PageData<UserDataModel>> selectPage(@PathVariable("page") int page, @PathVariable("size") int size, @RequestBody Map<String, Object> condition, @PathVariable("master") Cluster master) throws Exception;

}
