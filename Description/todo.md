# PizzaCompany Cart System - Tóm tắt ngữ cảnh

## 🎯 Dự án hiện tại
- **PizzaStore web app** (Java Servlet + JSP + MySQL)
- **Function 10**: Quản lý giỏ hàng
- **Trạng thái**: Backend hoàn thành, Frontend hoàn thành

## ✅ Đã hoàn thành
- **CartItem.java**: Model cho 1 sản phẩm trong giỏ (productID, productName, unitPrice, quantity, totalPrice)
- **Cart.java**: Model quản lý giỏ hàng (List<CartItem>, totalAmount, addItem/updateItem/removeItem methods)
- **CartController.java**: Servlet xử lý giỏ hàng (/cart)
  - `viewCart()`: GET request → hiển thị giỏ hàng
  - `addToCart()`: POST request → thêm sản phẩm → redirect (PRG pattern)
  - `updateCart()`: POST request → cập nhật số lượng
  - `removeFromCart()`: POST request → xóa sản phẩm
- **cart.jsp**: Giao diện giỏ hàng hoàn chỉnh với CRUD operations
- **home.jsp**: Thêm nút "Add to Cart" cho từng sản phẩm
- **navbar.jsp**: Thêm link "🛒 Cart" với counter số lượng sản phẩm
- **web.xml**: Cấu hình URL mapping cho CartController

## 🔧 Kiến thức quan trọng
- **Session**: Giỏ hàng lưu trong HttpSession, tự xóa khi đóng browser
- **Forward vs Redirect**: Redirect chuyển POST→GET, tránh duplicate submission khi F5
- **PRG Pattern**: Post-Redirect-Get cho actions thay đổi dữ liệu
- **Referer Header**: request.getHeader("Referer") để biết trang trước đó
- **Servlet Mapping**: Sử dụng web.xml thay vì @WebServlet (compatibility issues)

## 📊 Database Structure
- **Products**: ProductID, ProductName, UnitPrice, ProductImage, isActive
- **Orders**: OrderID, CustomerID, OrderDate, RequiredDate, ShippedDate, Freight, ShipAddress
- **Order Details**: OrderID, ProductID, UnitPrice, Quantity
- **Mối quan hệ**: Orders (1) ←→ (N) Order Details

## 🎯 Chức năng hoạt động
- **Thêm sản phẩm**: home.jsp → POST /cart?action=add → CartController.addToCart()
- **Xem giỏ hàng**: navbar → GET /cart → CartController.viewCart() → cart.jsp
- **Cập nhật số lượng**: cart.jsp → POST /cart?action=update → CartController.updateCart()
- **Xóa sản phẩm**: cart.jsp → POST /cart?action=remove → CartController.removeFromCart()

## 📋 Còn lại cần làm
- [ ] Tạo CheckoutController cho chức năng thanh toán
- [x] Tạo OrderDAO và OrderDetailDAO
- [x] Tạo order-confirmation.jsp
- [ ] Kết nối giỏ hàng với bảng Orders/Order Details
- [ ] CSS styling cải thiện

## 🔄 URL Mapping
- **GET /cart** → Hiển thị giỏ hàng
- **POST /cart?action=add** → Thêm sản phẩm
- **POST /cart?action=update** → Cập nhật số lượng
- **POST /cart?action=remove** → Xóa sản phẩm

**Trạng thái**: ✅ Hệ thống giỏ hàng hoàn chỉnh, sẵn sàng cho chức năng thanh toán!
