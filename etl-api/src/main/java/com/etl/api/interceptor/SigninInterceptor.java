package com.etl.api.interceptor;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.StringUtils;
import com.etl.base.common.util.Utils;
import com.etl.user.common.model.UserModel;
import com.etl.user.common.service.IUserService;
import com.etl.user.common.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2020-01-11 20:50:00
 * @Description: 登录拦截器，验证访问的url是否需要登录
 */
@Component
public class SigninInterceptor extends HandlerInterceptorAdapter{

  @Autowired
  private IUserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    // 访问的url，不包含server.servlet.context-path对应的路径
    String uri = request.getRequestURI();
    // 地址可能有多个/，多余的删除，只保留1个
    uri = uri.replaceAll("/+", "/");

    if(isNeedSingin(uri)){

      // 访问渠道，详见AccessChannel
      AccessChannel accessChannel = AccessChannel.parse(request.getParameter("c"));
      // 版本信息
      String v = request.getParameter("v");

      String user_id_str = request.getParameter("user_id");
      String token = request.getParameter("token");
      if(StringUtils.isEmpty(user_id_str) || StringUtils.isEmpty(token) ){
        Utils.throwsBizException("请先登录");
      }

      // 验证user_id token是否正确
      UserModel siginUser = userService.selectById(user_id_str, Cluster.slave);
      if(siginUser == null || !UserUtils.getToken(siginUser).equals(token)){
        Utils.throwsBizException("用户名或密码错误");
      }

      request.setAttribute("LOGIN_USER", siginUser);
    }

    return true;
  }

  /**
   * 判断uri是否需要登录
   * @param uri
   * @return
   */
  private boolean isNeedSingin(String uri) {
    if(noNeedSigninUrl.contains(uri)){
      return false;
    }
    return true;
  }

  // 不需要登录的uri
  private static final List<String> noNeedSigninUrl = new ArrayList<String>();
  static{
    // 系统uri
    noNeedSigninUrl.add("/");
    noNeedSigninUrl.add("/index.html");
    noNeedSigninUrl.add("/error");

    // 用户自定义uri
    noNeedSigninUrl.add("/user/signup");
    noNeedSigninUrl.add("/user/signin");
  }
}
