/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.AccountDAO;
import Models.Account;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller xử lý đăng ký tài khoản mới
 * @author QUYDAM
 */
@WebServlet("/register")
public class RegisterController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form đăng ký
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy parameters từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        
        // Validate: Không để trống
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Validate: Password phải khớp với Confirm Password
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Validate: Username chưa tồn tại
        if (AccountDAO.checkUsernameExists(username)) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Tạo account mới
        Account newAccount = new Account();
        newAccount.setUserName(username);
        newAccount.setPassword(password);
        newAccount.setFullName(fullName);
        // type = 2 (Customer) sẽ được set trong DAO
        
        // Gọi DAO để tạo account
        boolean success = AccountDAO.createAccount(newAccount);
        
        if (success) {
            // Đăng ký thành công -> redirect về login
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            // Đăng ký thất bại
            request.setAttribute("error", "Đăng ký thất bại! Vui lòng thử lại.");
            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Register Controller";
    }

}
