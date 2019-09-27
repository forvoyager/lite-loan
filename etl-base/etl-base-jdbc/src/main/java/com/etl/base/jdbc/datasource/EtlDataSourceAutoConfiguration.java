package com.etl.base.jdbc.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源自动配置
 */
@Configuration
public class EtlDataSourceAutoConfiguration {

  @Bean
  @Primary
  public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  @Primary
  public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    // set data source
    sessionFactory.setDataSource(dataSource);

    // set config location
    sessionFactory.setConfigLocation(new ClassPathResource("/mybatis/mybatis-config.xml"));

    // set mapper xml location
    Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mapper/*.xml");
    sessionFactory.setMapperLocations(resources);

    return sessionFactory.getObject();
  }

  private Logger logger = LoggerFactory.getLogger(getClass());

  public EtlDataSourceAutoConfiguration(){
    logger.info("init datasource...");
  }
}
