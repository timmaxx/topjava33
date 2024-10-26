package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {
    {
        MealsUtil.meals.forEach(meal -> save(meal.getUserId(), meal));
    }

    // null if updated object does not belong to userId
    @Override
    public Meal save(int userId, Meal obj) {
        log.info("save {}", obj);
        if (obj.getUserId() != userId) {
            log.info("unsuccessful saving {}, obj.getUserId() = {}, userId = {}", obj, obj.getUserId(), userId);
            return null;
        }

        if (obj.isNew()) {
            obj.setId(counter.incrementAndGet());
            repository.put(obj.getId(), obj);
            return obj;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(obj.getId(), (id, oldObj) -> obj);
    }

    // false if object does not belong to userId
    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        if (repository.get(id) == null || repository.get(id).getUserId() != userId) {
            log.info("unsuccessful deleting {}, repository.get(id).getUserId() = {}, userId = {}", repository.get(id), repository.get(id) == null ? null : repository.get(id).getUserId(), userId);
            return false;
        }

        return repository.remove(id) != null;
    }

    // null if object does not belong to userId
    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        if (repository.get(id) == null || repository.get(id).getUserId() != userId) {
            log.info("unsuccessful getting {}, repository.get(id).getUserId() = {}, userId = {}", repository.get(id), repository.get(id) == null ? null : repository.get(id).getUserId(), userId);
            return null;
        }

        return repository.get(id);
    }

    // ORDERED dateTime desc
    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }
}
