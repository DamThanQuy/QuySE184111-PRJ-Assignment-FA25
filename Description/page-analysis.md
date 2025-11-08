# LUỒNG THANH TOÁN (Payment Flow)

## Tổng quan
Luồng xử lý từ khi người dùng thêm sản phẩm vào giỏ hàng cho đến khi hoàn tất đặt hàng.

---

## 1. THÊM SẢN PHẨM VÀO GIỎ HÀNG

### 1.1. User Interface (home.jsp)
- **Người dùng**: Duyệt danh sách sản phẩm trên trang chủ
- **Hành động**: Click nút "Add to Cart" trên sản phẩm mong muốn
- **Form submission**:
  ```html
  POST /cart?action=add
  Parameters:
    - productID: [ID sản phẩm]
    - quantity: [Số lượng] (mặc định = 1)
  ```

### 1.2. Controller Processing (CartController.java)

**Method**: `addToCart(request, response)`

**Bước xử lý**:
1. **Validate input**:
   - Kiểm tra productID không null/empty
   - Kiểm tra quantity > 0 (mặc định = 1 nếu trống)
   - Parse string → int

2. **Lấy thông tin sản phẩm**:
   ```java
   Product product = ProductDAO.getProductByID(productID);
   ```
   - Kiểm tra sản phẩm tồn tại
   - Kiểm tra sản phẩm còn hoạt động (isActive = true)

3. **Xử lý giỏ hàng trong Session**:
   ```java
   HttpSession session = request.getSession();
   Cart cart = (Cart) session.getAttribute("cart");
   
   if (cart == null) {
       cart = new Cart();
       session.setAttribute("cart", cart);
   }
   ```

4. **Thêm sản phẩm vào giỏ**:
   ```java
   cart.addItem(product, quantity);
   ```
   - Nếu sản phẩm đã tồn tại → Cộng dồn số lượng
   - Nếu sản phẩm mới → Tạo CartItem mới
   - Tự động tính lại totalAmount

5. **Redirect**:
   - Lưu message thành công vào session
   - Redirect về trang trước đó (Referer header)
   - Nếu không có Referer → Redirect về /cart

**Response**:
- Status: 302 (Redirect)
- Pattern: **Post-Redirect-Get (PRG)** để tránh duplicate submission

---

## 2. XEM GIỎ HÀNG

### 2.1. User Interface (cart.jsp)
- **Truy cập**: Click "🛒 Cart" trên navbar hoặc sau khi thêm sản phẩm
- **URL**: `GET /cart` hoặc `GET /cart?action=view`

### 2.2. Controller Processing (CartController.java)

**Method**: `viewCart(request, response)`

**Bước xử lý**:
1. Lấy Cart từ session (tạo mới nếu chưa có)
2. Set attributes cho JSP:
   ```java
   request.setAttribute("cart", cart);
   request.setAttribute("totalItems", cart.getItems().size());
   request.setAttribute("isEmpty", cart.getItems().isEmpty());
   ```
3. Forward đến cart.jsp

### 2.3. Hiển thị trên cart.jsp
- **Danh sách sản phẩm**: Bảng hiển thị từng CartItem
  - Product Name
  - Unit Price
  - Quantity (có thể chỉnh sửa)
  - Total Price
- **Tổng tiền**: `${cart.totalAmount}`
- **Actions**:
  - Update quantity: Form POST /cart?action=update
  - Remove item: Form POST /cart?action=remove
  - Checkout: Link đến /checkout

---

## 3. CẬP NHẬT GIỎ HÀNG (Optional)

### 3.1. Update Quantity

**Form submission**:
```html
POST /cart?action=update
Parameters:
  - productID: [ID sản phẩm]
  - quantity: [Số lượng mới]
```

**Method**: `updateCart(request, response)`

**Bước xử lý**:
1. Validate input (productID, quantity >= 0)
2. Lấy Cart từ session
3. Gọi `cart.updateItem(productID, quantity)`:
   - Nếu quantity = 0 → Xóa sản phẩm
   - Nếu quantity > 0 → Cập nhật số lượng
4. Tự động tính lại totalAmount
5. Redirect về /cart

### 3.2. Remove Item

**Form submission**:
```html
POST /cart?action=remove
Parameters:
  - productID: [ID sản phẩm]
```

**Method**: `removeFromCart(request, response)`

**Bước xử lý**:
1. Validate productID
2. Gọi `cart.removeItem(productID)`
3. Redirect về /cart

---

## 4. CHECKOUT - BƯỚC 1: HIỂN thị TRANG THANH TOÁN

### 4.1. User Action
- **Từ cart.jsp**: Click nút "Proceed to Checkout"
- **URL**: `GET /checkout` hoặc `GET /checkout?action=checkout`

