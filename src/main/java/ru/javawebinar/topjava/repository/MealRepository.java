package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

// TODO add userId
public interface MealRepository extends BaseRepository<Meal> {
    // ORDERED dateTime desc
    Collection<Meal> getAll();
}
