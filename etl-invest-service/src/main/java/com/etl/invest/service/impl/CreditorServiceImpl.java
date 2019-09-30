package com.etl.invest.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.invest.common.model.CreditorModel;
import com.etl.invest.mapper.CreditorMapper;
import com.etl.invest.service.ICreditorService;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 11:51:35 <br>
 * <b>description</b>: 债权信息 服务实现 <br>
 */
@Service("creditorService")
public class CreditorServiceImpl extends BaseServiceImpl<CreditorMapper, CreditorModel> implements ICreditorService {
  @Override
  protected String getPrimaryKeyName() {
    return "creditor_id";
  }
}
