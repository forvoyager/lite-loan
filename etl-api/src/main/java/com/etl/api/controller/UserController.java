package com.etl.api.controller;

import com.etl.base.common.dto.ResultDto;
import com.etl.base.common.util.Utils;
import com.etl.user.common.model.UserModel;
import com.etl.user.common.service.IUserService;
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

  @RequestMapping("/signup")
  public ResultDto signUp(int user_role, long mobile, String pwd) throws Exception{

    UserModel userModel = userService.signUp(user_role, mobile, pwd, super.getAccessChannel());

    return ResultDto.success("注册成功", Utils.newHashMap("user_id", userModel.getUser_id()));
  }

}
