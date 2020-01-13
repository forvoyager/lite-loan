package com.etl.user.service.impl;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.RandomUtils;
import com.etl.base.common.util.Utils;
import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.user.common.model.UserAccountModel;
import com.etl.user.common.model.UserModel;
import com.etl.user.common.service.IUserAccountService;
import com.etl.user.mapper.UserMapper;
import com.etl.user.common.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
  public UserModel signUp(int user_role, long mobileNo, String pwd, AccessChannel channel) throws Exception {
    // 默认
    if (channel == null) { channel = AccessChannel.PC; }

    AssertUtils.notEmpty(pwd, "密码不能为空");
    if(String.valueOf(mobileNo).length() != 11){
      Utils.throwsBizException("非法的手机号");
    }

    UserModel user = this.selectOne(Utils.newHashMap(UserModel.MOBILE_NUMBER, mobileNo), Cluster.master);
    AssertUtils.isNull(user, "手机号已被注册");

    long current = DateUtils.currentTimeInSecond();
    
    user = new UserModel();
    user.setRegistry_channel(channel.getCode());
    user.setEncrypt_salt(RandomUtils.netLetterString(6));
    user.setPassword(Utils.md5(pwd + user.getEncrypt_salt()));
    user.setMobile_number(mobileNo);
    user.setUser_name("ETL_" + System.currentTimeMillis());
    user.setUser_role(user_role);
    user.setLast_signin_time(current);
    user.setCreate_time(current);
    user.setUpdate_time(current);
    
    user = this.insert(user);

    // TODO: 2019/9/29 发送注册成功消息
    
    // 初始化账户信息
    UserAccountModel account = new UserAccountModel();
    account.setUser_id(user.getUser_id());
    account.setAvailable(0L);
    account.setFrozen(0L);
    account.setCreate_time(current);
    account.setUpdate_time(current);
    account.setVersion(0);
    userAccountService.insert(account);
    
    return user;
  }

  @Override
  public UserModel signIn(long mobileNo, String pwd, AccessChannel channel) throws Exception {
    
    AssertUtils.notEmpty(pwd, "请输入密码");

    UserModel user = this.selectOne(Utils.newHashMap(UserModel.MOBILE_NUMBER, mobileNo), Cluster.master);
    if(user != null){
      if( Utils.md5(pwd + user.getEncrypt_salt()).equals(user.getPassword())){
        
        // 密码验证通过，更新最后登陆时间
        long current = DateUtils.currentTimeInSecond();
        UserModel updateUser = new UserModel();
        updateUser.setUser_id(user.getUser_id());
        updateUser.setLast_signin_time(current);
        updateUser.setUpdate_time(current);
        if(1 != this.update(updateUser)){
          Utils.throwsBizException("登录失败，请稍后重试。");
        }

        user.setLast_signin_time(current);
        return user;
      }
    }
    
    Utils.throwsBizException("帐号或密码不正确");
    return null;
  }

  @Override
  public void signOut(long user_id) throws Exception {
    // 更新一下最后登陆时间，让token失效
    long current = DateUtils.currentTimeInSecond();
    UserModel updateUser = new UserModel();
    updateUser.setUser_id(user_id);
    updateUser.setLast_signin_time(current);
    updateUser.setUpdate_time(current);
    if(1 != this.update(updateUser)){
      Utils.throwsBizException("登录失败，请稍后重试。");
    }
  }
  
  @Resource
  private IUserAccountService userAccountService;
}
