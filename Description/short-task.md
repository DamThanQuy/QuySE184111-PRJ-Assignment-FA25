# Chức năng lịch sử đặt hàng

## Các bước để triển khai chức năng lịch sử đặt hàng

### 1. Tạo trang JSP orders-history.jsp
- [x] Tạo file `orders-history.jsp` trong thư mục `web/`.
- [ ] Thiết kế giao diện hiển thị danh sách đơn hàng của người dùng (từ session).
- [ ] Hiển thị thông tin cơ bản: OrderID, OrderDate, TotalAmount, ShipAddress.
- [ ] Thêm liên kết xem chi tiết đơn hàng hoặc quay lại trang chủ.

### 2. Cập nhật Controller để xử lý trang lịch sử
- [x] Cập nhật `HomeController.java` hoặc tạo Controller mới để xử lý URL `/orders-history`.
- [x] Lấy `Account` từ session để xác định người dùng.
- [x] Sử dụng `OrderDAO` để lấy danh sách đơn hàng theo `CustomerID`.
- [x] Truyền dữ liệu vào JSP và forward đến `orders-history.jsp`.

### 3. Tích hợp dữ liệu từ DAO
- [ ] Trong Controller, gọi `OrderDAO.getOrdersByCustomerId(account.getAccountID())` để lấy danh sách đơn hàng.
- [ ] Nếu cần, lấy chi tiết đơn hàng bằng `OrderDetailDAO.getOrderDetailsByOrderId(orderID)`.
- [ ] Tính tổng tiền nếu chưa có trong model.

### 4. Xử lý trường hợp chưa đăng nhập
- [ ] Kiểm tra session: Nếu chưa đăng nhập, chuyển hướng đến trang login.
- [ ] Hiển thị thông báo nếu không có đơn hàng nào.

### 5. Test chức năng
- [ ] Test hiển thị lịch sử đơn hàng khi đăng nhập.
- [ ] Test liên kết từ navbar đến trang lịch sử.
- [ ] Đảm bảo dữ liệu chính xác và không lỗi bảo mật.
