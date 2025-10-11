<%-- 
    Document   : order-detail
    Created on : Oct 7, 2025, 3:04:07 PM
    Author     : QUYDAM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Models.Order" %>
<%@page import="Models.OrderDetail" %>
<%@page import="Models.Product" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="java.time.format.DateTimeFormatter" %>

<%
    Order order = (Order) request.getAttribute("order");
    List<OrderDetail> orderDetails = (List<OrderDetail>) request.getAttribute("orderDetails");
    Double totalAmountObj = (Double) request.getAttribute("totalAmount");
    double totalAmount = totalAmountObj != null ? totalAmountObj : 0.0;
    String error = (String) request.getAttribute("error");
    Map<Integer, Product> productMap = (Map<Integer, Product>) request.getAttribute("productMap");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết đơn hàng - PizzaStore</title>
        <link rel="stylesheet" href="css/navbar.css">
        <style>
            body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
            .container { max-width: 1000px; margin: 0 auto; }
            .header { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
            .info, .details { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
            .error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin-bottom: 20px; }
            .empty { text-align: center; padding: 20px; color: #666; }
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
            th { background: #f2f2f2; }
            .summary { text-align: right; font-size: 1.1rem; font-weight: bold; margin-top: 10px; }
            .btn { padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 4px; display: inline-block; }
            .btn:hover { background: #0056b3; }
            .actions { text-align: right; }
        </style>
    </head>
    <body>
        <jsp:include page="fragments/navbar.jsp" />
        <div class="container">
            <div class="header">
                <h1>Chi tiết đơn hàng</h1>
            </div>

            <% if (error != null) { %>
                <div class="error"><%= error %></div>
            <% } else if (order == null) { %>
                <div class="error">Không có dữ liệu đơn hàng để hiển thị.</div>
            <% } else { %>
                <div class="info">
                    <h2>Thông tin đơn hàng #<%= order.getOrderID() %></h2>
                    <p><strong>Ngày đặt hàng:</strong> <%= order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></p>
                    <p><strong>Địa chỉ giao hàng:</strong> <%= order.getShipAddress() %></p>
                    <p><strong>Số điện thoại:</strong> <%= order.getPhoneNumber() %></p>
                </div>

                <div class="details">
                    <h2>Chi tiết sản phẩm</h2>
                    <% if (orderDetails != null && !orderDetails.isEmpty()) { %>
                        <table>
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Tên sản phẩm</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% int index = 1;
                                   for (OrderDetail detail : orderDetails) {
                                       Product product = productMap != null ? productMap.get(detail.getProductID()) : null;
                                       String productName = product != null ? product.getProductName() : ("#" + detail.getProductID());
                                       double lineTotal = detail.getUnitPrice() * detail.getQuantity();
                                %>
                                    <tr>
                                        <td><%= index++ %></td>
                                        <td><%= productName %></td>
                                        <td>$<%= String.format("%.2f", detail.getUnitPrice()) %></td>
                                        <td><%= detail.getQuantity() %></td>
                                        <td>$<%= String.format("%.2f", lineTotal) %></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <div class="summary">Tổng tiền: $<%= String.format("%.2f", totalAmount) %></div>
                    <% } else { %>
                        <div class="empty">Đơn hàng này không có sản phẩm nào.</div>
                    <% } %>
                </div>
            <% } %>

            <div class="actions">
                <a href="orders-history" class="btn">Quay lại lịch sử đơn hàng</a>
            </div>
        </div>
    </body>
</html>
