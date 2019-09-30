package com.etl.invest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:11
 * @Description:
 */
@Configuration
@MapperScan(basePackageClasses = {InvestServiceConfiguration.class})
public class InvestServiceConfiguration {
}