### 4.2. Controller Processing (CheckoutController.java)

**Method**: `showCheckoutPage(request, response)`

**Bước xử lý**:
1. **Kiểm tra giỏ hàng**:
   ```java
   Cart cart = (Cart) session.getAttribute("cart");
   
   if (cart == null || cart.getItems().isEmpty()) {
       // Redirect về cart.jsp với thông báo lỗi
       return;
   }
   ```

2. **Tính tổng tiền**:
   ```java
   double totalAmount = cart.getTotalAmount();
   ```

3. **Set attributes và forward**:
   ```java
   request.setAttribute("cart", cart);
   request.setAttribute("totalAmount", totalAmount);
   request.getRequestDispatcher("checkout.jsp").forward(request, response);
   ```

### 4.3. Hiển thị trên checkout.jsp
- **Thông tin đơn hàng**:
  - Danh sách sản phẩm (chỉ xem, không chỉnh sửa)
  - Tổng tiền
- **Form nhập thông tin giao hàng**:
  ```html
  <form method="POST" action="checkout?action=process">
      <input name="shipAddress" required />
      <input name="phoneNumber" required />
      <button type="submit">Place Order</button>
  </form>
  ```

---

## 5. CHECKOUT - BƯỚC 2: XỬ LÝ ĐẶT HÀNG

### 5.1. Form Submission
```html
POST /checkout?action=process
Parameters:
  - shipAddress: [Địa chỉ giao hàng]
  - phoneNumber: [Số điện thoại]
```

### 5.2. Controller Processing (CheckoutController.java)

**Method**: `processCheckout(request, response)`

**Bước xử lý chi tiết**:

#### 5.2.1. Validation đầu vào
```java
// 1. Kiểm tra giỏ hàng
Cart cart = (Cart) session.getAttribute("cart");
if (cart == null || cart.getItems().isEmpty()) {
    // Error: Giỏ hàng trống
    return;
}

// 2. Lấy thông tin từ form
String shipAddress = request.getParameter("shipAddress");
String phoneNumber = request.getParameter("phoneNumber");

// 3. Validate shipAddress
if (shipAddress == null || shipAddress.trim().isEmpty()) {
    request.setAttribute("error", "Vui lòng nhập địa chỉ giao hàng.");
    // Forward về checkout.jsp
    return;
}

// 4. Validate phoneNumber
if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
    request.setAttribute("error", "Vui lòng nhập số điện thoại.");
    // Forward về checkout.jsp
    return;
}

// 5. Kiểm tra đăng nhập
Account account = (Account) session.getAttribute("account");
if (account == null) {
    request.setAttribute("error", "Please login to place an order.");
    // Forward về login.jsp
    return;
}
```

#### 5.2.2. Tạo Order object
```java
int customerID = account.getAccountID();

Order order = new Order();
order.setCustomerID(customerID);
order.setOrderDate(LocalDateTime.now());
order.setShipAddress(shipAddress.trim());
order.setPhoneNumber(phoneNumber.trim());
```

#### 5.2.3. Chuyển đổi Cart → OrderDetails
```java
List<OrderDetail> orderDetails = new ArrayList<>();
for (CartItem item : cart.getItems()) {
    OrderDetail detail = new OrderDetail();
    detail.setProductID(item.getProductID());
    detail.setUnitPrice(item.getUnitPrice());
    detail.setQuantity(item.getQuantity());
    orderDetails.add(detail);
}
```

#### 5.2.4. Lưu vào Database (Transaction)
```java
OrderDAO orderDAO = new OrderDAO();
int orderID = orderDAO.createCompleteOrder(order, orderDetails);
```

**OrderDAO.createCompleteOrder() thực hiện**:
1. Bắt đầu transaction: `conn.setAutoCommit(false)`
2. Insert vào bảng Orders → Lấy OrderID
3. Insert từng OrderDetail vào bảng OrderDetails
4. Commit transaction
5. Nếu có lỗi → Rollback

#### 5.2.5. Lấy lại thông tin đơn hàng
```java
Order createdOrder = orderDAO.getOrderById(orderID);
OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
List<OrderDetail> createdOrderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderID);
```

#### 5.2.6. Xóa giỏ hàng
```java
session.removeAttribute("cart");
```
**Lý do**: Đơn hàng đã được lưu vào database, không cần giữ trong session nữa.

#### 5.2.7. Chuyển đến trang xác nhận
```java
request.setAttribute("order", createdOrder);
request.setAttribute("orderDetails", createdOrderDetails);
request.setAttribute("message", "Order placed successfully! Order ID: #" + orderID);
request.getRequestDispatcher("order-confirmation.jsp").forward(request, response);
```

