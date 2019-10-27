package com.etl.invest.common.service;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.jdbc.service.IBaseService;
import com.etl.invest.common.model.InvestModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-10-26 11:44:26 <br>
 * <b>description</b>: 投资记录 服务定义 <br>
 */
public interface IInvestService extends IBaseService<InvestModel> {

  /**
   * 投标申请
   * @param user_id 用户
   * @param borrow_id 标的
   * @param amount 金额
   * @param channel 投资渠道
   * @throws Exception
   */
  void apply(long user_id, long borrow_id, long amount, AccessChannel channel) throws Exception;

  /**
   * 满标终审后初始化投资者的债权信息和收益报表
   *
   * @param borrow_id
   * @throws Exception
   */
  void verifyInitInvestorForm(long borrow_id) throws Exception;

  /**
   * 满标终审 投资人账户支出
   * @param borrow_id
   * @throws Exception
   */
  void verifyInvestorPayment(long borrow_id) throws Exception;

}
