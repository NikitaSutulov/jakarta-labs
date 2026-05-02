package com.team5.jakarta.data;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private final static String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private final static String USER = "sa";
    private final static String PASSWORD = "";
    private static JdbcDataSource ds = getDs();

    private ConnectionManager() {

    }

    public static Connection get() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JdbcDataSource getDs() {
        if (ds == null) {
            init();
        }
        return ds;
    }

    private static void init() {
        ds = new JdbcDataSource();
        ds.setURL(URL);
        ds.setUser(USER);
        ds.setPassword(PASSWORD);
    }
}
