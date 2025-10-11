/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.SalesReportDAO;
import Models.Account;
import Models.SalesReport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controller xử lý báo cáo doanh số cho Admin
 * @author QUYDAM
 */
@WebServlet("/admin/sales-report")
public class SalesReportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra phân quyền Admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginController");
            return;
        }
        
        Account account = (Account) session.getAttribute("account");
        if (account.getType() != 1) { // 1 = Admin
            response.sendRedirect(request.getContextPath() + "/HomeController");
            return;
        }
        
        // Lấy parameters
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        // Nếu chưa có parameters, hiển thị form rỗng
        if (startDateStr == null || endDateStr == null || 
            startDateStr.trim().isEmpty() || endDateStr.trim().isEmpty()) {
            request.getRequestDispatcher("/sales-report.jsp").forward(request, response);
            return;
        }
        
        try {
            // Parse ngày
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            
            // Validate: startDate <= endDate
            if (startDate.isAfter(endDate)) {
                request.setAttribute("error", "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!");
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.getRequestDispatcher("/sales-report.jsp").forward(request, response);
                return;
            }
            
            // Gọi DAO lấy dữ liệu
            List<SalesReport> salesReports = SalesReportDAO.getSalesReport(startDate, endDate);
            double totalRevenue = SalesReportDAO.getTotalRevenue(startDate, endDate);
            
            // Set attributes
            request.setAttribute("salesReports", salesReports);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            
            // Kiểm tra nếu không có dữ liệu
            if (salesReports.isEmpty()) {
                request.setAttribute("message", "Không có dữ liệu trong khoảng thời gian này.");
            }
            
            // Forward đến JSP
            request.getRequestDispatcher("/sales-report.jsp").forward(request, response);
            
        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Định dạng ngày không hợp lệ! Vui lòng chọn ngày đúng định dạng.");
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            request.getRequestDispatcher("/sales-report.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/sales-report.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Sales Report Controller for Admin";
    }

}
