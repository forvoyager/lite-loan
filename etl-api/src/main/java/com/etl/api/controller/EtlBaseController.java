package com.etl.api.controller;

import com.etl.base.common.constant.Constants;
import com.etl.base.common.util.AssertUtils;
import com.etl.user.common.model.UserModel;
import com.xr.base.web.controller.BaseController;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-31 18:01:00 <br>
 * <b>description</b>: <br>
 */
public class EtlBaseController extends BaseController {

  protected UserModel getSessionUser() throws Exception{
    Object obj = request.getAttribute(Constants.LOGIN_USER);
    AssertUtils.notNull(obj, "请先登陆");
    return (UserModel)obj;
  }

  protected Long getSessionUserId() throws Exception{
    return this.getSessionUser().getUser_id();
  }
}
