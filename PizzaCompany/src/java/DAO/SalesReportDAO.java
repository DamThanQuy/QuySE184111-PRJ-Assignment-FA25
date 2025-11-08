/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.SalesReport;
import Utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author QUYDAM
 */
public class SalesReportDAO {
    

    public static List<SalesReport> getSalesReport(LocalDate startDate, LocalDate endDate) throws Exception {
        List<SalesReport> list = new ArrayList<>();
        
        String sql = "SELECT " +
                     "    p.ProductID, " +
                     "    p.ProductName, " +
                     "    SUM(od.Quantity) AS TotalQuantity, " +
                     "    SUM(od.UnitPrice * od.Quantity) AS TotalSales " +
                     "FROM Orders o " +
                     "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                     "JOIN Products p ON od.ProductID = p.ProductID " +
                     "WHERE o.OrderDate BETWEEN ? AND ? " +
                     "GROUP BY p.ProductID, p.ProductName " +
                     "ORDER BY TotalSales DESC";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            
            // Set parameters
            st.setDate(1, java.sql.Date.valueOf(startDate));
            st.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    SalesReport report = new SalesReport();
                    report.setProductID(rs.getInt("ProductID"));
                    report.setProductName(rs.getString("ProductName"));
                    report.setTotalQuantity(rs.getInt("TotalQuantity"));
                    report.setTotalSales(rs.getDouble("TotalSales"));
                    list.add(report);
                }
            }
        }
        
        return list;
    }
    
    /**
     * Tính tổng doanh thu trong khoảng thời gian
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Tổng doanh thu
     * @throws Exception
     */
    public static double getTotalRevenue(LocalDate startDate, LocalDate endDate) throws Exception {
        String sql = "SELECT SUM(od.UnitPrice * od.Quantity) AS TotalRevenue " +
                     "FROM Orders o " +
                     "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                     "WHERE o.OrderDate BETWEEN ? AND ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            
            st.setDate(1, java.sql.Date.valueOf(startDate));
            st.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TotalRevenue");
                }
            }
        }
        
        return 0.0;
    }
}
