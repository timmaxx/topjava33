package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository extends BaseRepository<User> {
    List<User> getAll();

    // null if not found
    User getByEmail(String email);
}
