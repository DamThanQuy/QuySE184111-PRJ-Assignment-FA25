/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.Order;
import Models.OrderDetail;
import Utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author QUYDAM
 */
public class OrderDAO {
    
    // Tạo đơn hàng mới và trả về OrderID
    public int createOrder(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Orders (CustomerID, OrderDate, ShipAddress, PhoneNumber) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, order.getCustomerID());
            if (order.getOrderDate() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            } else {
                ps.setNull(2, java.sql.Types.TIMESTAMP);
            }
            ps.setString(3, order.getShipAddress());
            ps.setString(4, order.getPhoneNumber());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Trả về OrderID mới tạo
                    }
                }
            }
            throw new SQLException("Tạo đơn hàng thất bại, không có ID được tạo.");
        }
    }
    
    // Tạo đơn hàng hoàn chỉnh (Order + OrderDetails)
    public int createCompleteOrder(Order order, List<OrderDetail> orderDetails) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            conn = DbUtils.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Tạo Order
            int orderID = createOrder(order);
            order.setOrderID(orderID);
            
            // Tạo OrderDetails sử dụng OrderDetailDAO
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            for (OrderDetail detail : orderDetails) {
                detail.setOrderID(orderID);
            }
            orderDetailDAO.createOrderDetails(orderDetails);
            
            conn.commit(); // Commit transaction
            return orderID;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback nếu có lỗi
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    // Lấy đơn hàng theo OrderID
    public Order getOrderById(int orderID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Orders WHERE OrderID = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderID(rs.getInt("OrderID"));
                    order.setCustomerID(rs.getInt("CustomerID"));
                    Timestamp orderDateTs = rs.getTimestamp("OrderDate");
                    if (orderDateTs != null) {
                        order.setOrderDate(orderDateTs.toLocalDateTime());
                    }
                    order.setShipAddress(rs.getString("ShipAddress"));
                    order.setPhoneNumber(rs.getString("PhoneNumber"));
                    return order;
                }
            }
        }
        return null;
    }
    
    
    // Lấy tất cả đơn hàng của khách hàng
    public List<Order> getOrdersByCustomerId(int customerID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Orders WHERE CustomerID = ? ORDER BY OrderDate DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderID(rs.getInt("OrderID"));
                    order.setCustomerID(rs.getInt("CustomerID"));
                    Timestamp orderDateTs = rs.getTimestamp("OrderDate");
                    if (orderDateTs != null) {
                        order.setOrderDate(orderDateTs.toLocalDateTime());
                    }
                    order.setShipAddress(rs.getString("ShipAddress"));
                    order.setPhoneNumber(rs.getString("PhoneNumber"));
                    orders.add(order);
                }
            }
        }
        return orders;
    }
    
    
    // Xóa đơn hàng (cần xóa OrderDetails trước)
    public boolean deleteOrder(int orderID) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            conn = DbUtils.getConnection();
            conn.setAutoCommit(false);
            
            // Xóa OrderDetails trước (sử dụng OrderDetailDAO)
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            orderDetailDAO.deleteOrderDetailsByOrderId(orderID);
            
            // Xóa Order
            String deleteOrderSql = "DELETE FROM Orders WHERE OrderID = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteOrderSql)) {
                ps.setInt(1, orderID);
                int result = ps.executeUpdate();
                conn.commit();
                return result > 0;
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    // Tính tổng tiền cho một đơn hàng từ OrderDetails
    public double getTotalAmountByOrderId(int orderID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(UnitPrice * Quantity) as TotalAmount FROM OrderDetails WHERE OrderID = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TotalAmount");
                }
            }
        }
        return 0.0;
    }
    
    // Lấy chi tiết đơn hàng theo OrderID
    public List<OrderDetail> getOrderDetailsByOrderId(int orderID) throws SQLException, ClassNotFoundException {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        return orderDetailDAO.getOrderDetailsByOrderId(orderID);
    }
}
