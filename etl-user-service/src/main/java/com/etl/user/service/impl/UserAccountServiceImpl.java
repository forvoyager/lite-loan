package com.etl.user.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.user.common.model.UserAccountModel;
import com.etl.user.mapper.UserAccountMapper;
import com.etl.user.common.service.IUserAccountService;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-29 17:46:22 <br>
 * <b>description</b>: 用户账户 服务实现 <br>
 */
@Service("userAccountService")
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccountMapper, UserAccountModel> implements IUserAccountService {
  @Override
  protected String getPrimaryKeyName() {
    return "user_id";
  }
}
