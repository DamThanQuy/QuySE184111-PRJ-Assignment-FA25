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
                p.setIsActive(rs.getBoolean("isActive"));
                // joined fields
                try {
                    p.setCategoryName(rs.getString("CategoryName"));
                } catch (Exception ignore) {}
                try {
                    p.setDescription(rs.getString("Description"));
                } catch (Exception ignore) {}
                list.add(p);
            }
            return list;
        }
    }

    public static Product getProductByID(int id) throws Exception {
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.ProductID = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    p.setSupplierID(rs.getInt("SupplierID"));
                    p.setCategoryID(rs.getInt("CategoryID"));
                    p.setQuantityPerUnit(rs.getString("QuantityPerUnit"));
                    p.setUnitPrice(rs.getDouble("UnitPrice"));
                    p.setProductImage(rs.getString("ProductImage"));
                    p.setIsActive(rs.getBoolean("isActive"));
                    // joined fields
                    try {
                        p.setCategoryName(rs.getString("CategoryName"));
                    } catch (Exception ignore) {}
                    try {
                        p.setDescription(rs.getString("Description"));
                    } catch (Exception ignore) {}
                    return p;
                }
            }
        }
        return null;
    }

    public static List<Product> searchProducts(String keyword, Double minPrice, Double maxPrice) throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE 1=1";
        List<Object> params = new ArrayList<>();
        //nếu keyword không null và không rỗng thì thêm vào câu truy vấn
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND ProductName LIKE ?";
            params.add("%" + keyword.trim() + "%");
        }
        //nếu minPrice không null thì thêm vào câu truy vấn
        if (minPrice != null) {
            sql += " AND UnitPrice >= ?";
            params.add(minPrice);
        }
        //nếu maxPrice không null thì thêm vào câu truy vấn
        if (maxPrice != null) {
            sql += " AND UnitPrice <= ?";
            params.add(maxPrice);
        }
        //thực hiện truy vấn
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    p.setSupplierID(rs.getInt("SupplierID"));
                    p.setCategoryID(rs.getInt("CategoryID"));
                    p.setQuantityPerUnit(rs.getString("QuantityPerUnit"));
                    p.setUnitPrice(rs.getDouble("UnitPrice"));
                    p.setProductImage(rs.getString("ProductImage"));
                    p.setIsActive(rs.getBoolean("isActive"));
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
    public static int createProduct(String productName, Double unitPrice, int categoryID, String quantityPerUnit, String productImage, int supplierID) throws Exception {
        String sql = "INSERT INTO Products (ProductName, SupplierID, CategoryID, QuantityPerUnit, UnitPrice, ProductImage, isActive) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, productName);
            st.setInt(2, supplierID);
            st.setInt(3, categoryID);
            st.setString(4, quantityPerUnit);
            st.setDouble(5, unitPrice);
            st.setString(6, productImage);
            st.setBoolean(7, true); // isActive = true
            return st.executeUpdate(); // Trả về số hàng affected
        }
    }

    public static int updateProduct(int productID, String productName, Double unitPrice, int categoryID, String quantityPerUnit, String productImage, int supplierID, boolean isActive) throws Exception {
        String sql = "UPDATE Products SET ProductName = ?, SupplierID = ?, CategoryID = ?, QuantityPerUnit = ?, UnitPrice = ?, ProductImage = ?, isActive = ? WHERE ProductID = ?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, productName);
            st.setInt(2, supplierID);
            st.setInt(3, categoryID);
            st.setString(4, quantityPerUnit);
            st.setDouble(5, unitPrice);
            st.setString(6, productImage);
            st.setBoolean(7, isActive);
            st.setInt(8, productID);
            return st.executeUpdate(); // Trả về số hàng affected
        }
    }

}
