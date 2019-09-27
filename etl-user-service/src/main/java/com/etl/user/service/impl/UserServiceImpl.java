package com.etl.user.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.user.common.model.UserModel;
import com.etl.user.mapper.UserMapper;
import com.etl.user.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-27 14:05:16 <br>
 * <b>description</b>: 用户 服务实现 <br>
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserModel> implements IUserService {
  @Override
  protected String getPrimaryKeyName() {
    return "user_id";
  }
}
