package com.etl.invest.service.impl;

import com.etl.asset.common.enums.BorrowStatus;
import com.etl.asset.common.model.BorrowModel;
import com.etl.asset.common.service.IBorrowService;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.asset.common.model.RepaymentFormModel;
import com.etl.asset.common.service.IRepaymentFormService;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.common.service.IProfitFormService;
import com.etl.invest.common.service.IRepaymentService;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.service.IUserAccountService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-12 16:20
 * @Description: 借款人还款服务 服务实现
 */
@Service("repaymentService")
public class RepaymentServiceImpl implements IRepaymentService {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  @Resource
  private IProfitFormService profitFormService;

  @Autowired
  private IRepaymentFormService repaymentFormService;

  @Autowired
  private IBorrowService borrowService;
  
  @Autowired
  private IUserAccountService userAccountService;
  
  @GlobalTransactional
  @Override
  public void repayment(long repayment_form_id) throws Exception {

//    logger.info("tx_xid:{}", RootContext.getXID());

    RepaymentFormModel repaymentForm = repaymentFormService.selectById(repayment_form_id, Cluster.master);
    if(repaymentForm == null){ return; }
    
    if(repaymentForm.getStatus().intValue() == 1){
      Utils.throwsBizException("借款已还，无需重复还款。");
    }
    
    // 检查上一期是否已还
    if(repaymentForm.getPeriod() > 1){
      RepaymentFormModel lastRepaymentForm = repaymentFormService.selectOne(Utils.newHashMap(
              RepaymentFormModel.BORROW_ID, repaymentForm.getBorrow_id(),
              RepaymentFormModel.USER_ID, repaymentForm.getUser_id(),
              RepaymentFormModel.PERIOD, repaymentForm.getPeriod()-1
      ), Cluster.master);
      AssertUtils.notNull(lastRepaymentForm, "上一期借款信息不正确");
      if(lastRepaymentForm.getStatus().intValue() == 0){
        Utils.throwsBizException("请先还上一期借款");
      }
    }

    long current = DateUtils.currentTimeInSecond();
    
    // 还款金额=本金+利息+管理费+罚款费用-减免金额
    long repaymentAmount = repaymentForm.getCapital()+repaymentForm.getInterest()+
            repaymentForm.getManage_fee()+repaymentForm.getOverdue_penalty()-repaymentForm.getReduction_amount();
    
    // 冻结还款人资金
    userAccountService.frozen(repaymentForm.getUser_id(), repaymentAmount, FundsOperateType.repayment_frozen, RefTable.repayment_form, repaymentForm.getId());
    
    // 还款兑付（给投资人回款）
    repaymentCash(repaymentForm.getBorrow_id(), repaymentForm.getPeriod());

    // 解冻支出还款人资金
    userAccountService.unfrozen(repaymentForm.getUser_id(), repaymentAmount, FundsOperateType.repayment_unfrozen, RefTable.repayment_form, repaymentForm.getId());
    userAccountService.pay(repaymentForm.getUser_id(), repaymentAmount, FundsOperateType.repayment_pay, RefTable.repayment_form, repaymentForm.getId());

    // 更新回款报表为 已还
    RepaymentFormModel updateRepaymentForm = new RepaymentFormModel();
    updateRepaymentForm.setId(repaymentForm.getId());
    updateRepaymentForm.setWhere_version(repaymentForm.getVersion());
    updateRepaymentForm.setStatus(1);
    updateRepaymentForm.setActual_repayment_time(current);
    updateRepaymentForm.setUpdate_time(current);
    if(1 != repaymentFormService.update(updateRepaymentForm)){
      Utils.throwsBizException("更新回款报表失败");
    }
    
    // 如果是最后一期还款 更新标的状态为 还款结束
    BorrowModel currentBorrow = borrowService.selectById(repaymentForm.getBorrow_id(), Cluster.master);
    if(repaymentForm.getPeriod().intValue() == currentBorrow.getPeriod().intValue()){
      BorrowModel updateBorrow = new BorrowModel();
      updateBorrow.setBorrow_id(currentBorrow.getBorrow_id());
      updateBorrow.setWhere_version(currentBorrow.getVersion());
      updateBorrow.setUpdate_time(current);
      updateBorrow.setStatus(BorrowStatus.REPAYMENTED.getCode());
      if( 1 != borrowService.update(updateBorrow)){
        Utils.throwsBizException("还款，更新标的状态失败。");
      }
    }
    
  }

  /**
   * 还款兑付
   *
   * @param borrow_id 还款标的id
   * @param period 还款期数
   * @throws Exception
   */
  private void repaymentCash(long borrow_id, int period) throws Exception {

    List<ProfitFormModel> profitFormModels = profitFormService.selectList(Utils.newHashMap(
            ProfitFormModel.BORROW_ID, borrow_id,
            ProfitFormModel.PERIOD, period,
            ProfitFormModel.STATUS, 0
    ), Cluster.master);

    // 收益报表逐个兑付
    for(ProfitFormModel pfm : profitFormModels){
      profitFormService.cash(pfm);
      logger.info("标的：{}，收益报表：{}，兑付完成。", borrow_id, pfm.getId());
    }
  }
}
