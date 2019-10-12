package com.etl.invest.service.impl;

import com.etl.base.common.dto.RepaymentDetailDto;
import com.etl.base.common.dto.RepaymentPerMonthDto;
import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.FeeCalcUtils;
import com.etl.base.common.util.Utils;
import com.etl.borrow.common.enums.BorrowStatus;
import com.etl.borrow.common.model.BorrowModel;
import com.etl.borrow.common.service.IBorrowService;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.common.model.InvestRecordModel;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.common.service.ICreditorService;
import com.etl.invest.common.service.IInvestRecordService;
import com.etl.invest.common.service.IInvestService;
import com.etl.invest.common.service.IProfitFormService;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.model.UserAccountModel;
import com.etl.user.common.service.IUserAccountService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <b>author</b>：forvoyager@outlook.com
 * <b>time</b>：2019/10/3 14:52 <br>
 * <b>description</b>：投资操作 服务实现
 */
@Service("investService")
public class InvestServiceImpl implements IInvestService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Resource
  private ICreditorService creditorService;

  @Resource
  private IProfitFormService profitFormService;

  @Resource
  private IInvestRecordService investRecordService;

  @Autowired
  private IBorrowService borrowService;

  @Autowired
  private IUserAccountService userAccountService;

  @GlobalTransactional
  @Override
  public void apply(long user_id, long borrow_id, long amount, AccessChannel channel) throws Exception {

    // 信息验证
    AssertUtils.isTrue(amount > 0, "金额不合法");
    
    BorrowModel borrow = borrowService.selectById(borrow_id, Cluster.master);
    if(BorrowStatus.parse(borrow.getStatus()) != BorrowStatus.IN_BID || borrow.getAvailable_amount().longValue() == 0){
      Utils.throwsBizException("当前标的不可投");
    }
    if(amount > borrow.getAvailable_amount()){
      Utils.throwsBizException("剩余最大可投金额"+borrow.getAvailable_amount()/100);
    }

    UserAccountModel userAccount = userAccountService.selectById(user_id, Cluster.master);
    if(userAccount.getAvailable() < amount){
      Utils.throwsBizException("账户余额不足");
    }
    
    // 默认
    if (channel == null) { channel = AccessChannel.PC; }
    
    long current = DateUtils.currentTimeInSecond();

    // 生成投资记录信息
    InvestRecordModel investRecord = new InvestRecordModel();
    investRecord.setUser_id(user_id);
    investRecord.setBorrow_id(borrow_id);
    investRecord.setInvest_amount(amount);
    investRecord.setPartion((int)amount/10000); // ((amount/100)元/100)份
    investRecord.setChannel(channel.getCode());
    investRecord.setStatus(0);
    investRecord.setCreate_time(current);
    investRecord.setUpdate_time(current);
    investRecord.setVersion(0);
    investRecord = investRecordService.insert(investRecord);

    // 减少标的可投金额
    borrowService.changeAvailableAmount(borrow_id, -amount);

    // 资金冻结
    userAccountService.frozen(user_id, amount, FundsOperateType.invest_frozen, RefTable.invest_record, investRecord.getInvest_id());

    // TODO 发送投资成功消息
  }

  @Transactional
  @Override
  public void verifyInitInvestorForm(long borrow_id) throws Exception {

    logger.info("tx_xid:{}", RootContext.getXID());
    
    List<InvestRecordModel> allInvestRecords = investRecordService.selectList(Utils.newHashMap(
            InvestRecordModel.BORROW_ID, borrow_id,
            InvestRecordModel.STATUS, 0
    ), Cluster.master);
    AssertUtils.notEmpty(allInvestRecords, "投资记录不正确，标的："+borrow_id);

    // 用户在同一个标的可能会有多比投资 Map<用户ID, 投资列表>
    Map<Long, List<InvestRecordModel>> userInvestMap = new HashMap<>();
    List<InvestRecordModel> investList = null;
    for(InvestRecordModel irm : allInvestRecords){
      investList = userInvestMap.get(irm.getUser_id());
      if(investList == null){
        investList = new ArrayList<>();
        userInvestMap.put(irm.getUser_id(), investList);
      }
      investList.add(irm);
    }

    BorrowModel borrowModel = borrowService.selectById(borrow_id, Cluster.master);
    AssertUtils.notNull(borrowModel, "标的不存在");

    if( BorrowStatus.IN_REPAYMENT != BorrowStatus.parse(borrowModel.getStatus()) ) {
      Utils.throwsBizException("标的状态不正确");
    }

    long current = DateUtils.currentTimeInSecond();
    Long user_id = null;
    Long invest_amount = 0L; // 标投金额（分）
    CreditorModel creditorModel = null;
    List<CreditorModel> creditorModelList = new ArrayList<>();
    ProfitFormModel profitFormModel = null;
    List<ProfitFormModel> profitFormModelList = new ArrayList<>();
    for(Entry<Long, List<InvestRecordModel>> entry : userInvestMap.entrySet()){
      user_id = entry.getKey();
      investList = entry.getValue();
      invest_amount = 0L;
      for(InvestRecordModel ir : investList){
        invest_amount += ir.getInvest_amount();
      }

      RepaymentDetailDto repaymentDetailDto = FeeCalcUtils.averageInterest(invest_amount/100, borrowModel.getApr()/100, borrowModel.getPeriod(), current);

      creditorModel = new CreditorModel();
      creditorModel.setParent_creditor_id(0L);
      creditorModel.setUser_id(user_id);
      creditorModel.setBorrow_id(borrow_id);
      creditorModel.setStatus(0);
      creditorModel.setCapital((long)(repaymentDetailDto.getCapital()*100));
      creditorModel.setInterest((long)(repaymentDetailDto.getInterest()*100));
      creditorModel.setPeriod(repaymentDetailDto.getPeriod());
      creditorModel.setSurplus_period(repaymentDetailDto.getPeriod());
      creditorModel.setUnpaid_capital(creditorModel.getCapital());
      creditorModel.setUnpaid_interest(creditorModel.getInterest());
      creditorModel.setStart_time(repaymentDetailDto.getStart_time());
      creditorModel.setEnd_time(repaymentDetailDto.getEnd_time());
      creditorModel.setPartion((int)(invest_amount/100/100));
      creditorModel.setCreate_time(current);
      creditorModel.setUpdate_time(current);
      creditorModel.setVersion(0);
      creditorModelList.add(creditorModel);

      for(RepaymentPerMonthDto plan : repaymentDetailDto.getRepaymentPlan()){
        profitFormModel = new ProfitFormModel();
        profitFormModel.setUser_id(user_id);
        profitFormModel.setBorrow_id(borrow_id);
        profitFormModel.setStatus(0);
        profitFormModel.setCapital((long)(plan.getCapital()*100));
        profitFormModel.setInterest((long)(plan.getInterest()*100));
        profitFormModel.setPeriod(plan.getPeriod());
        profitFormModel.setPlan_repayment_time(plan.getRepaymentDate());
        profitFormModel.setCreate_time(current);
        profitFormModel.setUpdate_time(current);
        profitFormModel.setVersion(0);
        profitFormModelList.add(profitFormModel);
      }

    }

    // 初始化债权信息
    creditorService.insertBatch(creditorModelList);

    Map<Long, Long> userCreditorMap = new HashMap<>();
    creditorModelList = creditorService.selectList(Utils.newHashMap(CreditorModel.BORROW_ID, borrow_id), Cluster.master);
    for(CreditorModel cm : creditorModelList){
      userCreditorMap.put(cm.getUser_id(), cm.getCreditor_id());
    }

    // 初始化收益报表
    for(ProfitFormModel pfm : profitFormModelList){
      pfm.setCreditor_id(userCreditorMap.get(pfm.getUser_id()));
    }
    profitFormService.insertBatch(profitFormModelList);

  }

  @GlobalTransactional
  @Override
  public void verifyInvestorPayment(long borrow_id) throws Exception {
    List<InvestRecordModel> investRecords = this.investRecordService.selectList(Utils.newHashMap(
            InvestRecordModel.BORROW_ID, borrow_id,
            InvestRecordModel.STATUS, 0
    ), Cluster.master);
    AssertUtils.notEmpty(investRecords, "投资记录不完整");
    
    long current = DateUtils.currentTimeInSecond();
    InvestRecordModel updateInvestRecord = null;
    for(InvestRecordModel irm : investRecords){
      
      if(irm.getStatus().intValue() != 0) { continue; }
      
      // 投资人资金 解冻
      userAccountService.unfrozen(irm.getUser_id(), irm.getInvest_amount(), FundsOperateType.invest_unfrozen, RefTable.invest_record, irm.getInvest_id());
      // 投资人资金 支出
      userAccountService.pay(irm.getUser_id(), irm.getInvest_amount(), FundsOperateType.invest_pay, RefTable.invest_record, irm.getInvest_id());

      // 投资记录置为 成功
      updateInvestRecord = new InvestRecordModel();
      updateInvestRecord.setInvest_id(irm.getInvest_id());
      updateInvestRecord.setWhere_version(irm.getVersion());
      updateInvestRecord.setUpdate_time(current);
      updateInvestRecord.setStatus(1);
      if( 1 != investRecordService.update(updateInvestRecord)){
        Utils.throwsBizException("账户["+irm.getUser_id()+"]，投标支出失败。");
      }
    }
    
  }
}
