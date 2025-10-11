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

## Function 07: Báo cáo thống kê doanh số theo khoảng thời gian (Admin)

### ✅ Status: HOÀN THÀNH

**Đã implement:**
- ✅ Model: `SalesReport.java`
- ✅ DAO: `SalesReportDAO.java` (query với JOIN, GROUP BY, ORDER BY DESC)
- ✅ Controller: `SalesReportController.java` (validation, phân quyền Admin)
- ✅ View: `sales-report.jsp` (form, table, alerts, CSS)
- ✅ Navigation: Link "Sales Report" trong navbar cho Admin
- ✅ Tested: Chức năng hoạt động đúng, sắp xếp giảm dần

