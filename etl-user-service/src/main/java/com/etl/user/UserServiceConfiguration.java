package com.etl.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-27 15:44
 * @Description:
 */
@Configuration
@MapperScan(basePackageClasses = {UserServiceConfiguration.class})
public class UserServiceConfiguration {
}
