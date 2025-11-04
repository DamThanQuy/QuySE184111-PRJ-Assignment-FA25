<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Products</title>
        <style>
            table { border-collapse: collapse; width: 100%; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>
    </head>
    <body>
        <jsp:include page="fragments/navbar.jsp" />
        <h1>Manage Products</h1>
        <form action="createProduct" method="get" style="display:inline;">
    <button type="submit" class="back-btn">Create New Pizza</button>
</form>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Category</th>
                    <th>Active</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${requestScope.products}">
                    <tr>
                        <td>${p.productID}</td>
                        <td>${p.productName}</td>
                        <td>${p.unitPrice} $</td>
                        <td>${p.categoryName}</td>
                        <td>${p.isActive ? 'Yes' : 'No'}</td>
                        <td>
                            <!-- Khi nhấn View sẽ tới /detailProduct?id=... (DetailProductController) để xem chi tiết, Edit sẽ tới /editProduct?id=... để chỉnh sửa -->
                            <form action="detailProduct" method="get" style="display:inline;">
                                <input type="hidden" name="id" value="${p.productID}" />
                                <button type="submit" ">View</button>
                            </form>
                            <form action="editProduct" method="get" style="display:inline;">
                                <input type="hidden" name="id" value="${p.productID}" />
                                <button type="submit" ">Edit</button>
                            </form>
                            <form action="deleteProduct" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa pizza này?');">
                                <input type="hidden" name="id" value="${p.productID}" />
                                <button type="submit" ">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${empty requestScope.products}">
            <p>No products found.</p>
        </c:if>
    </body>
</html>
