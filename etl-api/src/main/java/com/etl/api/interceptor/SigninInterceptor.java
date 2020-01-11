package com.etl.api.interceptor;

import com.etl.base.common.enums.AccessChannel;
import com.etl.base.common.util.StringUtils;
import com.etl.base.common.util.Utils;
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

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String c_str = request.getParameter("c");
    String v = request.getParameter("v");

    AccessChannel accessChannel = AccessChannel.parse(c_str);

    // 访问的url，不包含server.servlet.context-path对应的路径
    String uri = request.getRequestURI();
    // 地址可能有多个/，多余的删除，只保留1个
    uri = uri.replaceAll("/+", "/");

    if(isNeedSingin(uri)){
      String user_id_str = request.getParameter("user_id");
      String token = request.getParameter("token");
      if(StringUtils.isEmpty(user_id_str) || StringUtils.isEmpty(token) ){
        Utils.throwsBizException("请先登录");
      }

      // 验证user_id token是否正确 todo

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
    noNeedSigninUrl.add("/error");
    noNeedSigninUrl.add("/user/signup");
  }
}
