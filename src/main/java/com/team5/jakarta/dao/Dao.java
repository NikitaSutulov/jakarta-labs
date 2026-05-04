package com.team5.jakarta.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll();
    Optional<T> findById(int id);
    T save(T t);
    boolean delete(int id);
    boolean delete(T t);
    boolean update(T t);
    int countAll();
}
