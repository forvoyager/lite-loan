package com.etl.base.jdbc.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 数据源自动配置
 */
@Configuration
public class EtlDataSourceAutoConfiguration {

  @Value("${spring.application.name}")
  private String applicationId;

  @Value("${seata.tx-service-group}")
  private String txGroup;

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DruidDataSource druidDataSource() {
    DruidDataSource druidDataSource = new DruidDataSource();
    return druidDataSource;
  }

  @Primary
  @Bean
  public DataSourceProxy dataSourceProxy(DruidDataSource druidDataSource) {
    return new DataSourceProxy(druidDataSource);
  }

  @Bean
  public DataSourceTransactionManager transactionManager(DataSourceProxy dataSourceProxy) {
    return new DataSourceTransactionManager(dataSourceProxy);
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    // set data source
    sessionFactory.setDataSource(dataSourceProxy);

    // set config location
    sessionFactory.setConfigLocation(new ClassPathResource("/mybatis/mybatis-config.xml"));

    // set mapper xml location
    Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mapper/*.xml");
    sessionFactory.setMapperLocations(resources);

    // set tx manager
    sessionFactory.setTransactionFactory(new SpringManagedTransactionFactory());

    return sessionFactory.getObject();
  }

  /**
   * init global transaction scanner
   *
   * @Return: GlobalTransactionScanner
   */
  @Bean
  public GlobalTransactionScanner globalTransactionScanner(){
    return new GlobalTransactionScanner(this.applicationId, txGroup);
  }

  private Logger logger = LoggerFactory.getLogger(getClass());

  public EtlDataSourceAutoConfiguration(){
    logger.info("init data source...");
  }
}
