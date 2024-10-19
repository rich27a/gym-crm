package com.example.gym.dao;

import com.example.gym.models.Storage;

import java.util.List;

public interface GenericDAO<T> {
    void save(T entity);
    T findById(int id);
    List<T> findAll();
    void delete(int id);
    void setStorage(Storage storage);
}
