//package com.harmonycloud.stuck.conf;
//
//import javax.sql.DataSource;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//@Configuration
//@Component
//public class DataSourceConfig {
//    private static Logger LOG = LogManager.getLogger(DataSourceConfig.class);
//
//    @Value("${datasource.driverClass}")
//    private String driverClass;
//
//    @Value("${datasource.url}")
//    private String jdbcUrl;
//
//    @Value("${datasource.username}")
//    private String user;
//
//    @Value("${datasource.password}")
//    private String password;
//
//    @Value("${datasource.minPool}")
//    private int minPoolSize;
//
//    @Value("${datasource.maxPool}")
//    private int maxPoolSize;
//
//    @Bean(name = "HikariCP")
//    public DataSource getHikariCPDataSource() {
//        try {
//            HikariConfig config = new HikariConfig();
//            config.setDriverClassName(driverClass);
//            config.setJdbcUrl(jdbcUrl);
//            config.setUsername(user);
//            config.setAutoCommit(false);
//            config.setPassword(password);
//            config.setMaximumPoolSize(maxPoolSize);
//            config.setMinimumIdle(minPoolSize);
//            config.setConnectionTimeout(3000L);
////            config.setLeakDetectionThreshold(60000L);
//            config.setPoolName("dataSource_Hikaricp");
//            return new HikariDataSource(config);
//        } catch (Throwable cause) {
//            // sybase初始化会报错
//            LOG.error("Create HikariCP failed.", cause);
//            return null;
//        }
//    }
//}
