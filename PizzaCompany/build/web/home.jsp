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
            .card button{margin-top:10px;padding:8px 16px;background:#007bff;color:#fff;border:none;cursor:pointer;border-radius:4px;font-size:14px;}
            .card button:hover{background:#0056b3;}
            .card button:disabled{background:#6c757d;cursor:not-allowed;}
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
                <label for="minPrice">Min Price: </label>
                <input id="minPrice" type="number" name="minPrice" placeholder="Min Price" value="${param.minPrice}" step="0.01"/>
                <label for="maxPrice">Max Price: </label>
                <input id="maxPrice" type="number" name="maxPrice" placeholder="Max Price" value="${param.maxPrice}" step="0.01"/>
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
                            <!-- Add to Cart Form -->
                            <form method="POST" action="cart" style="margin-top:10px;">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productID" value="${p.productID}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit">Add to Cart</button>
                            </form>
                        </c:if>
                        <c:if test="${sessionScope.account == null}">
                            <button disabled>Login to buy</button>
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
