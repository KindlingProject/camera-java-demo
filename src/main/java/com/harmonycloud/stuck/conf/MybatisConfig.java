package com.harmonycloud.stuck.conf;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import java.util.ArrayList;
import java.util.List;


@Configuration
@MapperScan(basePackages = "com.harmonycloud.stuck.mapper")
public class MybatisConfig implements TransactionManagementConfigurer {

    @Bean
    @Primary
    public DruidDataSource druidDataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        List<Filter> filterList = new ArrayList<>();
        filterList.add(wallFilter());
        dataSource.setProxyFilters(filterList);
        return dataSource;
    }

    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig());
        return wallFilter;
    }

    @Bean
    public WallConfig wallConfig() {
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);//允许一次执行多条语句
        config.setNoneBaseStatementAllow(true);//允许非基本语句的其他语句
        return config;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {

        return new DataSourceTransactionManager(druidDataSource());
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        return configuration;
    }
}
