<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>

    <div class="card border-dark">
        <div class="card-body pb-0">
            <form method="get" action="meals">
                <input type="hidden" name="action" value="filter">
                <div class="row">
                    <div class="col-2">
                        <label for="startDate">From date (inclusive)</label>
                        <input class="form-control" name="startDate" id="startDate" autocomplete="off">
                    </div>
                    <div class="col-2">
                        <label for="endDate">To date (inclusive)</label>
                        <input class="form-control" name="endDate" id="endDate" autocomplete="off">
                    </div>
                    <div class="offset-2 col-3">
                        <label for="startTime">From time (inclusive)</label>
                        <input class="form-control" name="startTime" id="startTime" autocomplete="off">
                    </div>
                    <div class="col-3">
                        <label for="endTime">To time (exclusive)</label>
                        <input class="form-control" name="endTime" id="endTime" autocomplete="off">
                    </div>
                </div>
                <button type="submit">Filter</button>
            </form>
        </div>
    </div>

    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>