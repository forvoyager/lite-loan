package com.etl.user.common.service;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.jdbc.service.IBaseService;
import com.etl.user.common.model.UserModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-27 14:05:16 <br>
 * <b>description</b>: 用户 服务定义 <br>
 */
public interface IUserService extends IBaseService<UserModel> {

  /**
   * 注册
   * @param user_role
   * @param mobileNumber
   * @param pwd
   * @param channel
   * @return
   * @throws Exception
   */
  UserModel signUp(int user_role, long mobileNumber, String pwd, AccessChannel channel) throws Exception;

  /**
   * 登录
   * @param mobileNumber
   * @param pwd
   * @return
   * @throws Exception
   */
  UserModel signIn(long mobileNumber, String pwd) throws Exception;

  /**
   * 退出
   * @param user_id
   * @throws Exception
   */
  void signOut(long user_id) throws Exception;
  
}
