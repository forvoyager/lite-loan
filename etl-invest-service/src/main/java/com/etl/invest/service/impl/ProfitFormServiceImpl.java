package com.etl.invest.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.invest.common.model.ProfitFormModel;
import com.etl.invest.mapper.ProfitFormMapper;
import com.etl.invest.service.IProfitFormService;
import org.springframework.stereotype.Service;

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
}
