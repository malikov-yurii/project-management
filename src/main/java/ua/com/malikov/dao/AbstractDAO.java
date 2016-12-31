package ua.com.malikov.dao;

import java.util.List;

interface AbstractDAO<T> {
    T save(T t);

    void saveAll(List<T> list);

    T load(int id);

    T load(String name);

    List<T> findAll();

    void deleteById(int id);

    void deleteAll();


}
