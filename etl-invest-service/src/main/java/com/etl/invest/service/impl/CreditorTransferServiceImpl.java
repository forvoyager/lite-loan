package com.etl.invest.service.impl;

import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.common.model.CreditorTransferModel;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.common.service.ICreditorService;
import com.etl.invest.common.service.ICreditorTransferService;
import com.etl.invest.common.service.IProfitFormService;
import com.etl.invest.mapper.CreditorTransferMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-05 17:02:07 <br>
 * <b>description</b>: 债权转让信息 服务实现 <br>
 */
@Service("creditorTransferService")
public class CreditorTransferServiceImpl extends BaseServiceImpl<CreditorTransferMapper, CreditorTransferModel> implements ICreditorTransferService {
  @Override
  protected String getPrimaryKeyName() {
    return "id";
  }

  @Transactional
  @Override
  public void apply(long creditor_id, int partition) throws Exception {
    AssertUtils.isTrue(partition > 0, "无效的转让份数");

    CreditorModel creditor = creditorService.selectById(creditor_id, Cluster.slave);
    AssertUtils.notNull(creditor, "债权不存在");
    AssertUtils.isTrue(creditor.getStatus().intValue() == 0, "债权不可转让"); // 0有效状态
    AssertUtils.isTrue(partition <= creditor.getPartition(), "无效的转让份数");

    List<ProfitFormModel> unpaidForm = profitFormService.selectList(Utils.newHashMap(
            ProfitFormModel.CREDITOR_ID, creditor.getId(),
            ProfitFormModel.STATUS, 0
    ), Cluster.slave);
    AssertUtils.notEmpty(unpaidForm, "收益报表信息不正确");

    Long unpaid_capital = 0L;
    Long unpaid_interest = 0L;
    for(ProfitFormModel pfm : unpaidForm){
      unpaid_capital += pfm.getCapital();
      unpaid_interest += pfm.getInterest();
    }

    long current = DateUtils.currentTimeInSecond();
    CreditorModel updateCreditor = new CreditorModel();
    updateCreditor.setId(creditor.getId());
    updateCreditor.setWhere_version(creditor.getVersion());
    updateCreditor.setUpdate_time(current);
    updateCreditor.setStatus(1); // 债权置为 转让中
    if(creditorService.update(updateCreditor) != 1){
      Utils.throwsBizException("债权不可转让");
    }

    // 写入债转记录
    CreditorTransferModel transfer = new CreditorTransferModel();
    transfer.setUser_id(creditor.getUser_id());
    transfer.setCreditor_id(creditor.getId());
    transfer.setBorrow_id(creditor.getBorrow_id());
    transfer.setPartition(partition);
    transfer.setFrozen_partition(0);
    transfer.setDiscount_apr(-0.02); // 默认都折价2%转让
    transfer.setUnpaid_capital(unpaid_capital);
    transfer.setUnpaid_interest(unpaid_interest);
    transfer.setStatus(0);
    transfer.setStart_time(current);
    transfer.setCreate_time(current);
    transfer.setUpdate_time(current);
    transfer.setVersion(0);
    this.insert(transfer);
  }

  @Resource
  private ICreditorService creditorService;

  @Resource
  private IProfitFormService profitFormService;
}
