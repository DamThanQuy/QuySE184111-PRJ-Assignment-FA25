# Task 5: Hiển thị chi tiết sản phẩm

## Mô tả
Khi người dùng nhấn **View** trong trang `manageProducts.jsp`, hệ thống sẽ chuyển tới `DetailProductController` để hiển thị thông tin chi tiết của một sản phẩm.

## Các bước thực hiện (Task List)

### Task 5.1: Kiểm tra và lấy tham số `id`
- [ ] Trong `DetailProductController.doGet`, lấy giá trị `id` từ `request.getParameter("id")`.
- [ ] Kiểm tra `id` không null và là số nguyên, nếu không hợp lệ trả về lỗi 400.

### Task 5.2: Gọi DAO để lấy chi tiết sản phẩm
- [ ] Sử dụng `ProductDAO.getProductByID(int id)` để lấy đối tượng `Product`.
- [ ] Xử lý ngoại lệ (`Exception`) và trả về lỗi 500 nếu có lỗi truy vấn.

### Task 5.3: Đặt thuộc tính cho JSP
- [ ] Nếu sản phẩm tồn tại, đặt `request.setAttribute("product", product);`
- [ ] Nếu không tìm thấy, trả về lỗi 404 (`response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");`).

### Task 5.4: Chuyển tiếp tới JSP
- [ ] Gọi `request.getRequestDispatcher("detailProduct.jsp").forward(request, response);`
- [ ] Đảm bảo JSP `detailProduct.jsp` tồn tại trong thư mục `web/`.

### Task 5.5: Chuyển đổi href thành button trong detailProduct.jsp
- [ ] Tìm kiếm tất cả các thẻ `<a>` có thuộc tính `href` và chuyển đổi chúng thành thẻ `<button>`.
- [ ] Đảm bảo rằng các button mới có thể hoạt động đúng cách và không ảnh hưởng đến layout của trang.

### Task 5.6: Kiểm thử
- [ ] Kiểm tra truy cập URL `/detailProduct?id=1` với ID hợp lệ → hiển thị chi tiết.
- [ ] Kiểm tra ID không tồn tại → trả về 404.
- [ ] Kiểm tra ID không phải số → trả về 400.
- [ ] Kiểm tra khi DAO ném ngoại lệ → trả về 500.

## Lợi ích
- Người admin có thể xem chi tiết sản phẩm ngay từ bảng quản lý.
- Cải thiện trải nghiệm người dùng và hỗ trợ kiểm tra dữ liệu nhanh chóng.
- Tách biệt logic lấy dữ liệu (DAO) và hiển thị (JSP) theo mô hình MVC.
