package com.etl.invest.service.impl;

import com.etl.asset.common.enums.BorrowStatus;
import com.etl.asset.common.model.BorrowModel;
import com.etl.asset.common.service.IBorrowService;
import com.etl.base.common.dto.RepaymentDetailDto;
import com.etl.base.common.dto.RepaymentPerMonthDto;
import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.FeeCalcUtils;
import com.etl.base.common.util.Utils;
import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.invest.common.dto.CreditorValueDto;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.common.model.CreditorTransferModel;
import com.etl.invest.common.model.InvestModel;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.common.service.ICreditorService;
import com.etl.invest.common.service.ICreditorTransferService;
import com.etl.invest.common.service.IInvestService;
import com.etl.invest.common.service.IProfitFormService;
import com.etl.invest.common.util.CreditorUtils;
import com.etl.invest.mapper.InvestMapper;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.model.UserAccountModel;
import com.etl.user.common.service.IUserAccountBiz;
import com.etl.user.common.service.IUserAccountService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-10-26 11:44:26 <br>
 * <b>description</b>: 投资记录 服务实现 <br>
 */
@Service("investService")
public class InvestServiceImpl extends BaseServiceImpl<InvestMapper, InvestModel> implements IInvestService {
  @Override
  protected String getPrimaryKeyName() {
    return "id";
  }

