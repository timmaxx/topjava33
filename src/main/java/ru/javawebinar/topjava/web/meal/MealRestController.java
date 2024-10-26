package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.*;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getFilterByPredicate(Predicate<Meal> filter) {
        log.info("getFilterByPredicate");
        return MealsUtil.getTos(service.getFilterByPredicate(authUserId(), filter), authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {} for user {}", id, authUserId());
        return service.get(authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("create {} for user {}", meal, authUserId());
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("delete {} for user {}", id, authUserId());
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} for user {}", meal, id, authUserId());
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}
