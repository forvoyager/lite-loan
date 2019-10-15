package com.etl.asset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 9:56
 * @Description: 资产端 服务
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ImportResource("classpath:spring.xml")
public class AssetServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(AssetServiceApplication.class, args);
  }
}
