<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Detail</title>
        <style>
            .product-detail { margin: 20px; padding: 20px; border: 1px solid #ddd; }
            .info-item { margin: 10px 0; }
            .back-btn { display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }
        </style>
    </head>
    <body>
        <jsp:include page="fragments/navbar.jsp" />
        <h1>Product Detail</h1>
        
        <c:if test="${not empty product}">
            <div class="product-detail">
                <h2>${product.productName}</h2>
                
                <!-- Hiển thị hình ảnh nếu có -->
                <c:if test="${not empty product.productImage}">
                    <div>
                        <img src="${product.productImage}" alt="${product.productName}" style="max-width: 300px;" />
                    </div>
                </c:if>
                
                <!-- Thông tin sản phẩm -->
                <div class="info-item"><strong>Product ID:</strong> ${product.productID}</div>
                <div class="info-item"><strong>Price:</strong> $${product.unitPrice}</div>
                <div class="info-item"><strong>Supplier:</strong> ${product.supplierName}</div>
                <div class="info-item"><strong>Category:</strong> ${product.categoryName}</div>
                <div class="info-item"><strong>Status:</strong> ${product.isActive ? 'Active' : 'Inactive'}</div>
                <div class="info-item"><strong>Quantity Per Unit:</strong> ${product.quantityPerUnit}</div>
                
                <!-- Mô tả sản phẩm nếu có -->
                <c:if test="${not empty product.description}">
                    <div style="margin-top: 20px;">
                        <h3>Description</h3>
                        <p>${product.description}</p>
                    </div>
                </c:if>
                
                <form action="admin" method="get" style="display:inline;">
                <button type="submit" class="back-btn" ">← Back to Product List</button>
</form>
            </div>
        </c:if>
        
        <c:if test="${empty product}">
            <div>
                <h2>Product not found!</h2>
                <p>The product you are looking for does not exist or has been removed.</p>
                <form action="admin" method="get" style="display:inline;">
                        <button type="submit" class="back-btn" ">← Back to Product List</button>
                </form>
            </div>
        </c:if>
    </body>
</html>
