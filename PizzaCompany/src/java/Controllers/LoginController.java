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


public class LoginController extends HttpServlet {

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
                response.sendRedirect("AdminController");
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
