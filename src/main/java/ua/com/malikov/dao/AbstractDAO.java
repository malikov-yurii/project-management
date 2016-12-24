package ua.com.malikov.dao;

import java.util.List;

interface AbstractDAO<T> {
    T save(T t);

    void saveAll(List<T> list);

    T load(int id);

    List<T> findAll();

    void delete(T t);

    void deleteById(int id);

    void deleteAll();


}
