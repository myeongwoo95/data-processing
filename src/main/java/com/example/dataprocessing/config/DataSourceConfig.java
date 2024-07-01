package com.example.dataprocessing.config;

import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class DataSourceConfig {

    private final  DataSource dataSource;

    @PostConstruct
    public void logHikariConfig() {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            HikariConfigMXBean configMXBean = hikariDataSource.getHikariConfigMXBean();

            System.out.println();
            System.out.println("HikariCP Configuration:");
            System.out.println("connectionTimeout: " + configMXBean.getConnectionTimeout());
            System.out.println("minimumIdle: " + configMXBean.getMinimumIdle());
            System.out.println("maximumPoolSize: " + configMXBean.getMaximumPoolSize());
            System.out.println("idleTimeout: " + configMXBean.getIdleTimeout());
            System.out.println("maxLifetime: " + configMXBean.getMaxLifetime());
        } else {
            System.out.println("DataSource is not an instance of HikariDataSource");
        }

    }

}
