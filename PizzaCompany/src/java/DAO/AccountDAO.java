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
}
