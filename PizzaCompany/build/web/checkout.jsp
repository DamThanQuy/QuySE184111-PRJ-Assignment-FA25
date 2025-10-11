<%-- 
    Document   : checkout
    Created on : Oct 4, 2025, 2:13:11 PM
    Author     : QUYDAM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Models.Cart" %>
<%@page import="Models.CartItem" %>
<%@page import="java.util.List" %>

<%
    Cart cart = (Cart) request.getAttribute("cart");
    Object totalAmountObj = request.getAttribute("totalAmount");
    double totalAmount = totalAmountObj != null ? (Double) totalAmountObj : 0.0;
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkout - PizzaStore</title>
        <link rel="stylesheet" href="css/navbar.css">
        <style>
            body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
            .container { max-width: 800px; margin: 0 auto; }
            .checkout-header { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
            .checkout-content { background: white; padding: 20px; border-radius: 8px; }
            .form-group { margin-bottom: 15px; }
            label { display: block; margin-bottom: 5px; font-weight: bold; }
            input, textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
            .btn { padding: 12px 25px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
            .btn:hover { background: #218838; }
            .btn-secondary { background: #6c757d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; margin-right: 10px; }
            .error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin-bottom: 20px; }
            .order-summary { background: #f8f9fa; padding: 15px; margin: 20px 0; border-radius: 4px; }
            .summary-item { display: flex; justify-content: space-between; margin: 5px 0; }
            .summary-total { font-weight: bold; font-size: 18px; border-top: 2px solid #dee2e6; padding-top: 10px; margin-top: 10px; }
        </style>
    </head>
    <body>
        <!-- Include Navbar -->
        <jsp:include page="fragments/navbar.jsp" />
        
        <div class="container">
            <div class="checkout-header">
                <h1>Thanh toán</h1>
            </div>
            
            <!-- Display Error -->
            <% if (error != null) { %>
                <div class="error"><%= error %></div>
            <% } %>
            
            <div class="checkout-content">
                <!-- Order Summary -->
                <div class="order-summary">
                    <h3>Tóm tắt đơn hàng</h3>
                    <%
                        if (cart != null && !cart.getItems().isEmpty()) {
                            for (CartItem item : cart.getItems()) {
                    %>
                        <div class="summary-item">
                            <span><%= item.getProductName() %> x<%= item.getQuantity() %></span>
                            <span>$<%= String.format("%.2f", item.getTotalPrice()) %></span>
                        </div>
                    <%
                            }
                    %>
                        <div class="summary-total">
                            <span>Tổng cộng:</span>
                            <span>$<%= String.format("%.2f", totalAmount) %></span>
                        </div>
                    <%
                        }
                    %>
                </div>
                
                <!-- Checkout Form -->
                <form action="checkout?action=process" method="POST">
                    <div class="form-group">
                        <label for="shipAddress">Địa chỉ giao hàng *</label>
                        <textarea id="shipAddress" name="shipAddress" rows="3" required placeholder="Nhập địa chỉ giao hàng chi tiết"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="phoneNumber">Số điện thoại *</label>
                        <input type="tel" id="phoneNumber" name="phoneNumber" 
                               required placeholder="Nhập số điện thoại liên hệ"
                               pattern="[0-9]{10,11}">
                    </div>
                    
                    <div style="margin-top: 30px;">
                        <a href="cart" class="btn-secondary">Quay lại giỏ hàng</a>
                        <button type="submit" class="btn">Đặt hàng</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
