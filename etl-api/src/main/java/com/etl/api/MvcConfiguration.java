package com.etl.api;

import com.etl.api.interceptor.SigninInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2020-01-11 20:45
 * @Description:
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

  @Resource
  private SigninInterceptor signinInterceptor;

  /**
   * 添加拦截器（可以多个）
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(signinInterceptor).addPathPatterns("/**");
  }

}
