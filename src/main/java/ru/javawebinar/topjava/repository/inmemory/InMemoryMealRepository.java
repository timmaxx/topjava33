package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {
    {
        MealsUtil.meals.forEach(this::save);
    }

    // null if updated object does not belong to userId
    @Override
    public Meal save(Meal obj) {
        if (obj.getUserId() != authUserId()) {
            log.info("unsuccessful saving {}, userId() = {}, authUserId() = {}", obj, obj.getUserId(), authUserId());
            return null;
        }
        return super.save(obj);
    }

    // false if object does not belong to userId
    @Override
    public boolean delete(int id) {
        if (repository.get(id) == null || repository.get(id).getUserId() != authUserId()) {
            log.info("unsuccessful deleting {}, userId() = {}, authUserId() = {}", repository.get(id), repository.get(id) == null ? null : repository.get(id).getUserId(), authUserId());
            return false;
        }
        return super.delete(id);
    }

    // null if object does not belong to userId
    @Override
    public Meal get(int id) {
        if (repository.get(id) == null || repository.get(id).getUserId() != authUserId()) {
            log.info("unsuccessful getting {}, userId() = {}, authUserId() = {}", repository.get(id), repository.get(id) == null ? null : repository.get(id).getUserId(), authUserId());
            return null;
        }
        return super.get(id);
    }

    // ORDERED dateTime desc
    @Override
    public Collection<Meal> getAll() {
        log.info("getAll");
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == authUserId())
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }
}
