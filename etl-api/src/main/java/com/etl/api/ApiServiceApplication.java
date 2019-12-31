package com.etl.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-12-16 18:10
 * @Description:
 */
@SpringBootApplication
@ImportResource("classpath:spring.xml")
public class ApiServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiServiceApplication.class, args);
  }

}
