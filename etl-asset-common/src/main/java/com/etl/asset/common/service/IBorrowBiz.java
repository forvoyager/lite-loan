package com.etl.asset.common.service;

/**
 * <b>author</b>：forvoyager@outlook.com
 * <b>time</b>：2019/10/7 0007 13:56 <br>
 * <b>description</b>：借款项目 服务定义（无事务）
 */
public interface IBorrowBiz {

  /**
   * 借款项目 已满标，执行满标终审。
   *
   * 满标终审主要操作内容如下：
   * 生成 借款人 还款报表
   * 生成 投资人 债权信息
   * 生成 投资人 收益报表
   * 发送满标终审消息
   *
   * 进入还款流程，状态变为 BorrowStatus#IN_REPAYMENT
   *
   * @param borrow_id
   * @throws Exception
   */
  void verify(long borrow_id) throws Exception;

}
