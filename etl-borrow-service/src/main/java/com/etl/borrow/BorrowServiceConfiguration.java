package com.etl.borrow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 9:57
 * @Description:
 */
@Configuration
@MapperScan(basePackageClasses = {BorrowServiceConfiguration.class})
public class BorrowServiceConfiguration {
}
