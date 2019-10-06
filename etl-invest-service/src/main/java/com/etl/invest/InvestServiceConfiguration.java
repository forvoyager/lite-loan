package com.etl.invest;

import org.apache.dubbo.remoting.http.servlet.DispatcherServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:11
 * @Description:
 */
@Configuration
@MapperScan(basePackageClasses = {InvestServiceConfiguration.class})
public class InvestServiceConfiguration {

  /**
   * 使用hessian协议暴露服务相关配置：
   * 1、启用此servlet
   * 2、调整server.context-path=/
   * 3、服务协议配置如下：
   *  service.protocol.name=hessian
   *  service.protocol.server=servlet
   *  service.protocol.port=${server.port}
   * @return
   */
  @Bean
  public ServletRegistrationBean hessianServle() {
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new DispatcherServlet(), "/*");
    servletRegistrationBean.setName("Hessian_Dispatcher_Servlet");
    servletRegistrationBean.setLoadOnStartup(1);
    return servletRegistrationBean;
  }

}
