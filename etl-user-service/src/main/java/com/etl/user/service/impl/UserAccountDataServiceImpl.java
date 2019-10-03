package com.etl.user.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.user.common.model.UserAccountDataModel;
import com.etl.user.mapper.UserAccountDataMapper;
import com.etl.user.common.service.IUserAccountDataService;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-29 17:46:22 <br>
 * <b>description</b>: 账户流水 服务实现 <br>
 */
@Service("userAccountDataService")
public class UserAccountDataServiceImpl extends BaseServiceImpl<UserAccountDataMapper, UserAccountDataModel> implements IUserAccountDataService {
  @Override
  protected String getPrimaryKeyName() {
    return "id";
  }
}
