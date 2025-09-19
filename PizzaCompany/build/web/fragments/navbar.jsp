<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/navbar.css"/>
<nav class="navbar">
    <ul>
        <li class="brand"><a href="HomeController">PizzaStore</a></li>
        <!-- Lien ket luon hien thi -->
        <li><a href="HomeController">Pizzas</a></li>
        <li><a href="categories.jsp">Categories</a></li>
        <li><a href="reviews.jsp">Reviews</a></li>

        <!-- Bo sung cho Normal User -->
        <c:if test="${sessionScope.account != null && sessionScope.account.type == 2}">
            <li><a href="orders-history.jsp">Orders history</a></li>
        </c:if>

        <!-- Bo sung cho Admin -->
        <c:if test="${sessionScope.account != null && sessionScope.account.type == 1}">
            <li style="color:#fff; padding:14px 20px;">Welcome, ${sessionScope.account.userName}</li>
        </c:if>

        <!-- Khu vuc nut hanh dong ben phai -->
        <c:choose>
            <c:when test="${sessionScope.account == null}">
                <li class="right"><a href="register.jsp">Register</a></li>
                <li class="right"><a href="login.jsp">Log In</a></li>
            </c:when>
            <c:otherwise>
                <li class="right"><a href="LogoutController">Log Out</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>
