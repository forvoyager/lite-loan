package com.etl.invest.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.invest.common.model.InvestRecordModel;
import com.etl.invest.mapper.InvestRecordMapper;
import com.etl.invest.service.IInvestRecordService;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 11:51:35 <br>
 * <b>description</b>: 投资记录 服务实现 <br>
 */
@Service("investRecordService")
public class InvestRecordServiceImpl extends BaseServiceImpl<InvestRecordMapper, InvestRecordModel> implements IInvestRecordService {
  @Override
  protected String getPrimaryKeyName() {
    return "invest_id";
  }
}
