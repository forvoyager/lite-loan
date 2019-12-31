package com.xr.base.web;

import com.xr.base.web.error.GlobalErrorAttributes;
import com.xr.base.web.error.GlobalErrorController;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <b>description</b>：web全局处理 自动配置 <br>
 * <b>time</b>：2019-02-11 13:29 <br>
 * <b>author</b>：forvoyager@outlook.com
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class BaseWebAutoConfiguration {

  @Bean
  public GlobalErrorAttributes errorAttributes() {
    return new GlobalErrorAttributes();
  }

  @Bean
  public GlobalErrorController defaultErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties, ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
    return new GlobalErrorController(errorAttributes, serverProperties.getError(), errorViewResolversProvider.getIfAvailable());
  }
}
