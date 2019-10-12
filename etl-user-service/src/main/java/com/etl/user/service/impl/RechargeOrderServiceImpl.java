package com.etl.user.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.user.common.model.RechargeOrderModel;
import com.etl.user.common.service.IRechargeOrderService;
import com.etl.user.mapper.RechargeOrderMapper;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-10-12 10:18:53 <br>
 * <b>description</b>: 用户充值订单 服务实现 <br>
 */
@Service("rechargeOrderService")
public class RechargeOrderServiceImpl extends BaseServiceImpl<RechargeOrderMapper, RechargeOrderModel> implements IRechargeOrderService {
  @Override
  protected String getPrimaryKeyName() {
    return "id";
  }
}
