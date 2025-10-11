/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.OrderDAO;
import DAO.ProductDAO;
import Models.Order;
import Models.OrderDetail;
import Models.Account;
import Models.Product;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author QUYDAM
 */
@WebServlet("/order-detail")
public class OrderDetailController extends HttpServlet {

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
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        
        try {
            if (account == null) {
                // Nếu chưa đăng nhập, chuyển về trang đăng nhập
                request.setAttribute("error", "Vui lòng đăng nhập để xem chi tiết đơn hàng.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            // Lấy orderID từ query string
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.isEmpty()) {
                request.setAttribute("error", "Không tìm thấy mã đơn hàng.");
                request.getRequestDispatcher("orders-history").forward(request, response);
                return;
            }
            
            int orderId = Integer.parseInt(orderIdParam);
            
            // Lấy thông tin đơn hàng
            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "Không tìm thấy đơn hàng.");
                request.getRequestDispatcher("orders-history").forward(request, response);
                return;
            }
            
            // Kiểm tra quyền truy cập (chỉ cho phép xem đơn hàng của chính mình)
            if (order.getCustomerID() != account.getAccountID()) {
                request.setAttribute("error", "Bạn không có quyền xem đơn hàng này.");
                request.getRequestDispatcher("orders-history").forward(request, response);
                return;
            }
            
            // Lấy chi tiết đơn hàng
            List<OrderDetail> orderDetails = orderDAO.getOrderDetailsByOrderId(orderId);
            ProductDAO productDAO = new ProductDAO();
            Map<Integer, Product> productMap = new HashMap<>();
            for (OrderDetail detail : orderDetails) {
                int productId = detail.getProductID();
                if (!productMap.containsKey(productId)) {
                    Product product = productDAO.getProductByID(productId);
                    if (product != null) {
                        productMap.put(productId, product);
                    }
                }
            }
            double totalAmount = orderDAO.getTotalAmountByOrderId(orderId);
            
            // Truyền dữ liệu vào request
            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            request.setAttribute("productMap", productMap);
            request.setAttribute("totalAmount", totalAmount);
            request.getRequestDispatcher("order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã đơn hàng không hợp lệ.");
            request.getRequestDispatcher("orders-history").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải chi tiết đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("orders-history").forward(request, response);
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
        return "Controller for displaying order details";
    }// </editor-fold>

}