---

## 6. XÁC NHẬN ĐƠN HÀNG

### 6.1. Hiển thị trên order-confirmation.jsp

**Thông tin hiển thị**:
- ✅ Thông báo thành công
- 📦 **Order Information**:
  - Order ID: `#${order.orderID}`
  - Order Date: `${order.orderDate}`
  - Ship Address: `${order.shipAddress}`
  - Phone Number: `${order.phoneNumber}`
- 📋 **Order Details**:
  - Bảng chi tiết sản phẩm (Product Name, Unit Price, Quantity, Subtotal)
  - Total Amount
- 🔗 **Actions**:
  - Link "View Order History" → /orders
  - Link "Continue Shopping" → /home

---

## 7. LUỒNG DỮ LIỆU (Data Flow)

### 7.1. Session Management
```
┌─────────────────────────────────────────────┐
│              HttpSession                    │
├─────────────────────────────────────────────┤
│ "cart" → Cart object                       │
│   ├── items: List<CartItem>                │
│   └── totalAmount: double                  │
│                                             │
│ "account" → Account object                 │
│   ├── accountID: int                       │
│   ├── username: String                     │
│   └── role: String                         │
└─────────────────────────────────────────────┘
```

### 7.2. Database Transaction Flow
```
CheckoutController
       |
       v
  OrderDAO.createCompleteOrder(order, orderDetails)
       |
       v
  [Transaction Start]
       |
       ├─> INSERT INTO Orders (...)
       │   └─> Get generated OrderID
       │
       ├─> For each OrderDetail:
       │   └─> INSERT INTO OrderDetails (...)
       │
       v
  [Transaction Commit]
       |
       v
  Return OrderID
```

### 7.3. Cart → Order Conversion
```
Cart (Session)                  Order (Database)
├── CartItem 1                  ├── Orders Table
│   ├── productID       ────────┼──> OrderID (Auto-generated)
│   ├── productName             │    CustomerID (from Account)
│   ├── unitPrice               │    OrderDate (LocalDateTime.now())
│   └── quantity                │    ShipAddress (from form)
│                               │    PhoneNumber (from form)
├── CartItem 2                  │
│   └── ...                     └── OrderDetails Table
│                                    ├── OrderDetail 1
└── totalAmount                      │   ├── OrderID
    (calculated)                     │   ├── ProductID
                                     │   ├── UnitPrice
                                     │   └── Quantity
                                     │
                                     └── OrderDetail 2
                                         └── ...
```

---

## 8. ERROR HANDLING

### 8.1. Validation Errors
| Lỗi | Message | Action |
|------|---------|--------|
| Giỏ hàng trống | "Your cart is empty" | Redirect về cart.jsp |
| Chưa đăng nhập | "Please login to place an order" | Forward về login.jsp |
| Thiếu địa chỉ | "Vui lòng nhập địa chỉ giao hàng" | Forward về checkout.jsp |
| Thiếu SĐT | "Vui lòng nhập số điện thoại" | Forward về checkout.jsp |
| Sản phẩm không tồn tại | "Không tìm thấy sản phẩm" | Error 404 |
| Sản phẩm inactive | "Sản phẩm không còn hoạt động" | Error 400 |

### 8.2. Database Errors
- **SQLException**: Rollback transaction, hiển thị "Failed to process checkout"
- **Transaction failure**: Tự động rollback, không tạo đơn hàng

---

## 9. URL MAPPING

### 9.1. Cart Operations
| Method | URL | Action | Controller Method |
|--------|-----|--------|------------------|
| GET | /cart | Xem giỏ hàng | CartController.viewCart() |
| POST | /cart?action=add | Thêm sản phẩm | CartController.addToCart() |
| POST | /cart?action=update | Cập nhật số lượng | CartController.updateCart() |
| POST | /cart?action=remove | Xóa sản phẩm | CartController.removeFromCart() |

### 9.2. Checkout Operations
| Method | URL | Action | Controller Method |
|--------|-----|--------|------------------|
| GET | /checkout | Hiển thị trang checkout | CheckoutController.showCheckoutPage() |
| POST | /checkout?action=process | Xử lý đặt hàng | CheckoutController.processCheckout() |

---

## 10. SECURITY & BEST PRACTICES

### 10.1. Security
- ✅ **Session-based Cart**: Mỗi user có giỏ hàng riêng
- ✅ **Authentication Check**: Kiểm tra đăng nhập trước khi checkout
- ✅ **Input Validation**: Validate tất cả input từ form
- ✅ **SQL Injection Prevention**: Sử dụng PreparedStatement
- ✅ **Transaction Safety**: Rollback nếu có lỗi

