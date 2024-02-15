package com.dm.bl.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.dm.bl.demo.config.DatabaseConfig.*;
import static com.dm.bl.demo.config.DatabaseConfig.URL;

public class DataSourceProvider {
    private final DataSource dataSource;

     {
        HikariConfig config = new HikariConfig();
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName(DRIVER);
        config.setJdbcUrl(URL);

        dataSource = new HikariDataSource(config);
    }

    public  Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public  DataSource getDataSource(){
        return dataSource;
    }

}
