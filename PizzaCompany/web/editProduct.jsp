<%-- 
    Document   : editProduct
    Created on : Sep 17, 2025, 4:42:12 PM
    Author     : QUYDAM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Product</title>
        <jsp:include page="fragments/navbar.jsp" />
        <style>
            body { font-family: Arial, sans-serif; margin: 20px; }
            form { max-width: 600px; margin: auto; }
            label { display: block; margin-top: 10px; }
            input, select { width: 100%; padding: 8px; margin-top: 5px; }
            button { margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: white; border: none; cursor: pointer; }
            button:hover { background-color: #0056b3; }
        </style>
    </head>
    <body>
        <h1>Edit Product</h1>
        <c:if test="${not empty requestScope.errorGeneral}">
            <p style="color: red;">${requestScope.errorGeneral}</p>
        </c:if>
        <c:if test="${not empty requestScope.product}">
            <form action="editProduct" method="post">
                <input type="hidden" name="id" value="${requestScope.product.productID}" />
                <label for="productName">Product Name:</label>
                <input type="text" id="productName" name="productName" value="${not empty requestScope.productName ? requestScope.productName : requestScope.product.productName}" required />
                <c:if test="${not empty requestScope.errorProductName}">
                    <p style="color: red;">${requestScope.errorProductName}</p>
                </c:if>

                <label for="unitPrice">Unit Price:</label>
                <input type="number" id="unitPrice" name="unitPrice" value="${not empty requestScope.unitPrice ? requestScope.unitPrice : requestScope.product.unitPrice}" step="0.01" required />
                <c:if test="${not empty requestScope.errorUnitPrice}">
                    <p style="color: red;">${requestScope.errorUnitPrice}</p>
                </c:if>

                <label for="categoryID">Category ID:</label>
                <input type="number" id="categoryID" name="categoryID" value="${not empty requestScope.categoryID ? requestScope.categoryID : requestScope.product.categoryID}" required />
                <c:if test="${not empty requestScope.errorCategoryID}">
                    <p style="color: red;">${requestScope.errorCategoryID}</p>
                </c:if>

                <label for="quantityPerUnit">Quantity Per Unit:</label>
                <input type="text" id="quantityPerUnit" name="quantityPerUnit" value="${not empty requestScope.quantityPerUnit ? requestScope.quantityPerUnit : requestScope.product.quantityPerUnit}" />

                <label for="productImage">Product Image URL:</label>
                <input type="text" id="productImage" name="productImage" value="${not empty requestScope.productImage ? requestScope.productImage : requestScope.product.productImage}" />

                <label for="supplierID">Supplier ID:</label>
                <input type="number" id="supplierID" name="supplierID" value="${not empty requestScope.supplierID ? requestScope.supplierID : requestScope.product.supplierID}" />
                <c:if test="${not empty requestScope.errorSupplierID}">
                    <p style="color: red;">${requestScope.errorSupplierID}</p>
                </c:if>

                <label for="isActive">Active:</label>
                <select id="isActive" name="isActive">
                    <option value="true" ${requestScope.isActive == 'true' ? 'selected' : (requestScope.product.isActive ? 'selected' : '')}>Yes</option>
                    <option value="false" ${requestScope.isActive == 'false' ? 'selected' : (!requestScope.product.isActive ? 'selected' : '')}>No</option>
                </select>

                <button type="submit">Update Product</button>
            </form>
        </c:if>
        <c:if test="${empty requestScope.product}">
            <p>Product not found.</p>
        </c:if>
        <form action="admin" method="get" style="display:inline;">
    <button type="submit" class="back-btn" >Back to Manage Products</button>
</form>
    </body>
</html>
