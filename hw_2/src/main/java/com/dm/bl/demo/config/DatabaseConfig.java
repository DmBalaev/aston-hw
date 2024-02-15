package com.dm.bl.demo.config;

import lombok.Data;

@Data
public class DatabaseConfig {
    public static String URL = "jdbc:postgresql://localhost:5432/test";
    public static String USER = "test";
    public static String PASSWORD = "test";
    public static String DRIVER = "org.postgresql.Driver";
}