### 10.2. Design Patterns
- ✅ **MVC Pattern**: Tách biệt Controller, Model, View
- ✅ **DAO Pattern**: Tách biệt business logic và data access
- ✅ **Post-Redirect-Get (PRG)**: Tránh duplicate submission khi F5
- ✅ **Session Management**: Lưu trạng thái user trong session

### 10.3. User Experience
- ✅ **Referer Redirect**: Quay về trang trước sau khi add to cart
- ✅ **Success Messages**: Hiển thị thông báo thành công/lỗi
- ✅ **Empty Cart Handling**: Kiểm tra và thông báo giỏ hàng trống
- ✅ **Order Confirmation**: Hiển thị chi tiết đơn hàng sau khi đặt

---

## 11. WORKFLOW DIAGRAM

```
[USER] → [home.jsp] → Click "Add to Cart"
           ↓
       POST /cart?action=add
           ↓
   [CartController.addToCart()]
           ├─> Validate input
           ├─> Get Product from DB
           ├─> Get/Create Cart in Session
           ├─> cart.addItem(product, quantity)
           └─> Redirect to Referer
           ↓
       [Trang trước] (với message thành công)


[USER] → Click "🛒 Cart" trong navbar
           ↓
       GET /cart
           ↓
   [CartController.viewCart()]
           ├─> Get Cart from Session
           └─> Forward to cart.jsp
           ↓
       [cart.jsp]
           ├─> Hiển thị danh sách CartItem
           ├─> Hiển thị Total Amount
           └─> Nút "Proceed to Checkout"


[USER] → Click "Proceed to Checkout"
           ↓
       GET /checkout
           ↓
   [CheckoutController.showCheckoutPage()]
           ├─> Validate Cart không trống
           ├─> Tính totalAmount
           └─> Forward to checkout.jsp
           ↓
       [checkout.jsp]
           ├─> Hiển thị Order Summary
           ├─> Form nhập shipAddress, phoneNumber
           └─> Nút "Place Order"


[USER] → Nhập thông tin & Click "Place Order"
           ↓
       POST /checkout?action=process
           ↓
   [CheckoutController.processCheckout()]
           ├─> Validate Cart, Account, Input
           ├─> Tạo Order object
           ├─> Convert Cart → List<OrderDetail>
           ├─> OrderDAO.createCompleteOrder()
           │      ├─> [Transaction Start]
           │      ├─> INSERT Orders
           │      ├─> INSERT OrderDetails (loop)
           │      └─> [Transaction Commit]
           ├─> Get created Order & OrderDetails
           ├─> Remove Cart from Session
           └─> Forward to order-confirmation.jsp
           ↓
       [order-confirmation.jsp]
           ├─> Hiển thị Order ID, thông tin đơn hàng
           ├─> Hiển thị chi tiết sản phẩm
           └─> Links: View Orders / Continue Shopping
```

---

## 12. FILES LIÊN QUAN

### Controllers
- `CartController.java` - Quản lý giỏ hàng (add/update/remove/view)
- `CheckoutController.java` - Xử lý thanh toán (show/process)

### Models
- `Cart.java` - Model giỏ hàng (items, totalAmount, methods)
- `CartItem.java` - Model sản phẩm trong giỏ (productID, quantity, price)
- `Order.java` - Model đơn hàng
- `OrderDetail.java` - Model chi tiết đơn hàng
- `Account.java` - Model tài khoản người dùng
- `Product.java` - Model sản phẩm

### DAO
- `OrderDAO.java` - CRUD operations cho Orders
- `OrderDetailDAO.java` - CRUD operations cho OrderDetails
- `ProductDAO.java` - CRUD operations cho Products

### Views (JSP)
- `home.jsp` - Trang chủ (hiển thị sản phẩm, nút Add to Cart)
- `cart.jsp` - Trang giỏ hàng (xem/sửa/xóa sản phẩm)
- `checkout.jsp` - Trang thanh toán (nhập thông tin giao hàng)
- `order-confirmation.jsp` - Trang xác nhận đơn hàng
- `navbar.jsp` - Navigation bar (link Cart, hiển thị số lượng)

### Database Tables
- `Orders` - Lưu thông tin đơn hàng
- `OrderDetails` - Lưu chi tiết sản phẩm trong đơn hàng
- `Products` - Lưu thông tin sản phẩm
- `Accounts` - Lưu thông tin tài khoản

---

**Tổng kết**: Luồng thanh toán hoàn chỉnh từ Add to Cart → View Cart → Checkout → Order Confirmation, với đầy đủ validation, transaction handling, và user experience tốt.
