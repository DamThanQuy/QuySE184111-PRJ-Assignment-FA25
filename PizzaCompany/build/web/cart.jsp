<%-- 
    Document   : cart
    Created on : Oct 3, 2025, 8:13:37 AM
    Author     : QUYDAM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Models.Cart" %>
<%@page import="Models.CartItem" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Giỏ hàng - PizzaStore</title>
        <link rel="stylesheet" href="css/navbar.css">
        <style>
            body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
            .container { max-width: 1000px; margin: 0 auto; }
            .cart-header { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
            .cart-title { margin: 0; font-size: 24px; }
            .message { padding: 10px; border-radius: 4px; margin-bottom: 20px; font-weight: bold; }
            .message.success { background: #d4edda; color: #155724; }
            .message.error { background: #f8d7da; color: #721c24; }
            .cart-content { background: white; border-radius: 8px; overflow: hidden; }
            .cart-empty { padding: 40px; text-align: center; }
            .btn-primary { background: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; }
            .btn-danger { background: #dc3545; color: white; padding: 5px 10px; border: none; border-radius: 4px; cursor: pointer; }
            .cart-items { width: 100%; border-collapse: collapse; }
            .cart-items th { background: #f8f9fa; padding: 15px; text-align: left; border-bottom: 2px solid #dee2e6; }
            .cart-items td { padding: 15px; border-bottom: 1px solid #dee2e6; }
            .quantity-input { width: 60px; padding: 5px; text-align: center; }
            .price { font-weight: bold; color: #28a745; }
            .cart-summary { background: #f8f9fa; padding: 20px; border-top: 2px solid #dee2e6; }
            .summary-total { font-size: 18px; font-weight: bold; border-top: 2px solid #dee2e6; padding-top: 10px; margin-top: 10px; }
            .cart-actions { margin-top: 20px; text-align: center; }
            .btn-secondary { background: #6c757d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; margin-right: 10px; }
            .btn-checkout { background: #28a745; color: white; padding: 12px 25px; text-decoration: none; border-radius: 4px; font-weight: bold; }
        </style>
    </head>
    <body>
        <!-- Include Navbar -->
        <jsp:include page="fragments/navbar.jsp" />
        
        <div class="container">
            <!-- Cart Header -->
            <div class="cart-header">
                <h1 class="cart-title">Giỏ hàng của bạn</h1>
            </div>
            
            <!-- Display Messages -->
            <%
                String message = (String) session.getAttribute("message");
                String messageType = (String) session.getAttribute("messageType");
                
                if (message != null) {
            %>
                <div class="message <%= messageType %>">
                    <%= message %>
                </div>
            <%
                    // Clear message after displaying
                    session.removeAttribute("message");
                    session.removeAttribute("messageType");
                }
            %>
            
            <!-- Cart Content -->
            <div class="cart-content">
                <%
                    Cart cart = (Cart) request.getAttribute("cart");
                    boolean isEmpty = (Boolean) request.getAttribute("isEmpty");
                    int totalItems = (Integer) request.getAttribute("totalItems");
                    
                    if (isEmpty) {
                %>
                    <!-- Empty Cart -->
                    <div class="cart-empty">
                        <h3>Giỏ hàng trống</h3>
                        <p>Bạn chưa có sản phẩm nào trong giỏ hàng.</p>
                        <a href="HomeController" class="btn-primary">Tiếp tục mua sắm</a>
                    </div>
                <%
                    } else {
                %>
                    <!-- Cart Items -->
                    <table class="cart-items">
                        <thead>
                            <tr>
                                <th>Sản phẩm</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<CartItem> items = cart.getItems();
                                for (CartItem item : items) {
                            %>
                                <tr>
                                    <td>
                                        <%= item.getProductName() %>
                                    </td>
                                    <td class="price">
                                        $<%= String.format("%.2f", item.getUnitPrice()) %>
                                    </td>
                                    <td>
                                        <!-- Update Quantity Form -->
                                        <form method="POST" action="cart" style="display: inline;">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="productID" value="<%= item.getProductID() %>">
                                            <input type="number" 
                                                   name="quantity" 
                                                   value="<%= item.getQuantity() %>" 
                                                   min="0" 
                                                   max="99"
                                                   class="quantity-input"
                                                   onchange="this.form.submit()">
                                        </form>
                                    </td>
                                    <td class="price">
                                        $<%= String.format("%.2f", item.getTotalPrice()) %>
                                    </td>
                                    <td>
                                        <!-- Remove Item Form -->
                                        <form method="POST" action="cart" style="display: inline;">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productID" value="<%= item.getProductID() %>">
                                            <button type="submit" 
                                                    class="btn-danger"
                                                    onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                                Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                    
                    <!-- Cart Summary -->
                    <div class="cart-summary">
                        <div>Tổng số sản phẩm: <%= totalItems %> sản phẩm</div>
                        <div class="summary-total">
                            Tổng tiền: <span class="price">$<%= String.format("%.2f", cart.getTotalAmount()) %></span>
                        </div>
                    </div>
                    
                    <!-- Cart Actions -->
                    <div class="cart-actions">
                        <a href="HomeController" class="btn-secondary">Tiếp tục mua sắm</a>
                        <a href="checkout" class="btn-checkout">Thanh toán</a>
                    </div>
                <%
                    }
                %>
            </div>
        </div>
        
        <script>
            // Auto-submit form when quantity changes
            document.querySelectorAll('.quantity-input').forEach(input => {
                input.addEventListener('change', function() {
                    if (this.value < 0) {
                        this.value = 0;
                    }
                    this.form.submit();
                });
            });
        </script>
    </body>
</html>
