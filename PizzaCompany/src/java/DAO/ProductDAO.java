package DAO;

import Models.Product;
import Utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {


    public static List<Product> getAllProducts() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setSupplierID(rs.getInt("SupplierID"));
                p.setCategoryID(rs.getInt("CategoryID"));
                p.setQuantityPerUnit(rs.getString("QuantityPerUnit"));
                p.setUnitPrice(rs.getDouble("UnitPrice"));
                p.setProductImage(rs.getString("ProductImage"));
                // joined fields
                try {
                    p.setCategoryName(rs.getString("CategoryName"));
                } catch (Exception ignore) {}
                try {
                    p.setDescription(rs.getString("Description"));
                } catch (Exception ignore) {}
                list.add(p);
            }
        }
        return list;
    }


}
