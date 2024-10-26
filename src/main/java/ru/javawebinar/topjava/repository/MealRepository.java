package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.function.Predicate;

public interface MealRepository {
    // null if updated object does not belong to userId
    Meal save(int userId, Meal obj);

    // false if object does not belong to userId
    boolean delete(int userId, int id);

    // null if object does not belong to userId
    Meal get(int userId, int id);

    // ORDERED dateTime desc
    Collection<Meal> getAll(int userId);
    Collection<Meal> getFilterByPredicate(int userId, Predicate<Meal> filter);
}
