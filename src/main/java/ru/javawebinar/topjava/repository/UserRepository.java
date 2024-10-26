package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository {
    // null if updated object does not belong to userId
    User save(User obj);

    // false if object does not belong to userId
    boolean delete(int id);

    // null if object does not belong to userId
    User get(int id);

    List<User> getAll();

    // null if not found
    User getByEmail(String email);
}
