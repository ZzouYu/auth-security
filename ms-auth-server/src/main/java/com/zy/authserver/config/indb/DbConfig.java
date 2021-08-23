package com.zy.authserver.config.indb;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author: zy
 * @date: 2021/8/9 下午2:20
 * @description:
 */
@Configuration
@MapperScan(basePackages = "com.zy.authserver.config.role.mapper")
public class DbConfig {

    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() {
        return new DruidDataSource();
    }
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
            .getResources("classpath:mybatis/**/*.xml"));
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.zy.authserver.config.role.entity");
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        if(null!= sqlSessionFactory) {
            sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        }
        return sqlSessionFactory;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    public DataSourceTransactionManager inquiryTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
