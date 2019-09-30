package com.etl.invest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 14:10
 * @Description:
 */
@SpringBootApplication
@ImportResource("classpath:spring.xml")
public class InvestServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(InvestServiceApplication.class, args);
  }
}
