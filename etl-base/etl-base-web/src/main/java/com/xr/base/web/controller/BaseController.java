package com.xr.base.web.controller;

import com.etl.base.common.enums.AccessChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Time: 2019-04-04 19:09
 * @Author: forvoyager@outlook.com
 * @Description: base Controller
 */
public abstract class BaseController {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  protected HttpServletRequest request;

  @Autowired
  protected HttpServletResponse response;

  /**
   * 获取访问渠道
   *
   * @return
   * @throws Exception
   */
  protected AccessChannel getAccessChannel() throws Exception{
    return AccessChannel.parse(request.getParameter("c"));
  }
}
