

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Models.Order" %>
<%@page import="Models.OrderDetail" %>
<%@page import="java.util.List" %>
<%@page import="java.time.format.DateTimeFormatter" %>

<%
    Order order = (Order) request.getAttribute("order");
    List<OrderDetail> orderDetails = (List<OrderDetail>) request.getAttribute("orderDetails");
    String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Confirmation - PizzaCompany</title>
        <style>
            body { font-family: Arial, sans-serif; margin: 20px; }
            .container { max-width: 800px; margin: 0 auto; }
            .success { color: green; font-size: 18px; margin-bottom: 20px; }
            .order-info { background: #f5f5f5; padding: 15px; margin: 20px 0; }
            .order-details { margin: 20px 0; }
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background: #f2f2f2; }
            .total { font-weight: bold; font-size: 16px; }
            .btn { padding: 10px 20px; margin: 10px 5px; text-decoration: none; background: #007bff; color: white; border-radius: 4px; }
            .btn:hover { background: #0056b3; }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Order Confirmation</h1>
            
            <% if (message != null) { %>
                <div class="success"><%= message %></div>
            <% } %>
            
            <% if (order != null) { %>
                <div class="order-info">
                    <h2>Order Information</h2>
                    <p><strong>Order ID:</strong> #<%= order.getOrderID() %></p>
                    <p><strong>Order Date:</strong> <%= order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></p>
                    <p><strong>Ship Address:</strong> <%= order.getShipAddress() %></p>
                    <p><strong>Số điện thoại:</strong> <%= order.getPhoneNumber() %></p>
                </div>
                
                <% if (orderDetails != null && !orderDetails.isEmpty()) { %>
                    <div class="order-details">
                        <h2>Order Details</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Product ID</th>
                                    <th>Unit Price</th>
                                    <th>Quantity</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    double totalAmount = 0;
                                    for (OrderDetail detail : orderDetails) {
                                        double itemTotal = detail.getUnitPrice() * detail.getQuantity();
                                        totalAmount += itemTotal;
                                %>
                                <tr>
                                    <td><%= detail.getProductID() %></td>
                                    <td>$<%= String.format("%.2f", detail.getUnitPrice()) %></td>
                                    <td><%= detail.getQuantity() %></td>
                                    <td>$<%= String.format("%.2f", itemTotal) %></td>
                                </tr>
                                <% } %>
                            </tbody>
                            <tfoot>
                                <tr class="total">
                                    <td colspan="3"><strong>Total Amount:</strong></td>
                                    <td><strong>$<%= String.format("%.2f", totalAmount) %></strong></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                <% } else { %>
                    <p>No order details found.</p>
                <% } %>
                
            <% } else { %>
                <p>No order information available.</p>
            <% } %>
            
            <div style="margin-top: 30px;">
                <a href="HomeController" class="btn">Continue Shopping</a>
                <a href="cart" class="btn">View Cart</a>
            </div>
        </div>
    </body>
</html>
