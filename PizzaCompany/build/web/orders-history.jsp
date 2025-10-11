<%-- 
    Document   : orders-history
    Created on : Oct 5, 2025, 6:13:56 PM
    Author     : QUYDAM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Models.Order" %>
<%@page import="Models.OrderDetail" %>
<%@page import="Models.Account" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="java.time.format.DateTimeFormatter" %>

<%
    Account account = (Account) session.getAttribute("account");
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    Map<Integer, Double> totalAmounts = (Map<Integer, Double>) request.getAttribute("totalAmounts");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lịch sử đặt hàng - PizzaStore</title>
        <link rel="stylesheet" href="css/navbar.css">
        <style>
            body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
            .container { max-width: 1200px; margin: 0 auto; }
            .orders-header { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
            .orders-content { background: white; padding: 20px; border-radius: 8px; }
            .error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin-bottom: 20px; }
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background: #f2f2f2; }
            .btn { padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 4px; }
            .btn:hover { background: #0056b3; }
            .no-orders { text-align: center; padding: 20px; }
        </style>
    </head>
    <body>
        <!-- Include Navbar -->
        <jsp:include page="fragments/navbar.jsp" />
        
        <div class="container">
            <div class="orders-header">
                <h1>Lịch sử đặt hàng</h1>
            </div>
            
            <!-- Display Error -->
            <% if (error != null) { %>
                <div class="error"><%= error %></div>
            <% } %>
            
            <div class="orders-content">
                <% if (account != null) { %>
                    <% if (orders != null && !orders.isEmpty()) { %>
                        <table>
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Ngày đặt hàng</th>
                                    <th>Tổng tiền</th>
                                    <th>Địa chỉ giao hàng</th>
                                    <th>Số điện thoại</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Order order : orders) { %>
                                    <tr>
                                        <td>#<%= order.getOrderID() %></td>
                                        <td><%= order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></td>
                                        <td>$<%= String.format("%.2f", totalAmounts.getOrDefault(order.getOrderID(), 0.0)) %></td>
                                        <td><%= order.getShipAddress() %></td>
                                        <td><%= order.getPhoneNumber() != null ? order.getPhoneNumber() : "N/A" %></td>
                                        <td><a href="order-detail?orderId=<%= order.getOrderID() %>" class="btn">Xem chi tiết</a></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } else { %>
                        <div class="no-orders">
                            <p>Bạn chưa có đơn hàng nào.</p>
                        </div>
                    <% } %>
                <% } else { %>
                    <div class="error">
                        <p>Vui lòng đăng nhập để xem lịch sử đặt hàng.</p>
                        <a href="login.jsp" class="btn">Đăng nhập</a>
                    </div>
                <% } %>
                
                <div style="margin-top: 20px;">
                    <a href="HomeController" class="btn">Quay lại trang chủ</a>
                </div>
            </div>
        </div>
    </body>
</html>
