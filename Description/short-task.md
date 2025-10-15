# Short Task 3 - PRJ-Assignment-FA25

## ⚠️ BUG: Sản phẩm isActive=0 vẫn hiển thị trên Home Page

### 🔍 Nguyên nhân:
**ProductDAO.getAllProducts() không filter theo isActive**

```java
// Dòng 16 - ProductDAO.java
String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID";
```

Query này lấy **TẤT CẢ** sản phẩm, không kiểm tra `isActive = 1`

**Kết quả:** Sản phẩm "New Pizza 1" (ProductID=7, isActive=0) vẫn hiển thị cho khách hàng ở home page

### ✅ Task Fix:

**File cần sửa:** `DAO/ProductDAO.java`

**Dòng 16 - Thêm WHERE clause:**
```java
// Cũ:
String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID";

// Mới:
String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.isActive = 1";
```

**Lý do:** Chỉ hiển thị sản phẩm đang active cho khách hàng. Sản phẩm bị vô hiệu hóa không được phép mua.

---

## 📝 TODO: Function Register - Đăng ký tài khoản mới

### Mô tả chức năng:
Cho phép người dùng mới tạo tài khoản để mua hàng trên hệ thống.

### Yêu cầu:
- **Input**: 
  - Username (unique, không trùng)
  - Password (tối thiểu 6 ký tự)
  - Confirm Password (phải khớp với Password)
  - Full Name
  - Email (optional)
- **Output**: 
  - Tạo account mới với `type = 2` (Customer)
  - Redirect về trang login sau khi đăng ký thành công
- **Validation**:
  - Username không được trống, không trùng với account đã có
  - Password phải khớp với Confirm Password
  - Full Name không được trống

### Tasks cần thực hiện:

#### 1. Tầng DAO
- [x] Tạo method `AccountDAO.checkUsernameExists(String username)`:
  - Kiểm tra username đã tồn tại chưa
  - Return `true` nếu đã tồn tại, `false` nếu chưa
- [x] Tạo method `AccountDAO.createAccount(Account account)`:
  - Insert account mới vào database
  - Set `type = 2` (Customer) mặc định
  - Return `true` nếu thành công

#### 2. Tầng Controller
- [x] Tạo `Controllers/RegisterController.java`:
  - Annotation `@WebServlet("/register")`
  - **doGet()**: Hiển thị form đăng ký (`register.jsp`)
  - **doPost()**: Xử lý đăng ký
    - Lấy parameters: username, password, confirmPassword, fullName
    - Validate:
      - Không để trống
      - Password == confirmPassword
      - Username chưa tồn tại (gọi `checkUsernameExists()`)
    - Tạo account mới với `type = 2`
    - Gọi `AccountDAO.createAccount()`
    - Redirect về `/login.jsp` nếu thành công
    - Hiển thị lỗi nếu thất bại

#### 3. Tầng View (JSP)
- [x] Tạo `web/register.jsp`:
  - Form đăng ký với các field:
    - Username (required, giữ lại giá trị khi lỗi)
    - Password (required, type="password")
    - Confirm Password (required, type="password")
    - Full Name (required, giữ lại giá trị khi lỗi)
  - Button "Đăng ký"
  - Link "Đã có tài khoản? Đăng nhập ngay"
  - Hiển thị error message (màu đỏ) và success message (màu xanh)
  - CSS styling đẹp mắt với container, shadow, hover effects

#### 4. Navigation
- [x] Cập nhật `navbar.jsp`:
  - Link "Register" đã có sẵn (dòng 67)
  - Đã sửa: `href="register.jsp"` → `href="register"` ✅

#### 5. Database
- [ ] Kiểm tra bảng `Accounts`:
  - Có cột `UserName`, `Password`, `FullName`, `Type`
  - Type: 1 = Admin, 2 = Customer

#### 6. Testing
- [ ] Test case 1: Đăng ký thành công
  - Nhập đầy đủ thông tin hợp lệ
  - Submit → Tạo account mới → Redirect về login
- [ ] Test case 2: Username đã tồn tại
  - Nhập username đã có trong DB → Báo lỗi
- [ ] Test case 3: Password không khớp
  - Password ≠ Confirm Password → Báo lỗi
- [ ] Test case 4: Để trống field
  - Bỏ trống username/password/fullName → Báo lỗi

---

## Function 07: Báo cáo thống kê doanh số theo khoảng thời gian (Admin)

### ✅ Status: HOÀN THÀNH

**Đã implement:**
- ✅ Model: `SalesReport.java`
- ✅ DAO: `SalesReportDAO.java` (query với JOIN, GROUP BY, ORDER BY DESC)
- ✅ Controller: `SalesReportController.java` (validation, phân quyền Admin)
- ✅ View: `sales-report.jsp` (form, table, alerts, CSS)
- ✅ Navigation: Link "Sales Report" trong navbar cho Admin
- ✅ Tested: Chức năng hoạt động đúng, sắp xếp giảm dần

