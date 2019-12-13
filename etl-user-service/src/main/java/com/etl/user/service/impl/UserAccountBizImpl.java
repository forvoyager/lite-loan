package com.etl.user.service.impl;

import com.etl.base.common.enums.RefTable;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.service.IUserAccountBiz;
import com.etl.user.common.service.IUserAccountService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-11-19 13:41:22 <br>
 * <b>description</b>: 账户操作 服务实现 <br>
 */
@Service("userAccountBiz")
public class UserAccountBizImpl implements IUserAccountBiz {

  @Resource
  private IUserAccountService userAccountService;

  @GlobalTransactional
  @Transactional
  @Override
  public void borrowPay(long borrow_id, long investor, long borrower, long amount, long invest_id) throws Exception {

    // 投资人账户 冻结（投标时已冻结）

    // 投资人账户 解冻
    userAccountService.unfrozen(investor, amount, FundsOperateType.invest_bid_unfrozen, RefTable.invest_record, invest_id);

    // 投资人资金 支出
    userAccountService.pay(investor, amount, FundsOperateType.invest_bid_pay, RefTable.invest_record, invest_id);

    // 借款人账户 入账
    userAccountService.incoming(borrower, amount, FundsOperateType.loan_entry, RefTable.invest_record, invest_id);
  }

}
