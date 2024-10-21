package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRepository repository;

    @Override
    public void init() {
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        repository.save(meal).getId();
        log.info("Actual {}", meal);

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "getAll" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                if (!repository.delete(id)) {
                    log.warn("Meal with id={} not found", id);
                }
                response.sendRedirect("meals");
                break;
            case "create":
                final Meal mealForCreating = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
                log.info("Prepare meal for inserting with default fields {}", mealForCreating);
                request.setAttribute("meal", mealForCreating);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "update":
                int idForUpdating = getId(request);
                final Meal mealForUpdating = repository.get(idForUpdating);
                if (mealForUpdating == null) {
                    log.warn("Meal with id={} is not exists", idForUpdating);
                    response.sendRedirect("meals");
                } else {
                    log.info("Prepare meal for updating {}", mealForUpdating);
                    request.setAttribute("meal", mealForUpdating);
                    request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                }
                break;
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        MealsUtil.getTos(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId;
        try {
            paramId = Objects.requireNonNull(request.getParameter("id"));
        } catch (NullPointerException npe) {
            log.error("Wrong request. There is no param 'id' in request");
            throw npe;
        }

        try {
            return Integer.parseInt(paramId);
        } catch (NumberFormatException nfe) {
            log.error("Wrong request. paramId={} is not integer", paramId);
            throw nfe;
        }
    }
}
