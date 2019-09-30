package com.etl.user.service.impl;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.RandomUtils;
import com.etl.base.common.util.Utils;
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

  @Override
  public UserModel signUp(int user_role, long mobileNumber, String pwd, AccessChannel channel) throws Exception {
    // 默认
    if (channel == null) { channel = AccessChannel.PC; }

    AssertUtils.notEmpty(pwd, "密码不能为空");

    UserModel user = this.selectOne(Utils.newHashMap(UserModel.MOBILE_NUMBER, mobileNumber), Cluster.master);
    AssertUtils.isNull(user, "手机号已被注册");

    long current = DateUtils.currentTimeInSecond();
    
    user = new UserModel();
    user.setRegistry_channel(channel.getCode());
    user.setEncrypt_salt(RandomUtils.netLetterString(6));
    user.setPassword(Utils.md5(pwd + user.getEncrypt_salt()));
    user.setMobile_number(mobileNumber);
    user.setUser_name("ETL_" + System.currentTimeMillis());
    user.setUser_role(user_role);
    user.setCreate_time(current);
    user.setUpdate_time(current);
    
    user = this.insert(user);

    // TODO: 2019/9/29 发送注册成功消息
    
    return user;
  }

  @Override
  public UserModel signIn(long mobileNumber, String pwd) throws Exception {
    
    AssertUtils.notEmpty(pwd, "请输入密码");

    UserModel user = this.selectOne(Utils.newHashMap(UserModel.MOBILE_NUMBER, mobileNumber), Cluster.master);
    if(user != null){
      if( Utils.md5(pwd + user.getEncrypt_salt()).equals(user.getPassword())){
        return user;
      }
    }
    
    Utils.throwsBizException("帐号或密码不正确");
    return user;
  }

  @Override
  public void signOut(long user_id) throws Exception {
    // TODO: 2019/9/29  
  }
}
