package com.etl.asset.common.util;

import com.etl.asset.common.model.RepaymentFormModel;
import com.etl.base.common.dto.RepaymentDetailDto;
import com.etl.base.common.dto.RepaymentPerMonthDto;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.FeeCalcUtils;
import com.etl.asset.common.model.BorrowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>author</b>：forvoyager@outlook.com
 * <b>time</b>：2019/10/3 12:52 <br>
 * <b>description</b>：
 */
public final class BorrowUtils {

  /**
   * 生成借款人还款报表
   * @param borrow 标的信息
   * @return
   * @throws Exception
   */
  public static List<RepaymentFormModel> buildRepaymentForm(BorrowModel borrow) throws Exception {

    long current = DateUtils.currentTimeInSecond();
    RepaymentDetailDto repaymentDetailDto = FeeCalcUtils.averageInterest(borrow.getAmount()/100, borrow.getApr(), borrow.getPeriod(), current);
    RepaymentFormModel repaymentForm = null;
    List<RepaymentFormModel> repaymentFormModels = new ArrayList<>();
    for(RepaymentPerMonthDto plan : repaymentDetailDto.getRepaymentPlan()){
      repaymentForm = new RepaymentFormModel();
      repaymentForm.setBorrow_id(borrow.getBorrow_id());
      repaymentForm.setUser_id(borrow.getUser_id());
      repaymentForm.setPeriod(plan.getPeriod());
      repaymentForm.setCapital((long)(plan.getCapital()*100));
      repaymentForm.setInterest((long)(plan.getInterest()*100));
      repaymentForm.setManage_fee(0L); // 管理费暂不收取
      repaymentForm.setPlan_repayment_time(plan.getRepaymentDate());
      repaymentForm.setActual_repayment_time(0L);
      repaymentForm.setOverdue_days(0);
      repaymentForm.setOverdue_penalty(0L);
      repaymentForm.setReduction_amount(0L);
      repaymentForm.setAdvance(0);
      repaymentForm.setAdvance_time(0L);
      repaymentForm.setStatus(0); // 未还
      repaymentForm.setCreate_time(current);
      repaymentForm.setUpdate_time(current);
      repaymentForm.setVersion(0);
      repaymentFormModels.add(repaymentForm);
    }

    return repaymentFormModels;
  }

  private BorrowUtils(){}
}
