package com.etl.asset.service.impl;

import com.etl.asset.common.enums.BorrowStatus;
import com.etl.asset.common.model.BorrowModel;
import com.etl.asset.common.service.IBorrowBiz;
import com.etl.asset.common.service.IBorrowService;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.Utils;
import com.etl.invest.common.service.IInvestService;
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

  @GlobalTransactional(timeoutMills = 10*1000)
  @Override
  public void verify(long borrow_id) throws Exception {

    logger.info("满标终审，tx_xid:{}", RootContext.getXID());

    BorrowModel borrow = borrowService.selectById(borrow_id, Cluster.master);
    if(BorrowStatus.parse(borrow.getStatus()) != BorrowStatus.FULL_BID){
      Utils.throwsBizException("未满标，不可发起终审。");
    }

    // 满标 生成 借款人 还款报表
    borrowService.initBorrowerForm(borrow_id);

    // 满标 生成 投资人 债权信息及收益报表
    investService.initInvestorForm(borrow_id);

    // 满标 放款
    investService.borrowPay(borrow_id);

    // TODO 发送满标终审消息
  }
}
