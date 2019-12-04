package com.etl.asset.service.impl;

import com.etl.asset.common.service.IRepaymentFormService;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.asset.common.enums.BorrowStatus;
import com.etl.asset.common.enums.RepaymentMode;
import com.etl.asset.common.model.BorrowModel;
import com.etl.asset.common.model.RepaymentFormModel;
import com.etl.asset.common.service.IBorrowService;
import com.etl.asset.common.util.BorrowUtils;
import com.etl.asset.mapper.BorrowMapper;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 09:30:12 <br>
 * <b>description</b>: 借款项目 服务实现 <br>
 */
@Service("borrowService")
public class BorrowServiceImpl extends BaseServiceImpl<BorrowMapper, BorrowModel> implements IBorrowService {
  @Override
  protected String getPrimaryKeyName() {
    return "borrow_id";
  }

  @Override
  public BorrowModel apply(BorrowModel borrow) throws Exception {
    long current = DateUtils.currentTimeInSecond();
    borrow.setRepayment_mode(RepaymentMode.AVERGE_INEREST.getCode());
    borrow.setStatus(BorrowStatus.CHECK.getCode());
    borrow.setCreate_time(current);
    borrow.setUpdate_time(current);
    return this.insert(borrow);
  }

  @Override
  public void check(long borrow_id) throws Exception {

    BorrowModel borrowModel = this.selectById(borrow_id, Cluster.master);
    AssertUtils.notNull(borrowModel, "标的不存在");

    if( BorrowStatus.CHECK != BorrowStatus.parse(borrowModel.getStatus()) ) {
      Utils.throwsBizException("标的状态不正确");
    }

    // todo 各种审核及验证

    // 审核通过，进入发标流程
    BorrowModel updateModel = new BorrowModel();
    updateModel.setBorrow_id(borrow_id);
    updateModel.setStatus(BorrowStatus.CHECKED.getCode());
    updateModel.setUpdate_time(DateUtils.currentTimeInSecond());
    updateModel.setWhere_version(borrowModel.getVersion());
    if( 1 != this.update(updateModel)){
      Utils.throwsBizException("标的审核失败，请稍后重试。");
    }

  }

  @Override
  public void release(long borrow_id) throws Exception {
    BorrowModel borrowModel = this.selectById(borrow_id, Cluster.master);
    AssertUtils.notNull(borrowModel, "标的不存在");

    if( BorrowStatus.CHECKED != BorrowStatus.parse(borrowModel.getStatus()) ) {
      Utils.throwsBizException("标的状态不正确");
    }

    // todo 各种审核及验证

    // 发标，进入投标流程
    BorrowModel updateModel = new BorrowModel();
    updateModel.setBorrow_id(borrow_id);
    updateModel.setStatus(BorrowStatus.IN_BID.getCode());
    updateModel.setAvailable_amount(borrowModel.getAmount());
    updateModel.setPartition_amount(100*100); // 每份100元
    updateModel.setInvest_start_time(DateUtils.addDay(1)); // 下一日开始可以投标
    updateModel.setUpdate_time(DateUtils.currentTimeInSecond());
    updateModel.setWhere_version(borrowModel.getVersion());
    if( 1 != this.update(updateModel)){
      Utils.throwsBizException("标的审核失败，请稍后重试。");
    }
  }

  @GlobalTransactional
  @Transactional
  @Override
  public void initBorrowerForm(long borrow_id) throws Exception {

    logger.info("tx_xid:{}", RootContext.getXID());
    
    BorrowModel borrowModel = this.selectById(borrow_id, Cluster.master);
    AssertUtils.notNull(borrowModel, "标的不存在");

    if( BorrowStatus.FULL_BID != BorrowStatus.parse(borrowModel.getStatus()) ) {
      Utils.throwsBizException("标的状态不正确");
    }

    // todo 各种验证

    // 生成 借款人 还款报表
    List<RepaymentFormModel> repaymentFormModels = BorrowUtils.buildRepaymentForm(borrowModel);
    repaymentFormService.insertBatch(repaymentFormModels);

    // 标的更新为还款中
    BorrowModel updateModel = new BorrowModel();
    updateModel.setBorrow_id(borrow_id);
    updateModel.setStatus(BorrowStatus.IN_REPAYMENT.getCode());
    updateModel.setUpdate_time(DateUtils.currentTimeInSecond());
    updateModel.setWhere_version(borrowModel.getVersion());
    if( 1 != this.update(updateModel)){
      Utils.throwsBizException("标的审核失败，请稍后重试。");
    }

  }

  @Override
  public void changeAvailableAmount(long borrow_id, long amount) throws Exception {
    
    if(amount == 0){
      return;
    }
    
    long current = DateUtils.currentTimeInSecond();
    
    // 更新标的可投金额
    Map updateModel = Utils.newHashMap(
            BorrowModel.BORROW_ID, borrow_id,
            "change_available_amount", amount,
            BorrowModel.UPDATE_TIME, current
    );
    if( 1 != this.updateByMap(updateModel)){
      Utils.throwsBizException("可投金额不正确，稍后重试。");
    }

    // 更新标的状态
    BorrowModel borrowModel = this.selectById(borrow_id, Cluster.master);
    updateModel = Utils.newHashMap(
            BorrowModel.BORROW_ID, borrow_id,
            BorrowModel.WHERE_VERSION, borrowModel.getVersion()
    );
    
    // 如果是减少金额，在可投金额为0时，标的置为 已满标
    if(borrowModel.getAvailable_amount().longValue() == 0 && BorrowStatus.parse(borrowModel.getStatus()) != BorrowStatus.FULL_BID){
      updateModel.put(BorrowModel.STATUS, BorrowStatus.FULL_BID.getCode());
      updateModel.put(BorrowModel.INVEST_END_TIME, current);
      if( 1 != this.updateByMap(updateModel)){
        Utils.throwsBizException("更新标的状态失败，稍后重试。");
      }
    }
    
    // 如果是增加金额，可能是满标后部分标的投资处理失败，释放出可投金额，需将标的置为 投标中
    if(borrowModel.getAvailable_amount().longValue() > 0 && BorrowStatus.parse(borrowModel.getStatus()) == BorrowStatus.FULL_BID){
      updateModel.put(BorrowModel.STATUS, BorrowStatus.IN_BID.getCode());
      if( 1 != this.updateByMap(updateModel)){
        Utils.throwsBizException("更新标的状态失败，稍后重试。");
      }
    }
    
  }

  @Resource
  private IRepaymentFormService repaymentFormService;
}
