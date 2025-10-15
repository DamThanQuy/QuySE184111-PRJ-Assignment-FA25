package DAO;

import Models.Account;
import Utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    
    public static Account getAccountByUsernameAndPassword(String username, String password) {
        Account account = null;
        String query = "SELECT AccountID, UserName, Password, FullName, Type FROM Account WHERE UserName = ? AND Password = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    account = new Account();
                    account.setAccountID(rs.getInt("AccountID"));
                    account.setUserName(rs.getString("UserName"));
                    account.setPassword(rs.getString("Password"));
                    account.setFullName(rs.getString("FullName"));
                    account.setType(rs.getInt("Type"));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error in getAccountByUsernameAndPassword: " + e.getMessage());
        }
        
        return account;
    }
    
    /**
     * Kiểm tra username đã tồn tại trong database chưa
     * @param username Username cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public static boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM Account WHERE UserName = ?";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error in checkUsernameExists: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Tạo account mới trong database
     * @param account Account object chứa thông tin đăng ký
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public static boolean createAccount(Account account) {
        String query = "INSERT INTO Account (UserName, Password, FullName, Type) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, account.getUserName());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getFullName());
            ps.setInt(4, 2); // Set type = 2 (Customer) mặc định
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error in createAccount: " + e.getMessage());
            return false;
        }
    }
}
