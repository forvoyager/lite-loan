package com.etl.borrow.service.impl;

import com.etl.borrow.common.service.IBorrowBiz;
import com.etl.borrow.common.service.IBorrowService;
import com.etl.invest.common.service.IInvestService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <b>author</b>：forvoyager@outlook.com
 * <b>time</b>：2019/10/7 0007 13:56 <br>
 * <b>description</b>：借款项目 服务实现（无事务）
 */
@Service("borrowBiz")
public class BorrowBizImpl implements IBorrowBiz {

  @Resource
  private IBorrowService borrowService;

  @Autowired
  private IInvestService investService;

  @GlobalTransactional
  @Override
  public void verify(long borrow_id) throws Exception {

    System.out.println("--------------:"+RootContext.getXID());

    // 生成 借款人 还款报表
    borrowService.verifyInitBorrowerForm(borrow_id);

    // 生成 投资人 债权信息
    // 生成 投资人 收益报表
    investService.verifyInitInvestorForm(borrow_id);

    // 发送满标终审消息
    System.out.println("--------------:"+RootContext.getXID());
  }
}
