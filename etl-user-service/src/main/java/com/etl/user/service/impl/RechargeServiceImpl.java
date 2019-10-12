package com.etl.user.service.impl;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.StringUtils;
import com.etl.base.common.util.Utils;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.model.RechargeOrderModel;
import com.etl.user.common.service.IRechargeOrderService;
import com.etl.user.common.service.IRechargeService;
import com.etl.user.common.service.IUserAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-12 10:41
 * @Description: 充值服务 服务实现
 */
@Service("rechargeService")
public class RechargeServiceImpl implements IRechargeService {
  
  @Resource
  private IRechargeOrderService rechargeOrderService;
  
  @Resource
  private IUserAccountService userAccountService;
  
  @Override
  public long initOrder(long user_id, long amount, AccessChannel channel) throws Exception {

    AssertUtils.isTrue( amount > 0, "充值金额不正确");
    
    if (channel == null) { channel = AccessChannel.PC; }
    long current = DateUtils.currentTimeInSecond();

    RechargeOrderModel rechargeOrder = new RechargeOrderModel();
    rechargeOrder.setUser_id(user_id);
    rechargeOrder.setAmount(amount);
    rechargeOrder.setStatus(0);
    rechargeOrder.setChannel(channel.getCode());
    rechargeOrder.setCreate_time(current);
    rechargeOrder.setUpdate_time(current);
    rechargeOrder = this.rechargeOrderService.insert(rechargeOrder);
    
    return rechargeOrder.getId();
  }

  @Transactional
  @Override
  public void callback(long order_id, int status, String trace_id) throws Exception {

    AssertUtils.notEmpty(trace_id, "第三方交易流水号不正确");
    
    // status 1成功 2失败
    if(status != 1 && status != 2){
      Utils.throwsBizException("未知的订单状态");
    }

    RechargeOrderModel rechargeOrder = this.rechargeOrderService.selectById(order_id, Cluster.master);
    AssertUtils.notNull(rechargeOrder, "充值订单不存在");

    if(rechargeOrder.getStatus().intValue() != 0){
      Utils.throwsBizException("充值订单["+order_id+"]已被处理，不可重复处理。");
    }
    
    if(status == 1){
      // 成功 给账户加钱
      userAccountService.changeAvailable(
              rechargeOrder.getUser_id(), 
              rechargeOrder.getAmount(),
              FundsOperateType.recharge_online,
              RefTable.recharge_order,
              rechargeOrder.getId()
      );
    }

    RechargeOrderModel updateOrder = new RechargeOrderModel();
    updateOrder.setId(rechargeOrder.getId());
    updateOrder.setWhere_version(rechargeOrder.getVersion());
    updateOrder.setUpdate_time(DateUtils.currentTimeInSecond());
    updateOrder.setStatus(status);
    if(StringUtils.isNotEmpty(trace_id)){
      updateOrder.setTrace_id(trace_id);
    }
    if(1 != this.rechargeOrderService.update(updateOrder)){
      Utils.throwsBizException("更新充值订单["+order_id+"]失败。");
    }
    
  }
}
