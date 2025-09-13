<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PizzaStore - Home</title>
        <link rel="stylesheet" href="css/navbar.css"/>
        <style>
            .container{width:90%;margin:20px auto;}
            .search-box{margin-bottom:20px;}
            .products{display:flex;flex-wrap:wrap;gap:20px;}
            .card{border:1px solid #ddd;padding:10px;width:200px;text-align:center;}
            .card img{width:100%;height:120px;object-fit:cover;}
            .card button{margin-top:10px;padding:6px 10px;background:#777;color:#fff;border:none;cursor:not-allowed;}
        </style>
    </head>
    <body>
        <!-- Navbar -->
        <jsp:include page="fragments/navbar.jsp" />

        <div class="container">
            <h2>Pizzas Menu</h2>
            <!-- Search -->
            <form action="HomeController" method="get" class="search-box">
                <label for="search">Search: </label>
                <input id="search" type="text" name="keyword" placeholder="Search pizzas..." value="${param.keyword}"/>
                <input type="submit" value="Search" />
            </form>

            <h3>All Pizzas</h3>

            <!-- Product list -->
            <div class="products">
                <c:forEach var="p" items="${requestScope.products}">
                    <div class="card">
                        <img src="${p.productImage}" alt="${p.productName}"/>
                        <h4>${p.productName}</h4>
                        <p>${p.unitPrice} $</p>
                        <p><em>${p.categoryName}</em></p>
                        <p>${p.description}</p>
                        <c:if test="${sessionScope.account != null}">
                            <button>Add to Cart</button>
                        </c:if>
                        <c:if test="${sessionScope.account == null}">
                            <button disabled>Login to Buy</button>
                        </c:if>
                    </div>
                </c:forEach>
                <c:if test="${empty requestScope.products}">
                    <p>No products found.</p>
                </c:if>
            </div>
        </div>
    </body>
</html>
