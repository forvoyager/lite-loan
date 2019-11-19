package com.etl.invest.service.impl;

import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.common.service.ICreditorService;
import com.etl.invest.common.service.IProfitFormService;
import com.etl.invest.mapper.ProfitFormMapper;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.service.IUserAccountService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 11:51:35 <br>
 * <b>description</b>: 投资人收益报表 服务实现 <br>
 */
@Service("profitFormService")
public class ProfitFormServiceImpl extends BaseServiceImpl<ProfitFormMapper, ProfitFormModel> implements IProfitFormService {
  @Override
  protected String getPrimaryKeyName() {
    return "id";
  }

  @Override
  public void cash(long form_id) throws Exception {
    this.cash(this.selectById(form_id, Cluster.master));
  }

  @GlobalTransactional
  @Override
  public void cash(ProfitFormModel profitForm) throws Exception {

    AssertUtils.notNull(profitForm, "收益报表不完整");

    if(profitForm.getStatus().intValue() != 0){
      Utils.throwsBizException("兑付失败，收益报表状态不正确。");
    }

    // 投资人账户增加可用余额
    if(profitForm.getCapital() > 0){
      userAccountService.incoming(
              profitForm.getUser_id(),
              profitForm.getCapital(),
              FundsOperateType.repayment_capital_entry,
              RefTable.profit_form,
              profitForm.getId()
      );
    }
    if(profitForm.getInterest() > 0){
      userAccountService.incoming(
              profitForm.getUser_id(),
              profitForm.getInterest(),
              FundsOperateType.repayment_interest_entry,
              RefTable.profit_form,
              profitForm.getId()
      );
    }

    long current = DateUtils.currentTimeInSecond();

    // 收益报表置为 已还
    ProfitFormModel updateProfitForm = new ProfitFormModel();
    updateProfitForm.setId(profitForm.getId());
    updateProfitForm.setWhere_version(profitForm.getVersion());
    updateProfitForm.setUpdate_time(current);
    updateProfitForm.setStatus(1);
    if( 1 != this.update(updateProfitForm)){
      Utils.throwsBizException("更新收益报表["+profitForm.getId()+"]失败");
    }

    // 更新债权信息
    CreditorModel currentCreditor = creditorService.selectById(profitForm.getCreditor_id(), Cluster.master);
    Map updateCreditor = Utils.newHashMap(
            CreditorModel.ID, profitForm.getCreditor_id(),
            CreditorModel.WHERE_VERSION, currentCreditor.getVersion(),
            CreditorModel.UPDATE_TIME, current,
            CreditorModel.UNPAID_CAPITAL, currentCreditor.getUnpaid_capital() - profitForm.getCapital(),
            CreditorModel.UNPAID_INTEREST, currentCreditor.getUnpaid_interest() - profitForm.getInterest(),
            CreditorModel.SURPLUS_PERIOD, currentCreditor.getSurplus_period() - 1
    );
    // 如果是最后一期还款，债权置为 结束
    if( profitForm.getPeriod().intValue() == currentCreditor.getPeriod().intValue() ){
      updateCreditor.put(CreditorModel.STATUS, -1);
      updateCreditor.put(CreditorModel.END_TIME, current);
    }
    if( 1 != creditorService.updateByMap(updateCreditor)){
      Utils.throwsBizException("更新债权["+profitForm.getCreditor_id()+"]失败");
    }

  }

  @Autowired
  private IUserAccountService userAccountService;

  @Resource
  private ICreditorService creditorService;
}
