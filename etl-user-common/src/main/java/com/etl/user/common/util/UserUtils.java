package com.etl.user.common.util;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.Utils;
import com.etl.user.common.model.UserModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-02 18:00:00 <br>
 * <b>description</b>: 用户操作工具类<br>
 */
public final class UserUtils {

  /**
   * 生成token
   * 只允许一个端登陆，重新登陆后原来的token将失效。
   *
   * @param user
   * @return
   * @throws Exception
   */
  public static final String getToken(UserModel user) throws Exception{
    StringBuffer str = new StringBuffer();
    str.append(user.getUser_id());
    str.append(user.getPassword());
    str.append(user.getEncrypt_salt());
    str.append(user.getLast_signin_time());

    return Utils.md5(str.toString());
  }

}
