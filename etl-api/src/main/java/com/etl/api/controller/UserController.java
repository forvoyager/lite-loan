package com.etl.api.controller;

import com.etl.base.common.dto.ResultDto;
import com.etl.base.common.util.Utils;
import com.etl.user.common.model.UserModel;
import com.etl.user.common.service.IUserService;
import com.etl.user.common.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-31 15:21:00 <br>
 * <b>description</b>: <br>
 */
@RestController
@RequestMapping("/user")
public class UserController extends EtlBaseController {

  @Autowired
  private IUserService userService;

  /**
   * 注册
   * @param user_role 用户角色 0-出借人 1-借款人 2-担保人
   * @param mobileNo 手机号
   * @param pwd 密码（MD5）
   * @return
   * @throws Exception
   */
  @RequestMapping("/signup")
  public ResultDto signUp(int user_role, long mobileNo, String pwd) throws Exception{

    UserModel userModel = userService.signUp(user_role, mobileNo, pwd, super.getAccessChannel());

    return ResultDto.success("注册成功", Utils.newHashMap("user_id", userModel.getUser_id()));
  }

  /**
   * 登陆
   * @param mobileNo 手机号
   * @param pwd 密码（MD5）
   * @return
   * @throws Exception
   */
  @RequestMapping("/signin")
  public ResultDto signIn(long mobileNo, String pwd) throws Exception{

    UserModel userModel = userService.signIn(mobileNo, pwd, super.getAccessChannel());

    return ResultDto.success("登陆成功", Utils.newHashMap(
            "user_id", userModel.getUser_id(),
            "token", UserUtils.getToken(userModel)
    ));
  }
}
