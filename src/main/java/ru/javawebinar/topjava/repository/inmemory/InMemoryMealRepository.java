package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;

public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {
    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }
}
