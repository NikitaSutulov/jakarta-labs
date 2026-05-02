package com.team5.jakarta.dao;

import com.team5.jakarta.data.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    default Connection getConnection() throws SQLException {
        return ConnectionManager.get();
    }

    List<T> findAll();
    Optional<T> findById(int id);
    T save(T t);
    boolean delete(int id);
    boolean delete(T t);
    boolean update(T t);
    int countAll();
}
