
package Controllers;

import DAO.OrderDAO;
import DAO.OrderDetailDAO;
import Models.Cart;
import Models.CartItem;
import Models.Order;
import Models.OrderDetail;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import Models.Account;

/**
 *
 * @author QUYDAM
 */
@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "checkout";
        }
        
        try {
            switch (action) {
                case "checkout":
                    showCheckoutPage(request, response);
                    break;
                case "process":
                    processCheckout(request, response);
                    break;
                default:
                    showCheckoutPage(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== CHECKOUT ERROR ===");
            System.out.println("Error message: " + e.getMessage());
            System.out.println("Error class: " + e.getClass().getName());
            e.printStackTrace();
            System.out.println("=====================");
            
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }
    
    /**
     * Hiển thị trang checkout
     */
    private void showCheckoutPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null || cart.getItems().isEmpty()) {
            request.setAttribute("message", "Your cart is empty. Please add some items before checkout.");
            request.setAttribute("cart", cart);
            request.setAttribute("totalAmount", 0.0);
            request.setAttribute("isEmpty", true);
            request.setAttribute("totalItems", 0);
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }
        
        // Tính tổng tiền
        double totalAmount = cart.getTotalAmount();
        
        request.setAttribute("cart", cart);
        request.setAttribute("totalAmount", totalAmount);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }
    
    /**
     * Xử lý thanh toán
     */
    private void processCheckout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        // Kiểm tra giỏ hàng
        if (cart == null || cart.getItems().isEmpty()) {
            request.setAttribute("error", "Your cart is empty.");
            request.setAttribute("cart", cart);
            request.setAttribute("totalAmount", 0.0);
            request.setAttribute("isEmpty", true);
            request.setAttribute("totalItems", 0);
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }
        
        try {
            // Lấy thông tin từ form
            String shipAddress = request.getParameter("shipAddress");
            String phoneNumber = request.getParameter("phoneNumber");
            
            // Lấy account từ session
            Account account = (Account) session.getAttribute("account");
            
            // Validation
            if (shipAddress == null || shipAddress.trim().isEmpty()) {
                double totalAmount = cart != null ? cart.getTotalAmount() : 0.0;
                request.setAttribute("cart", cart);
                request.setAttribute("totalAmount", totalAmount);
                request.setAttribute("error", "Vui lòng nhập địa chỉ giao hàng.");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }
            
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                double totalAmount = cart != null ? cart.getTotalAmount() : 0.0;
                request.setAttribute("cart", cart);
                request.setAttribute("totalAmount", totalAmount);
                request.setAttribute("error", "Vui lòng nhập số điện thoại.");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }
            
            if (account == null) {
                request.setAttribute("error", "Please login to place an order.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            // Sử dụng accountID thay vì customerID
            int customerID = account.getAccountID();
            
            // Tạo Order
            Order order = new Order();
            order.setCustomerID(customerID); // Sử dụng accountID
            order.setOrderDate(LocalDateTime.now());
            order.setShipAddress(shipAddress.trim());
            order.setPhoneNumber(phoneNumber.trim());
            
            // Tạo OrderDetails từ Cart
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItem item : cart.getItems()) {
                OrderDetail detail = new OrderDetail();
                detail.setProductID(item.getProductID());
                detail.setUnitPrice(item.getUnitPrice());
                detail.setQuantity(item.getQuantity());
                orderDetails.add(detail);
            }
            
            // Lưu vào database
            OrderDAO orderDAO = new OrderDAO();
            int orderID = orderDAO.createCompleteOrder(order, orderDetails);
            
            // Lấy thông tin đơn hàng vừa tạo
            Order createdOrder = orderDAO.getOrderById(orderID);
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            List<OrderDetail> createdOrderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderID);
            
            // Xóa giỏ hàng
            session.removeAttribute("cart");
            
            // Chuyển đến trang xác nhận
            request.setAttribute("order", createdOrder);
            request.setAttribute("orderDetails", createdOrderDetails);
            request.setAttribute("message", "Order placed successfully! Order ID: #" + orderID);
            request.getRequestDispatcher("order-confirmation.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            // Lấy lại dữ liệu để hiển thị trên checkout.jsp
            double totalAmount = cart != null ? cart.getTotalAmount() : 0.0;
            
            request.setAttribute("cart", cart);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("error", "Failed to process checkout: " + e.getMessage());
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Checkout Controller for processing orders";
    }// </editor-fold>

}
