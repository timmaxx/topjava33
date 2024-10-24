package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.AbstractBaseEntity;

public interface BaseRepository<T extends AbstractBaseEntity> {
    // null if updated object does not belong to userId
    T save(T obj);

    // false if object does not belong to userId
    boolean delete(int id);

    // null if object does not belong to userId
    T get(int id);
}
