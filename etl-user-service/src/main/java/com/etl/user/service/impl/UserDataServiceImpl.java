package com.etl.user.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.user.common.model.UserDataModel;
import com.etl.user.mapper.UserDataMapper;
import com.etl.user.service.IUserDataService;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-27 14:05:16 <br>
 * <b>description</b>: 用户信息 服务实现 <br>
 */
@Service("userDataService")
public class UserDataServiceImpl extends BaseServiceImpl<UserDataMapper, UserDataModel> implements IUserDataService {
  @Override
  protected String getPrimaryKeyName() {
    return "user_id";
  }
}
