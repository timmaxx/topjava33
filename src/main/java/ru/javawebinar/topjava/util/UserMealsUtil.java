package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsToByCycles = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByCycles.forEach(System.out::println);

        List<UserMealWithExcess> mealsToByCyclesFromTopJava = filteredByCyclesFromTopJava(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByCyclesFromTopJava.forEach(System.out::println);

        // System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        List<UserMealWithExcess> mealsToByStreams = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByStreams.forEach(System.out::println);

        List<UserMealWithExcess> mealsToByStreamsFromTopJava = filteredByStreamsFromTopJava(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByStreamsFromTopJava.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> localDate_Integer_Map = new HashMap<>();
        for (UserMeal userMeal : meals) {
            localDate_Integer_Map.put(
                    userMeal.getDate(),
                    userMeal.getCalories() + localDate_Integer_Map.getOrDefault(userMeal.getDate(), 0)
            );
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (isBetweenHalfOpen(userMeal.getTime(), startTime, endTime)) {
                result.add(
                        new UserMealWithExcess(
                                userMeal,
                                localDate_Integer_Map.get(userMeal.getDate()) > caloriesPerDay
                        )
                );
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByCyclesFromTopJava(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(
                        meal.getDate(),
                        meal.getCalories(),
                        Integer::sum
                )
        );

        final List<UserMealWithExcess> mealsTo = new ArrayList<>();
        meals.forEach(meal -> {
            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                mealsTo.add(
                        createTo(
                                meal,
                                caloriesSumByDate.get(meal.getDate()) > caloriesPerDay
                        )
                );
            }
        });
        return mealsTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> localDate_Integer_Map = meals
                .stream()
                .collect(
                        Collectors.toMap(
                                UserMeal::getDate,      //  m -> m.getDate(),
                                UserMeal::getCalories,  //  m -> m.getCalories(),
                                Integer::sum            //  (c1, c2) -> c1 + c2
                        )
                );

        return meals
                .stream()
                .filter(m -> isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal,
                        localDate_Integer_Map.get(meal.getDate()) > caloriesPerDay)
                )
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsFromTopJava(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(
                        meal,
                        caloriesSumByDate.get(meal.getDate()) > caloriesPerDay)
                )
                .collect(Collectors.toList());
    }

    //  Этот метод для упрощения предложен разработчиками TopJava. Я сделал альтернативный конструктор UserMealWithExcess.
    private static UserMealWithExcess createTo(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
