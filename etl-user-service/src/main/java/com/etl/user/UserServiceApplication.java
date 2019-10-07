package com.etl.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-27 14:17
 * @Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ImportResource("classpath:spring.xml")
public class UserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserServiceApplication.class, args);
  }

}
