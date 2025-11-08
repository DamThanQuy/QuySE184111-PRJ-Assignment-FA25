# Mô tả code base

## Tổng quan dự án
- Ứng dụng web bán Pizza viết bằng Java Servlet/JSP, mô hình MVC đơn giản.
- Dữ liệu lưu trữ trên SQL Server, truy cập thông qua các lớp DAO sử dụng JDBC.
- Giao diện người dùng xây dựng bằng JSP kết hợp HTML/CSS và forward từ servlet.

## Cấu trúc thư mục chính
- `Database/`: chứa script cấu hình và dữ liệu mẫu cho SQL Server (chưa duyệt chi tiết).
- `Description/`: tài liệu dự án, bao gồm tệp mô tả hiện tại.
- `PizzaCompany/`: mã nguồn ứng dụng chính với các thư mục con:
  - `src/java/Controllers`: các servlet xử lý request (Home, Cart, Checkout, Admin,...).
  - `src/java/DAO`: lớp truy cập dữ liệu cho Product, Order, Account, Category, v.v.
  - `src/java/Models`: các lớp POJO như `Product`, `Cart`, `Order`, `Account`,...
  - `src/java/Utils`: tiện ích kết nối DB (`DbUtils`).
  - `web/`: JSP, tài nguyên tĩnh và cấu hình `WEB-INF/web.xml` cho servlet mapping.

## Lớp tiện ích & kết nối
- `Utils/DbUtils.java`: khởi tạo kết nối SQL Server, cấu hình tên DB, tài khoản, driver @PizzaCompany/src/java/Utils/DbUtils.java#17-27.

## Tầng dữ liệu (DAO)
- `ProductDAO`: lấy danh sách sản phẩm, tìm kiếm, CRUD, join với `Categories` @PizzaCompany/src/java/DAO/ProductDAO.java#14-195.
- `OrderDAO`: quản lý đơn hàng, tạo transaction với `OrderDetailDAO`, truy vấn lịch sử @PizzaCompany/src/java/DAO/OrderDAO.java#25-199.
- `AccountDAO`: xác thực đăng nhập, kiểm tra username, tạo tài khoản mới @PizzaCompany/src/java/DAO/AccountDAO.java#12-88.

## Tầng mô hình (Models)
- `Cart`: quản lý giỏ hàng trong session, gồm phương thức thêm/cập nhật/xóa sản phẩm @PizzaCompany/src/java/Models/Cart.java#15-94.
- `Order`, `OrderDetail`, `Product`, `Account`, `CartItem`: đại diện dữ liệu từ DB hoặc giỏ hàng.

## Tầng điều khiển (Controllers)
- `HomeController`: hiển thị trang chủ, tìm kiếm sản phẩm, load danh mục @PizzaCompany/src/java/Controllers/HomeController.java#30-60.
- `CartController`: xử lý xem/ thêm/ cập nhật/ xóa giỏ hàng, lưu trữ trong session @PizzaCompany/src/java/Controllers/CartController.java#45-390.
- `CheckoutController`: xác thực thông tin checkout, tạo đơn hàng hoàn chỉnh và forward tới trang xác nhận @PizzaCompany/src/java/Controllers/CheckoutController.java#38-200.
- `LoginController`: kiểm tra tài khoản, phân quyền admin/user @PizzaCompany/src/java/Controllers/LoginController.java#22-45.
- `web/WEB-INF/web.xml`: khai báo servlet và URL mapping (ví dụ `/cart`, `/CheckoutController`) @PizzaCompany/web/WEB-INF/web.xml#3-127.

## Tầng giao diện (JSP)
- Các servlet forward tới JSP tương ứng (`home.jsp`, `cart.jsp`, `checkout.jsp`, `order-confirmation.jsp`...) trong thư mục `web/` để render nội dung động.

## Ghi chú triển khai
- Session timeout 30 phút cấu hình trong `web.xml`.
- `HomeController` được cấu hình là trang chào mừng mặc định.
