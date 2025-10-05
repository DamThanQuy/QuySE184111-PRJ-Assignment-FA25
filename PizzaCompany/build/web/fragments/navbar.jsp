<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/navbar.css"/>

<nav class="navbar">

    <style>
        /* Dropdown minimal styles (local to navbar) */
        .navbar .dropdown { position: relative; }
        .navbar .dropdown > button { background: none; border: none; color: #fff; padding: 14px 20px; cursor: pointer; font: inherit; display: block; position: relative; padding-right: 16px; }
        .navbar .dropdown > button::after {
            content: "";
            position: absolute; right: 6px; top: 50%; transform: translateY(-50%);
            width: 0; height: 0;
            border-left: 5px solid transparent;
            border-right: 5px solid transparent;
            border-top: 6px solid #fff;
        }
        .navbar .dropdown-menu { display: none; position: absolute; top: 100%; left: 0; background: #fff; border: 1px solid #ddd; min-width: 200px; z-index: 1000; padding: 6px 0; }
        .navbar .dropdown-menu a { color: #333; padding: 10px 14px; }
        .navbar .dropdown-menu a:hover { background: #f5f5f5; }
        .navbar .dropdown.open > .dropdown-menu { display: block; }
    </style>
    <ul>
        <li class="brand"><a href="HomeController">PizzaStore</a></li>
        <!-- Lien ket luon hien thi -->
        <li><a href="HomeController">Pizzas</a></li>
        <li class="dropdown">
    <button type="button" class="dropdown-toggle">Categories</button>
    <ul class="dropdown-menu">
        <li><a href="HomeController">All</a></li>
        <!-- loop categories -->
        <c:forEach var="cat" items="${requestScope.categoriesMenu}">
            <li>
                <a href="HomeController?category=${cat.categoryID}">${cat.categoryName}</a>
                <!-- dieu huong toi trang homeController với tham số category = ${cat.categoryID} -->
            </li>
        </c:forEach>
    </ul>
</li>
        

        <!-- Bo sung cho Normal User -->
        <c:if test="${sessionScope.account != null && sessionScope.account.type == 2}">
            <li><a href="cart"> Cart 
                <c:if test="${sessionScope.cart != null && !sessionScope.cart.items.isEmpty()}">
                    (${sessionScope.cart.items.size()})
                </c:if>
            </a></li>
            <li><a href="orders-history">Orders history</a></li>
        </c:if>

        <!-- Bo sung cho Admin -->
        <!-- hien thi dashboard -->
        <c:if test="${sessionScope.account != null && sessionScope.account.type == 1}">
            <li><a href="AdminController">Dashboard</a></li>
        </c:if>
        <!-- hien thi ten user -->
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
<script>
// Toggle dropdown on click (works even without CSS changes)
document.addEventListener('click', function(e){
    const toggle = e.target.closest('.dropdown > button');
    const anyDropdown = e.target.closest('.dropdown');
    // Click on the Categories link: toggle
    if (toggle) {
        const li = toggle.parentElement;
        li.classList.toggle('open');
        return;
    }
    // Click outside: close all
    if (!anyDropdown) {
        document.querySelectorAll('.navbar .dropdown.open').forEach(function(el){
            el.classList.remove('open');
        });
    }
});
</script>

    