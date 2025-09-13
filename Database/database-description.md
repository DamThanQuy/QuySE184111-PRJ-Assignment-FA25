# Yêu cầu dự án: Xây dựng Cơ sở dữ liệu cho PizzaStore

## Tổng quan dự án
Đây là một dự án xây dựng ứng dụng web cho một nhà bán lẻ sản phẩm tên là PizzaStore. Ứng dụng này sẽ hỗ trợ quản lý thành viên, sản phẩm và đơn hàng. [cite_start]Dữ liệu sẽ được lưu trữ trong cơ sở dữ liệu MS SQL Server[cite: 9].

## Sơ đồ cơ sở dữ liệu (Database Schema)
Dưới đây là sơ đồ chi tiết của các bảng và các trường tương ứng:

### Bảng: Products
* `ProductID`: Khóa chính
* `ProductName`: Tên sản phẩm
* `SupplierID`: Khóa ngoại liên kết đến bảng `Suppliers`
* `CategoryID`: Khóa ngoại liên kết đến bảng `Categories`
* `QuantityPerUnit`
* `UnitPrice`
* `ProductImage`

### Bảng: Categories
* `CategoryID`: Khóa chính
* `CategoryName`: Tên danh mục
* `Description`: Mô tả

### Bảng: Suppliers
* `SupplierID`: Khóa chính
* `CompanyName`: Tên công ty
* `Address`: Địa chỉ
* `Phone`: Số điện thoại

### Bảng: Orders
* `OrderID`: Khóa chính
* `CustomerID`: Khóa ngoại liên kết đến bảng `Customers`
* `OrderDate`: Ngày đặt hàng
* `RequiredDate`: Ngày yêu cầu
* `ShippedDate`: Ngày giao hàng
* `Freight`
* `ShipAddress`: Địa chỉ giao hàng

### Bảng: Customers
* `CustomerID`: Khóa chính
* `ContactName`: Tên liên hệ
* `Address`: Địa chỉ
* `Phone`: Số điện thoại

### Bảng: Order Details
* `OrderID`: Khóa chính, Khóa ngoại liên kết đến bảng `Orders`
* `ProductID`: Khóa chính, Khóa ngoại liên kết đến bảng `Products`
* `UnitPrice`
* `Quantity`

### Bảng: Account
* `AccountID`: Khóa chính
* `UserName`: Tên đăng nhập
* `Password`: Mật khẩu
* `FullName`: Tên đầy đủ
* [cite_start]`Type`: Phân loại người dùng (Staff = 1, Normal = 2) [cite: 79]

## Yêu cầu chức năng
* Tạo cơ sở dữ liệu trên SQL Server.
* [cite_start]Thực hiện các thao tác **CRUD** (Create, Read, Update, Delete) cho các bảng `Member`, `Product`, và `Order`[cite: 7, 72].
* [cite_start]Tìm kiếm sản phẩm theo `ProductID`, `ProductName` (không phân biệt chữ hoa/thường) và `UnitPrice`[cite: 73].
* [cite_start]Tạo báo cáo thống kê doanh số bán hàng theo khoảng thời gian từ `StartDate` đến `EndDate`, và sắp xếp theo thứ tự giảm dần[cite: 74].
* Phân quyền người dùng:
    * [cite_start]**Staff (`Type=1`)**: Có quyền thực hiện tất cả các hành động (CRUD)[cite: 79].
    * [cite_start]**Normal user (`Type=2`)**: Chỉ được phép xem/tạo/cập nhật hồ sơ của mình, đặt hàng, và xem lịch sử đơn hàng của họ[cite: 79].