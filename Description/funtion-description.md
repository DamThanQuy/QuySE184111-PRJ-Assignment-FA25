
# Mô tả chức năng chi tiết cho Hệ thống PizzaStore


## 1. Tổng quan
Đây là một dự án ứng dụng web cho PizzaStore, được xây dựng để quản lý sản phẩm (pizza), đơn hàng, và người dùng. Hệ thống có hai loại người dùng chính với các quyền hạn khác nhau: **Admin** và **User** thông thường.

## 2. Các yêu cầu về công nghệ và kiến trúc
* **Mô hình kiến trúc:** Phát triển ứng dụng web Java theo **MVC2 Model**.
* **Công nghệ sử dụng:**
    * **JavaBeans:** Sử dụng JavaBeans để đóng gói dữ liệu và logic nghiệp vụ.
    * **JSP (JavaServer Pages):** Dùng JSP để tạo các trang giao diện người dùng.
    * **JSTL (JSP Standard Tag Library) & Custom Tag:** Sử dụng JSTL và các tag tùy chỉnh để đơn giản hóa việc hiển thị dữ liệu và logic trên các trang JSP.
    * **EL (Expression Language):** Dùng EL để truy cập dữ liệu từ JavaBeans và các scope khác.
    * **Filter & Servlet:** Sử dụng Servlet để xử lý các yêu cầu từ người dùng và Filter để thực hiện các tác vụ trước hoặc sau khi yêu cầu được xử lý.
* **Kết nối cơ sở dữ liệu:** Thực hiện các thao tác **CRUD** (Create, Read, Update, Delete) bằng **JDBC**.
* **LINQ trong Java:** Yêu cầu này là tùy chọn, nhưng có thể sử dụng LINQ để truy vấn và sắp xếp dữ liệu.
* **Xác thực dữ liệu:** Đảm bảo xác thực kiểu dữ liệu cho tất cả các trường.
Đây là một dự án ứng dụng web cho PizzaStore, được xây dựng để quản lý sản phẩm (pizza), đơn hàng, và người dùng. Hệ thống có hai loại người dùng chính với các quyền hạn khác nhau: **Admin** và **User** thông thường.

## 3. Các chức năng chi tiết

### Chức năng chung
* **Function 01: Đăng nhập (Login)**:
    * Người dùng đăng nhập vào hệ thống bằng tên đăng nhập (`UserName`) và mật khẩu (`Password`) từ bảng `Account`.
    * Sau khi đăng nhập thành công, hệ thống sẽ kiểm tra trường `Type` để xác định vai trò người dùng (Admin hoặc User) và chuyển hướng đến trang tương ứng.
    * Nếu `Type = 1`, người dùng là Admin.
    * Nếu `Type = 2`, người dùng là User thông thường.

### Chức năng dành cho Admin
* **Function 02: Tạo pizza (Create a pizza)**:
    * Cho phép Admin thêm một sản phẩm pizza mới vào cơ sở dữ liệu.
    * Thông tin cần thêm bao gồm: `ProductName`, `SupplierID`, `CategoryID`, `QuantityPerUnit`, `UnitPrice`, và `ProductImage` vào bảng `Products`.
* **Function 03: Xóa pizza (Delete a pizza)**:
    * Cho phép Admin xóa một sản phẩm pizza đã tồn tại khỏi cơ sở dữ liệu.
    * Thực hiện xóa dựa trên `ProductID` của sản phẩm.
* **Function 04: Cập nhật pizza (Update a pizza)**:
    * Cho phép Admin chỉnh sửa thông tin của một sản phẩm pizza.
    * Thực hiện cập nhật dựa trên `ProductID`. Các trường có thể chỉnh sửa bao gồm: `ProductName`, `SupplierID`, `CategoryID`, `QuantityPerUnit`, `UnitPrice`, và `ProductImage`.
* **Function 05: Xem danh sách pizza (View pizza)**:
    * Hiển thị danh sách tất cả các sản phẩm pizza hiện có trong hệ thống.
    * Thông tin hiển thị bao gồm: `ProductID`, `ProductName`, `UnitPrice`, `ProductImage`, v.v.
* **Function 06: Xem chi tiết pizza (View details pizza)**:
    * Hiển thị thông tin chi tiết của một sản phẩm pizza cụ thể.
    * Thông tin được truy xuất dựa trên `ProductID`.
* **Function 07: Tạo báo cáo thống kê doanh số (Create a report statistics sales)**:
    * Tạo một báo cáo thống kê doanh số bán hàng trong một khoảng thời gian cụ thể.
    * Báo cáo được tạo dựa trên ngày bắt đầu (`StartDate`) và ngày kết thúc (`EndDate`).
    * Sắp xếp kết quả doanh số theo thứ tự giảm dần.

### Chức năng dành cho User
* **Function 08: Tìm kiếm pizza (Search pizza)**:
    * Cho phép người dùng tìm kiếm sản phẩm pizza.
    * Các tiêu chí tìm kiếm bao gồm:
        * Tìm kiếm theo tên (`ProductName`) (không phân biệt chữ hoa/thường).
        * Tìm kiếm theo giá (`UnitPrice`).
* **Function 09: Xem danh sách pizza (View the pizza list)**:
    * Hiển thị danh sách các sản phẩm pizza có sẵn để người dùng xem.
    * Mỗi sản phẩm hiển thị `ProductName`, `UnitPrice`, `ProductImage`, và các thông tin liên quan khác.
* **Function 10: Quản lý giỏ hàng (View/update/remove item in cart)**:
    * Cho phép người dùng xem các sản phẩm đã thêm vào giỏ hàng.
    * **Cập nhật số lượng**: Người dùng có thể thay đổi số lượng của một sản phẩm trong giỏ hàng.
    * **Xóa sản phẩm**: Người dùng có thể xóa một sản phẩm khỏi giỏ hàng.
    * Lưu ý: Giỏ hàng chỉ là tạm thời (session) hoặc có thể lưu trữ trong cơ sở dữ liệu nếu có yêu cầu.