/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.OrderDetail;
import Utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author QUYDAM
 */
public class OrderDetailDAO {
    
    // Tạo chi tiết đơn hàng
    public boolean createOrderDetail(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO OrderDetails (OrderID, ProductID, UnitPrice, Quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderDetail.getOrderID());
            ps.setInt(2, orderDetail.getProductID());
            ps.setDouble(3, orderDetail.getUnitPrice());
            ps.setInt(4, orderDetail.getQuantity());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Tạo nhiều chi tiết đơn hàng cùng lúc
    public boolean createOrderDetails(List<OrderDetail> orderDetails) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO OrderDetails (OrderID, ProductID, UnitPrice, Quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            for (OrderDetail orderDetail : orderDetails) {
                ps.setInt(1, orderDetail.getOrderID());
                ps.setInt(2, orderDetail.getProductID());
                ps.setDouble(3, orderDetail.getUnitPrice());
                ps.setInt(4, orderDetail.getQuantity());
                ps.addBatch();
            }
            
            // Thực thi các lệnh insert thì dùng executeBatch() thay vì executequery()
            int[] results = ps.executeBatch();
            return results.length > 0;
        }
    }
    
    // Lấy chi tiết đơn hàng theo OrderID
    public List<OrderDetail> getOrderDetailsByOrderId(int orderID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetails WHERE OrderID = ?";
        List<OrderDetail> orderDetails = new ArrayList<>();
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderID(rs.getInt("OrderID"));
                    detail.setProductID(rs.getInt("ProductID"));
                    detail.setUnitPrice(rs.getDouble("UnitPrice"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    orderDetails.add(detail);
                }
            }
        }
        return orderDetails;
    }
    
    // Lấy chi tiết đơn hàng theo ProductID
    public List<OrderDetail> getOrderDetailsByProductId(int productID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM OrderDetails WHERE ProductID = ?";
        List<OrderDetail> orderDetails = new ArrayList<>();
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, productID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderID(rs.getInt("OrderID"));
                    detail.setProductID(rs.getInt("ProductID"));
                    detail.setUnitPrice(rs.getDouble("UnitPrice"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    orderDetails.add(detail);
                }
            }
        }
        return orderDetails;
    }
    
    // Cập nhật số lượng sản phẩm trong đơn hàng
    public boolean updateOrderDetailQuantity(int orderID, int productID, int newQuantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE OrderDetails SET Quantity = ? WHERE OrderID = ? AND ProductID = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, newQuantity);
            ps.setInt(2, orderID);
            ps.setInt(3, productID);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Xóa một sản phẩm khỏi đơn hàng
    public boolean deleteOrderDetail(int orderID, int productID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetails WHERE OrderID = ? AND ProductID = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            ps.setInt(2, productID);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Xóa tất cả chi tiết đơn hàng theo OrderID
    public boolean deleteOrderDetailsByOrderId(int orderID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM OrderDetails WHERE OrderID = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            
            return ps.executeUpdate() >= 0; // Trả về true ngay cả khi không có record nào để xóa
        }
    }
    
    // Lấy tổng số lượng sản phẩm đã bán theo ProductID
    public int getTotalQuantitySoldByProductId(int productID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(Quantity) as TotalQuantity FROM OrderDetails WHERE ProductID = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, productID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("TotalQuantity");
                }
            }
        }
        return 0;
    }
    
    // Lấy tổng doanh thu theo ProductID
    public double getTotalRevenueByProductId(int productID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(UnitPrice * Quantity) as TotalRevenue FROM OrderDetails WHERE ProductID = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, productID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TotalRevenue");
                }
            }
        }
        return 0.0;
    }
}
