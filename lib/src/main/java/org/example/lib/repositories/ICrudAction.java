package org.example.lib.repositories;

import java.util.List;

public interface ICrudAction<T, D> { // lop co ban nhat cua 1 hanh dong(them sua xoa)

    void save(T t);

    void delete(T t);

    T findById(D id);

    List<T> findAll();
}