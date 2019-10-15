package com.etl.asset.service.impl;

import com.etl.asset.common.enums.BorrowStatus;
import com.etl.asset.common.service.IBorrowBiz;
import com.etl.asset.common.service.IBorrowService;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.Utils;
import com.etl.asset.common.model.BorrowModel;
import com.etl.invest.common.service.IInvestService;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.service.IUserAccountService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Resource
  private IBorrowService borrowService;

  @Autowired
  private IInvestService investService;
  
  @Autowired
  private IUserAccountService userAccountService;

  @GlobalTransactional(timeoutMills = 10*1000)
  @Override
  public void verify(long borrow_id) throws Exception {

    logger.info("tx_xid:{}", RootContext.getXID());
    
    BorrowModel borrow = borrowService.selectById(borrow_id, Cluster.master);
    if(BorrowStatus.parse(borrow.getStatus()) != BorrowStatus.FULL_BID){
      Utils.throwsBizException("未满标，不可发起终审。");
    }

    // 生成 借款人 还款报表
    borrowService.verifyInitBorrowerForm(borrow_id);

    // 生成 投资人 债权信息
    // 生成 投资人 收益报表
    investService.verifyInitInvestorForm(borrow_id);

    // 投资人账户资金支出
    investService.verifyInvestorPayment(borrow_id);

    // 借款人账户资金入账
    userAccountService.changeAvailable(borrow.getUser_id(), borrow.getAmount(), FundsOperateType.loan_entry, RefTable.borrow, borrow_id);

    // TODO 发送满标终审消息
  }
}
