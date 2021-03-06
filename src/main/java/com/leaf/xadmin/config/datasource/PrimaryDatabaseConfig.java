package com.leaf.xadmin.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.leaf.xadmin.config.MybatisPlusConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author leaf
 * <p>date: 2018-03-05 18:24</p>
 * <p>version: 1.0</p>
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:druid/druid.properties"})
@MapperScan(basePackages = "com.leaf.xadmin.mapper.bg", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryDatabaseConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "primary")
    public DruidDataSource customDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager customTransactionManager(@Qualifier("primaryDataSource")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate customSqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(value = "primarySqlSessionFactory")
    public SqlSessionFactory customSqlSessionFactory(@Qualifier(value = "primaryDataSource") DataSource dataSource) throws Exception {
        return MybatisPlusConfig.createSqlSessionFactory(dataSource);
    }
}
