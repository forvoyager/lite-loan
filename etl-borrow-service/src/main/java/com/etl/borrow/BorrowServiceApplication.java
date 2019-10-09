package com.etl.borrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 9:56
 * @Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ImportResource("classpath:spring.xml")
public class BorrowServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(BorrowServiceApplication.class, args);
  }
}
