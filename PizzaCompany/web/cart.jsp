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
            body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #fff; }
            .container { max-width: 960px; margin: 0 auto; }
            .cart-header, .cart-content { background: #fff; border: 1px solid #ddd; border-radius: 4px; padding: 16px; margin-bottom: 16px; }
            .cart-title { margin: 0 0 8px; font-size: 22px; }
            .message { padding: 8px 12px; border-radius: 4px; margin-bottom: 16px; font-weight: 600; }
            .message.success { background: #e6ffed; color: #23653a; }
            .message.error { background: #ffecec; color: #9b1c1c; }
            .cart-empty { padding: 32px; text-align: center; }
            .cart-items { width: 100%; border-collapse: collapse; }
            .cart-items th, .cart-items td { padding: 10px; border-bottom: 1px solid #eee; text-align: left; }
            .quantity-input { width: 60px; text-align: center; }
            .price { font-weight: 600; color: #2f9e44; }
            .cart-summary { margin-top: 16px; }
            .summary-total { margin-top: 8px; font-weight: 600; }
            .cart-actions { margin-top: 20px; text-align: center; }
            .btn-primary, .btn-secondary, .btn-checkout, .btn-danger { display: inline-block; padding: 8px 16px; border: none; border-radius: 4px; color: #fff; text-decoration: none; cursor: pointer; }
            .btn-primary { background: #4a74d1; }
            .btn-secondary { background: #777; margin-right: 10px; }
            .btn-checkout { background: #2f9e44; font-weight: 600; }
            .btn-danger { background: #d64545; }
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
                    // 1. Lấy đối tượng Cart từ request attribute (được đặt bởi CartController.viewCart())
                    Cart cart = (Cart) request.getAttribute("cart");
                    
                    // 2. Lấy giá trị isEmpty từ request attribute (kiểu Object vì attribute có thể là bất kỳ kiểu nào)
                    Object isEmptyObj = request.getAttribute("isEmpty");
                    // Kiểm tra kiểu dữ liệu và chuyển đổi an toàn: nếu là Boolean thì dùng giá trị đó, nếu không thì mặc định là true
                    boolean isEmpty = (isEmptyObj instanceof Boolean) ? (Boolean) isEmptyObj : true;
                    
                    // 3. Lấy tổng số lượng sản phẩm từ request attribute
                    Object totalItemsObj = request.getAttribute("totalItems");
                    // Kiểm tra kiểu dữ liệu và chuyển đổi an toàn: nếu là Integer thì dùng giá trị đó, nếu không thì mặc định là 0
                    int totalItems = (totalItemsObj instanceof Integer) ? (Integer) totalItemsObj : 0;
                    
                    // 4. Kiểm tra nếu giỏ hàng trống → hiển thị thông báo giỏ hàng trống
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
                                        <%= item.getProductName() %> <!-- Hiển thị tên sản phẩm -->
                                    </td>
                                    <td class="price">
                                        $<%= String.format("%.2f", item.getUnitPrice()) %> <!-- Hiển thị price sản phẩm -->
                                    </td>
                                    <td>
                                        <!-- Update Quantity Form -->
                                        <form method="POST" action="cart" style="display: inline;">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="productID" value="<%= item.getProductID() %>">
                                            <!-- Hiển thị quantity sản phẩm -->
                                            <input type="number" 
                                                   name="quantity" 
                                                   value="<%= item.getQuantity() %>"
                                                   min="0" 
                                                   max="99"
                                                   class="quantity-input"
                                                   onchange="this.form.submit()" />
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
                        <form action="HomeController" method="get" style="display:inline-block;">
                            <button type="submit" class="btn-secondary">Tiếp tục mua sắm</button>
                        </form>
                        <form action="checkout" method="get" style="display:inline-block;">
                            <button type="submit" class="btn-checkout">Thanh toán</button>
                        </form>
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
