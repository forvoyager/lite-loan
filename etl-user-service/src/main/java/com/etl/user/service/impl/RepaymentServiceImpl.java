package com.etl.user.service.impl;

import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.Utils;
import com.etl.borrow.common.model.RepaymentFormModel;
import com.etl.borrow.common.service.IRepaymentFormService;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.service.IRepaymentService;
import com.etl.user.common.service.IUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-12 16:20
 * @Description: 借款人还款服务 服务实现
 */
public class RepaymentServiceImpl implements IRepaymentService {
  
  @Resource
  private IUserAccountService userAccountService;
  
  @Autowired
  private IRepaymentFormService repaymentFormService;
  
  @Override
  public void repayment(long form_id) throws Exception {

    RepaymentFormModel repaymentForm = repaymentFormService.selectById(form_id, Cluster.master);
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
    
    // 还款金额=本金+利息+管理费+罚款费用-减免金额
    long repaymentAmount = repaymentForm.getCapital()+repaymentForm.getInterest()+
            repaymentForm.getManage_fee()+repaymentForm.getOverdue_penalty()-repaymentForm.getReduction_amount();
    
    // 冻结还款人资金
    userAccountService.frozen(repaymentForm.getUser_id(), repaymentAmount, FundsOperateType.repayment_frozen, RefTable.repayment_form, repaymentForm.getId());
    
    // 还款兑付（给投资人回款） TODO
    
    // 更新回款报表为 已还 TODO
    
  }
}
