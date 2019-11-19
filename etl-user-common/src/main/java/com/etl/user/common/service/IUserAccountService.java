package com.etl.user.common.service;

import com.etl.base.common.enums.RefTable;
import com.etl.base.jdbc.service.IBaseService;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.model.UserAccountModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-29 17:46:22 <br>
 * <b>description</b>: 用户账户 服务定义 <br>
 */
public interface IUserAccountService extends IBaseService<UserAccountModel> {

  /**
   * 冻结资金
   * 
   * @param user_id 用户
   * @param amount 冻结金额（分）
   * @param biz_type 操作类型
   * @param ref_table 关联表
   * @param ref_id 关联表id
   * @throws Exception
   */
  void frozen(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception;

  /**
   * 解冻资金
   *
   * @param user_id 用户
   * @param amount 解冻金额（分）
   * @param biz_type 操作类型
   * @param ref_table 关联表
   * @param ref_id 关联表id
   * @throws Exception
   */
  void unfrozen(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception;

  /**
   * 支出、支付
   *
   * @param user_id 用户
   * @param amount 支出金额（分）
   * @param biz_type 操作类型
   * @param ref_table 关联表
   * @param ref_id 关联表id
   * @throws Exception
   */
  void pay(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception;

  /**
   * 资金入账
   *
   * @param user_id 用户
   * @param amount 金额（分）
   * @param biz_type 操作类型
   * @param ref_table 关联表
   * @param ref_id 关联表id
   * @throws Exception
   */
  void incoming(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception;
  
}
