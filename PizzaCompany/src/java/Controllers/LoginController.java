/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Models.Account;
import DAO.AccountDAO;
import javax.servlet.http.HttpSession;

/**
 *
 * @author QUYDAM
 */
public class LoginController extends HttpServlet {
   
 

 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        Account account = AccountDAO.getAccountByUsernameAndPassword(username, password);
        
        if (account != null) {
            // Đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            
            // Kiểm tra role và redirect
            if (account.getType() == 1) {
                // Admin
                response.sendRedirect("admin-dashboard.jsp");
            } else {
                // User
                response.sendRedirect("HomeController");
            }
        } else {
            // Đăng nhập thất bại
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
