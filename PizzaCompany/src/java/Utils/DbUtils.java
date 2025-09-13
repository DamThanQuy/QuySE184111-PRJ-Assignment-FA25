/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author QUYDAM
 */
public class DbUtils {
    
    private static final String DB_NAME = "PizzaStoreDB";
    private static final String DB_USER_NAME = "sa";
    private static final String DB_PASSWORD = "12345";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DB_NAME;
        conn = DriverManager.getConnection(url, DB_USER_NAME, DB_PASSWORD);
        return conn;
    }
    
//    public static void main(String[] args) {
//        try (Connection conn = getConnection()) {
//            if (conn != null) {
//                System.out.println("Kết nối database thành công!");
//            } else {
//                System.out.println("Không thể kết nối đến database. Vui lòng kiểm tra lại thông tin kết nối.");
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            System.out.println("Lỗi kết nối: " + e.getMessage());
//        }
//    }
}
