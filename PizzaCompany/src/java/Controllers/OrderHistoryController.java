/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.OrderDAO;
import Models.Order;
import Models.Account;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
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
@WebServlet("/orders-history")
public class OrderHistoryController extends HttpServlet {

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
                // Nếu chưa đăng nhập, set error và forward đến JSP
                request.setAttribute("error", "Vui lòng đăng nhập để xem lịch sử đặt hàng.");
                request.getRequestDispatcher("orders-history.jsp").forward(request, response);
                return;
            }
            
            // Lấy danh sách đơn hàng của người dùng
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders = orderDAO.getOrdersByCustomerId(account.getAccountID());
            
            // Tính tổng tiền cho mỗi đơn hàng
            Map<Integer, Double> totalAmounts = new HashMap<>();
            for (Order order : orders) {
                double total = orderDAO.getTotalAmountByOrderId(order.getOrderID());
                totalAmounts.put(order.getOrderID(), total);
            }
            
            // Truyền dữ liệu vào request
            request.setAttribute("orders", orders);
            request.setAttribute("totalAmounts", totalAmounts);
            request.getRequestDispatcher("orders-history.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải lịch sử đặt hàng: " + e.getMessage());
            request.getRequestDispatcher("orders-history.jsp").forward(request, response);
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
        return "Controller for displaying user's order history";
    }// </editor-fold>

}
