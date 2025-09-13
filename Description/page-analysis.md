# Phân tích số lượng trang cần tạo cho dự án PizzaStore

## Tổng quan
Dựa trên file chức năng, dự án PizzaStore cần tổng cộng **khoảng 15-20 trang** (bao gồm JSP, Servlet và các trang hỗ trợ). Các trang được phân loại theo vai trò người dùng và chức năng.

## Chi tiết các trang cần tạo

### 1. Trang chung (Common) - 4 trang
- **login.jsp**: Trang đăng nhập (JSP + LoginServlet để xử lý).
- **home.jsp**: Trang chủ truy cập công khai.
  - Khi **guest** (chưa đăng nhập): cho phép xem và **tìm kiếm** sản phẩm nhưng **không thể mua**.
  - Khi đã đăng nhập: có thể hiển thị menu tương ứng với role hoặc tự redirect tới dashboard phù hợp.
- **logout.jsp**: Trang xác nhận đăng xuất (hoặc redirect trực tiếp qua LogoutServlet).
- **error.jsp / success.jsp**: Trang hiển thị lỗi hoặc thành công cho các thao tác.

### 2. Trang dành cho Admin - 7 trang
- **admin-dashboard.jsp**: Trang bảng điều khiển Admin (overview, menu CRUD).
- **product-list-admin.jsp**: Xem danh sách sản phẩm (JSP + ProductListAdminServlet).
- **product-details-admin.jsp**: Xem chi tiết sản phẩm (JSP + ProductDetailsAdminServlet).
- **product-add.jsp**: Form thêm sản phẩm mới (JSP + ProductAddServlet).
- **product-update.jsp**: Form cập nhật sản phẩm (JSP + ProductUpdateServlet).
- **product-delete-confirm.jsp**: Trang xác nhận xóa sản phẩm (JSP + ProductDeleteServlet).
- **sales-report.jsp**: Trang báo cáo doanh số (form chọn ngày + hiển thị kết quả, JSP + SalesReportServlet).

### 3. Trang dành cho User - 6 trang
- **user-dashboard.jsp**: Trang bảng điều khiển User (menu xem/tìm kiếm).
- **product-search.jsp**: Trang tìm kiếm sản phẩm (form search theo tên/giá, JSP + ProductSearchServlet).
- **product-list-user.jsp**: Xem danh sách sản phẩm (JSP + ProductListUserServlet).
- **product-details-user.jsp**: Xem chi tiết sản phẩm (JSP + ProductDetailsUserServlet).
- **cart.jsp**: Trang giỏ hàng (hiển thị, cập nhật/xóa item, JSP + CartServlet).
- **cart-add-confirm.jsp**: Trang xác nhận thêm vào giỏ (hoặc redirect trực tiếp).

### 4. Trang hỗ trợ bổ sung - 2-3 trang
- **confirm-action.jsp**: Trang xác nhận chung cho các hành động (delete, update).
- **session-expired.jsp**: Trang thông báo phiên hết hạn (nếu cần).
- **not-found.jsp**: Trang 404 (nếu cần).

## Lưu ý
- Mỗi JSP thường đi kèm với 1 Servlet tương ứng để xử lý logic (Controller).
- Tổng số Servlet: Khoảng 10-15 (tương ứng với các chức năng CRUD, search, report).
- Các trang có thể được tối ưu hóa bằng JSTL/EL để giảm số lượng.
- Giỏ hàng có thể sử dụng session, không cần lưu DB trừ khi có yêu cầu đặt hàng thực tế.

**Tổng số trang ước tính: 15-20** (bao gồm JSP và Servlet).
