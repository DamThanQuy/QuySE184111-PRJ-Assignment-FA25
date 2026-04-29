# PizzaStore - Hướng dẫn sử dụng

## Giới thiệu
PizzaStore là ứng dụng web quản lý cửa hàng pizza, hỗ trợ đặt hàng trực tuyến và quản lý sản phẩm, đơn hàng. Ứng dụng được xây dựng bằng Java (JSP/Servlet) và sử dụng cơ sở dữ liệu MS SQL Server.

## Tính năng chính

### 1. Quản trị viên (Staff - Type=1)
- **Quản lý sản phẩm**: Tạo, xem, cập nhật, xóa thông tin pizza
- **Xem chi tiết sản phẩm**: Xem thông tin chi tiết từng loại pizza
- **Báo cáo thống kê**: Thống kê doanh số bán hàng theo khoảng thời gian (StartDate đến EndDate), sắp xếp giảm dần

### 2. Người dùng thường (Normal User - Type=2)
- **Tìm kiếm pizza**: Tìm theo tên và giá
- **Xem danh sách**: Xem tất cả các loại pizza có sẵn
- **Giỏ hàng**: 
  - Xem giỏ hàng
  - Cập nhật số lượng sản phẩm trong giỏ
  - Xóa sản phẩm khỏi giỏ
- **Đặt hàng**: Tạo đơn hàng mới
- **Lịch sử đơn hàng**: Xem các đơn hàng đã đặt

## Yêu cầu hệ thống

### Phần cứng & Phần mềm
- Java Development Kit (JDK) 8 trở lên
- Apache Tomcat 9 trở lên
- Microsoft SQL Server
- Trình duyệt web hiện đại (Chrome, Firefox, Edge)

### Thư viện cần thiết
- JDBC Driver cho SQL Server
- Các thư viện JSTL (nếu có)

## Cài đặt

### Bước 1: Thiết lập cơ sở dữ liệu
1. Mở SQL Server Management Studio
2. Chạy file script tạo bảng:
   ```sql
   -- Đường dẫn: Database/create_tables.sql
   ```
3. Kiểm tra các bảng đã được tạo thành công:
   - Products
   - Categories
   - Suppliers
   - Orders
   - Customers
   - Order Details
   - Account

### Bước 2: Cấu hình kết nối database
1. Mở file cấu hình kết nối trong dự án (thường là `context.xml` hoặc file utility class)
2. Cập nhật thông tin kết nối:
   ```
   URL: jdbc:sqlserver://localhost:1433;databaseName=PizzaStore
   Username: [tên đăng nhập SQL Server của bạn]
   Password: [mật khẩu SQL Server của bạn]
   ```

### Bước 3: Build và Deploy dự án
1. Mở terminal/command prompt tại thư mục `PizzaCompany/`
2. Sử dụng Ant để build:
   ```bash
   ant clean
   ant compile
   ant war
   ```
3. Copy file `.war` vào thư mục `webapps` của Apache Tomcat
4. Khởi động Tomcat

### Bước 4: Truy cập ứng dụng
Mở trình duyệt và truy cập:
```
http://localhost:8080/PizzaCompany/
```

## Hướng dẫn sử dụng

### Đăng nhập
1. Truy cập trang chủ
2. Nhập `UserName` và `Password`
3. Hệ thống tự động phân quyền dựa trên trường `Type`:
   - `Type = 1`: Admin/Staff
   - `Type = 2`: Normal User

### Đối với Admin
1. **Tạo pizza mới**:
   - Vào trang quản lý sản phẩm
   - Click "Create" và điền thông tin: tên, giá, danh mục, nhà cung cấp, hình ảnh
   
2. **Cập nhật/Xóa pizza**:
   - Chọn sản phẩm cần sửa/xóa
   - Click "Update" hoặc "Delete"
   
3. **Xem báo cáo**:
   - Vào trang báo cáo
   - Chọn StartDate và EndDate
   - Click "Generate Report" để xem thống kê doanh số

### Đối với User
1. **Tìm kiếm pizza**:
   - Nhập tên hoặc khoảng giá vào ô tìm kiếm
   - Kết quả sẽ hiển thị ngay lập tức
   
2. **Thêm vào giỏ hàng**:
   - Chọn sản phẩm muốn mua
   - Click "Add to Cart"
   
3. **Quản lý giỏ hàng**:
   - Xem giỏ: Click "View Cart"
   - Cập nhật số lượng: Sửa số lượng và click "Update"
   - Xóa sản phẩm: Click "Remove" bên cạnh sản phẩm
   
4. **Đặt hàng**:
   - Điền thông tin giao hàng
   - Xác nhận đơn hàng

## Cấu trúc dự án

```
PizzaCompany/
├── src/                    # Source code Java (Servlets, Models, Utils)
├── web/                    # JSP files, CSS, JavaScript, Images
│   ├── index.jsp
│   ├── login.jsp
│   ├── admin/              # Trang quản trị
│   └── user/               # Trang người dùng
├── build.xml               # Ant build script
└── nbproject/              # NetBeans project configuration

Database/
├── create_tables.sql       # Script tạo bảng
└── database-description.md # Mô tả chi tiết CSDL

Description/
├── funtion-description.md  # Mô tả chức năng
├── page-analysis.md        # Phân tích trang
├── short-task.md           # Task ngắn hạn
└── todo.md                 # Danh sách việc cần làm
```

## Tài khoản mặc định (nếu có)

| Username | Password | Type | Vai trò |
|----------|----------|------|---------|
| admin    | [xem trong DB] | 1 | Quản trị viên |
| user     | [xem trong DB] | 2 | Người dùng thường |

*Lưu ý: Mật khẩu mặc định nên được thay đổi sau khi cài đặt*

## Xử lý sự cố thường gặp

### Lỗi kết nối database
- Kiểm tra SQL Server đang chạy
- Xác minh thông tin kết nối trong file cấu hình
- Đảm bảo JDBC Driver đã được thêm vào classpath

### Lỗi build project
- Kiểm tra biến môi trường JAVA_HOME
- Đảm bảo Ant đã được cài đặt
- Xóa thư mục `build/` và build lại từ đầu

### Lỗi deploy lên Tomcat
- Kiểm tra port 8080 không bị chiếm dụng
- Xem log của Tomcat trong `logs/catalina.out`
- Đảm bảo file .war không bị lỗi

## Liên hệ & Hỗ trợ

Nếu gặp vấn đề trong quá trình cài đặt hoặc sử dụng, vui lòng kiểm tra:
- File log của ứng dụng
- File log của Tomcat
- SQL Server error logs

---

**Lưu ý**: Đây là dự án mẫu phục vụ học tập. Vui lòng không sử dụng cho mục đích thương mại mà không có sự cho phép.
