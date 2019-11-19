package com.etl.user.common.service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-11-19 13:41:22 <br>
 * <b>description</b>: 账户操作 服务定义 <br>
 */
public interface IUserAccountBiz{

  /**
   * 标的放款
   * 资金从投资人账户划拨到借款人账户
   *
   * @param borrow_id 标的id
   * @param investor 投资人id
   * @param borrower 借款人id
   * @param amount 投标金额
   * @param invest_id 投资记录id
   * @throws Exception
   */
  void borrowPay(long borrow_id, long investor, long borrower, long amount, long invest_id) throws Exception;

}
