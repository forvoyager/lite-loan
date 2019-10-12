package com.etl.user.mapper;

import com.etl.base.jdbc.mapper.IBaseMapper;
import com.etl.user.common.model.RechargeOrderModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-10-12 10:18:53 <br>
 * <b>description</b>: 用户充值订单 mapper操作 <br>
 */
@Mapper
public interface RechargeOrderMapper extends IBaseMapper<RechargeOrderModel> {
}
