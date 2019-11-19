package com.etl.asset.common.service;

import com.etl.base.jdbc.service.IBaseService;
import com.etl.asset.common.model.BorrowModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 09:30:12 <br>
 * <b>description</b>: 借款项目 服务定义 <br>
 */
public interface IBorrowService extends IBaseService<BorrowModel> {

  /**
   * 申请借款
   * 状态 BorrowStatus#CHECK
   * @param borrow
   * @throws Exception
   */
  BorrowModel apply(BorrowModel borrow) throws Exception;

  /**
   * 借款项目 审核
   *
   * 审核内容：
   * 借款信息是否正确，如：金额、利率、期数等
   * 各种费用收取，如：审核费等
   * 各种认证材料，如：身份证照片、企业营业执照、许可证            等
   *
   * 通过：
   * 记录审核意见
   * 进入发标流程
   * 状态变为 BorrowStatus#CHECKED
   *
   * 拒绝：
   * 记录拒绝原因
   * 状态变为 BorrowStatus#REFUSE
   *
   * @param borrow_id
   * @throws Exception
   */
  void check(long borrow_id) throws Exception;

  /**
   * 借款项目 审核通过，发布标的
   *
   * 主要内容：
   * 设置标的的各种配置信息，如：最低投标金额、投资限额、开始投标时间等
   *
   * 记录审核意见
   * 进入投标流程，状态变为 BorrowStatus#IN_BID
   * 投标完成后，进入满标终审流程，状态变为 BorrowStatus#FULL_BID
   *
   * @param borrow_id
   * @throws Exception
   */
  void release(long borrow_id) throws Exception;

  /**
   * 满标终审 初始化 借款人 的还款报表，更新标的信息
   *
   * @param borrow_id
   * @throws Exception
   */
  void initBorrowerForm(long borrow_id) throws Exception;

  /**
   * 修改标的可投金额
   * 
   * 减少可投金额时：如果没有可投金额后，将标的置为 已满标
   * 增加可投金额时：将标的置为 投标中
   *
   * @param borrow_id
   * @param amount 大于0表示增加可投金额 小于0表示减少可投金额
   * @throws Exception
   */
  void changeAvailableAmount(long borrow_id, long amount) throws Exception;
}