  @GlobalTransactional
  @Override
  public void bid(long user_id, long borrow_id, long amount, AccessChannel channel) throws Exception {

    // 信息验证
    AssertUtils.isTrue(amount > 0, "金额不合法");

    long current = DateUtils.currentTimeInSecond();

    BorrowModel borrow = borrowService.selectById(borrow_id, Cluster.master);
    if(
            current < borrow.getInvest_start_time() ||
                    BorrowStatus.parse(borrow.getStatus()) != BorrowStatus.IN_BID ||
                    borrow.getAvailable_amount().longValue() == 0){
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

    // 生成投资记录信息
    InvestModel invest = new InvestModel();
    invest.setUser_id(user_id);
    invest.setType(1);
    invest.setBiz_id(borrow_id);
    invest.setInvest_amount(amount);
    invest.setPartition((int)amount/10000); // ((amount/100)元/100)份
    invest.setChannel(channel.getCode());
    invest.setInvest_status(0); // 投资待处理
    invest.setPay_status(0); // 待放款（给借款人）
    invest.setCreate_time(current);
    invest.setUpdate_time(current);
    invest.setVersion(0);
    invest = investService.insert(invest);

    // 减少标的可投金额
    borrowService.changeAvailableAmount(borrow_id, -amount);

    // 资金冻结
    userAccountService.frozen(user_id, amount, FundsOperateType.invest_bid_frozen, RefTable.invest_record, invest.getId());

    // TODO 发送投资成功消息
  }

  @GlobalTransactional
  @Override
  public void creditorRight(long user_id, long transfer_id, int partition, AccessChannel channel) throws Exception {
    AssertUtils.isTrue(partition > 0, "购买份数不合法");

    CreditorTransferModel transferModel = creditorTransferService.selectById(transfer_id, Cluster.master);
    AssertUtils.notNull(transferModel, "债转不存在");
    AssertUtils.isTrue(transferModel.getStatus().intValue() == 0, "手慢，被别人买了！");
    AssertUtils.isTrue(partition <= transferModel.getAvailable_partition(), "最大可购买份数："+transferModel.getAvailable_partition() );

    long current = DateUtils.currentTimeInSecond();

    // 冻结购买份数 可购买份数--
    CreditorTransferModel updateTransfer = new CreditorTransferModel();
    updateTransfer.setId(transferModel.getId());
    updateTransfer.setWhere_version(transferModel.getVersion());
    updateTransfer.setUpdate_time(current);
    updateTransfer.setAvailable_partition(transferModel.getAvailable_partition() - partition);
    if(updateTransfer.getAvailable_partition().intValue() == 0){
      updateTransfer.setStatus(-1); // 转让完毕
    }
    if(creditorTransferService.update(updateTransfer) != 1){
      Utils.throwsBizException("冻结购买份数失败");
    }

    // 用户资金验证
    List<ProfitFormModel> profitForms = profitFormService.selectList(Utils.newHashMap(
            ProfitFormModel.CREDITOR_ID, transferModel.getCreditor_id()
    ), Cluster.master);
    CreditorValueDto creditorValue = CreditorUtils.valueDto(transferModel, profitForms, 1);
    AssertUtils.isTrue(creditorValue.getBuy_price()>0, "债权信息不正确，稍后重试。");
    UserAccountModel userAccount = userAccountService.selectById(user_id, Cluster.master);
    if(userAccount.getAvailable() < creditorValue.getBuy_price()){
      Utils.throwsBizException("账户余额不足");
    }

    // 生成投资记录信息
    InvestModel invest = new InvestModel();
    invest.setUser_id(user_id);
    invest.setType(2);
    invest.setBiz_id(transferModel.getId());
    invest.setInvest_amount(creditorValue.getBuy_price());
    invest.setPartition(partition);
    invest.setChannel(channel.getCode());
    invest.setInvest_status(0); // 投资待处理
    invest.setPay_status(0); // 待放款（给借款人）
    invest.setCreate_time(current);
    invest.setUpdate_time(current);
    invest.setVersion(0);
    invest = investService.insert(invest);

    // 冻结用户资金
    userAccountService.frozen(user_id, creditorValue.getBuy_price(), FundsOperateType.invest_creditor_frozen, RefTable.invest_record, invest.getId());

    // TODO 投递购买债权消息

  }

  @GlobalTransactional
  @Transactional
  @Override
  public void initInvestorForm(long borrow_id) throws Exception {

    logger.info("tx_xid:{}", RootContext.getXID());

    List<InvestModel> allInvest = investService.selectList(Utils.newHashMap(
            InvestModel.TYPE, 1,
            InvestModel.BIZ_ID, borrow_id,
            InvestModel.INVEST_STATUS, 0,
            "sort","id asc"
    ), Cluster.master);
    AssertUtils.notEmpty(allInvest, "投资记录不正确，标的："+borrow_id);

    // 用户在同一个标的可能会有多比投资 Map<用户ID, 投资列表>
    Map<Long, List<InvestModel>> userInvestMap = new HashMap<>();
    List<InvestModel> userInvestList = null;
    for(InvestModel irm : allInvest){
      userInvestList = userInvestMap.get(irm.getUser_id());
      if(userInvestList == null){
        userInvestList = new ArrayList<>();
        userInvestMap.put(irm.getUser_id(), userInvestList);
      }
      userInvestList.add(irm);
    }

    BorrowModel borrowModel = borrowService.selectById(borrow_id, Cluster.master);
    AssertUtils.notNull(borrowModel, "标的不存在");

    if( BorrowStatus.IN_REPAYMENT != BorrowStatus.parse(borrowModel.getStatus()) ) {
      Utils.throwsBizException("标的状态不正确");
    }

    long current = DateUtils.currentTimeInSecond();
    Long user_id = null;
    Long creditor_id = null; // 债权id
    Long invest_amount = 0L; // 标投金额（分）
    CreditorModel creditorModel = null;
    List<CreditorModel> creditorModelList = new ArrayList<>();
    ProfitFormModel profitFormModel = null;
    List<ProfitFormModel> profitFormModelList = new ArrayList<>();
    for(Map.Entry<Long, List<InvestModel>> entry : userInvestMap.entrySet()){
      creditor_id = null; // 债权id（取invest_id，多个取最大）
      user_id = entry.getKey();
      userInvestList = entry.getValue();
      invest_amount = 0L;
      for(InvestModel ir : userInvestList){
        invest_amount += ir.getInvest_amount();
        creditor_id = ir.getId();
      }

      RepaymentDetailDto repaymentDetailDto = FeeCalcUtils.averageInterest(invest_amount/100, borrowModel.getApr(), borrowModel.getPeriod(), current);

      creditorModel = new CreditorModel();
      creditorModel.setId(creditor_id);
      creditorModel.setParent_id(0L);
      creditorModel.setCreditor_transfer_id(0L);
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
      creditorModel.setPartition((int)(invest_amount/100/100));
      creditorModel.setCreate_time(current);
      creditorModel.setUpdate_time(current);
      creditorModel.setVersion(0);
      creditorModelList.add(creditorModel);

      for(RepaymentPerMonthDto plan : repaymentDetailDto.getRepaymentPlan()){
        profitFormModel = new ProfitFormModel();
        profitFormModel.setCreditor_id(creditor_id); // 等初始化债权有再更新债权id
        profitFormModel.setUser_id(user_id);
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

    // 初始化收益报表
    profitFormService.insertBatch(profitFormModelList);

  }

  @GlobalTransactional
  @Override
  public void borrowPay(long borrow_id) throws Exception {
    List<InvestModel> allInvest = this.investService.selectList(Utils.newHashMap(
            InvestModel.TYPE, 1,
            InvestModel.BIZ_ID, borrow_id,
            InvestModel.INVEST_STATUS, 0,
            InvestModel.PAY_STATUS, 0
    ), Cluster.master);
    AssertUtils.notEmpty(allInvest, "投资记录不完整");

    BorrowModel borrow = borrowService.selectById(borrow_id, Cluster.slave);

    long current = DateUtils.currentTimeInSecond();
    InvestModel updateInvest = null;
    for(InvestModel irm : allInvest){

      if(irm.getInvest_status().intValue() != 0) { continue; }
      if(irm.getPay_status().intValue() != 0) { continue; }

      // 放款
      userAccountBiz.borrowPay(borrow_id, irm.getUser_id(), borrow.getUser_id(), irm.getInvest_amount(), irm.getId());

      // 投资记录置为 成功
      updateInvest = new InvestModel();
      updateInvest.setId(irm.getId());
      updateInvest.setWhere_version(irm.getVersion());
      updateInvest.setUpdate_time(current);
      updateInvest.setInvest_status(1);
      updateInvest.setPay_status(1);
      if( 1 != investService.update(updateInvest)){
        Utils.throwsBizException("账户["+irm.getUser_id()+"]，投标支出失败。");
      }

    }
  }

  @Resource
  private ICreditorService creditorService;

  @Resource
  private ICreditorTransferService creditorTransferService;

  @Resource
  private IProfitFormService profitFormService;

  @Resource
  private IInvestService investService;

  @Autowired
  private IBorrowService borrowService;

  @Autowired
  private IUserAccountService userAccountService;

  @Autowired
  private IUserAccountBiz userAccountBiz;
}
