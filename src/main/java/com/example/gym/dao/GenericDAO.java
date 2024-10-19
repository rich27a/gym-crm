package com.example.gym.dao;

import com.example.gym.models.Storage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericDAO<T> {
    void save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    void delete(int id);

}
