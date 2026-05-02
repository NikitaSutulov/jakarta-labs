package com.team5.jakarta.data;

import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;


import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static DataSource ds;

    static {
        init();
    }

    private ConnectionManager() {

    }

    public static Connection get() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource getDs() {
        return ds;
    }

    private static void init() {
        Properties props = new Properties();

        try (InputStream is = ConnectionManager.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (is == null) {
                throw new RuntimeException("Файл db.properties не знайдено у resources!");
            }
            props.load(is);

        } catch (IOException e) {
            throw new RuntimeException("Помилка завантаження db.properties", e);
        }

        URL = props.getProperty("db.url");
        USER = props.getProperty("db.user");
        PASSWORD = props.getProperty("db.password");

        if (URL.contains("jdbc:h2:")) {
            JdbcDataSource h2Ds = new JdbcDataSource();
            h2Ds.setURL(URL);
            h2Ds.setUser(USER);
            h2Ds.setPassword(PASSWORD);
            ds = h2Ds;
        } else if (URL.contains("jdbc:postgresql:")) {
            PGSimpleDataSource pgDs = new PGSimpleDataSource();
            pgDs.setUrl(URL);
            pgDs.setUser(USER);
            pgDs.setPassword(PASSWORD);
            ds = pgDs;
        } else {
            throw new RuntimeException("Непідтримуваний тип БД у URL: " + URL);
        }
    }

    public static String getURL() {
        return URL;
    }
}
