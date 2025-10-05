/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Models.Cart;
import Models.Product;
import DAO.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * CartController - Xử lý các thao tác với giỏ hàng
 * 
 * Chức năng:
 * - Hiển thị giỏ hàng (GET)
 * - Thêm sản phẩm vào giỏ hàng (POST)
 * 
 * URL Mapping: /cart (configured in web.xml)
 * 
 * @author QUYDAM
 */
public class CartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("CartController is working!");
        }
    }

    /**
     * Xử lý HTTP GET request
     * 
     * URL: GET /cart hoặc GET /cart?action=view
     * Chức năng: Hiển thị giỏ hàng
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số action từ URL
        String action = request.getParameter("action");
        
        // Nếu action = null hoặc "view" → hiển thị giỏ hàng
        if (action == null || action.equals("view")) {
            viewCart(request, response);
        } else {
            // Các action khác (chưa implement)
            processRequest(request, response);
        }
    }

    /**
     * Xử lý HTTP POST request
     * 
     * URL: POST /cart?action=add&productID=101&quantity=2
     * Chức năng: Thêm sản phẩm vào giỏ hàng
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số action từ form
        String action = request.getParameter("action");
        
        // Xử lý các action khác nhau
        if (action != null) {
            switch (action) {
                case "add":
                    addToCart(request, response);
                    break;
                case "update":
                    updateCart(request, response);
                    break;
                case "remove":
                    removeFromCart(request, response);
                    break;
                default:
                    // Action không hợp lệ
                    processRequest(request, response);
                    break;
            }
        } else {
            // Không có action → lỗi
            processRequest(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Cart Controller Servlet";
    }

    /**
     * Method hiển thị giỏ hàng
     */
    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy session hiện tại của user
            HttpSession session = request.getSession();
            
            // Lấy giỏ hàng từ session (có thể null nếu chưa có)
            Cart cart = (Cart) session.getAttribute("cart");
            
            // Nếu giỏ hàng chưa tồn tại → tạo giỏ hàng mới
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);  // Lưu vào session
            }
            
            // Đặt giỏ hàng vào request attribute để JSP có thể sử dụng
            request.setAttribute("cart", cart);
            
            // Thông tin bổ sung cho JSP
            request.setAttribute("totalItems", cart.getItems().size());  // Tổng số sản phẩm
            request.setAttribute("isEmpty", cart.getItems().isEmpty());   // Giỏ hàng có trống không
            
            // Forward đến trang cart.jsp để hiển thị giỏ hàng
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Gửi lỗi 500 về browser nếu có exception
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Lỗi khi hiển thị giỏ hàng: " + e.getMessage());
        }
    }

    /**
     * Method thêm sản phẩm vào giỏ hàng
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tham số từ form (productID và quantity)
            String productIDStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");
            
            // Validate: ProductID không được để trống
            if (productIDStr == null || productIDStr.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ProductID không được để trống");
                return;
            }
            
            // Nếu quantity trống → mặc định là 1
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                quantityStr = "1";
            }
            
            // Chuyển đổi string thành int
            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            // Validate: Số lượng phải > 0
            if (quantity <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Số lượng phải lớn hơn 0");
                return;
            }
            
            // Sử dụng ProductDAO để lấy thông tin sản phẩm
            Product product = ProductDAO.getProductByID(productID);
            
            // Kiểm tra sản phẩm có tồn tại không
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
                return;
            }
            
            // Kiểm tra sản phẩm còn hoạt động không (isActive = true)
            if (!product.isIsActive()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sản phẩm không còn hoạt động");
                return;
            }
            
            // Lấy session hiện tại của user
            HttpSession session = request.getSession();
            
            // Lấy giỏ hàng từ session (có thể null nếu chưa có)
            Cart cart = (Cart) session.getAttribute("cart");
            
            // Nếu giỏ hàng chưa tồn tại → tạo giỏ hàng mới
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);  // Lưu vào session
            }
            
            // Gọi method addItem() của Cart để thêm sản phẩm
            cart.addItem(product, quantity);
            
            // Lưu thông báo thành công vào session để JSP hiển thị
            session.setAttribute("message", "Đã thêm " + product.getProductName() + " vào giỏ hàng");
            session.setAttribute("messageType", "success");
            
            // Lấy URL trang trước đó từ Referer header
            String referer = request.getHeader("Referer");
            
            if (referer != null && !referer.isEmpty()) {
                // Nếu có trang trước → redirect về trang đó
                response.sendRedirect(referer);
            } else {
                // Nếu không có trang trước → redirect về giỏ hàng
                response.sendRedirect("cart");
            }
            
        } catch (NumberFormatException e) {
            // Lỗi khi parse int (productID hoặc quantity không phải số)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ProductID hoặc quantity không hợp lệ");
        } catch (Exception e) {
            // Lỗi khác (database, network, etc.)
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Lỗi khi thêm sản phẩm vào giỏ hàng: " + e.getMessage());
        }
    }

    /**
     * Method cập nhật số lượng sản phẩm trong giỏ hàng
     */
    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tham số từ form (productID và quantity)
            String productIDStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");
            
            // Validate: ProductID không được để trống
            if (productIDStr == null || productIDStr.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ProductID không được để trống");
                return;
            }
            
            // Validate: Quantity không được để trống
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity không được để trống");
                return;
            }
            
            // Chuyển đổi string thành int
            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            // Validate: Số lượng phải >= 0 (0 = xóa sản phẩm)
            if (quantity < 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Số lượng không được âm");
                return;
            }
            
            // Lấy session hiện tại của user
            HttpSession session = request.getSession();
            
            // Lấy giỏ hàng từ session
            Cart cart = (Cart) session.getAttribute("cart");
            
            // Kiểm tra giỏ hàng có tồn tại không
            if (cart == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giỏ hàng không tồn tại");
                return;
            }
            
            // Lấy thông tin sản phẩm để hiển thị thông báo
            String productName = "sản phẩm";
            try {
                Product product = ProductDAO.getProductByID(productID);
                if (product != null) {
                    productName = product.getProductName();
                }
            } catch (Exception e) {
                // Không cần thiết phải dừng nếu không lấy được tên sản phẩm
            }
            
            // Kiểm tra sản phẩm có trong giỏ không trước khi cập nhật
            boolean itemExists = cart.getItems().stream()
                    .anyMatch(item -> item.getProductID() == productID);
            
            if (itemExists) {
                // Cập nhật số lượng sản phẩm trong giỏ
                cart.updateItem(productID, quantity);
                
                if (quantity == 0) {
                    // Số lượng = 0 → đã xóa sản phẩm
                    session.setAttribute("message", "Đã xóa " + productName + " khỏi giỏ hàng");
                } else {
                    // Cập nhật thành công
                    session.setAttribute("message", "Đã cập nhật số lượng " + productName + " thành " + quantity);
                }
                session.setAttribute("messageType", "success");
            } else {
                // Không tìm thấy sản phẩm trong giỏ
                session.setAttribute("message", "Không tìm thấy sản phẩm trong giỏ hàng");
                session.setAttribute("messageType", "error");
            }
            
            // Redirect về trang giỏ hàng để hiển thị kết quả
            response.sendRedirect("cart");
            
        } catch (NumberFormatException e) {
            // Lỗi khi parse int (productID hoặc quantity không phải số)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ProductID hoặc quantity không hợp lệ");
        } catch (Exception e) {
            // Lỗi khác (database, network, etc.)
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Lỗi khi cập nhật giỏ hàng: " + e.getMessage());
        }
    }

    /**
     * Method xóa sản phẩm khỏi giỏ hàng
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tham số từ form (productID)
            String productIDStr = request.getParameter("productID");
            
            // Validate: ProductID không được để trống
            if (productIDStr == null || productIDStr.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ProductID không được để trống");
                return;
            }
            
            // Chuyển đổi string thành int
            int productID = Integer.parseInt(productIDStr);
            
            // Lấy session hiện tại của user
            HttpSession session = request.getSession();
            
            // Lấy giỏ hàng từ session
            Cart cart = (Cart) session.getAttribute("cart");
            
            // Kiểm tra giỏ hàng có tồn tại không
            if (cart == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giỏ hàng không tồn tại");
                return;
            }
            
            // Lấy thông tin sản phẩm để hiển thị thông báo
            String productName = "sản phẩm";
            try {
                Product product = ProductDAO.getProductByID(productID);
                if (product != null) {
                    productName = product.getProductName();
                }
            } catch (Exception e) {
                // Không cần thiết phải dừng nếu không lấy được tên sản phẩm
            }
            
            // Kiểm tra sản phẩm có trong giỏ không trước khi xóa
            boolean itemExists = cart.getItems().stream()
                    .anyMatch(item -> item.getProductID() == productID);
            
            if (itemExists) {
                // Xóa sản phẩm khỏi giỏ hàng
                cart.removeItem(productID);
                
                // Xóa thành công
                session.setAttribute("message", "Đã xóa " + productName + " khỏi giỏ hàng");
                session.setAttribute("messageType", "success");
            } else {
                // Không tìm thấy sản phẩm trong giỏ
                session.setAttribute("message", "Không tìm thấy sản phẩm trong giỏ hàng");
                session.setAttribute("messageType", "error");
            }
            
            // Redirect về trang giỏ hàng để hiển thị kết quả
            response.sendRedirect("cart");
            
        } catch (NumberFormatException e) {
            // Lỗi khi parse int (productID không phải số)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ProductID không hợp lệ");
        } catch (Exception e) {
            // Lỗi khác (database, network, etc.)
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Lỗi khi xóa sản phẩm khỏi giỏ hàng: " + e.getMessage());
        }
    }

}