<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Product</title>
        <jsp:include page="fragments/navbar.jsp" />
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            }
            form {
                max-width: 600px;
                margin: auto;
            }
            label {
                display: block;
                margin-top: 10px;
            }
            input, select {
                width: 100%;
                padding: 8px;
                margin-top: 5px;
            }
            button {
                margin-top: 20px;
                padding: 10px 20px;
                background-color: #007bff;
                color: white;
                border: none;
                cursor: pointer;
            }
            button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <h1>Create New Pizza</h1>
        <c:if test="${not empty requestScope.errorGeneral}">
            <p style="color: red;">${requestScope.errorGeneral}</p>
        </c:if>
        <form action="createProduct" method="post">
            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="productName" value="${not empty requestScope.productName ? requestScope.productName : ''}" required />
            <c:if test="${not empty requestScope.errorProductName}">
                <p style="color: red;">${requestScope.errorProductName}</p>
            </c:if>

            <label for="unitPrice">Unit Price:</label>
            <input type="number" id="unitPrice" name="unitPrice" value="${not empty requestScope.unitPrice ? requestScope.unitPrice : ''}" step="0.01" required />
            <c:if test="${not empty requestScope.errorUnitPrice}">
                <p style="color: red;">${requestScope.errorUnitPrice}</p>
            </c:if>

            <label for="categoryID">Category:</label>
            <select id="categoryID" name="categoryID" required>
                <option value="">Select a category</option>
                <c:forEach var="category" items="${requestScope.categories}">
                    <option value="${category.categoryID}" ${not empty requestScope.categoryID && requestScope.categoryID == category.categoryID ? 'selected' : ''}>
                        ${category.categoryName}
                    </option>
                </c:forEach>
            </select>
            <c:if test="${not empty requestScope.errorCategoryID}">
                <p style="color: red;">${requestScope.errorCategoryID}</p>
            </c:if>

            <label for="quantityPerUnit">Quantity Per Unit:</label>
            <input type="text" id="quantityPerUnit" name="quantityPerUnit" value="${not empty requestScope.quantityPerUnit ? requestScope.quantityPerUnit : ''}" />

            <label for="productImage">Product Image URL:</label>
            <input type="text" id="productImage" name="productImage" value="${not empty requestScope.productImage ? requestScope.productImage : ''}" />

            <label for="supplierID">Supplier:</label>
            <select id="supplierID" name="supplierID">
                <option value="">Select a supplier</option>
                <c:forEach var="supplier" items="${requestScope.suppliers}">
                    <option value="${supplier.supplierID}" ${not empty requestScope.supplierID && requestScope.supplierID == supplier.supplierID ? 'selected' : ''}>
                        ${supplier.companyName}
                    </option>
                </c:forEach>
            </select>
            <c:if test="${not empty requestScope.errorSupplierID}">
                <p style="color: red;">${requestScope.errorSupplierID}</p>
            </c:if>

            <label for="isActive">Active:</label>
            <select id="isActive" name="isActive">
                <option value="true" ${requestScope.isActive == 'true' ? 'selected' : ''}>Yes</option>
                <option value="false" ${requestScope.isActive == 'false' ? 'selected' : ''}>No</option>
            </select>

            <button type="submit">Create Product</button>
        </form>
        <form action="admin" method="get" style="display:inline;">
            <button type="submit" class="back-btn">Back to Manage Products</button>
        </form>
    </body>
</html>
